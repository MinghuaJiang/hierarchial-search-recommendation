package edu.virginia.cs.search.api;

import com.google.code.stackexchange.schema.User;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface QuestionSearch {
    public String searchQuestions(String searchTerm, int pageNum, User.QuestionSortOrder order);
}
