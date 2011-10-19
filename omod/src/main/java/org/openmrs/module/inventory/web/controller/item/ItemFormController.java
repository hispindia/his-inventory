package org.openmrs.module.inventory.web.controller.item;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryConstants;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.openmrs.module.inventory.util.Action;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.web.controller.property.editor.ItemCategoryPropertyEditor;
import org.openmrs.module.inventory.web.controller.property.editor.ItemSubCategoryPropertyEditor;
import org.openmrs.module.inventory.web.controller.property.editor.ItemUnitPropertyEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("ItemFormController")
@RequestMapping("/module/inventory/item.form")
public class ItemFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("item") InventoryItem item, @RequestParam(value="itemId",required=false) Integer id, Model model) {
		if( id != null ){
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			item = inventoryService.getItemById(id);
			int  countItemInTransactionDetail = inventoryService.checkExistItemTransactionDetail(item.getId());
			int  countItemInIndentDetail = inventoryService.checkExistItemIndentDetail(item.getId());
			if(countItemInIndentDetail > 0 ||  countItemInTransactionDetail > 0){
				model.addAttribute("delete", "This item is used in receipt or issue , you can't edit it");
			}
			model.addAttribute("item",item);
		}
		return "/module/inventory/item/item";
	}
	/*@ModelAttribute("categories")
	public List<InventoryItemCategory> populateCategories() {
 
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItemCategory> categories = inventoryService.findItemCategory("");
		return categories;
	}*/
	@ModelAttribute("subCategories")
	public List<InventoryItemSubCategory> populateSubCategories() {
 
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItemSubCategory> subCategories = inventoryService.findItemSubCategory("");
		return subCategories;
	}
	@ModelAttribute("specifications")
	public List<InventoryItemSpecification> populateFormulation() {
 
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItemSpecification> specifications = inventoryService.findItemSpecification("");
		return specifications;
	}
	@ModelAttribute("units")
	public List<InventoryItemUnit> populateUnit() {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItemUnit> units = inventoryService.findItemUnit("");
		return units;
	}
	@ModelAttribute("attributes")
	public List<Action> populateAttribute() {
		List<Action> attributes = ActionValue.getListItemAttribute();
		return attributes;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(InventoryItemCategory.class, new ItemCategoryPropertyEditor());
		binder.registerCustomEditor(InventoryItemSubCategory.class, new ItemSubCategoryPropertyEditor());
		binder.registerCustomEditor(java.lang.Integer.class,new CustomNumberEditor(java.lang.Integer.class, true));
		binder.registerCustomEditor(InventoryItemUnit.class, new ItemUnitPropertyEditor());
		binder.registerCustomEditor(Set.class, "specifications",new CustomCollectionEditor(Set.class){
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			protected Object convertElement(Object element)
			    {
				  Integer specificationId = null;
			      if (element instanceof Integer)
			    	  specificationId = (Integer) element;
			      else if (element instanceof String)
			    	  specificationId = NumberUtils.toInt((String) element , 0);
			      return specificationId != null ? inventoryService.getItemSpecificationById(specificationId) : null;
			    }
		});
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("item") InventoryItem item, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new ItemValidator().validate(item, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/inventory/item/item";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			item.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			item.setCreatedOn(new Date());
			item.setCategory(item.getSubCategory().getCategory());
			inventoryService.saveItem(item);
			status.setComplete();
			return "redirect:/module/inventory/itemList.form";
		}
	}
}
