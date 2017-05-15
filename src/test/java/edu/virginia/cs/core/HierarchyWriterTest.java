package edu.virginia.cs.core;

import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import edu.virginia.cs.core.algorithm.HierarchyWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cutehuazai on 5/9/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HierarchyWriterTest {
    @Autowired
    private HierarchyWriter writer;

    @Test
    public void testWriterHierarchy() throws Exception {
        writer.writeHierarchy();
    }
}
