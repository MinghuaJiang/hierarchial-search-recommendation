package edu.virginia.cs.core.algorithm;

import java.util.*;

/**
 * Created by VINCENTWEN on 5/1/17.
 */
public class Hierarchy {
    private HierarchyNode root;
    private int totalNodes;
    private int depth;
    private double epsilon;
    private double rootToFirstLevel;
    private List<List<HierarchyNode>> buckets;
    private TreeMap<String, HierarchyNode> tagTree;

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public Hierarchy(HierarchyBuild HB){
        this.root = new HierarchyNode();
        root.setParentNode(null);
        root.setChildren(new ArrayList<>());
        root.setLevel(0);
        this.tagTree = new TreeMap<>();
        this.buckets = new ArrayList<>();
        tagTree.put("root", root);
        TreeSet<ScoredTag> T = HB.getScoredTagsTreeSet();

        ScoredTag top = T.pollFirst();
        HierarchyNode firstNode = new HierarchyNode(top);
        addNode(root, firstNode);
        rootToFirstLevel = getMaxRootToFirstLevel(root);
        while(T.size() > 0){
            ScoredTag t = T.pollFirst();
            HierarchyNode tPrime = new HierarchyNode(t);
            List<HierarchyNode> candidateParents = getCandidateParents();
            HierarchyNode newParent = new HierarchyNode();
            double minDiff = 0;
            for(HierarchyNode tk : candidateParents){
                double diff = getMinCostDiff(tk);
                if(diff < minDiff){
                    minDiff = diff;
                    newParent = tk;
                }
            }
            addNode(newParent, tPrime);
            rootToFirstLevel = getMaxRootToFirstLevel(root);
        }
    }

    private double getMinCostDiff(HierarchyNode t){
        List<Double> costFunction = new ArrayList<>();
        for(int i=0; i<buckets.size(); i++){
            for(int j=0; j<buckets.get(i).size(); j++){
                double temp = getMinDistance(t, buckets.get(i).get(j)) + this.epsilon * this.depth / this.totalNodes;
                costFunction.add(temp);
            }
        }
        return Collections.min(costFunction);
    }

    public void addNode(HierarchyNode parent, HierarchyNode newNode){
        newNode.setParentNode(parent);
        parent.addChildren(newNode);
        totalNodes++;
        if(parent.getLevel() == this.depth){
            this.depth++;
            newNode.setLevel(this.depth);
        }else{
            newNode.setLevel(this.depth - 1);
        }
        buckets.get(newNode.getLevel()).add(newNode);
        tagTree.put(newNode.getName(), newNode);
    }

    public double getMaxRootToFirstLevel(HierarchyNode root){
        List<HierarchyNode> realRoots = root.getChildren();
        List<Double> listOfDistance = new ArrayList<>();
        for(int i=0; i<realRoots.size(); i++){
            for(int j=0; j<realRoots.size(); j++){
                listOfDistance.add(getEdgeWeight(realRoots.get(i), realRoots.get(j)));
            }
        }
        return Collections.max(listOfDistance);
    }

    public double getMinDistance(HierarchyNode n1, HierarchyNode n2){
        HierarchyNode commonParent = getCommonParent(n1, n2);
        double minDistance = 0;
        while(n1.getParentNode() != commonParent){
            minDistance += getEdgeWeight(n1, n1.getParentNode());
            n1 = n1.getParentNode();
        }
        minDistance += getEdgeWeight(n1, commonParent);
        while(n2.getParentNode() != commonParent){
            minDistance += getEdgeWeight(n2, n2.getParentNode());
            n2 = n2.getParentNode();
        }
        minDistance += getEdgeWeight(n2, commonParent);
        return minDistance;
    }

    public HierarchyNode getCommonParent(HierarchyNode n1, HierarchyNode n2){
        Map<HierarchyNode, HierarchyNode> ancestors = new HashMap<>();
        while(n1.getParentNode() != null){
            ancestors.put(n1, n1.getParentNode());
            n1 = n1.getParentNode();
        }
        while(!ancestors.containsKey(n2)){
            n2 = ancestors.get(n2);
        }
        return n2;
    }

    public double getEdgeWeight(HierarchyNode n1, HierarchyNode n2){
        //
        return 0.5;
    }

    public void addNodeToGraph(HierarchyNode node){

    }

    public List<HierarchyNode> getCandidateParents(){
        List<HierarchyNode> candidateParents = new ArrayList<>();
        candidateParents.addAll(getNodesOfLevel(depth - 1));
        candidateParents.addAll(getNodesOfLevel(depth));
        return candidateParents;
    }

    public List<HierarchyNode> getNodesOfLevel(int level){
        return this.buckets.get(level);
    }

    public TreeMap<String, HierarchyNode> getTagTree() {
        return tagTree;
    }
}
