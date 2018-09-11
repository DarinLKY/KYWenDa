package com.KyLee.service;

import com.KyLee.model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: 搜索服务
 * @author: KyLee
 * @create: 2018-05-10 20:41
 **/
@Service
public class SearchService {

    String urlString = "http://localhost:8983/solr/zhihu_c";
    private SolrClient client = new HttpSolrClient.Builder(urlString).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";


    /**
     * @description: 搜索
     * @param keyword 搜索词
     * @param offset 分页前缀
     * @param count 限制条数
     * @param hlPre 强调字体前缀
     * @param hlPos 强调字体后缀
     * @return List<Question>
     * @throws Exception
     */
    public List<Question> searchQuestion(String keyword, int offset, int count , String hlPre, String hlPos)throws  Exception{
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("h1.f1",QUESTION_TITLE_FIELD+" "+QUESTION_CONTENT_FIELD);
        query.set("df",QUESTION_TITLE_FIELD);
        QueryResponse response = client.query(query);
        //通过solr的map结构提取题目与内容。
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        return questionList;
    }

    /**
     * @description:加入问题的时候添加索引
     * @param qid
     * @param title
     * @param content
     * @return
     * @throws Exception
     */
    public boolean indexQuestion(int qid, String title, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField(QUESTION_CONTENT_FIELD, content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
