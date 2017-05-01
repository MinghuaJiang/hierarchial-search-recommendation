package edu.virginia.cs.search.api;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.schema.User;
import edu.virginia.cs.search.impl.QuestionSearchImpl;
import edu.virginia.cs.search.impl.TagSearchImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cutehuazai on 4/29/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TagSearchTest {
    @Autowired
    private TagSearch search;

    @Test
    public void testTagSearch(){
        System.out.println(search.getAllTags().size());
    }
}
