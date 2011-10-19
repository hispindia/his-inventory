package org.openmrs.module.inventory.web.controller.substoreItem;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.springframework.validation.Errors;

public class IssueItemAccountValidator {
	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryStoreItemAccount.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryStoreItemAccount cmd = (InventoryStoreItemAccount) command;
    	if( StringUtils.isBlank(cmd.getName())){
    		error.reject("inventory.issueItem.name.required");
    	}
    }
}
