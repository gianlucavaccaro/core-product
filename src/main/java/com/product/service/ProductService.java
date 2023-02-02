package com.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.exception.ResourceNotFoundException;
import com.product.model.Prodotto;
import com.product.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public ProductService() {
	}

	/*READ restituisce la lista di tutti i prodotti*/
	public List<Prodotto> retrieveAllProducts(){
		return productRepository.findAll();
	}
	
	/*READ restituisce il prodotto con l'id specificato in input*/
	public Prodotto retrieveById(Long id) throws ResourceNotFoundException {
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}
	
	public Prodotto createProduct(String descrizioneProdotto) {
		Prodotto prod= new Prodotto(descrizioneProdotto);
		return productRepository.save(prod);
	}
	
	/*UPDATE aggiorna il prodotto con l'id specificato in input con la nuova descrizione passata in input*/
	public Prodotto updateProdotto(Long id, String descrizione) throws ResourceNotFoundException {
		Prodotto prodotto= productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		prodotto.setDescrizioneProdotto(descrizione);
		return productRepository.save(prodotto);
	}
	
	public void deleteProdotto(Long id) throws ResourceNotFoundException {
		Prodotto prodotto= productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		productRepository.delete(prodotto);
	}
}
