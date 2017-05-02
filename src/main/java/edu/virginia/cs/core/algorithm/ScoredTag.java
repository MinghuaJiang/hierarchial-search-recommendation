package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.solr.model.Tag;

/**
 * Created by VINCENTWEN on 4/30/17.
 */
public class ScoredTag{
    private Tag tag;
    private double score;

    public long co_occurrence(Topic topic){
        return 1;
    }

    public ScoredTag(Tag tag) {
        this.tag = tag;
    }

    public String getTagName(){
        return tag.getTagName();
    }

    public long getTagDistinctCount() {
        return tag.getTagDistinctCount();
    }

    public long getTagRawCount(){
        return 100;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
