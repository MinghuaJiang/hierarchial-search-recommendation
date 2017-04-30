package edu.virginia.cs.search.api;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.schema.User;
import edu.virginia.cs.search.impl.QuestionSearchImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by cutehuazai on 4/29/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QuestionSearchTest {
    @Autowired
    private QuestionSearch search;

    @Test
    public void testQuestionSearch(){
        search.searchQuestions("overload and override", 1, User.QuestionSortOrder.MOST_RELEVANT);
    }
}
