package org.openmrs.module.inventory.web.controller.drug;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrug;
import org.springframework.validation.Errors;

public class DrugValidator {
	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryDrug.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryDrug drug = (InventoryDrug) command;
    	
    	if( StringUtils.isBlank(drug.getName())){
    		error.reject("inventory.drug.name.required");
    	}
    	if( drug.getCategory() == null){
    		error.reject("inventory.drug.category.required");
    	}
    	if( drug.getDrugCore() == null){
    		error.reject("inventory.drug.drug.required");
    	}
    	if( CollectionUtils.isEmpty(drug.getFormulations()) && drug.getId() == null){
    		error.reject("inventory.drug.formulation.required");
    	}
    	if( drug.getUnit() == null ){
    		error.reject("inventory.drug.unit.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryDrug drugE = inventoryService.getDrugByName(drug.getName());
    	if(drug.getId() != null){
    		int  countDrugInTransactionDetail = inventoryService.checkExistDrugTransactionDetail(drug.getId());
			int  countDrugInIndentDetail = inventoryService.checkExistDrugIndentDetail(drug.getId());
			if(countDrugInIndentDetail > 0 || countDrugInTransactionDetail >0){
				drug.setFormulations(inventoryService.getDrugById(drug.getId()).getFormulations());
			}
    		if(drugE != null ){
    			if(drugE.getId().intValue() != drug.getId().intValue()){
    				error.reject("inventory.drug.name.existed");
    			}
    		}
    	}else{
    		if(drugE != null){
    	    		error.reject("inventory.drug.name.existed");
    		}
    	}
    	
    	
    }
}
