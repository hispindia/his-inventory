package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.module.inventory.util.ActionValue;

public class InventoryStoreDrugIndent implements  Serializable {
	 private static final long serialVersionUID = 1L;
	 private Integer id;
	 private InventoryStore store;
	 private String name;
	 private Date createdOn;
	 private Integer subStoreStatus;
	 private Integer mainStoreStatus;
	 private InventoryStoreDrugTransaction transaction;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public InventoryStore getStore() {
		return store;
	}
	public void setStore(InventoryStore store) {
		this.store = store;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getSubStoreStatus() {
		return subStoreStatus;
	}
	public void setSubStoreStatus(Integer subStoreStatus) {
		this.subStoreStatus = subStoreStatus;
	}
	public Integer getMainStoreStatus() {
		return mainStoreStatus;
	}
	public String getMainStoreStatusName() {
		return ActionValue.getIndentMainbStoreName(mainStoreStatus);
	}
	public String getSubStoreStatusName() {
		return ActionValue.getIndentSubStoreName(subStoreStatus);
	}
	public void setMainStoreStatus(Integer mainStoreStatus) {
		this.mainStoreStatus = mainStoreStatus;
	}
	public InventoryStoreDrugTransaction getTransaction() {
		return transaction;
	}
	public void setTransaction(InventoryStoreDrugTransaction transaction) {
		this.transaction = transaction;
	}
	 
}
