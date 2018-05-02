package org.openmrs.module.inventory.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class InventoryItemSubCategory implements  Serializable {

		
		 private static final long serialVersionUID = 1L;
		  private Integer id;
		  private String name;
		  private String code;
		  private InventoryItemCategory category;
		  private String description ;
		  private Date createdOn;
		  private String createdBy;
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
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
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
		public InventoryItemCategory getCategory() {
			return category;
		}
		public void setCategory(InventoryItemCategory category) {
			this.category = category;
		}
		  
		  
}
