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


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * TODO
 *
 * @author linjiedeng
 * @date 16/8/26 下午4:00
 * @since TODO
 */
public class KafkaProducer {

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.put("zk.connect", "localhost:2181");
        prop.put("serializer.class", "kafka.serializer.StringEncoder");
        prop.put("metadata.broker.list", "localhost:9092");
        ProducerConfig config = new ProducerConfig(prop);
        Producer producer = new Producer(config);
        for(int i = 0; i < 10; i++) {
            KeyedMessage<String, String> message = new KeyedMessage<String, String>("test", "heihei" + i);
            producer.send(message);
            Thread.sleep(1000);
        }
    }

}
