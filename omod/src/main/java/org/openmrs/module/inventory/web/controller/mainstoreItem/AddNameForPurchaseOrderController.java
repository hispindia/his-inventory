package org.openmrs.module.inventory.web.controller.mainstoreItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller("AddNameForPurchaseItemOrderController")
@RequestMapping("/module/inventory/itemAddNameForPurchaseOrderSlip.form")
public class AddNameForPurchaseOrderController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(Model model) {
		return "/module/inventory/mainstoreItem/itemAddNameForPurchaseOrderSlip";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		String indentName = request.getParameter("indentName");
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		Date date = new Date();
		int userId = Context.getAuthenticatedUser().getId();
		InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		
		
		
		String fowardParam = "itemPurchase_"+userId;
		List<InventoryStoreItemIndentDetail> list = (List<InventoryStoreItemIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list != null && list.size() > 0){
			InventoryStoreItemIndent indent = new InventoryStoreItemIndent();
			indent.setName(indentName);
			indent.setCreatedOn(date);
			indent.setStore(store);
			indent.setMainStoreStatus(0);
			indent.setSubStoreStatus(0);
			indent = inventoryService.saveStoreItemIndent(indent);
			
			for(int i=0;i< list.size();i++){
				InventoryStoreItemIndentDetail indentDetail = list.get(i);
				indentDetail.setCreatedOn(date);
				indentDetail.setIndent(indent);
				inventoryService.saveStoreItemIndentDetail(indentDetail);
			}
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			model.addAttribute("message", "Succesfully");
			model.addAttribute("urlS", "itemPurchaseOrderForGeneralStoreList.form");
		}else{
			model.addAttribute("message", "Sorry don't have any purchase to save");
			model.addAttribute("urlS", "itemPurchaseOrderForGeneralStore.form");
		}
	 return "/module/inventory/thickbox/success";
	}
}
