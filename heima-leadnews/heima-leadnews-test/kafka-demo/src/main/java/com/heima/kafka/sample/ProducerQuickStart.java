package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * producer
 */
public class ProducerQuickStart {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //1.kafka link configuration information
        Properties prop = new Properties();
        //the kafka link address
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.31.125:9092");
        //key和value的序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //ack配置  message acknowledgment mechanism
        prop.put(ProducerConfig.ACKS_CONFIG,"all");

        //number of retries
        prop.put(ProducerConfig.RETRIES_CONFIG,10);

        //data compression
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"lz4");

        //2.create a kafka producer object
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(prop);

        //3.send a message
        /**
         * the first parameter ：topic
         * the second parameter：key of message
         * the third parameter：value of message
         */

//        ProducerRecord<String,String> kvProducerRecord = new ProducerRecord<String,String>("topic-first","key-001","hello kafka");
//        producer.send(kvProducerRecord);
        for (int i = 0; i < 5; i++) {
            ProducerRecord<String,String> kvProducerRecord = new ProducerRecord<String,String>("itcast-topic-input","hello kafka");
            producer.send(kvProducerRecord);
        }

//        ProducerRecord<String,String> kvProducerRecord = new ProducerRecord<String,String>("topic-first","hello kafka");
        //send messages synchronously
//        RecordMetadata recordMetadata = producer.send(kvProducerRecord).get();
//        System.out.println(recordMetadata.offset());

        //asynchronous message sending
//        producer.send(kvProducerRecord, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                if(e != null){
//                    System.out.println("记录异常信息到日志表中");
//                }
//                System.out.println(recordMetadata.offset());
//            }
//        });

        //4.Closing the Message Channel must be closed, otherwise the message will not be sent successfully
        producer.close();
    }
}
