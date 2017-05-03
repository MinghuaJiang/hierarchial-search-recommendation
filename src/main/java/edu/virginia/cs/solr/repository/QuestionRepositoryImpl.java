package edu.virginia.cs.solr.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;

import java.io.IOException;
import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */

public class QuestionRepositoryImpl implements QuestionSearch {
    private SolrTemplate template;
    private static final int PAGE_SIZE = 5;

    public QuestionRepositoryImpl(SolrTemplate template) {
        this.template = template;
    }


    @Override
    public Cursor<Question> getAllQuestions() {
        //Pageable pageRequest = new PageRequest(pageNum - 1, 10000);
        Criteria criteria = Criteria.where("title_t").isNotNull();
        SimpleQuery query = new SimpleQuery(criteria);
        Cursor<Question> results = template.queryForCursor(query, Question.class);
        //QuestionResult result = new QuestionResult(results.getTotalElements(),
                //results.getTotalPages(), results.getContent());
        //Gson gson = new GsonBuilder().create();
        //System.out.println(gson.toJson(result));
        return results;
    }

    @Override
    public long[] getQuestionsByTagDifference(String tag1, String tag2) {
        long[] result = new long[2];
        Criteria criteria = Criteria.where("tagName_ss").expression(tag1).and("tagName_ss").expression(tag2).not();
        SimpleQuery query = new SimpleQuery(criteria);
        result[0] = template.count(query);
        criteria = Criteria.where("tagName_ss").expression(tag2).and("tagName_ss").expression(tag1).not();
        query = new SimpleQuery(criteria);
        result[1] = template.count(query);
        return result;
    }

    @Override
    public int getTotalTermFrequency(String tagName) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        query.set("fl","ttf(body_t,"+tagName+")");
        query.setRows(1);
        query.setStart(0);
        QueryResponse resp = template.getSolrClient().
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(), query);
        System.out.println(resp.getResults());
        return 0;
    }

    @Override
    public List<Question> recommendQuestionsByTag(String tagName, int topCount) {
        Pageable pageRequest = new PageRequest(0, topCount);
        Criteria criteria = Criteria.where("tagName_ss").expression(tagName);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest).
                addProjectionOnFields("title_t", "answers_l", "vote_l", "view_l", "favorite_l");
        Page results = template.queryForPage(query, Question.class);
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(results.getContent()));
        return results.getContent();
    }

    @Override
    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) throws Exception {
        Pageable pageRequest = new PageRequest(pageNum - 1, PAGE_SIZE);
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
        System.out.println(resp.getResults());
        QuestionResult result = new QuestionResult(resp.getResults().getNumFound(),
                (int) ((resp.getResults().getNumFound() - 1) / PAGE_SIZE + 1), results);
        return result;
    }
}
