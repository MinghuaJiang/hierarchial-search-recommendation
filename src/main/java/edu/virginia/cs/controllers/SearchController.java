package edu.virginia.cs.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.QuestionResult;
import edu.virginia.cs.solr.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;


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
        try (Writer writer = new FileWriter("search.json")) {
            Gson gson_out = new GsonBuilder().create();
            gson_out.toJson(result, writer);
            System.out.println("Successfully Copied search result to File...");
            System.out.println("\nJSON Object: " + result);
        }
        return gson.toJson(result);
    }


    @RequestMapping(value="/recommendation/question/{tag}/{count}")
    public String recommendTopQuestionByTagName(@PathVariable("tag") String tag, @PathVariable("count")int count) throws Exception{
        List<Question> result = repository.recommendQuestionsByTag(tag, count);
        Gson gson = new GsonBuilder().create();
        try (FileWriter file = new FileWriter("recommendation.json")) {
            Gson gson_out = new GsonBuilder().create();
            gson_out.toJson(result, file);
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + result);
        }
        return gson.toJson(result);
    }

    @RequestMapping(value="/recommendation/tag/{term}/{count}")
    public String recommendTags(@PathVariable("term") String term, @PathVariable("count")int count) throws Exception{
        Map<String,Object> result = repository.recommendNode(term, count);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(result);
    }
}
