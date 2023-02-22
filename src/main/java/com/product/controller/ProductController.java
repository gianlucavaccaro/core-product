package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.service.ProductService;
import com.product.exception.ResourceNotFoundException;
import com.product.model.Prodotto;

@RestController
@RequestMapping("/store")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<List<Prodotto>> getAllProducts(){
		List<Prodotto> listProducts=productService.retrieveAllProducts();
		if(listProducts.isEmpty() || listProducts.size()==0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(listProducts,HttpStatus.OK);
	}
	
	@GetMapping("/productById")
	public ResponseEntity<Prodotto> getProductById(@RequestParam(required=true) Long idProdotto) {
		try {
			Prodotto prod=productService.retrieveById(idProdotto);
			return new ResponseEntity<Prodotto>(prod,HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/createProduct")
	public ResponseEntity<Prodotto> createProduct(@RequestParam(required=true) String descrizioneProdotto) {
		try {
			Prodotto prod=productService.createProduct(descrizioneProdotto);
			return new ResponseEntity<Prodotto>(prod,HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/updateProduct")
	public ResponseEntity<Prodotto> updateProduct(@RequestParam(required=true) Long idProdotto, @RequestParam(required=true) String descrizioneProdotto) throws ResourceNotFoundException {
		try {
			Prodotto prod= productService.updateProdotto(idProdotto, descrizioneProdotto);
			return new ResponseEntity<Prodotto>(prod,HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/deleteProduct")
	public ResponseEntity<String> deleteProduct(@RequestParam(required=true) Long idProdotto) {
		try {
			productService.deleteProdotto(idProdotto);
			return new ResponseEntity<String>("Deleted.",HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Resource not found",HttpStatus.OK);
		}
		
	}

}
