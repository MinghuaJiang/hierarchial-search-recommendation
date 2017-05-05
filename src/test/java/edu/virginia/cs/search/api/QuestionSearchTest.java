package edu.virginia.cs.search.api;

import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.repository.QuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cutehuazai on 4/29/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionSearchTest {
    @Autowired
    private QuestionRepository repository;

    @Test
    public void testQuestionSearch() throws Exception {


    }
}
