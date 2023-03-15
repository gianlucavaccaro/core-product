package com.product.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static final Logger logger = LogManager.getLogger(KafkaProductConsumer.class);
	
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
		logger.info("Received an OrderEvent in core-product for product id: " + event.getIdProdotto());
		
		try {
			productRecord= event.getLastTracking();
			productRecord.setServiceName("core-product");
			service.retrieveById(event.getIdProdotto());
			productRecord.setStatus("OK");
			event.getTracking().add(productRecord);
			logger.info("Sending OrderEvent back to topic with present product id: " + event.getIdProdotto());
			producer.sendAckProductOrder(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productRecord.setStatus("KO");
			productRecord.setServiceName("core-product");
			if(e instanceof ResourceNotFoundException) {
				logger.info("Product with id "+ event.getIdProdotto()+" not found.");
				productRecord.setFailureReason("ID_NOT_PRESENT");
			}
			event.getTracking().add(productRecord);
			logger.error("KO. Sending OrderEvent back from core-product to orchestrator with present product id: " + event.getIdProdotto());
			producer.sendAckProductOrder(event);
		}
	}
	
}
