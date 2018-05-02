package org.openmrs.module.inventory.web.controller.item;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("ItemCategoryFormController")
@RequestMapping("/module/inventory/itemCategory.form")
public class ItemCategoryFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("itemCategory") InventoryItemCategory itemCategory, @RequestParam(value="itemCategoryId",required=false) Integer id, Model model) {
		if( id != null ){
			itemCategory = Context.getService(InventoryService.class).getItemCategoryById(id);
			model.addAttribute("itemCategory",itemCategory);
		}
		return "/module/inventory/item/itemCategory";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("itemCategory") InventoryItemCategory itemCategory, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new ItemCategoryValidator().validate(itemCategory, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/item/itemCategory";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			itemCategory.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			itemCategory.setCreatedOn(new Date());
			inventoryService.saveItemCategory(itemCategory);
			status.setComplete();
			return "redirect:/module/inventory/itemCategoryList.form";
		}
	}
	
}
