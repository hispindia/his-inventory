package org.openmrs.module.inventory.web.controller.substore;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.model.InventoryStoreDrugPatient;
import org.springframework.validation.Errors;

public class IssueDrugPatientValidator {
	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryStoreDrugPatient.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryStoreDrugPatient cmd = (InventoryStoreDrugPatient) command;
    	String identifier = cmd.getIdentifier();
    	String prefix = Context.getAdministrationService().getGlobalProperty("registration.identifier_prefix");
    	if( identifier.contains("-") && !identifier.contains(prefix)){
			identifier = prefix+identifier;
    	}
    	if( StringUtils.isBlank(cmd.getIdentifier())){
    		error.reject("inventory.issueDrug.identifier.required");
    	}
    	if(Context.getPatientService().getPatients(identifier) == null){
    		error.reject("inventory.issueDrug.identifier.required");
    	}
    	
    	
    }
}
