package edu.virginia.cs.solr.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
@XmlRootElement(name = "postlinks")
public class PostLinks {
    private List<PostLink> postLinks;

    @XmlElement(name = "row")
    public List<PostLink> getPostLinks() {
        return postLinks;
    }

    public void setPostLinks(List<PostLink> postLinks) {
        this.postLinks = postLinks;
    }
}
