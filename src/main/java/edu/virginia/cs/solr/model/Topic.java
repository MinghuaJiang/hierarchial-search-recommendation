package edu.virginia.cs.solr.model;

/**
 * Created by cutehuazai on 5/3/17.
 */
public class Topic {
    private String topicName;
    private long documentFrequency;

    public Topic(String name, Long count){
        this.topicName = name;
        this.documentFrequency = count;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getDocumentFrequency() {
        return documentFrequency;
    }

    public void setDocumentFrequency(Long documentFrequency) {
        this.documentFrequency = documentFrequency;
    }
}
