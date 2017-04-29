package edu.virginia.cs.solr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cutehuazai on 4/27/17.
 */
@XmlRootElement(name="row")
@SolrDocument(solrCoreName = "hierachy-recommendation")
public class Tag {
    @Id
    @Indexed(name = "id")
    private String id;
    @Indexed(name = "tagName_t")
    private String tagName;
    @Indexed(name = "count_i", searchable = false)
    private String count;
    @Indexed(name = "wikiId_s", searchable = false)
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
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
                ", count='" + count + '\'' +
                ", wikiId='" + wikiId + '\'' +
                '}';
    }
}
