package com.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="prodotto")
public class Prodotto {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idProdotto;
	private String descrizioneProdotto;
	
	public Prodotto() {
	}

	public Prodotto(String descrizioneProdotto) {
		this.descrizioneProdotto = descrizioneProdotto;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(long idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getDescrizioneProdotto() {
		return descrizioneProdotto;
	}

	public void setDescrizioneProdotto(String descrizioneProdotto) {
		this.descrizioneProdotto = descrizioneProdotto;
	}

	@Override
	public String toString() {
		return "Prodotto [idProdotto=" + idProdotto + ", descrizioneProdotto=" + descrizioneProdotto + "]";
	}
	
	
}
