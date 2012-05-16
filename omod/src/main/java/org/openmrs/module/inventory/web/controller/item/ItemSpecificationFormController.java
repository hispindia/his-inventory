package org.openmrs.module.inventory.web.controller.item;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("ItemSpecificationFormController")
@RequestMapping("/module/inventory/itemSpecification.form")
public class ItemSpecificationFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("itemSpecification") InventoryItemSpecification itemSpecification, @RequestParam(value="itemSpecificationId",required=false) Integer id, Model model) {
		if( id != null ){
			itemSpecification = Context.getService(InventoryService.class).getItemSpecificationById(id);
			model.addAttribute("itemSpecification",itemSpecification);
		}
		return "/module/inventory/item/itemSpecification";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("itemSpecification") InventoryItemSpecification itemSpecification, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new ItemSpecificationValidator().validate(itemSpecification, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/item/itemSpecification";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			itemSpecification.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			itemSpecification.setCreatedOn(new Date());
			inventoryService.saveItemSpecification(itemSpecification);
			status.setComplete();
			return "redirect:/module/inventory/itemSpecificationList.form";
		}
	}
	
}
