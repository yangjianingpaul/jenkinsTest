package com.heima.article.stream;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.HotArticleConstants;
import com.heima.model.mess.ArticleVisitStreamMess;
import com.heima.model.mess.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class HotArticleStreamHandler {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        //Receive messages
        KStream<String, String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
        //aggregate streaming
        stream.map((key, value) -> {
                    UpdateArticleMess mess = JSON.parseObject(value, UpdateArticleMess.class);
                    //reset the key of the message:1234343434   和  value: likes:1
                    return new KeyValue<>(mess.getArticleId().toString(), mess.getType().name() + ":" + mess.getAdd());
                })
                //aggregate by article id
                .groupBy((key, value) -> key)
                //time window
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                /**
                 * Complete the calculation of the aggregate by yourself
                 */
                .aggregate(new Initializer<String>() {
                    /**
                     * Initial method, the return value is the value of the message
                     * @return
                     */
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                    /**
                     * For a true aggregation operation, the return value is the value of the message
                     */
                }, new Aggregator<String, String, String>() {
                    @Override
                    public String apply(String key, String value, String aggValue) {
                        if (StringUtils.isBlank(value)) {
                            return aggValue;
                        }
                        String[] aggAry = aggValue.split(",");
                        int col = 0, com = 0, lik = 0, vie = 0;
                        for (String agg : aggAry) {
                            String[] split = agg.split(":");
                            /**
                             * The initial value is obtained, which is also the value after the calculation within the time window
                             */
                            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])) {
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    lik = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }
                        /**
                         * additive operations
                         */
                        String[] valAry = value.split(":");
                        switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])) {
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                lik += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }

                        String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
                        System.out.println("the id of the article:" + key);
                        System.out.println("The result of the message processing within the current time window：" + formatStr);
                        return formatStr;
                    }
                }, Materialized.as("hot-article-stream-count-001"))
                .toStream()
                .map((key, value) -> {
                    return new KeyValue<>(key.key().toString(), formatObj(key.key().toString(), value));
                })
                //send a message
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);

        return stream;
    }

    /**
     * format the value data of the message
     *
     * @param articleId
     * @param value
     * @return
     */
    public String formatObj(String articleId, String value) {
        ArticleVisitStreamMess mess = new ArticleVisitStreamMess();
        mess.setArticleId(Long.valueOf(articleId));
        //COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])) {
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        log.info("the result of the aggregate message processing is:{}", JSON.toJSONString(mess));
        return JSON.toJSONString(mess);
    }
}