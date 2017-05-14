package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.TagComparator;
import edu.virginia.cs.solr.model.Topic;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by cutehuazai on 5/1/17.
 */
@Repository
public class TagRepositoryImpl implements TagSearch{
    @Autowired
    private QuestionRepository questionRepository;

    private SolrTemplate template;
    public TagRepositoryImpl(SolrTemplate template){
        this.template = template;
    }

    @Override
    public Tag getTagByName(String tagName) {
        Criteria criteria = Criteria.where("tagName_s").isNotNull();
        SimpleQuery query = new SimpleQuery(criteria);
        Page results = template.queryForPage(query, Tag.class);
        List<Tag> tags = results.getContent();
        return tags.get(0);
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> result = new ArrayList<Tag>();
        Sort sort = new Sort(Sort.Direction.ASC, "tagName_s");
        Criteria criteria = Criteria.where("tagName_s").isNotNull();
        Pageable pageRequest = new PageRequest(0, 100, sort);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page results = template.queryForPage(query, Tag.class);
        result.addAll(results.getContent());
        int pages = results.getTotalPages();
        for(int i = 1;i <= pages;i++){
            pageRequest = pageRequest.next();
            query = new SimpleQuery(criteria, pageRequest);
            results = template.queryForPage(query, Tag.class);
            result.addAll(results.getContent());
        }
        return result;
    }

    @Override
    public TreeSet<Tag> getRankingTags(Set<Topic> topK) throws IOException, SolrServerException {
        TreeSet<Tag> tags = new TreeSet<>(new TagComparator());
        List<Tag> lists = getAllTags();
        double maxScore = 0;
        int count = 0;
        for(Tag tag: lists){
            // make topic = tag
//            if(!topK.contains(new Topic(tag.getTagName(), tag.getTagDistinctCount()))){
//                continue;
//            }
            tag.setTagRawCount(questionRepository.getTotalTermFrequency(tag.getTagName()));
            tag.calculateITScore(topK, lists.size(), questionRepository);
            maxScore = Math.max(maxScore, tag.getScore());
            tags.add(tag);
            count++;
            if(count % 100 == 0){
                System.out.println(count + " tags passed");
            }
        }
        for(Tag tag: tags){
            tag.normalizeScore(maxScore);
        }
        return tags;
    }
}
