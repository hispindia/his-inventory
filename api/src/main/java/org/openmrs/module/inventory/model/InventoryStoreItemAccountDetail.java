package org.openmrs.module.inventory.model;

import java.io.Serializable;

public class InventoryStoreItemAccountDetail implements  Serializable {
		 private static final long serialVersionUID = 1L;
		 private Integer id;
		 private InventoryStoreItemAccount itemAccount;
		 private Integer quantity;
		 private InventoryStoreItemTransactionDetail transactionDetail;
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
		public InventoryStoreItemAccount getItemAccount() {
			return itemAccount;
		}
		public void setItemAccount(InventoryStoreItemAccount itemAccount) {
			this.itemAccount = itemAccount;
		}
		public InventoryStoreItemTransactionDetail getTransactionDetail() {
			return transactionDetail;
		}
		public void setTransactionDetail(
				InventoryStoreItemTransactionDetail transactionDetail) {
			this.transactionDetail = transactionDetail;
		}
		
		
		 
		 
}
