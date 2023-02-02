package com.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.product.model.Prodotto;

@Service
public interface ProductRepository extends JpaRepository<Prodotto,Long>{

	
}
