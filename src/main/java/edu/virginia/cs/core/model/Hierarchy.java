package edu.virginia.cs.core.model;

import edu.virginia.cs.solr.repository.QuestionRepository;

import java.io.Serializable;
import java.util.*;

/**
 * Created by VINCENTWEN on 5/1/17.
 */
public class Hierarchy implements Serializable {
    private HierarchyNode root;
    private int totalNodes;
    private int depth;

    private TreeMap<String, HierarchyNode> tagTree;



    public Hierarchy() {
        this.root = new HierarchyNode();
        root.setParentNode(null);
        root.setChildren(new ArrayList<>());
        root.setLevel(0);
        tagTree = new TreeMap<>();
        tagTree.put("root", root);
    }

    public void addNode(HierarchyNode parent, HierarchyNode newNode) {
        newNode.setParentNode(parent);
        parent.addChildren(newNode);
        totalNodes++;
        if (parent.getLevel() == this.depth) {
            this.depth++;
            newNode.setLevel(this.depth);
        } else {
            newNode.setLevel(this.depth);
        }
        tagTree.put(newNode.getName(), newNode);
    }

    public void addNodeToGraph(HierarchyNode node) {

    }

    public HierarchyNode getHierachyNode(String name) {
        return tagTree.get(name);
    }

    public HierarchyNode getRoot() {
        return root;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }
}
