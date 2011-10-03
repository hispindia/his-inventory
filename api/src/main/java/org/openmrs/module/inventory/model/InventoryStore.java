package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.openmrs.Role;

public class InventoryStore implements  Serializable {

	
	 private static final long serialVersionUID = 1L;
	  private Integer id;
	  private String name;
	  private Date createdOn;
	  private String createdBy;
	  private Role role;
	  private Boolean retired = false;
	  private String code;
	  private int isDrug;
	  private InventoryStore parent;
	  private Set<InventoryStore> subStores;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Boolean getRetired() {
		return retired;
	}

	public void setRetired(Boolean retired) {
		this.retired = retired;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public InventoryStore getParent() {
		return parent;
	}
	public void setParent(InventoryStore parent) {
		this.parent = parent;
	}
	public Set<InventoryStore> getSubStores() {
		return subStores;
	}
	public void setSubStores(Set<InventoryStore> subStores) {
		this.subStores = subStores;
	}
	public int getIsDrug() {
		return isDrug;
	}
	public String getIsDrugName() {
		
		return isDrug == 1? "Yes" : "No";
	}
	public void setIsDrug(int isDrug) {
		this.isDrug = isDrug;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	  
	  
	  
}
