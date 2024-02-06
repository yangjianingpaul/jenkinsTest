package com.heima.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * streaming
 */
public class KafkaStreamQuickStart {

    public static void main(String[] args) {

        //kafka configuration center
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.5.157:9092");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-quickstart");

        //stream builder
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //stream computing
        streamProcessor(streamsBuilder);


        //create a kafka stream object
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),prop);
        //enable stream computing
        kafkaStreams.start();
    }

    /**
     * stream computing
     * the content of the message：hello kafka  hello itcast
     * @param streamsBuilder
     */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
        //Create a kstream object and specify the topic from which message to receive
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        /**
         * the value of the processed message
         */
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                return Arrays.asList(value.split(" "));
            }
        })
                //aggregate by value
                .groupBy((key,value)->value)
                //time window
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                //count the number of words
                .count()
                //convert to k stream
                .toStream()
                .map((key,value)->{
                    System.out.println("key:"+key+",value:"+value);
                    return new KeyValue<>(key.key().toString(),value.toString());
                })
                //send a message
                .to("itcast-topic-out");
    }
}