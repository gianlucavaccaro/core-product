package com.product.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.exception.ResourceNotFoundException;
import com.product.model.Prodotto;
import com.product.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	/*restituisce la lista di tutti i prodotti*/
	public List<Prodotto> retrieveAllProducts(){
		return productRepository.findAll();
	}
	
	/*restituisce il prodotto specificando il suo id*/
	public Prodotto retrieveById(Long id) throws ResourceNotFoundException {
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}
	
	/*crea un nuovo prodotto*/
	public Prodotto createProduct(String descrizioneProdotto) {
		Prodotto prod= new Prodotto(descrizioneProdotto);
		return productRepository.save(prod);
	}
	
	/*aggiorna il prodotto con l'id specificato in input con la nuova descrizione passata in input*/
	public Prodotto updateProdotto(Long id, String descrizione) throws ResourceNotFoundException {
		Prodotto prodotto= productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		prodotto.setDescrizioneProdotto(descrizione);
		return productRepository.save(prodotto);
	}
	
	/*cancellazione di un prodotto*/
	public void deleteProdotto(Long id) throws ResourceNotFoundException {
		Prodotto prodotto= productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		productRepository.delete(prodotto);
	}
	
	
}
