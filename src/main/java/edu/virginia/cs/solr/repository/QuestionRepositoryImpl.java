package edu.virginia.cs.solr.repository;

import edu.virginia.cs.search.api.QuestionSearch;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
public class QuestionRepositoryImpl implements QuestionSearch {
    private SolrTemplate template;

    public QuestionRepositoryImpl(SolrTemplate template) {
        this.template = template;
    }

    @Override
    public QuestionResult getQuestionsByTag(String tagName, int pageNum) {
        return null;
    }

    @Override
    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) {
        Sort sort = new Sort(Sort.Direction.ASC, "tagName_s");
        Criteria criteria = Criteria.where("title_t").expression(searchTerm).or(Criteria.where("body_t").expression(searchTerm)).and(Criteria.where("answers_l").greaterThanEqual(1));
        Pageable pageRequest = new PageRequest(pageNum - 1, 5, sort);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page results = template.queryForPage(query, Question.class);
        QuestionResult result = new QuestionResult(results.getTotalElements(), results.getTotalPages(), results.getContent());
        return result;
    }

    @Override
    public String searchQuestions(String searchTerm, int pageNum) {
        return null;
    }

    @Override
    public long getMatchCount(String searchTerm) {
        Criteria conditions = Criteria.where("question_title_t").
                expression(searchTerm).and("question_body_t").expression(searchTerm);
        SimpleQuery search = new SimpleQuery(conditions);
        return template.count(search);
    }

    @Override
    public long getMatchCountOfTwoTags(String tag1, String tag2) {
        return 0;
    }

}
