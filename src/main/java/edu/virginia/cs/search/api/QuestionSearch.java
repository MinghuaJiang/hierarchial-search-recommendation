package edu.virginia.cs.search.api;

import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

import java.util.List;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface QuestionSearch {
    public String searchQuestions(String searchTerm, int pageNum);

    public String getMatchCount(String searchTerm);

    public long getMatchCountOfTwoTags(String tag1, String tag2);
}

