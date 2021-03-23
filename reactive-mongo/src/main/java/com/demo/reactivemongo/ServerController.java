package com.demo.reactivemongo;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.reactivemongo.entity.Device;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sse-event")
public class ServerController {
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@GetMapping(path = { "/stream-mongo/{deviceid}" }, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamFlux(@PathVariable(name = "deviceid", required = true) final String deviceid) {
		Flux changeStream = this.reactiveMongoTemplate.changeStream(Device.class)
				.filter(Criteria.where("deviceid").is((Object)deviceid)).listen()
				.map(s -> String.valueOf(((Document) s.getRaw().getFullDocument()).toJson()));
		;
		return changeStream;

	}
}
