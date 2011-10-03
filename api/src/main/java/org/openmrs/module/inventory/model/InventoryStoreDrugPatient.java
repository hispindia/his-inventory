package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.Patient;
import org.openmrs.module.hospitalcore.util.PatientUtil;

public class InventoryStoreDrugPatient implements  Serializable {
	 private static final long serialVersionUID = 1L;
	 private Integer id;
	 private InventoryStore store;
	 private String name;
	 private String prescription;
	 private Date createdOn;
	 private String createdBy;
	 private Patient patient;
	 private String identifier;
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
	public String getPrescription() {
		return prescription;
	}
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getPatientCategory(){
		return PatientUtil.getPatientCategory(patient);
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	 
	 
}
