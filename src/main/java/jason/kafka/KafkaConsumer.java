/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jason.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import kafka.producer.Producer;
import scala.Int;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author linjiedeng
 * @date 16/8/26 下午7:15
 * @since TODO
 */
public class KafkaConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("zk.connectiontimeout.ms", "1000000");
        props.put("group.id", "test_group2");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("test", 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = consumerConnector.createMessageStreams(map);
        KafkaStream<byte[], byte[]> streams = topicMessageStreams.get("test").get(0);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        ConsumerIterator<byte[], byte[]> iterator = streams.iterator();
        while (iterator.hasNext()) {
            MessageAndMetadata messageAndMetadata = iterator.next();
            System.out.println(messageAndMetadata.topic());
            byte[] message = (byte[]) messageAndMetadata.message();
            System.out.println(new String(message));
        }
    }
}
