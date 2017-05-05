package edu.virginia.cs.search.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.solr.model.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by cutehuazai on 4/30/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionByTagTest {
    //@Autowired
    //private QuestionSearch search;

    @Test
    public void testQuestionByTag() throws Exception{
        //System.out.println(search.getMatchCountOfTwoTags("java","hadoop"));
        ObjectInputStream ois = null;
        ois = new ObjectInputStream(new FileInputStream("top_k_queue.dat"));
        try {
            List<Topic> topicList = (List<Topic>) ois.readObject();
            for(Topic topic:topicList){
                System.out.println(topic);
            }
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
}
