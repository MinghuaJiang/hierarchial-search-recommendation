package edu.virginia.cs.solr.index;

import edu.virginia.cs.solr.parser.StreamBasedXMLParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cutehuazai on 5/1/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionIndexCreationTest {
    @Autowired
    private StreamBasedXMLParser parser;

    @Test
    public void testIndexCreation(){
        parser.parsePosts();
    }
}
