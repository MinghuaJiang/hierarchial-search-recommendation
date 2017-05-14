package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import edu.virginia.cs.solr.model.TopicComparator;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.*;
import java.util.*;

/**
 * Created by cutehuazai on 5/3/17.
 */
public class HierarchyBuilder{
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Hierarchy hierarchy;
    private String hierarchyFile;
    private LinkedHashMap<Integer, List<HierarchyNode>> buckets;
    private double epsilon = 0.7;
    private double radiusParameter = 10;

    public HierarchyBuilder(String hierarchyFile) {
        this.hierarchyFile = hierarchyFile;
        this.buckets = new LinkedHashMap<>();
    }

    public void buildHierarchy() throws Exception{
        System.out.println("build first level: real roots");
        Set<Topic> topics = generateTopKTopics(100);
        TreeSet<Tag> tags = getRankingTags(topics);
        List<HierarchyNode> HNodeList = new ArrayList<>();
        Tag top = tags.pollFirst();
        HierarchyNode firstNode = new HierarchyNode(top);

        hierarchy = new Hierarchy();
        hierarchy.addNode(hierarchy.getRoot(), firstNode);
        HNodeList.add(firstNode);
        //insert at the minimal parent
        for(Tag tag : tags){
            HierarchyNode newNode = new HierarchyNode(tag);
            HierarchyNode minParent = null;
            double minSimilarity = Double.MAX_VALUE;
            for(HierarchyNode node : HNodeList){
                double similarity = getEdgeWeight(node, newNode);
                if(similarity < minSimilarity){
                    minSimilarity = similarity;
                    minParent = node;
                }
            }
            hierarchy.addNode(minParent, newNode);
            HNodeList.add(newNode);
            System.out.println(minParent.getName());
        }
//        System.out.println("finished building");
    }

//    public void buildHierarchy() throws Exception {
//        File file = new File(hierarchyFile);
//        if (file.exists()) {
//            ObjectInputStream ois = null;
//            ois = new ObjectInputStream(new FileInputStream(file));
//            try {
//                hierarchy = (Hierarchy) ois.readObject();
//                return;
//            } finally {
//                if (ois != null) {
//                    ois.close();
//                }
//            }
//        }
//        System.out.println("start to build k topics");
//        Set<Topic> topics = generateTopKTopics(100);
//        System.out.println("start to build ranking tags");
//        TreeSet<Tag> tags = getRankingTags(topics);
//        System.out.println("start to build hierarchy");
//        Tag top = tags.pollFirst();
//        hierarchy = new Hierarchy();
//        if(!buckets.containsKey(0)){
//            buckets.put(0, new ArrayList<HierarchyNode>());
//        }
//        buckets.get(0).add(hierarchy.getRoot());
//        HierarchyNode firstNode = new HierarchyNode(top);
//        hierarchy.addNode(hierarchy.getRoot(), firstNode);
//        if(!buckets.containsKey(firstNode.getLevel())){
//            buckets.put(firstNode.getLevel(), new ArrayList<HierarchyNode>());
//        }
//        buckets.get(firstNode.getLevel()).add(firstNode);
//
//        if (firstNode.getLevel() == 1) {
//            hierarchy.getRoot().setMaxToRoot(getMaxRootToFirstLevel());
//        }
//        int count = 0;
//        while (tags.size() > 0) {
//            Tag t = tags.pollFirst();
//            count++;
//            if(count % 200 == 0){
//                System.out.println(count + " tag passed");
//                ObjectOutputStream oos = null;
//                try {
//                    oos = new ObjectOutputStream(new FileOutputStream(new File(hierarchyFile)));
//                    oos.writeObject(hierarchy);
//                } finally {
//                    if (oos != null) {
//                        oos.close();
//                    }
//                }
//            }
//
//            HierarchyNode tPrime = new HierarchyNode(t);
//            List<HierarchyNode> candidateParents = getCandidateParents(hierarchy.getDepth());
//            HierarchyNode newParent = null;
//            double minDiff = Double.MAX_VALUE;
//            for (HierarchyNode tk : candidateParents) {
//                double diff = getMinCostDiff(tk, hierarchy) + getMinDistance(tk, tPrime);
//                if (diff < minDiff) {
//                    minDiff = diff;
//                    newParent = tk;
//                }
//            }
//            hierarchy.addNode(newParent, tPrime);
//            if(!buckets.containsKey(tPrime.getLevel())){
//                buckets.put(tPrime.getLevel(), new ArrayList<HierarchyNode>());
//            }
//            buckets.get(tPrime.getLevel()).add(tPrime);
//
//            if (tPrime.getLevel() == 1) {
//                hierarchy.getRoot().setMaxToRoot(getMaxRootToFirstLevel());
//            }
//        }
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(new FileOutputStream(new File(hierarchyFile)));
//            oos.writeObject(hierarchy);
//        } finally {
//            if (oos != null) {
//                oos.close();
//            }
//        }
//    }

