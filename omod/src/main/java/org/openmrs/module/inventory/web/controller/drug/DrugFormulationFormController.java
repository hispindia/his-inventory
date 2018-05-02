package org.openmrs.module.inventory.web.controller.drug;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.inventory.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("DrugFormulationFormController")
@RequestMapping("/module/inventory/drugFormulation.form")
public class DrugFormulationFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("drugFormulation") InventoryDrugFormulation drugFormulation, @RequestParam(value="drugFormulationId",required=false) Integer id, Model model) {
		if( id != null ){
			drugFormulation = Context.getService(InventoryService.class).getDrugFormulationById(id);
			model.addAttribute("drugFormulation",drugFormulation);
		}
		return "/module/inventory/drug/drugFormulation";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("drugFormulation") InventoryDrugFormulation drugFormulation, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new DrugFormulationValidator().validate(drugFormulation, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/drug/drugFormulation";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			drugFormulation.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			drugFormulation.setCreatedOn(new Date());
			inventoryService.saveDrugFormulation(drugFormulation);
			status.setComplete();
			return "redirect:/module/inventory/drugFormulationList.form";
		}
	}
	
}
