package com.heima.search.listener;


import com.heima.common.constants.ArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DeleteArticleListener {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_DELETE_TOPIC)
    public void onMessage(String message) {
        if (StringUtils.isNotBlank(message)) {

            log.info("DeleteArticleListener, message = {}", message);

            DeleteRequest request = new DeleteRequest("app_info_article", message);
            try {
                DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
                log.info(deleteResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("sync es error = {}", e);
            }
        }
    }
}
