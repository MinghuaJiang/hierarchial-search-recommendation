package edu.virginia.cs.solr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * Created by cutehuazai on 5/1/17.
 */
@SolrDocument(solrCoreName = "tags")
public class Tag {
    @Id
    @Indexed(name = "id")
    private String id;
    @Indexed(name = "tagName_t")
    private String tagName;
    @Indexed(name = "count_i")
    private String tagDistinctCount;
    @Indexed(name = "wikiId_s")
    private String wikiId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDistinctCount() {
        return tagDistinctCount;
    }

    public void setTagDistinctCount(String tagDistinctCount) {
        this.tagDistinctCount = tagDistinctCount;
    }

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