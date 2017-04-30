package edu.virginia.cs.search.impl;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.search.api.QuestionSearch;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cutehuazai on 4/29/17.
 */

public class QuestionSearchImpl implements QuestionSearch {
    @Autowired
    private StackExchangeApiQueryFactory factory;

    public QuestionSearchImpl(StackExchangeApiQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public String searchQuestions(String searchTerm, int pageNum, User.QuestionSortOrder order) {
        Paging paging = new Paging(pageNum, 5);
        PagedList<Question> questions = factory.newAdvanceSearchApiQuery().withMinAnswers(1).withPaging(paging)
                .withSort(order).withQuery(searchTerm).list();
        questions.forEach((x) -> System.out.println(x.getBody()));
        Gson gson = new GsonBuilder().create();
        return gson.toJson(questions);
    }
}
