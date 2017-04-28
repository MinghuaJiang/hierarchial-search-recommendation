package edu.virginia.cs.solr.model;

import edu.virginia.cs.solr.model.Comment;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
@XmlRootElement( name = "comments" )
public class Comments {
    private List<Comment> comments;

    @XmlElement(name = "row")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
