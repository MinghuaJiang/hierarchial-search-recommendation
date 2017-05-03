package edu.virginia.cs.solr.parser;

import edu.virginia.cs.solr.index.IndexService;
import edu.virginia.cs.solr.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cutehuazai on 5/1/17.
 */
public class StreamBasedXMLParser {
    @Autowired
    private IndexService indexService;
    @Autowired
    private Environment environment;

    public void parsePosts(){
        XMLInputFactory factory = XMLInputFactory.newInstance();
        List<Question> result = new ArrayList<Question>();
        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileInputStream(environment.getRequiredProperty("dataset.postfile")));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("row")) {
                        Question question = new Question();

                        Field[] fields = Question.class.getDeclaredFields();

                        for (Field field : fields) {
                            String name = field.getAnnotation(XmlAttribute.class).name();
                            Attribute attr = startElement.getAttributeByName(new QName(name));
                            if (attr != null) {
                                ReflectionUtils.makeAccessible(field);
                                if (field.getType() == String.class) {
                                    try{
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        ReflectionUtils.setField(field, question, outputFormat.format(format.parse(attr.getValue())));
                                    }catch (Exception e){
                                        ReflectionUtils.setField(field, question, attr.getValue());
                                    }
                                } else if (field.getType() == List.class) {
                                    ReflectionUtils.setField(field, question, Arrays.asList(attr.getValue().split("><")).stream().map(x -> x.replace("<", "").replace(">", "")).collect(Collectors.toList()));
                                }  else {
                                    ReflectionUtils.setField(field, question, Long.valueOf(attr.getValue()));
                                }
                            }
                        }
                        if(question.getPostTypeId().equals("1")){
                            result.add(question);
                        }
                        if(result.size() == 20000){
                            indexService.buildQuestionIndex(result);
                            result = new ArrayList<Question>();
                        }
                    }
                }
            }
            indexService.buildQuestionIndex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
