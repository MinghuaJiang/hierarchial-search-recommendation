package edu.virginia.cs.solr.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.search.api.QuestionSearch;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import edu.virginia.cs.solr.model.Tag;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

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
    public QuestionResult recommendQuestionsByTag(String tagName, int topCount) {
        Pageable pageRequest = new PageRequest(0, topCount);
        Criteria criteria = Criteria.where("tagName_ss").expression(tagName);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page results = template.queryForPage(query, Question.class);
        QuestionResult result = new QuestionResult(results.getTotalElements(),
                results.getTotalPages(), results.getContent());
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(result));
        return result;
    }

    @Override
    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) throws Exception {
        Pageable pageRequest = new PageRequest(pageNum - 1, PAGE_SIZE);
        SolrQuery query = new SolrQuery(searchTerm);
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
                query(Question.class.getAnnotation(SolrDocument.class).solrCoreName(),query);
        List<Question> results = template.convertQueryResponseToBeans(resp, Question.class);
        QuestionResult result = new QuestionResult(resp.getResults().getNumFound(),
                (int) ((resp.getResults().getNumFound() - 1) / PAGE_SIZE + 1), results);
        return result;
    }
}
