package org.openmrs.module.inventory.model;

import java.io.Serializable;

public class InventoryStoreDrugAccountDetail implements  Serializable {
		 private static final long serialVersionUID = 1L;
		 private Integer id;
		 private InventoryStoreDrugAccount drugAccount;
		 private Integer quantity;
		 private InventoryStoreDrugTransactionDetail transactionDetail;
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
		
		public InventoryStoreDrugAccount getDrugAccount() {
			return drugAccount;
		}
		public void setDrugAccount(InventoryStoreDrugAccount drugAccount) {
			this.drugAccount = drugAccount;
		}
		public InventoryStoreDrugTransactionDetail getTransactionDetail() {
			return transactionDetail;
		}
		public void setTransactionDetail(
				InventoryStoreDrugTransactionDetail transactionDetail) {
			this.transactionDetail = transactionDetail;
		}
		
		
		 
		 
}
