package edu.virginia.cs.solr.repository;

import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.io.IOException;
import java.util.*;

/**
 * Created by cutehuazai on 5/1/17.
 */

public class QuestionRepositoryImpl implements QuestionSearch {
    @Autowired
    private HierarchyBuilder factory;
    @Autowired
    private TagRepository repository;
    private SolrTemplate template;
    private static final int PAGE_SIZE = 5;

    public QuestionRepositoryImpl(SolrTemplate template) {
        this.template = template;
    }


    @Override
    public Page<Question> getAllQuestions(int page) throws Exception {
        Pageable pageRequest = new PageRequest(page, 1000);
        Criteria criteria = Criteria.where("title_t").isNotNull();
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page<Question> results = template.queryForPage(query, Question.class);
        int count = 0;
        for (Question question : results) {
            List<String> tags = question.getTags();
            String topic = null;
            double maxTF = 0.0;
            String tagName = tags.get((int)(Math.random() * tags.size()));
            /*for(String tag: tags){
                double tf = getTermFrequency(tag, count).get("");
                if(tf > maxTF){
                    maxTF = tf;
                    topic = tag;
                }
            }*/
            count++;
            QuestionResult result = this.getQuestionsByTagName(tagName, 0);
            question.setTopic(new Topic(tagName, result.getTotalCount()));
        }
        return results;
    }

    @Override
    public long[] getQuestionsByTagDifference(String tag1, String tag2) {
        long[] result = new long[2];
        Criteria criteria = Criteria.where("tagName_ss").expression(tag1).and("tagName_ss").expression(tag2).not();
        SimpleQuery query = new SimpleQuery(criteria);
        result[0] = template.count(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        criteria = Criteria.where("tagName_ss").expression(tag2).and("tagName_ss").expression(tag1).not();
        query = new SimpleQuery(criteria);
        result[1] = template.count(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        return result;
    }

    @Override
    public Map<String, Double> getTermFrequency(String tagName, int count) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        String key = "termfreq('body_t'," + tagName + ")";
        query.set("q", "*:*");
        query.set("fl", key);
        query.setRows(1);
        query.setStart(count);
        QueryResponse resp = template.getSolrClient().
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        Map<String, Double> result = new HashMap<>();
        result.put("tf", Double.valueOf(resp.getResults().get(0).getFieldValue(key).toString()));
        return result;
    }

    @Override
    public int getTotalTermFrequency(String tagName) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        String key = "ttf(body_t," + tagName + ")";
        query.set("q", "*:*");
        query.set("fl", key);
        query.setRows(1);
        query.setStart(0);
        QueryResponse resp = template.getSolrClient().
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        return Integer.valueOf(resp.getResults().get(0).getFieldValue(key).toString());
    }

    @Override
    public String recommendNode(String searchTerm, int nodeCount) throws Exception {
        Pageable pageRequest = new PageRequest(0, 20);
        SolrQuery query = new SolrQuery();
        query.set("q", searchTerm);
        query.set("qf", "title_t^1.5 body_t");
        query.set("defType", "edismax");
        query.addFilterQuery("answers_l:[1 TO *]");
        query.addHighlightField("body_t");
        query.set("stopwords", true);
        query.set("lowercaseOperators", true);
        query.setStart(pageRequest.getPageNumber());
        query.setRows(pageRequest.getPageSize());
        QueryResponse resp = template.getSolrClient().
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        List<Question> results = template.convertQueryResponseToBeans(resp, Question.class);
        Set<Tag> set = new HashSet();
        results.forEach((x) ->{
            for(String tag: x.getTags()){
                set.add(repository.getTagByName(tag));
            }
        });
        HierarchyNode central = calculateCentralTag(set);
        String cluster = getNodeCluster(central, nodeCount);
        return cluster;
    }

    private String getNodeCluster(HierarchyNode central, int count) {
        return "";
    }

    private HierarchyNode calculateCentralTag(Set<Tag> tags) {
        List<HierarchyNode> list = new ArrayList<HierarchyNode>();
        double avgScore = 0;
        for (Tag tag : tags) {
            HierarchyNode node = factory.getHierarchy().getHierachyNode(tag.getTagName());
            list.add(node);
            avgScore += node.getScore();
        }
        avgScore = avgScore / tags.size();
        HierarchyNode result = null;
        double min = Double.MAX_VALUE;
        for (HierarchyNode node : list) {
            if (Math.abs(node.getScore() - avgScore) < min) {
                result = node;
                min = Math.abs(node.getScore() - avgScore);
            }
        }

        return result;
    }

    @Override
    public List<Question> recommendQuestionsByTag(String tagName, int topCount) throws Exception {
        List<Question> result = new ArrayList<Question>();
        Queue<Question> queue = new PriorityQueue<Question>(topCount, new Comparator<Question>() {
            @Override
            public int compare(Question t1, Question t2) {
                try {
                    if (t1.getScore() < t2.getScore()) {
                        return -1;
                    } else if (t1.getScore() > t2.getScore()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Pageable pageRequest = new PageRequest(0, topCount);
        Criteria criteria = Criteria.where("tagName_ss").expression(tagName);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page results = template.queryForPage(query, Question.class);
        List<Question> questions = results.getContent();
        questions.forEach((x) -> queue.offer(x));
        while (results.hasNext() && pageRequest.getPageNumber() <= 10) {
            pageRequest = pageRequest.next();
            query = new SimpleQuery(criteria, pageRequest);
            results = template.queryForPage(query, Question.class);
            questions = results.getContent();
            for (Question question : questions) {
                if (question.getScore() <= queue.peek().getScore()) {
                    continue;
                } else {
                    queue.poll();
                    queue.offer(question);
                }
            }
        }
        while (!queue.isEmpty()) {
            result.add(0, queue.poll());
        }
        return result;
    }

    @Override
    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) throws Exception {
        Pageable pageRequest = new PageRequest(pageNum - 1, PAGE_SIZE);
        SolrQuery query = new SolrQuery();
        query.set("q", searchTerm);
        query.set("qf", "title_t^1.5 body_t");
        query.set("defType", "edismax");
        query.addFilterQuery("answers_l:[1 TO *]");
        query.set("stopwords", true);
        query.set("lowercaseOperators", true);
        query.setStart(pageRequest.getPageNumber());
        query.setRows(pageRequest.getPageSize());
        QueryResponse resp = template.getSolrClient().
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        List<Question> results = template.convertQueryResponseToBeans(resp, Question.class);
        QuestionResult result = new QuestionResult(resp.getResults().getNumFound(),
                (int) ((resp.getResults().getNumFound() - 1) / PAGE_SIZE + 1), results);
        return result;
    }

    @Override
    public QuestionResult getQuestionsByTagName(String tagName, int pageNum) throws Exception {
        Criteria criteria = Criteria.where("tagName_ss").expression(tagName);
        SimpleQuery query = new SimpleQuery(criteria);
        Page<Question> questions = template.queryForPage(query, Question.class);
        QuestionResult result = new QuestionResult(questions.getTotalElements(), questions.getTotalPages(),
                questions.getContent());
        return result;
    }
}
