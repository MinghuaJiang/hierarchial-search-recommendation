package edu.virginia.cs.solr.parser;


import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Tags;
import edu.virginia.cs.solr.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cutehuazai on 5/1/17.
 */
public class JaxbXMLParser {
    public static List<Tag> parseTag(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Tags.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xmlFile = new File(xml);
        Tags tags = (Tags) unmarshaller.unmarshal(xmlFile);
        return tags.getTags();
    }
}
