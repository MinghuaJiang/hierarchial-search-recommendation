package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.Cursor;

import java.io.IOException;
import java.util.List;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface QuestionSearch {
    public List<Question> recommendQuestionsByTag(String tagName, int topCount) throws Exception;

    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) throws Exception;

    public Page<Question> getAllQuestions(int page);

    public long[] getQuestionsByTagDifference(String tag1, String tag2);

    public int getTotalTermFrequency(String tagName) throws IOException, SolrServerException;

    public String recommendNode(String searchTerm, int nodeCount) throws Exception;
}

