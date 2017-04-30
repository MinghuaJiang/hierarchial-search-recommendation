package edu.virginia.cs.core.model;


import com.google.code.stackexchange.schema.Tag;

import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
public class TagVector {
    private Tag tag;
    private int[] vector;
    private List<Tag> neighbors;


    public void setVector(int[] vector) {
        this.vector = vector;
    }

    public Tag getTag() {
        return tag;
    }

    public List<Tag> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Tag> neighbors) {
        this.neighbors = neighbors;
    }

    public TagVector(Tag tag, int size){
        this.tag = tag;
        this.vector = new int[size];
    }

    public double calculateCosineSimularity(TagVector v){
        if(vector.length != v.getNumOfFeatures()){
            throw new IllegalStateException("Two vectors must have same number of features");
        }
        double score = 0.0;
        for(int i = 0; i < vector.length;i++){
            score += vector[i] * v.getFeatureValue(i);
        }
        return score / (Math.sqrt(Integer.valueOf(tag.getCount())) * Math.sqrt(Integer.valueOf(v.tag.getCount())));
    }

    public int getNumOfFeatures(){
        return vector.length;
    }

    public int getFeatureValue(int i)
    {
        return vector[i];
    }

    public int getCount(){
        return Integer.parseInt(tag.getCount());
    }

}
