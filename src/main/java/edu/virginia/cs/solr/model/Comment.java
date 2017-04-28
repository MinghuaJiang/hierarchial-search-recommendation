package edu.virginia.cs.solr.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cutehuazai on 4/27/17.
 */
public class Comment {
    private String id;
    private String postId;
    private String score;
    private String text;
    private String creationDate;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", score='" + score + '\'' +
                ", text='" + text + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
