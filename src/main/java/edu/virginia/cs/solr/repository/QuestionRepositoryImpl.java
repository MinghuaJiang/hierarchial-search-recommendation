package edu.virginia.cs.solr.repository;

import edu.virginia.cs.search.api.QuestionSearch;
import edu.virginia.cs.solr.model.Question;
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
    public List<Question> getQuestionsByTag(String tagName, int pageNum) {
        return null;
    }

    @Override
    public List<Question> searchQuestionsBySearchTerm(String searchTerm, int pageNum) {
        return null;
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
