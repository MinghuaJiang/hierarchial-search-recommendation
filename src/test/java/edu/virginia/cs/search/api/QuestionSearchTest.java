package edu.virginia.cs.search.api;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.schema.User;
import edu.virginia.cs.search.impl.QuestionSearchImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by cutehuazai on 4/29/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionSearchTest {
    //@Autowired
    //private QuestionSearch search;

    @Test
    public void testQuestionSearch(){
        //System.out.println(search.searchQuestions("overide and overload", 1));
    }
}
