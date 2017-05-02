package edu.virginia.cs.solr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cutehuazai on 5/1/17.
 */
@XmlRootElement(name="row")
@SolrDocument(solrCoreName = "tags")
public class Tag {
    @Id
    @Indexed(name = "id")
    private String id;
    @Indexed(name = "tagName_s")
    private String tagName;
    @Indexed(name = "count_l")
    private long tagDistinctCount;
    @Indexed(name = "wikiId_s")
    private String wikiId;
    @XmlAttribute(name="Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlAttribute(name="TagName")
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    @XmlAttribute(name="Count")
    public long getTagDistinctCount() {
        return tagDistinctCount;
    }

    public void setTagDistinctCount(long tagDistinctCount) {
        this.tagDistinctCount = tagDistinctCount;
    }

    @XmlAttribute(name="WikiPostId")
    public String getWikiId() {
        return wikiId;
    }

    public void setWikiId(String wikiId) {
        this.wikiId = wikiId;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                ", tagDistinctCount='" + tagDistinctCount + '\'' +
                ", wikiId='" + wikiId + '\'' +
                '}';
    }
}