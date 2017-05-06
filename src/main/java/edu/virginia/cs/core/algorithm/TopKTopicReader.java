package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.solr.model.Topic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by cutehuazai on 5/4/17.
 */
public class TopKTopicReader {

    public List<Topic> getTopKTopic() throws Exception {
        ObjectInputStream ois = null;
        ois = new ObjectInputStream(new FileInputStream("top_k_queue.dat"));
        try {
            PriorityQueue<Topic> topicList = (PriorityQueue<Topic>) ois.readObject();
            List<Topic> result = new ArrayList<Topic>();
            while(!topicList.isEmpty()){
                result.add(topicList.poll());
            }
            return result;
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public TopKTopicReader() {
    }

    public static void main(String[] args) throws Exception {
        TopKTopicReader reader = new TopKTopicReader();
        List<Topic> topicList = reader.getTopKTopic();
        topicList.forEach(System.out::println);
        //topicList.forEach(System.out::println);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("topic.dat"));
            oos.writeObject(topicList);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }
}
