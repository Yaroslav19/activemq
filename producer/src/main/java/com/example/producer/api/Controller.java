package com.example.producer.api;

import com.example.producer.Client;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/message")
public class Controller {

    private final Client client;

    public Controller(Client client) {
        this.client = client;
    }

    @PostMapping
    public void produceMessage(@RequestParam String message) {
        client.produceMessage(message);

    }

}
