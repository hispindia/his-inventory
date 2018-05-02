package org.openmrs.module.inventory.web.controller.item;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("ItemUnitFormController")
@RequestMapping("/module/inventory/itemUnit.form")
public class ItemUnitFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("itemUnit") InventoryItemUnit itemUnit, @RequestParam(value="itemUnitId",required=false) Integer id, Model model) {
		if( id != null ){
			itemUnit = Context.getService(InventoryService.class).getItemUnitById(id);
			model.addAttribute("itemUnit",itemUnit);
		}
		return "/module/inventory/item/itemUnit";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("itemUnit") InventoryItemUnit itemUnit, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new ItemUnitValidator().validate(itemUnit, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/item/itemUnit";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			itemUnit.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			itemUnit.setCreatedOn(new Date());
			inventoryService.saveItemUnit(itemUnit);
			status.setComplete();
			return "redirect:/module/inventory/itemUnitList.form";
		}
	}
	
}
