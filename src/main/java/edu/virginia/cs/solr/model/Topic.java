package edu.virginia.cs.solr.model;

import java.io.Serializable;

/**
 * Created by cutehuazai on 5/3/17.
 */
public class Topic implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        return topicName != null ? topicName.equals(topic.topicName) : topic.topicName == null;

    }

    @Override
    public int hashCode() {
        return topicName != null ? topicName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicName='" + topicName + '\'' +
                ", documentFrequency=" + documentFrequency +
                '}';
    }
}
