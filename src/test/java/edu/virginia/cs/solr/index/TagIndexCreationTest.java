package edu.virginia.cs.solr.index;

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
public class TagIndexCreationTest {
    @Autowired
    private IndexService service;

    @Test
    public void testIndexCreation(){
        service.buildTagIndex();
    }
}
