package com.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.model.Prodotto;

@Service
public interface ProductRepository extends JpaRepository<Prodotto,Long>{

	/*@Transactional(rollbackFor = Exception.class)
	public Prodotto createProduct(String desc);*/
	
}
