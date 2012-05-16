package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

public class InventoryStoreItemIndentDetail implements  Serializable {
	 private static final long serialVersionUID = 1L;
	 private Integer id;
	 private InventoryStoreItemIndent indent;
	 private InventoryItem item;
	 private InventoryItemSpecification specification;
	 private Integer quantity;
	 private Integer mainStoreTransfer;
	 private Date createdOn;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getMainStoreTransfer() {
		return mainStoreTransfer;
	}
	public void setMainStoreTransfer(Integer mainStoreTransfer) {
		this.mainStoreTransfer = mainStoreTransfer;
	}
	public InventoryStoreItemIndent getIndent() {
		return indent;
	}
	public void setIndent(InventoryStoreItemIndent indent) {
		this.indent = indent;
	}
	public InventoryItem getItem() {
		return item;
	}
	public void setItem(InventoryItem item) {
		this.item = item;
	}
	public InventoryItemSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(InventoryItemSpecification specification) {
		this.specification = specification;
	}
	
	 
	 
	 
}
