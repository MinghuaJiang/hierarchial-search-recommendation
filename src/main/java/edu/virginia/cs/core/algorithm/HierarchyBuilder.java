package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.*;
import java.util.*;

/**
 * Created by cutehuazai on 5/3/17.
 */
public class HierarchyBuilder {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository questionRepository;

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
        List<Topic> topics = generateTopKTopics(100);
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

    private List<Topic> generateTopKTopics(int k){
        List<Topic> result = new ArrayList<Topic>();
        Page<Question> questions = questionRepository.getAllQuestions(0);
        Queue<Topic> queue = new PriorityQueue<Topic>(k, new Comparator<Topic>() {
            @Override
            public int compare(Topic t1, Topic t2) {
                return (int)(t1.getDocumentFrequency() - t2.getDocumentFrequency());
            }
        });
        Set<Topic> topics = new HashSet<Topic>();
        for(Question question : questions){
            Topic topic = question.getTopic();
            if(topics.contains(topic)){
                continue;
            }
            topics.add(topic);
            if(queue.size() < k){
                queue.offer(topic);
            }else{
                if(topic.getDocumentFrequency() < queue.peek().getDocumentFrequency()){
                    continue;
                }
                queue.poll();
                queue.offer(topic);
            }
        }
        while (questions.hasNext()){
            int page = questions.nextPageable().getPageNumber();
            questions = questionRepository.getAllQuestions(page);
            for(Question question : questions){
                Topic topic = question.getTopic();
                if(topics.contains(topic)){
                    continue;
                }
                topics.add(topic);
                if(queue.size() < k){
                    queue.offer(topic);
                }else{
                    if(topic.getDocumentFrequency() < queue.peek().getDocumentFrequency()){
                        continue;
                    }
                    queue.poll();
                    queue.offer(topic);
                }
            }

        }
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }

    public Hierarchy getHierarchy(){
        if(hierarchy == null){

        }
        return hierarchy;
    }
}
