package org.openmrs.module.inventory.web.controller.store;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StoreValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryStore.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryStore store = (InventoryStore) command;
    	
    	if( StringUtils.isBlank(store.getName())){
    		error.reject("inventory.store.name.required");
    	}
    	if(store.getRole() == null){
    		error.reject("inventory.store.role.required");
    	}
    	/*if( StringUtils.isBlank(store.getCode())){
    		error.reject("inventory.store.code.required");
    	}*/
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryStore storeE = inventoryService.getStoreByName(store.getName());
    	if(store.getId() != null){
    		if(storeE != null && storeE.getId().intValue() != store.getId().intValue()){
    			error.reject("inventory.store.name.existed");
    		}
    	}else{
    		if(storeE != null){
    	    		error.reject("inventory.store.name.existed");
    		}
    	}
    	
    	
    }

}