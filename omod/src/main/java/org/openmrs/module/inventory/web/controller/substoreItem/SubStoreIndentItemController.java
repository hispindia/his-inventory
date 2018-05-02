package org.openmrs.module.inventory.web.controller.substoreItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreIndentItemController")
@RequestMapping("/module/inventory/subStoreIndentItem.form")
public class SubStoreIndentItemController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 if(categoryId != null && categoryId > 0){
		 List<InventoryItem> items = inventoryService.findItem(categoryId, null);
		 model.addAttribute("items",items);
			
	 }
	 
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 model.addAttribute("store",store);
	 model.addAttribute("date",new Date());
	 
	 
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "subStoreIndentItem_"+userId;
	 List<InventoryStoreItemIndentDetail> list = (List<InventoryStoreItemIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 model.addAttribute("listIndent", list);
	 
	 return "/module/inventory/substoreItem/subStoreIndentItem";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
		 model.addAttribute("listCategory", listCategory);
		int category = NumberUtils.toInt(request.getParameter("category"),0);
		int specification = NumberUtils.toInt(request.getParameter("specification"),0);
		int itemId = NumberUtils.toInt(request.getParameter("itemId"), 0);
		int quantity = NumberUtils.toInt(request.getParameter("quantity"),0);
		
		InventoryItem item = inventoryService.getItemById(itemId);
		if(item == null){
			errors.add("inventory.indent.item.required");
			model.addAttribute("category", category);
			model.addAttribute("formulation", specification);
			model.addAttribute("itemId", itemId);
			model.addAttribute("quantity", quantity);
			model.addAttribute("errors", errors);
			return "/module/inventory/substore/subStoreIndentItem";
		}else if(CollectionUtils.isNotEmpty(item.getSpecifications()) && specification == 0 )
		{
			errors.add("inventory.receiptItem.specification.required");
			return "/module/inventory/substore/subStoreIndentItem";
		}
		
		InventoryStoreItemIndentDetail indentDetail = new InventoryStoreItemIndentDetail();
		indentDetail.setItem(item);
		indentDetail.setSpecification(inventoryService.getItemSpecificationById(specification));
		indentDetail.setQuantity(quantity);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "subStoreIndentItem_"+userId;
		List<InventoryStoreItemIndentDetail> list = (List<InventoryStoreItemIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		
		List<InventoryStoreItemIndentDetail> listExt = null;
		if(list == null){
			listExt = list = new ArrayList<InventoryStoreItemIndentDetail>();
		}else{
			listExt = new ArrayList<InventoryStoreItemIndentDetail>(list);
		}
		for(int i=0;i< list.size();i ++ ){
			InventoryStoreItemIndentDetail d = list.get(i);
			
			if(d.getItem().getId().equals(indentDetail.getItem().getId()))
				if((d.getSpecification() != null && 
					d.getSpecification().getId().equals(indentDetail.getSpecification().getId())) ||
					(d.getSpecification() == null && indentDetail.getSpecification() == null)	
				)
				{
					indentDetail.setQuantity(indentDetail.getQuantity() + d.getQuantity());
					listExt.remove(i);
					break;
				}
		}
		
		listExt.add(indentDetail);
		
		StoreSingleton.getInstance().getHash().put(fowardParam, listExt);
		//model.addAttribute("listIndent", list);
	 return "redirect:/module/inventory/subStoreIndentItem.form";
}
}