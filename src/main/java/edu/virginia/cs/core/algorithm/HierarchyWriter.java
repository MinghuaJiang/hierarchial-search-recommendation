package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by cutehuazai on 5/6/17.
 */
public class HierarchyWriter {

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
        return null;
    }
}
