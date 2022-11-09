package org.lsi.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.lsi.dto.Diabete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class ProcessConsumer {

    public static void main(String[] args) {
        new ProcessConsumer().process();
    }

    @Value(value = "demo1")
    private String topic;



    public static void process() {


        Properties props=new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-app1");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(Diabete.class).getClass());


        final Serializer<JsonNode> jsonNodeSerializer = new JsonSerializer();
        final Deserializer<JsonNode> jsonNodeDeserializer = new JsonDeserializer();
        final Serde<JsonNode> jsonNodeSerde = Serdes.serdeFrom(jsonNodeSerializer,jsonNodeDeserializer);

    StreamsBuilder builder = new StreamsBuilder();
    KStream<Integer, JsonNode> stream = builder.stream("demo1", Consumed.with(Serdes.Integer(), jsonNodeSerde));


    KTable<Integer, Long> combinedDocuments = stream
            .flatMapValues(textLine-> Arrays.asList(textLine.get("classe")))
            .groupByKey(Grouped.with(Serdes.Integer(), jsonNodeSerde))
            .count();





         log.info("Hellooooo"+combinedDocuments.toString());
        combinedDocuments.toStream().to("demo3",Produced.with(Serdes.Integer(),Serdes.Long()));


    Topology topology=builder.build();
    KafkaStreams kafkaStreams=new KafkaStreams(topology,props);
    kafkaStreams.start();

   }



}

