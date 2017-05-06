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
        List<Map<String, Integer>> nodes = new ArrayList<Map<String, Integer>>();
        Map<String, Object> finalResult = new HashMap<String, Object>();
        int index = 0;
        while (current.getParentNode() != null) {
            Map<String, Integer> each = new HashMap<>();
            Map<String, Integer> node = new HashMap<>();
            node.put(current.getName(), index++);
            nodes.add(node);
            each.put("source", node.get(current.getName()));
            current = current.getParentNode();
            node = new HashMap<>();
            node.put(current.getName(), index++);
            nodes.add(node);
            node.put(current.getName(), index++);
            each.put("target", node.get(current.getName()));
            result.add(each);
            if (nodes.size() >= count) {
                finalResult.put("links", result);
                finalResult.put("nodes", nodes);
                return finalResult;
            }
        }

        Queue<HierarchyNode> queue = new LinkedList<HierarchyNode>();
        queue.offer(central);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                HierarchyNode node = queue.poll();
               // if(!nodes.containsKey(node)){
                Map<String, Integer> rootnode = new HashMap<>();
                rootnode.put(node.getName(), index++);
                nodes.add(rootnode);
                for (HierarchyNode child : node.getChildren()) {
                    Map<String,Integer> nodeeach = new HashMap<>();
                    nodeeach.put(child.getName(), index++);
                    nodes.add(nodeeach);

                    Map<String, Integer> each = new HashMap<String, Integer>();
                    each.put("source", rootnode.get(node.getName()));
                    each.put("target", nodeeach.get(child.getName()));
                    result.add(each);
                    queue.offer(child);
                    if (nodes.size() >= count) {
                        finalResult.put("links", result);
                        finalResult.put("nodes", nodes);
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

