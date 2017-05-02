package edu.virginia.cs.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.solr.model.QuestionResult;
import edu.virginia.cs.solr.repository.QuestionRepository;
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
    private QuestionRepository repository;

    @RequestMapping(value="/question/search/relevant/{term}/{page_id}")
    public String searchMostRelevantQuestion(@PathVariable("term") String term, @PathVariable("page_id")int page) throws Exception{
        QuestionResult result = repository.searchQuestionsBySearchTerm(term, page);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(result);
    }


    @RequestMapping(value="/recommend/{tag}/{count}")
    public String recommendTopQuestionByTagName(@PathVariable("tag") String tag, @PathVariable("count")int count) throws Exception{
        QuestionResult result = repository.recommendQuestionsByTag(tag, count);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(result);
    }
}
