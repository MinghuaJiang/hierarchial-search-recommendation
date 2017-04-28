package edu.virginia.cs.solr.service;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cutehuazai on 4/28/17.
 */
@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepository repository;

    @Override
    public Tag findById(String id) {
        return repository.findOne(id);
    }

    @Override
    public Tag findByName(String tagName) {
        return repository.findByName(tagName);
    }
}
