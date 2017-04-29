package edu.virginia.cs.core.model;

import edu.virginia.cs.solr.model.Tag;

/**
 * Created by cutehuazai on 4/28/17.
 */
public class TagVector {
    private Tag tag;
    private int[] vector;

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
        return score / (Long.valueOf(tag.getCount()) * Long.valueOf(v.tag.getCount()));
    }

    public int getNumOfFeatures(){
        return vector.length;
    }

    public int getFeatureValue(int i){
        return vector[i];
    }
}
