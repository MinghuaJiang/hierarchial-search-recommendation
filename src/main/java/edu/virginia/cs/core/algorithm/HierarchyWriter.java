package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by cutehuazai on 5/6/17.
 */
public class HierarchyWriter {
    @Autowired
    private TagRepository repository;
    public void writeHierarchy() throws Exception{
        ObjectOutputStream oos = null;
        try {
            Hierarchy hierarchy = buildHierarchy();
            oos = new ObjectOutputStream(new FileOutputStream(new File("hierarchy.dat")));
            oos.writeObject(hierarchy);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    private Hierarchy buildHierarchy(){
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.addNode(hierarchy.getRoot(), new HierarchyNode(repository.getTagByName("java")));
        return hierarchy;
    }
}
