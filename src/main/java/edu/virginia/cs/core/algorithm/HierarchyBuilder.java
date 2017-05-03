package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by cutehuazai on 5/3/17.
 */
public class HierarchyBuilder {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository repository;

    private Hierarchy hierarchy;
    private String outputFile;

    public HierarchyBuilder(String outputFile){
        this.outputFile = outputFile;
    }

    public void buildHierachy() throws Exception {
        File file = new File(outputFile);
        if(file.exists()){
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(new FileInputStream(file));
            try {
                hierarchy = (Hierarchy)ois.readObject();
                return;
            }finally {
                if(ois != null){
                    ois.close();
                }
            }
        }
        List<Topic> topics = generateTopKTopics();
        TreeSet<Tag> tags = tagRepository.getRankingTags(topics);

        Tag top = tags.pollFirst();
        hierarchy = new Hierarchy();
        HierarchyNode firstNode = new HierarchyNode(top);
        hierarchy.addNode(hierarchy.getRoot(), firstNode);
        double rootToFirstLevel = hierarchy.getMaxRootToFirstLevel();

        while (tags.size() > 0) {
            Tag t = tags.pollFirst();
            HierarchyNode tPrime = new HierarchyNode(t);
            List<HierarchyNode> candidateParents = hierarchy.getCandidateParents();
            HierarchyNode newParent = new HierarchyNode();
            double minDiff = 0;
            for (HierarchyNode tk : candidateParents) {
                double diff = hierarchy.getMinCostDiff(tk);
                if (diff < minDiff) {
                    minDiff = diff;
                    newParent = tk;
                }
            }
            hierarchy.addNode(newParent, tPrime);
            rootToFirstLevel = hierarchy.getMaxRootToFirstLevel();
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(outputFile)));
            oos.writeObject(hierarchy);
        } finally {
            if(oos != null){
                oos.close();
            }
        }
    }

    private List<Topic> generateTopKTopics(){
        return null;
    }

    public Hierarchy getHierarchy(){
        if(hierarchy == null){

        }
        return hierarchy;
    }
}
