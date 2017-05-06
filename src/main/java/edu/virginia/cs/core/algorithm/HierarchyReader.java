package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by cutehuazai on 5/6/17.
 */
public class HierarchyReader {
    public Hierarchy readHierachy() throws Exception {
        ObjectInputStream ois = null;
        ois = new ObjectInputStream(new FileInputStream("hierarchy.dat"));
        try {
            Hierarchy hierarchy = (Hierarchy) ois.readObject();

            return hierarchy;
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        HierarchyReader reader = new HierarchyReader();
        Hierarchy hierarchy = reader.readHierachy();

    }
}
