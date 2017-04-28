package edu.virginia.cs.solr.parser;

import edu.virginia.cs.solr.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by cutehuazai on 4/27/17.
 */
public class DataSetParser {
    public static List<Tag> parseTag(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Tags.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xmlFile = new File(xml);
        Tags tags = (Tags) unmarshaller.unmarshal(xmlFile);
        return tags.getTags();
    }



    public static void main(String[] args) throws Exception{
        List<Tag> result = DataSetParser.parseTag("/home/cutehuazai/mj2eu/Tags.xml");
        result.forEach(System.out::println);
    }
}
