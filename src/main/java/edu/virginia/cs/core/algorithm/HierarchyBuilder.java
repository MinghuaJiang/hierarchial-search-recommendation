package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.TagRepository;
import org.apache.solr.client.solrj.SolrServerException;
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
    private String hierarchyFile;
    private List<List<HierarchyNode>> buckets;
    private double epsilon = 10;
    private double radiusParamter = 100;

    public HierarchyBuilder(String hierarchyFile) {
        this.hierarchyFile = hierarchyFile;
        this.buckets = new ArrayList<>();
    }

    public void buildHierarchy() throws Exception {
        File file = new File(hierarchyFile);
        if (file.exists()) {
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(new FileInputStream(file));
            try {
                hierarchy = (Hierarchy) ois.readObject();
                return;
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        }
        System.out.println("start to build k topics");
        List<Topic> topics = generateTopKTopics(100);
        System.out.println("start to build ranking tags");
        TreeSet<Tag> tags = getRankingTags(topics);
        System.out.println("start to build hierarchy");
        Tag top = tags.pollFirst();
        hierarchy = new Hierarchy();
        HierarchyNode firstNode = new HierarchyNode(top);
        hierarchy.addNode(hierarchy.getRoot(), firstNode);
        buckets.get(firstNode.getLevel()).add(firstNode);
        double rootToFirstLevel = getMaxRootToFirstLevel();
        if (firstNode.getLevel() == 1) {
            firstNode.setWeightToParent(rootToFirstLevel);
        }
        while (tags.size() > 0) {
            Tag t = tags.pollFirst();
            HierarchyNode tPrime = new HierarchyNode(t);
            List<HierarchyNode> candidateParents = getCandidateParents(hierarchy.getDepth());
            HierarchyNode newParent = new HierarchyNode();
            double minDiff = 0;
            for (HierarchyNode tk : candidateParents) {
                double diff = getMinCostDiff(tk, hierarchy);
                if (diff < minDiff) {
                    minDiff = diff;
                    newParent = tk;
                }
            }
            hierarchy.addNode(newParent, tPrime);
            buckets.get(tPrime.getLevel()).add(tPrime);
            rootToFirstLevel = getMaxRootToFirstLevel();
            if (tPrime.getLevel() == 1) {
                tPrime.setWeightToParent(rootToFirstLevel);
            }
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(hierarchyFile)));
            oos.writeObject(hierarchy);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    private TreeSet<Tag> getRankingTags(List<Topic> topics) throws Exception {
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
        for (int i = 0; i < buckets.size(); i++) {
            for (int j = 0; j < buckets.get(i).size(); j++) {
                double temp = getMinDistance(t, buckets.get(i).get(j)) + this.epsilon * hierarchy.getDepth() / hierarchy.getTotalNodes();
                costFunction.add(temp);
            }
        }
        return Collections.min(costFunction);
    }

    private double getMinDistance(HierarchyNode n1, HierarchyNode n2) {
        HierarchyNode commonParent = getCommonParent(n1, n2);
        double minDistance = 0;
        while (n1.getParentNode() != commonParent) {
            minDistance += getEdgeWeight(n1, n1.getParentNode());
            n1 = n1.getParentNode();
        }
        minDistance += getEdgeWeight(n1, commonParent);
        while (n2.getParentNode() != commonParent) {
            minDistance += getEdgeWeight(n2, n2.getParentNode());
            n2 = n2.getParentNode();
        }
        minDistance += getEdgeWeight(n2, commonParent);
        return minDistance;
    }

    private double getMaxRootToFirstLevel() {
        List<HierarchyNode> realRoots = hierarchy.getRoot().getChildren();
        List<Double> listOfDistance = new ArrayList<>();
        for (int i = 0; i < realRoots.size(); i++) {
            for (int j = 0; j < realRoots.size(); j++) {
                listOfDistance.add(getEdgeWeight(realRoots.get(i), realRoots.get(j)));
            }
        }
        return Collections.max(listOfDistance);
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
        long[] diff = questionRepository.getQuestionsByTagDifference(n1.getName(), n2.getName());
        return Math.exp(-1 * (Math.pow(diff[0], 2) + Math.pow(diff[1], 2)) / Math.pow(radiusParamter,2));
    }

    private HierarchyNode getCommonParent(HierarchyNode n1, HierarchyNode n2) {
        Map<HierarchyNode, HierarchyNode> ancestors = new HashMap<>();
        while (n1.getParentNode() != null) {
            ancestors.put(n1, n1.getParentNode());
            n1 = n1.getParentNode();
        }
        while (!ancestors.containsKey(n2)) {
            n2 = ancestors.get(n2);
        }
        return n2;
    }


    private List<Topic> generateTopKTopics(int k) throws Exception {
        List<Topic> result = new ArrayList<Topic>();
        File file = new File("topic.dat");
        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                result = (List<Topic>) ois.readObject();
                return result;
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        }

        Page<Question> questions = questionRepository.getAllQuestions(0);
        Queue<Topic> queue = new PriorityQueue<Topic>(k, new Comparator<Topic>() {
            @Override
            public int compare(Topic t1, Topic t2) {
                return (int) (t1.getDocumentFrequency() - t2.getDocumentFrequency());
            }
        });
        Set<Topic> topics = new HashSet<Topic>();
        for (Question question : questions) {
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
