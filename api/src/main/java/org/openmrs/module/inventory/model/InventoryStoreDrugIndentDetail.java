package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;

public class InventoryStoreDrugIndentDetail implements  Serializable {
	 private static final long serialVersionUID = 1L;
	 private Integer id;
	 private InventoryStoreDrugIndent indent;
	 private InventoryDrug drug;
	 private InventoryDrugFormulation formulation;
	 private Integer quantity;
	 private Integer mainStoreTransfer;
	 private Date createdOn;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public InventoryStoreDrugIndent getIndent() {
		return indent;
	}
	public void setIndent(InventoryStoreDrugIndent indent) {
		this.indent = indent;
	}
	public InventoryDrug getDrug() {
		return drug;
	}
	public void setDrug(InventoryDrug drug) {
		this.drug = drug;
	}
	public InventoryDrugFormulation getFormulation() {
		return formulation;
	}
	public void setFormulation(InventoryDrugFormulation formulation) {
		this.formulation = formulation;
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
	
	 
	 
	 
}
