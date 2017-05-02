package edu.virginia.cs.search.api;

import edu.virginia.cs.solr.model.QuestionResult;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface QuestionSearch {
    public QuestionResult recommendQuestionsByTag(String tagName, int topCount) throws Exception;

    public QuestionResult searchQuestionsBySearchTerm(String searchTerm, int pageNum) throws Exception;

}

