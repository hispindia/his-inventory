package org.openmrs.module.inventory.web.controller.item;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.springframework.validation.Errors;

public class ItemValidator {
	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryItem.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryItem item = (InventoryItem) command;
    	
    	if( StringUtils.isBlank(item.getName())){
    		error.reject("inventory.item.name.required");
    	}
    	/*if( item.getCategory() == null){
    		error.reject("inventory.item.category.required");
    	}*/
    	if( item.getSubCategory() == null){
    		error.reject("inventory.item.subCategory.required");
    	}
    	if( item.getUnit() == null ){
    		error.reject("inventory.item.unit.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryItem itemE = inventoryService.getItemByName(item.getName());
    	if(item.getId() != null){
    		int  countItemInTransactionDetail = inventoryService.checkExistItemTransactionDetail(item.getId());
			int  countItemInIndentDetail = inventoryService.checkExistItemIndentDetail(item.getId());
			if(countItemInIndentDetail > 0 ||  countItemInTransactionDetail > 0){
    			item.setSpecifications(inventoryService.getItemById(item.getId()).getSpecifications());
			}
    		if(itemE != null){
    			if(itemE.getId().intValue() != item.getId().intValue()){
    				error.reject("inventory.item.name.existed");
    			}
    		}
    	}else{
    		if(itemE != null){
    	    		error.reject("inventory.item.name.existed");
    		}
    	}
    	
    	
    }
}
