package edu.virginia.cs.controllers;


import edu.virginia.cs.solr.service.IndexService;
import edu.virginia.cs.solr.service.IndexServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cutehuazai on 4/28/17.
 */
@RestController
public class IndexController {

    @Autowired
    private IndexService writer;
    @RequestMapping(value="/index2")
    public String buildIndex() {
        writer.buildIndex();
        return "Index Build Successfully";
    }
}
