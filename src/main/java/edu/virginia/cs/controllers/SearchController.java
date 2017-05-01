package edu.virginia.cs.controllers;


import com.google.code.stackexchange.schema.User;
import edu.virginia.cs.search.api.QuestionSearch;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


/**
 * Created by cutehuazai on 4/28/17.
 */
@RestController
public class SearchController {
    @Autowired
    private QuestionSearch search;

    @RequestMapping(value="/question/search/relevant/{term}/{page_id}")
    public String searchMostRelevantQuestion(@PathVariable("term") String term, @PathVariable("page_id")int page) {
        return search.searchQuestions(term, page);
    }


    @RequestMapping(value="/question/search/count/{term}")
    public String getMatchedCount(@PathVariable("term") String term) {
        return search.getMatchCount(term);
    }
}
