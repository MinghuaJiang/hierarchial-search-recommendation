package edu.virginia.cs.search.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cutehuazai on 4/30/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionByTagTest {
    @Autowired
    private QuestionSearch search;

    @Test
    public void testQuestionByTag(){
        System.out.println(search.getMatchCountOfTwoTags("java","hadoop"));
    }
}
