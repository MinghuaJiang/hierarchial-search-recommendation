package edu.virginia.cs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cutehuazai on 4/16/17.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
