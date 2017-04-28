package edu.virginia.cs.solr.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cutehuazai on 4/28/17.
 */
@XmlRootElement(name="row")
public class PostLink {
    private String id;
    private String postId;
    private String relatedPostId;
    private String linkTypeId;
    @XmlAttribute(name="Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlAttribute(name="PostId")
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    @XmlAttribute(name="RelatedPostId")
    public String getRelatedPostId() {
        return relatedPostId;
    }

    public void setRelatedPostId(String relatedPostId) {
        this.relatedPostId = relatedPostId;
    }

    @XmlAttribute(name="LinkTypeId")
    public String getLinkTypeId() {
        return linkTypeId;
    }

    public void setLinkTypeId(String linkTypeId) {
        this.linkTypeId = linkTypeId;
    }

    @Override
    public String toString() {
        return "PostLink{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", relatedPostId='" + relatedPostId + '\'' +
                ", linkTypeId='" + linkTypeId + '\'' +
                '}';
    }
}
