package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.hospitalcore.util.ActionValue;

public class InventoryItem  implements  Serializable{

	 private static final long serialVersionUID = 1L;
	  private Integer id;
	  private String name;
	  private InventoryItemUnit unit;
	  private InventoryItemCategory category;
	  private InventoryItemSubCategory subCategory;
	  private Set<InventoryItemSpecification> specifications;
	  private Date createdOn;
	  private String createdBy;
	  private int attribute;
	  private Integer reorderQty;
	  private Integer consumption;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getNameShort() {
		return StringUtils.isNotBlank(name) && name.length() > 30 ?name.substring(0,30)+"..." : name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InventoryItemUnit getUnit() {
		return unit;
	}
	public void setUnit(InventoryItemUnit unit) {
		this.unit = unit;
	}
	public InventoryItemCategory getCategory() {
		return category;
	}
	public void setCategory(InventoryItemCategory category) {
		this.category = category;
	}
	public InventoryItemSubCategory getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(InventoryItemSubCategory subCategory) {
		this.subCategory = subCategory;
	}
	public Set<InventoryItemSpecification> getSpecifications() {
		return specifications;
	}
	public void setSpecifications(Set<InventoryItemSpecification> specifications) {
		this.specifications = specifications;
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
	public int getAttribute() {
		return attribute;
	}
	public String getAttributeName(){
		return ActionValue.getItemAttribute(attribute);
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
	public Integer getConsumption() {
		return consumption;
	}
	public void setConsumption(Integer consumption) {
		this.consumption = consumption;
	}
	public Integer getReorderQty() {
		return reorderQty;
	}
	public void setReorderQty(Integer reorderQty) {
		this.reorderQty = reorderQty;
	}
	//03/07/2012: Kesavulu:sort Item Names  #300
	public int compareTo(InventoryItem item) {
	    
	    return (this.name).compareTo(item.name);
    }
	  
	  
	  
}
