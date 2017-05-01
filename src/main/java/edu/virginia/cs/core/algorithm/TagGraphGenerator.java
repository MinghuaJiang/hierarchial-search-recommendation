package edu.virginia.cs.core.algorithm;

import com.google.code.stackexchange.schema.Tag;
import edu.virginia.cs.core.model.TagVector;

import java.util.*;

/**
 * Created by cutehuazai on 4/28/17.
 */
public class TagGraphGenerator {
    private double taxThreshold;

    public TagGraphGenerator(double taxThreshold) {
        this.taxThreshold = taxThreshold;
    }

    public double getTaxThreshold() {
        return taxThreshold;
    }

    public void setTaxThreshold(double taxThreshold) {
        this.taxThreshold = taxThreshold;
    }

    public Map<TagVector, List<TagVector>> generateTagGraph(List<TagVector> tagVectors){
        //sort L generality in descending order
        Collections.sort(tagVectors, (x,y) -> (int)(y.getCount() - x.getCount()));

        TagVector root = tagVectors.get(0);
        Map<TagVector, List<TagVector>> simiGraph = new HashMap<>();
        Set<TagVector> vertices = new HashSet<>();
        Set<TagVector[]> edges = new HashSet<>();
        for(TagVector each : tagVectors){
            simiGraph.put(each, new ArrayList<TagVector>());
        }
        vertices.add(root);

        for(int i=1; i<tagVectors.size(); i++){
            TagVector ti = tagVectors.get(i);
            TagVector maxCandidate = null;
            double maxCandidateVal = 0;
            double similarityVal = 0;
            for(TagVector tj : vertices){
                similarityVal = ti.calculateCosineSimularity(tj);
                if(similarityVal > maxCandidateVal){
                    maxCandidateVal = similarityVal;
                    maxCandidate = tj;
                }
            }
            if(maxCandidateVal > taxThreshold){
                vertices.add(maxCandidate);
                TagVector[] edge = new TagVector[2];
                edge[0] = ti; edge[1] = maxCandidate;
                edges.add(edge);
                simiGraph.get(ti).add(maxCandidate);
            }else{
                vertices.add(maxCandidate);
                TagVector[] edge = new TagVector[2];
                edge[0] = ti; edge[1] = maxCandidate;
                edges.add(edge);
                simiGraph.get(root).add(maxCandidate);
            }
        }

        return simiGraph;
    }

    public void printGraph (Map<TagVector, List<TagVector>> simiGraph){
        Iterator it = simiGraph.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
//            TagVector key = entry.getKey();
            List<TagVector> neighbors = (List<TagVector>)entry.getValue();
            for(TagVector each : neighbors){
                System.out.print(each.getTag().getName());
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<TagVector> tagVectors = new ArrayList<>();
        // init tag 1
        Tag t1 = new Tag();
        t1.setName("Machine Learning");
        t1.setUserId(101);
        t1.setCount(5);
        TagVector tv1 = new TagVector(t1, 5);
        int[] vector1 = {1,1,1,1,1};
        tv1.setVector(vector1);
        //init tag2
        Tag t2 = new Tag();
        t2.setName("Support Vector Machine");
        t2.setUserId(102);
        t2.setCount(3);
        TagVector tv2 = new TagVector(t2, 5);
        int[] vector2 = {1,1,1,0,0};
        tv2.setVector(vector2);
        //init tag3
        Tag t3 = new Tag();
        t3.setName("Decision Tree");
        t3.setUserId(103);
        t3.setCount(2);
        TagVector tv3 = new TagVector(t3, 5);
        int[] vector3 = {1,0,1,0,0};
        tv3.setVector(vector3);
        //init tag4
        Tag t4 = new Tag();
        t4.setName("Linear Regression");
        t4.setUserId(104);
        t4.setCount(3);
        TagVector tv4 = new TagVector(t4, 5);
        int[] vector4 = {0,0,1,1,1};
        tv4.setVector(vector4);
        //init tag5
        Tag t5 = new Tag();
        t5.setName("K Nearest Neighbor");
        t5.setUserId(105);
        t5.setCount(1);
        TagVector tv5 = new TagVector(t5, 5);
        int[] vector5 = {0,0,1,0,0};
        tv5.setVector(vector5);
        tagVectors.add(tv1);
        tagVectors.add(tv2);
        tagVectors.add(tv3);
        tagVectors.add(tv4);
        tagVectors.add(tv5);

        TagGraphGenerator tg = new TagGraphGenerator(0.5);
        tg.printGraph(tg.generateTagGraph(tagVectors));

//        for(int i=0; i<10; i++){
//            Tag tag = new Tag();
//            tag.setCount(""+i);
//            tag.setUserId(""+i);
//            tag.setName("tag-"+i);
//            tag.setWikiId(""+i);
//            TagVector tagVector = new TagVector(tag, 5);
//            int[] vector = new int[5];
//            Arrays.fill(vector,1);
//            tagVector.setVector(vector);
//            tagVectors.add(tagVector);
//        }
    }
}
