package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

public class InventoryStoreItem  implements  Serializable {

	 private static final long serialVersionUID = 1L;
	 private Integer id;
	 private InventoryStore store;
	 private InventoryItem item;
	 private InventoryItemSpecification specification;
	 private long currentQuantity;
	 private long receiptQuantity;
	 private long issueQuantity;
	 private Integer statusIndent;
	 private Integer reorderQty;
	 private Integer status;
	 private Date createdOn;
	 private long openingBalance;
	 private long closingBalance;
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
	public long getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(long currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	public long getReceiptQuantity() {
		return receiptQuantity;
	}
	public void setReceiptQuantity(long receiptQuantity) {
		this.receiptQuantity = receiptQuantity;
	}
	public long getIssueQuantity() {
		return issueQuantity;
	}
	public void setIssueQuantity(long issueQuantity) {
		this.issueQuantity = issueQuantity;
	}
	public Integer getStatusIndent() {
		return statusIndent;
	}
	public String getStatusIndentName(){
		if(closingBalance < reorderQty){
			return "Reorder";
		}
		return " ";
	}
	public void setStatusIndent(Integer statusIndent) {
		this.statusIndent = statusIndent;
	}
	public Integer getReorderQty() {
		return reorderQty;
	}
	public void setReorderQty(Integer reorderQty) {
		this.reorderQty = reorderQty;
	}
	public long getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(long openingBalance) {
		this.openingBalance = openingBalance;
	}
	public long getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(long closingBalance) {
		this.closingBalance = closingBalance;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
}
