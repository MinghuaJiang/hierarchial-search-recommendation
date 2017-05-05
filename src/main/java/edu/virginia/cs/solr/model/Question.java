package edu.virginia.cs.solr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cutehuazai on 5/1/17.
 */
@SolrDocument(solrCoreName = "posts")
public class Question implements Serializable {
    @Id
    @Indexed(name = "id")
    @XmlAttribute(name="Id")
    private String id;

    @Indexed(name = "tagName_ss")
    @XmlAttribute(name="Tags")
    private List<String> tags;

    @XmlAttribute(name="Title")
    @Indexed(name = "title_t")
    private String questionTitle;

    @XmlAttribute(name="Body")
    @Indexed(name = "body_t")
    private String questionBody;

    @XmlAttribute(name="PostTypeId")
    @Indexed(name = "post_type_s")
    private String postTypeId;

    @XmlAttribute(name="AnswerCount")
    @Indexed(name = "answers_l")
    private long answerCount;

    @XmlAttribute(name="Score")
    @Indexed(name = "vote_l")
    private long vote;

    @XmlAttribute(name="ViewCount")
    @Indexed(name = "view_l")
    private long viewCount;

    @XmlAttribute(name="FavoriteCount")
    @Indexed(name = "favorite_l")
    private long favoriteCount;

    @XmlAttribute(name="CreationDate")
    @Indexed(name = "creation_dt")
    private String creationDate;

    @XmlAttribute(name="LastActivityDate")
    @Indexed(name="modification_dt")
    private String lastModifiedDate;

    private Topic topic;

    private double searchScore;

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public String getPostTypeId() {
        return postTypeId;
    }

    public long getAnswerCount() {
        return answerCount;
    }

    public long getVote() {
        return vote;
    }

    public long getViewCount() {
        return viewCount;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    public void setAnswerCount(long answerCount) {
        this.answerCount = answerCount;
    }

    public void setVote(long vote) {
        this.vote = vote;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", tags=" + tags +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionBody='" + questionBody + '\'' +
                ", postTypeId='" + postTypeId + '\'' +
                ", answerCount=" + answerCount +
                ", vote=" + vote +
                ", viewCount=" + viewCount +
                ", favoriteCount=" + favoriteCount +
                ", creationDate='" + creationDate + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                ", topic=" + topic +
                ", searchScore=" + searchScore +
                '}';
    }

    public double getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(double searchScore) {
        this.searchScore = searchScore;
    }

    public double getScore() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date date = new Date();
        long creationDiffInSecond = (date.getTime() - format.parse(this.creationDate).getTime())/1000;
        long modificationDiffInSecond = (date.getTime() - format.parse(this.lastModifiedDate).getTime())/1000;
        double count = Math.log10(viewCount)  * 4 + vote * answerCount / 5;
        double age = Math.pow((creationDiffInSecond / 2 + modificationDiffInSecond / 2 + 1), 1.5);
        return count / age;
    }
}