    private TreeSet<Tag> getRankingTags(Set<Topic> topics) throws Exception {
        File file = new File("ranking.dat");
        if (file.exists()) {
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(new FileInputStream(file));
            return (TreeSet<Tag>) ois.readObject();
        }
        TreeSet<Tag> result = tagRepository.getRankingTags(topics);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("ranking.dat")));
            oos.writeObject(result);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
        return result;
    }

    private double getMinCostDiff(HierarchyNode t, Hierarchy hierarchy) {
        List<Double> costFunction = new ArrayList<>();
        for (int i : buckets.keySet()) {
            for (int j = 0; j < buckets.get(i).size(); j++) {
//                double temp = getMinDistance(t, buckets.get(i).get(j)) + this.epsilon * hierarchy.getDepth() / hierarchy.getTotalNodes();
                double temp = getMinDistance(t, buckets.get(i).get(j));
                costFunction.add(temp);
            }
        }
        return Collections.min(costFunction);
    }

    private double getMinDistance(HierarchyNode n1, HierarchyNode n2) {
        if(n1.getLevel() == 1 && n2.getLevel() == 1){
            return 2 * hierarchy.getRoot().getMaxToRoot();
        }
        Map<HierarchyNode, HierarchyNode> ancestors = new HashMap<>();
        Map<HierarchyNode, Double> cache = new HashMap<>();
        double distance = 0.0;
        cache.put(n1, 0.0);
        while (n1.getParentNode() != null) {
            ancestors.put(n1, n1.getParentNode());
            distance += getEdgeWeight(n1, n1.getParentNode());
            cache.put(n1.getParentNode(), distance);
            n1 = n1.getParentNode();
        }
        double distance_d2 = 0.0;
        while (n2.getParentNode() != null && !ancestors.containsKey(n2)) {
            distance_d2 += getEdgeWeight(n2, n2.getParentNode());
            n2 = n2.getParentNode();
        }
        return distance_d2 + cache.getOrDefault(n2, 0.0);
    }

    private double getMaxRootToFirstLevel() {
        List<HierarchyNode> realRoots = hierarchy.getRoot().getChildren();
        if(realRoots.size() <= 1){
            return 0;
        }
        if(realRoots.size() == 2){
            return getEdgeWeight(realRoots.get(0), realRoots.get(1));
        }
        double maxToRoot = hierarchy.getRoot().getMaxToRoot();

        HierarchyNode newNode = realRoots.get(realRoots.size() - 1);
        for (int i = 0; i < realRoots.size() - 1; i++) {
            maxToRoot = Math.max(getEdgeWeight(realRoots.get(i), newNode), maxToRoot);
        }
        return maxToRoot;
    }

    public List<HierarchyNode> getCandidateParents(int depth) {
        List<HierarchyNode> candidateParents = new ArrayList<>();
        candidateParents.addAll(getNodesOfLevel(depth - 1));
        candidateParents.addAll(getNodesOfLevel(depth));
        return candidateParents;
    }

    public List<HierarchyNode> getNodesOfLevel(int level) {
        return this.buckets.get(level);
    }

    private double getEdgeWeight(HierarchyNode n1, HierarchyNode n2) {
        if(n1 == hierarchy.getRoot() || n2 == hierarchy.getRoot()){
            return hierarchy.getRoot().getMaxToRoot();
        }
        long[] diff = questionRepository.getQuestionsByTagDifference(n1.getName(), n2.getName());
        return Math.exp(-1 * (Math.pow(diff[0], 2) + Math.pow(diff[1], 2)) / Math.pow(radiusParameter,2));
    }

    private Set<Topic> generateTopKTopics(int k) throws Exception {
        Set<Topic> result = new HashSet<Topic>();
        File file = new File("topic.dat");
        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                result = new HashSet<Topic>();
                result.addAll((List<Topic>) ois.readObject());
                return result;
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        }

        Page<Question> questions = questionRepository.getAllQuestions(0);
        Queue<Topic> queue = new PriorityQueue<Topic>(k, new TopicComparator());
        int count = 1;
        Set<Topic> topics = new HashSet<Topic>();
        for (Question question : questions) {
            if(count % 10000 == 0){
                System.out.println(count + " question passed");
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream("top_k_queue.dat"));
                    oos.writeObject(queue);
                } finally {
                    if (oos != null) {
                        oos.close();
                    }
                }
            }
            count++;
            Topic topic = question.getTopic();
            if (topics.contains(topic)) {
                continue;
            }
            topics.add(topic);
            if (queue.size() < k) {
                queue.offer(topic);
            } else {
                if (topic.getDocumentFrequency() < queue.peek().getDocumentFrequency()) {
                    continue;
                }
                queue.poll();
                queue.offer(topic);
            }
        }
        while (questions.hasNext()) {
            int page = questions.nextPageable().getPageNumber();
            questions = questionRepository.getAllQuestions(page);
            for (Question question : questions) {
                if(count % 10000 == 0){
                    System.out.println(count + " question passed");
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(new FileOutputStream("top_k_queue.dat"));
                        oos.writeObject(queue);
                    } finally {
                        if (oos != null) {
                            oos.close();
                        }
                    }
                }
                Topic topic = question.getTopic();
                count++;
                if (topics.contains(topic)) {
                    continue;
                }
                topics.add(topic);
                if (queue.size() < k) {
                    queue.offer(topic);
                } else {
                    if (topic.getDocumentFrequency() < queue.peek().getDocumentFrequency()) {
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
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(result);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
        return result;
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }
}
