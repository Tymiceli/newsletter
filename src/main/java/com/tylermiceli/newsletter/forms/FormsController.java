package com.tylermiceli.newsletter.forms;

import com.tylermiceli.newsletter.ConvertKitProperties;
import com.tylermiceli.newsletter.domain.SubscriberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.concurrent.Flow;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/api/forms")
public class FormsController {

    private static final Logger log = LoggerFactory.getLogger(FormsController.class);
    private final ConvertKitProperties ckProps;
    private final RestClient restClient;

    public FormsController(ConvertKitProperties ckProps) {
        this.ckProps = ckProps;
        this.restClient = RestClient.create(ckProps.url());
    }

    @PostMapping("/subscribe")
    ResponseEntity<String> subscribe(@RequestBody SubscriberRequest subscriberRequest) {
        // create new subscriber request because subscriber request is a record and therefore immutable
        var body = new SubscriberRequest(subscriberRequest.email(), subscriberRequest.formId(), ckProps.apiKey());
        log.info("Api Key: {}", ckProps.apiKey());
        log.info("subscribe {}", body);
        ResponseEntity<String> response = restClient.post()
                .uri("/forms/" + body.formId() + "/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        log.info("subscribe: {}", response);
        return response;
    }
}
