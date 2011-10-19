package org.openmrs.module.inventory.web.controller.drug;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrugUnit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("DrugUnitFormController")
@RequestMapping("/module/inventory/drugUnit.form")
public class DrugUnitFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("drugUnit") InventoryDrugUnit drugUnit, @RequestParam(value="drugUnitId",required=false) Integer id, Model model) {
		if( id != null ){
			drugUnit = Context.getService(InventoryService.class).getDrugUnitById(id);
			model.addAttribute("drugUnit",drugUnit);
		}
		return "/module/inventory/drug/drugUnit";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("drugUnit") InventoryDrugUnit drugUnit, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new DrugUnitValidator().validate(drugUnit, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/inventory/drug/drugUnit";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			drugUnit.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			drugUnit.setCreatedOn(new Date());
			inventoryService.saveDrugUnit(drugUnit);
			status.setComplete();
			return "redirect:/module/inventory/drugUnitList.form";
		}
	}
	
}
