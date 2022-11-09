package org.lsi.controllers;

import org.lsi.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(path = "/producer")
public class KafkaProducerController {

    @Autowired
    private Producer producer;

    @PostMapping(path = "/readfile")
    public ResponseEntity<String> sendMessage() throws FileNotFoundException {
        this.producer.send();
        String status = "The file is read successfully";
        return ResponseEntity.ok(status);
    }
}
