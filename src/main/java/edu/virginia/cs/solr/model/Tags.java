package edu.virginia.cs.solr.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
@XmlRootElement( name = "tags" )
public class Tags {
    private List<Tag> tags;

    @XmlElement(name = "row")
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
