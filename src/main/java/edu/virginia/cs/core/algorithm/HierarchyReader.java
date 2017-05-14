package edu.virginia.cs.core.algorithm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * Created by cutehuazai on 5/6/17.
 */
public class HierarchyReader {
    public Hierarchy readHierachy() throws Exception {
        ObjectInputStream ois = null;
        ois = new ObjectInputStream(new FileInputStream("hierarchy.dat"));
        try {
            Hierarchy hierarchy = (Hierarchy) ois.readObject();

            return hierarchy;
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        HierarchyReader reader = new HierarchyReader();
        Hierarchy hierarchy = reader.readHierachy();
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(getNodeHierachy(hierarchy.getRoot(), 100)));
    }

    private static Map<String, Object> getNodeHierachy(HierarchyNode central, int count) {
        List<Map<String, Integer>> result = new ArrayList<Map<String, Integer>>();
        HierarchyNode current = central;
        Map<String, Integer> nodes = new HashMap<String, Integer>();
        List<Map<String, Object>> nodes_result = new ArrayList<Map<String, Object>>();
        Map<String, Object> finalResult = new HashMap<String, Object>();
        int index = 0;
        while (current.getParentNode() != null) {
            Map<String, Integer> each = new HashMap<>();
            nodes.put(current.getName(), index++);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", current.getName());
            map.put("group", current.getLevel());
            nodes_result.add(map);
            each.put("source", nodes.get(current.getName()));
            current = current.getParentNode();
            nodes.put(current.getName(), index++);
            each.put("target", nodes.get(current.getName()));
            result.add(each);
            map = new HashMap<String, Object>();
            map.put("name", current.getName());
            map.put("group", current.getLevel());
            nodes_result.add(map);
            if (nodes.size() >= count) {
                finalResult.put("links", result);
                finalResult.put("nodes", nodes_result);
                return finalResult;
            }
        }

        Queue<HierarchyNode> queue = new LinkedList<HierarchyNode>();
        queue.offer(central);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                HierarchyNode node = queue.poll();
                if(!nodes.containsKey(node)){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", node.getName());
                    map.put("group", node.getLevel());
                    nodes_result.add(map);
                    nodes.put(node.getName(), index++);
                }
                for (HierarchyNode child : node.getChildren()) {
                    nodes.put(child.getName(), index++);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", child.getName());
                    map.put("group", child.getLevel());
                    nodes_result.add(map);

                    Map<String, Integer> each = new HashMap<String, Integer>();
                    each.put("source", nodes.get(node.getName()));
                    each.put("target", nodes.get(child.getName()));
                    result.add(each);
                    queue.offer(child);
                    if (nodes.size() >= count) {
                        finalResult.put("links", result);
                        finalResult.put("nodes", nodes_result);
                        return finalResult;
                    }
                }
            }
        }
        finalResult.put("links", result);
        finalResult.put("nodes", nodes);
        return finalResult;
    }
}

