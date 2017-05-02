package edu.virginia.cs.core.algorithm;

import java.util.*;

/**
 * Created by VINCENTWEN on 4/30/17.
 */
public class HierarchyBuild {
    private long totalDistinctTag;
    private int k = 100;
    private List<ScoredTag> topKTopics;
    private TreeSet<ScoredTag> scoredTagsTreeSet;
    private double z;

    public HierarchyBuild(int k, DocIterator docIterator) {
        this.k = k;
        this.scoredTagsTreeSet = new TreeSet<>(new Comparator<ScoredTag>() {
            @Override
            public int compare(ScoredTag o1, ScoredTag o2) {
                if(o2.getScore() - o1.getScore() <= 0){
                    return 1;
                }else return -1;
            }
        });
        this.topKTopics = getTopKTopics(docIterator);
        setZ();
        normalizeTagRankingIT();
    }

    private List<ScoredTag> getTopKTopics(DocIterator docIterator) {
        Map<ScoredTag, Integer> tagHash = new HashMap<>();
        while(docIterator.hasNext()){
            tagHash.put(docIterator.next(),
                    tagHash.getOrDefault(docIterator.next(), 0) + 1);
        }
        this.totalDistinctTag = tagHash.size();
        this.scoredTagsTreeSet.addAll(tagHash.keySet());
        PriorityQueue<Map.Entry<ScoredTag, Integer>> heap = new PriorityQueue<>(this.k, (x, y) -> (int)(x.getValue() - y.getValue()));
        for(Map.Entry<ScoredTag, Integer> entry : tagHash.entrySet()){
            if(heap.size() < this.k){
                heap.add(entry);
            }else{
                Map.Entry<ScoredTag, Integer> top = heap.peek();
                if(top.getValue() < entry.getValue()){
                    heap.poll();
                    heap.add(entry);
                }
            }
        }
        List<ScoredTag> topTopics = new LinkedList<>();
        for(int i=0; i<this.k; i++){
            topTopics.add(heap.poll().getKey());
        }
        Collections.reverse(topTopics);
        return topTopics;
    }

    private double[] getTopicProbArr(ScoredTag tag){
        double[] topicProb = new double[this.k];
        Topic[] topKTopicsArr = this.topKTopics.toArray(new Topic[0]);
        for(int i=0; i<this.k; i++){
            topicProb[i] = tag.co_occurrence(topKTopicsArr[i]) / this.totalDistinctTag;
        }
        return topicProb;
    }

    // get entropy
    private double getTagEntropy(ScoredTag tag){
        double entropy = 0;
        double[] topic_pro_arr = getTopicProbArr(tag);
        int N = topic_pro_arr.length;
        for(int i=0; i<N; i++){
            entropy += topic_pro_arr[i] * Math.log(topic_pro_arr[i]);
        }
        return entropy;
    }
    
    // calculate tag ranking by I(t) = H(t) * [D(t) * log(C(t)+1)] / Z
    private double tagRankingIT (ScoredTag tag){
        return getTagEntropy(tag) * (tag.getTagDistinctCount() * Math.log(tag.getTagRawCount() + 1));
    }

    private void setZ(){
        double maxScore = 0;
        for(ScoredTag tag : this.scoredTagsTreeSet){
            double score = tagRankingIT(tag);
            maxScore = Math.max(maxScore, score);
            tag.setScore(score);
        }
        this.z = maxScore;
    }

     private void normalizeTagRankingIT(){
        for(ScoredTag tag : this.scoredTagsTreeSet){
            tag.setScore(tag.getScore() / this.z);
        }
    }

    public TreeSet<ScoredTag> getScoredTagsTreeSet() {
        return scoredTagsTreeSet;
    }
}
