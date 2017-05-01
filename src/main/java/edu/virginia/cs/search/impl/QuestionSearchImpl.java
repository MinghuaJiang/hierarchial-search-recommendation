package edu.virginia.cs.search.impl;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Statistics;
import com.google.code.stackexchange.schema.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.virginia.cs.search.api.QuestionSearch;
import org.apache.commons.lang.CharSet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cutehuazai on 4/29/17.
 */
@PropertySource("classpath:application.properties")
public class QuestionSearchImpl implements QuestionSearch {
    @Autowired
    private StackExchangeApiQueryFactory factory;
    @Resource
    private Environment environment;

    public QuestionSearchImpl(StackExchangeApiQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public String searchQuestions(String searchTerm, int pageNum) {
        try {
            String url = "https://api.stackexchange.com/2.2/search/advanced?key=" + environment.getRequiredProperty("stackoverflow.api.key") + "&order=desc&sort=relevance&answers=1&pagesize=5&page=" + pageNum + "&q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&site=stackoverflow&filter=withbody";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                throw new Exception(response.getStatusLine().getReasonPhrase());
            }
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getMatchCount(String searchTerm) {
        try {
            String url = "https://api.stackexchange.com/2.2/search/advanced?key=" + environment.getRequiredProperty("stackoverflow.api.key") + "&order=desc&sort=activity&q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&site=stackoverflow&filter=total";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                throw new Exception(response.getStatusLine().getReasonPhrase());
            }
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getMatchCountOfTwoTags(String tag1, String tag2) {
        try {
            String url = "https://api.stackexchange.com/2.2/search/advanced?order=desc&sort=activity&tagged=" + tag1 + ";" + tag2 + "&site=stackoverflow&filter=total";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            Gson gson = new GsonBuilder().create();
            Map<String, Double> map = gson.fromJson(result, Map.class);
            return map.get("total").longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
