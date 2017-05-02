package edu.virginia.cs.core.algorithm;

/**
 * Created by VINCENTWEN on 4/30/17.
 */
public class Topic {
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    String topicName;

    public long getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(Long topicCount) {
        this.topicCount = topicCount;
    }

    Long topicCount;

    public Topic(String name, Long count){
        this.topicName = name;
        this.topicCount = count;
    }

}
