package edu.virginia.cs.core;

import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cutehuazai on 5/3/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildHierarchyTest {
    @Autowired
    private HierarchyBuilder builder;

    @Test
    public void testBuildHierarchy() throws Exception {
        builder.buildHierarchy();

    }
}
