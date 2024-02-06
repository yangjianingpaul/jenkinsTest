package com.heima.kafka.sample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * consumer
 */
public class ConsumerQuickStart {

    public static void main(String[] args) {

        //1.kafka configuration information
        Properties prop = new Properties();
        //url
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.5.157:9092");
        //key和value的反序列化器
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        //set up consumer groups
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
//        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");

        //manually commit offsets
//        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


        //2.create a consumer object
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);

        //3.subscribe to a topic
//        consumer.subscribe(Collections.singletonList("topic-first"));
        consumer.subscribe(Collections.singletonList("itcast-topic-out"));

        //4.pull messages

//        while (true) {
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
//            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//                System.out.println(consumerRecord.key());
//                System.out.println(consumerRecord.value());
//                System.out.println(consumerRecord.partition());
//            }
//        }

        //synchronous and asynchronous commit offsets
        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.key());
                    System.out.println(consumerRecord.value());
                    System.out.println(consumerRecord.offset());
                    System.out.println(consumerRecord.partition());
                }
                //asynchronous commit offsets
                consumer.commitAsync();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("record incorrect information："+e);
        }finally {
            //synchronous
            consumer.commitSync();
        }


        /*while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.key());
                System.out.println(consumerRecord.value());
                System.out.println(consumerRecord.offset());
                System.out.println(consumerRecord.partition());

               *//* try {
                    //同步提交偏移量
                    consumer.commitSync();
                }catch (CommitFailedException e){
                    System.out.println("记录提交失败的异常："+e);
                }*//*
            }
            //异步的方式提交偏移量
            *//*consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                    if(e != null){
                        System.out.println("记录错误的提交偏移量："+map+",异常信息为："+e);
                    }
                }
            });*//*
        }*/
    }
}
