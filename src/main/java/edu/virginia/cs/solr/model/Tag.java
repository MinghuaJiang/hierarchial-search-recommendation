package edu.virginia.cs.solr.model;

import edu.virginia.cs.solr.repository.QuestionRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by cutehuazai on 5/1/17.
 */
@XmlRootElement(name = "row")
@SolrDocument(solrCoreName = "tags")
public class Tag implements Serializable {
    @Id
    @Indexed(name = "id")
    private String id;
    @Indexed(name = "tagName_s")
    private String tagName;
    @Indexed(name = "count_l")
    private long tagDistinctCount;
    @Indexed(name = "wikiId_s")
    private String wikiId;
    private long tagRawCount;
    private double score;

    @XmlAttribute(name = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "TagName")
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @XmlAttribute(name = "Count")
    public long getTagDistinctCount() {
        return tagDistinctCount;
    }

    public void setTagDistinctCount(long tagDistinctCount) {
        this.tagDistinctCount = tagDistinctCount;
    }

    @XmlAttribute(name = "WikiPostId")
    public String getWikiId() {
        return wikiId;
    }

    public void setWikiId(String wikiId) {
        this.wikiId = wikiId;
    }

    public double getScore() {
        return score;
    }

    public void calculateITScore(Set<Topic> topics, int tagSize, QuestionRepository repository) {
        double[] topic_pro_arr = getTopKTopicProbArr(topics, tagSize, repository);
        this.score = (tagDistinctCount * Math.log(tagRawCount + 1)) * this.getTagEntropy(topic_pro_arr);
    }

    public void normalizeScore(double maxScore) {
        this.score = this.score / maxScore;
    }

    private double[] getTopKTopicProbArr(Set<Topic> topics, int tagSize, QuestionRepository repository) {
        double[] topicProb = new double[topics.size()];

        Topic[] topKTopicsArr = topics.toArray(new Topic[0]);
        for (int i = 0; i < topics.size(); i++) {
            topicProb[i] = this.co_occurrence(topKTopicsArr[i], repository) / tagSize;
        }
        return topicProb;
    }

    public long co_occurrence(Topic topic, QuestionRepository repository) {
        return  repository.getQuestionsCocurrence(tagName, topic.getTopicName());
    }

    private double getTagEntropy(double[] topic_pro_arr) {
        double entropy = 0;
        for (int i = 0; i < topic_pro_arr.length;i++){
            entropy += topic_pro_arr[i] * Math.log(topic_pro_arr[i]);
        }
        return entropy * (-1);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                ", tagDistinctCount=" + tagDistinctCount +
                ", wikiId='" + wikiId + '\'' +
                ", tagRawCount=" + tagRawCount +
                '}';
    }

    public void setTagRawCount(long tagRawCount) {
        this.tagRawCount = tagRawCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return tagName != null ? tagName.equals(tag.tagName) : tag.tagName == null;

    }

    @Override
    public int hashCode() {
        return tagName != null ? tagName.hashCode() : 0;
    }

}