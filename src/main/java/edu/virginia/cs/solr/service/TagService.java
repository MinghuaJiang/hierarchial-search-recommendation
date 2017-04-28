package edu.virginia.cs.solr.service;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.parser.DataSetParser;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */

public interface TagService {
    public Tag findById(String id);
    public Tag findByName(String tagName);
}
