package org.lsi.consumers;

import lombok.extern.slf4j.Slf4j;
import org.lsi.dto.Diabete;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {
//
//    @KafkaListener(topics ="${topic.name.producer}", groupId ="group_id", containerFactory = "diabeteListener")
//    public void listener(Diabete message) {
//        log.info("Received message = {}", message);
//    }
}
