package com.product.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.exception.ResourceNotFoundException;
import com.product.service.ProductService;

import jakarta.annotation.PostConstruct;

@Component
public class KafkaProductConsumer {

	@Autowired
	private ProductService service;
	@Autowired
	private KafkaProductProducer producer;
	
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void init() {
		objectMapper=new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	/*
	 * leggo il mess, prendo id e checko se presente: se si OK, altrimenti KO
	 * */
	@KafkaListener(topics="TOPIC_PRODUCT_IN",groupId="CORE-PRODUCT_KAFKA_TOPIC_PRODUCT_IN")
	public void consume(String message) throws JsonProcessingException{
		TrackingEvent productRecord=new TrackingEvent();
		OrderEvent event= objectMapper.readValue(message, OrderEvent.class);
		System.out.println("Messaggio in product ricevuto:"+ event.getLastTracking().toString());
		
		try {
			productRecord= event.getLastTracking();
			productRecord.setServiceName("core-product");
			service.retrieveById(event.getIdProdotto());
			productRecord.setStatus("OK");
			event.getTracking().add(productRecord);
			producer.sendAckProductOrder(event);
			System.out.println("Messaggio in product ricevuto."+ event.getLastTracking().getServiceName().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productRecord.setStatus("KO");
			productRecord.setServiceName("core-product");
			if(e instanceof ResourceNotFoundException)
				productRecord.setFailureReason("Product not found.");
			event.getTracking().add(productRecord);
			producer.sendAckProductOrder(event);
		}
	}
	
}
