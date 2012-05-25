package com.smartgames.client;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StockHistory implements IsSerializable {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private Double price;
  @Persistent
  private String symbol;
  @Persistent
  private Date createDate;

  public StockHistory(Double price, String symbol) {
	this();
	this.price = price;
	this.symbol = symbol;
  }

  public StockHistory() {
	super();
	createDate = new Date();
  }

  public Double getPrice() {
	return price;
  }
	
  public void setPrice(Double price) {
	this.price = price;
  }
	
  public String getSymbol() {
	return symbol;
  }
	
  public void setSymbol(String symbol) {
	this.symbol = symbol;
  }
	  
  

}
