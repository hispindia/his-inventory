package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("CreatePatientIssueDrugController")
@RequestMapping("/module/inventory/createPatientIssueDrug.form")
public class CreatePatientIssueDrugController {

	@RequestMapping(method = RequestMethod.GET)
	public String firstView(HttpServletRequest request,Model model, @RequestParam(value="patientId",required=false)  Integer patientId) {
		
		if(patientId != null && patientId > 0){
			Patient patient = Context.getPatientService().getPatient(patientId);
			if(patient != null){
				InventoryStoreDrugPatient issue = new InventoryStoreDrugPatient();
				InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
				int userId = Context.getAuthenticatedUser().getId();
				InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
				issue.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
				issue.setCreatedOn(new Date());
				issue.setStore(subStore);
				issue.setIdentifier(patient.getPatientIdentifier().getIdentifier());
				issue.setName(patient.getGivenName()+" "+patient.getFamilyName());
				issue.setPatient(patient);
				String fowardParam = "issueDrug_"+userId;
				StoreSingleton.getInstance().getHash().put(fowardParam,issue);
				String totalValue=request.getParameter("totalValue");
				Float totalValu=Float.parseFloat(totalValue);
				String waiverPercentage=request.getParameter("waiverPercentage");
				Float waiverPercentge=Float.parseFloat(waiverPercentage);
				String totalAmountPay=request.getParameter("totalAmountPayable");
				Float totalAmountPy=Float.parseFloat(totalAmountPay);
				return "redirect:/module/inventory/subStoreIssueDrugForm.form?totalValue=" + totalValu + "&waiverPercentage=" + waiverPercentge +"&totalAmountPayable=" + totalAmountPy;
			}
			
		}
		return "/module/inventory/substore/createPatientIssueDrug";
	}
	
}
