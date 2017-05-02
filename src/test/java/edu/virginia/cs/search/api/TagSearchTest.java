package edu.virginia.cs.search.api;
import edu.virginia.cs.solr.repository.TagRepository;
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
    private TagRepository repository;

    @Test
    public void testTagSearch(){
        System.out.println(repository.getAllTags().size());
    }
}
