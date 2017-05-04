package edu.virginia.cs.core.model;

import edu.virginia.cs.solr.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VINCENTWEN on 5/1/17.
 */
public class HierarchyNode implements Serializable {
    private Tag node;
    private int level;
    private HierarchyNode parentNode;
    private List<HierarchyNode> children;
    private double weightToParent;

    public HierarchyNode() {
        this(null);
    };

    public HierarchyNode(Tag node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public double getWeightToParent() {
        return weightToParent;
    }

    public void setWeightToParent(double weight2Parent) {
        this.weightToParent = weight2Parent;
    }

    public HierarchyNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(HierarchyNode parentNode) {
        this.parentNode = parentNode;
    }

    public void setChildren(List<HierarchyNode> children) {
        this.children = children;
    }

    public List<HierarchyNode> getChildren() {
        return children;
    }

    public void addChildren(HierarchyNode newNode){
        this.children.add(newNode);
    }

    public String getName(){
        if(node == null) return "root";
        return node.getTagName();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getScore(){
        return this.node.getScore();
    }
}
