package edu.virginia.cs.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import edu.virginia.cs.core.model.Hierarchy;
import edu.virginia.cs.core.model.HierarchyNode;
import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.repository.QuestionRepositoryImpl;
import edu.virginia.cs.solr.repository.TagRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.SolrClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cutehuazai on 5/3/17.
 */
@RunWith(JUnit4.class)
public class NodeRecommendationTest {
    private HierarchyBuilder builder;
    private TagRepository repository;
    private SolrTemplate template;
    private SolrClient client;
    private Hierarchy hierarchy;
    private QuestionRepositoryImpl questionRepository;
    private SolrClientFactory clientFactory;

    @Before
    public void setUpBefore() {
        hierarchy = Mockito.mock(Hierarchy.class);
        client = Mockito.mock(SolrClient.class);
        template = Mockito.mock(SolrTemplate.class);
        repository = Mockito.mock(TagRepository.class);
        builder = Mockito.mock(HierarchyBuilder.class);
        clientFactory = Mockito.mock(SolrClientFactory.class);
        //template = new SolrTemplate(clientFactory);
        questionRepository = new QuestionRepositoryImpl(template);
        questionRepository.setFactory(builder);
        questionRepository.setRepository(repository);
    }
    @Test
    public void testSearchTerm() throws Exception {
        QueryResponse response = new QueryResponse();
        Mockito.when(client.query(Mockito.anyObject())).thenReturn(response);

        List<Question> questions = new ArrayList<Question>();
        Question question = new Question();
        List<String> tags = new ArrayList<String>();
        tags.add("machine learning");
        tags.add("svm");
        tags.add("linear regression");
        tags.add("logistic regression");
        question.setTags(tags);
        questions.add(question);
        Mockito.when(template.convertQueryResponseToBeans(response, Question.class)).thenReturn(questions);
        Tag ml = new Tag();
        ml.setTagName("machine learning");
        Tag svm = new Tag();
        svm.setTagName("svm");
        Tag lr = new Tag();
        svm.setTagName("linear regression");
        Tag logr = new Tag();
        svm.setTagName("logistic regression");
        ml.setScore(100);
        svm.setScore(70);
        lr.setScore(65);
        logr.setScore(64);
        Mockito.when(repository.getTagByName("machine learning")).thenReturn(ml);
        Mockito.when(repository.getTagByName("svm")).thenReturn(svm);
        Mockito.when(repository.getTagByName("linear regression")).thenReturn(lr);
        Mockito.when(repository.getTagByName("logistic regression")).thenReturn(logr);
        HierarchyNode mlNode = new HierarchyNode();
        mlNode.setLevel(1);
        HierarchyNode svmNode = new HierarchyNode();
        svmNode.setLevel(2);
        HierarchyNode lrNode = new HierarchyNode();
        lrNode.setLevel(2);
        HierarchyNode logrNode = new HierarchyNode();
        logrNode.setLevel(2);
        mlNode.addChildren(svmNode);
        mlNode.addChildren(lrNode);
        mlNode.addChildren(logrNode);
        svmNode.setParentNode(mlNode);
        lrNode.setParentNode(mlNode);
        logrNode.setParentNode(mlNode);
        Mockito.when(builder.getHierarchy()).thenReturn(hierarchy);
        Mockito.when(hierarchy.getHierachyNode("machine learning")).thenReturn(mlNode);
        Mockito.when(hierarchy.getHierachyNode("svm")).thenReturn(svmNode);
        Mockito.when(hierarchy.getHierachyNode("linear regression")).thenReturn(lrNode);
        Mockito.when(hierarchy.getHierachyNode("logistic regression")).thenReturn(logrNode);
       // System.out.println(template.getSolrClient());
        //Mockito.when(template.getSolrClient()).thenReturn(client);
        Map<String, Object> result = questionRepository.recommendNode("machine learning",4);
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(result));
    }
}
