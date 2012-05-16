package org.openmrs.module.inventory.model;

import java.io.Serializable;

public class InventoryStoreDrugPatientDetail implements  Serializable {
		 private static final long serialVersionUID = 1L;
		 private Integer id;
		 private InventoryStoreDrugPatient storeDrugPatient;
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
		
		public InventoryStoreDrugPatient getStoreDrugPatient() {
			return storeDrugPatient;
		}
		public void setStoreDrugPatient(InventoryStoreDrugPatient storeDrugPatient) {
			this.storeDrugPatient = storeDrugPatient;
		}
		public InventoryStoreDrugTransactionDetail getTransactionDetail() {
			return transactionDetail;
		}
		public void setTransactionDetail(
				InventoryStoreDrugTransactionDetail transactionDetail) {
			this.transactionDetail = transactionDetail;
		}
		
		 
		 
}
