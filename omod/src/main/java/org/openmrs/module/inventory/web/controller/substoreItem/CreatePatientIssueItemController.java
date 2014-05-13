package org.openmrs.module.inventory.web.controller.substoreItem;

import java.util.ArrayList;
import java.util.Date;

import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreItemPatient;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("CreatePatientIssueItemController")
@RequestMapping("/module/inventory/createPatientIssueItem.form")
public class CreatePatientIssueItemController {

	@RequestMapping(method = RequestMethod.GET)
	public String firstView(Model model, @RequestParam(value="patientId",required=false)  Integer patientId) {
		
		if(patientId != null && patientId > 0){
			Patient patient = Context.getPatientService().getPatient(patientId);
			if(patient != null){
				InventoryStoreItemPatient issue = new InventoryStoreItemPatient();
				InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
				int userId = Context.getAuthenticatedUser().getId();
				InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
				issue.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
				issue.setCreatedOn(new Date());
				issue.setStore(subStore);
				issue.setIdentifier(patient.getPatientIdentifier().getIdentifier());
				issue.setName(patient.getGivenName()+" "+patient.getMiddleName()+" "+patient.getFamilyName());
				issue.setPatient(patient);
				String fowardParam = "issueItemPatient_"+userId;
				StoreSingleton.getInstance().getHash().put(fowardParam,issue);
				return "redirect:/module/inventory/subStoreIssueItemPatientForm.form";
			}
			
		}
		return "/module/inventory/substoreItem/createPatientIssueItem";
	}
	
}
