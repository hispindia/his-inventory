package org.openmrs.module.inventory.web.controller.drug;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.inventory.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("DrugCategoryFormController")
@RequestMapping("/module/inventory/drugCategory.form")
public class DrugCategoryFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("drugCategory") InventoryDrugCategory drugCategory, @RequestParam(value="drugCategoryId",required=false) Integer id, Model model) {
		if( id != null ){
			drugCategory = Context.getService(InventoryService.class).getDrugCategoryById(id);
			model.addAttribute("drugCategory",drugCategory);
		}
		return "/module/inventory/drug/drugCategory";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("drugCategory") InventoryDrugCategory drugCategory, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new DrugCategoryValidator().validate(drugCategory, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/drug/drugCategory";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			drugCategory.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			drugCategory.setCreatedOn(new Date());
			inventoryService.saveDrugCategory(drugCategory);
			status.setComplete();
			return "redirect:/module/inventory/drugCategoryList.form";
		}
	}
	
}
