package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("AddNameIndentSubStoreController")
@RequestMapping("/module/inventory/addNameIndentSlip.form")
public class AddNameIndentSubStoreController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@RequestParam(value="send",required=false)  String send,Model model) {
		model.addAttribute("send", send);
		return "/module/inventory/substore/addNameIndentSlip";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		String indentName = request.getParameter("indentName");
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		Date date = new Date();
		int userId = Context.getAuthenticatedUser().getId();
		InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		
		InventoryStoreDrugIndent indent = new InventoryStoreDrugIndent();
		indent.setName(indentName);
		indent.setCreatedOn(date);
		indent.setStore(store);
		
		if(!StringUtils.isBlank(request.getParameter("send"))){
			indent.setMainStoreStatus(1);
			indent.setSubStoreStatus(2);
		}else{
			indent.setMainStoreStatus(0);
			indent.setSubStoreStatus(1);
		}
		String fowardParam = "subStoreIndentDrug_"+userId;
		List<InventoryStoreDrugIndentDetail> list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list != null && list.size() > 0){
			indent = inventoryService.saveStoreDrugIndent(indent);
			for(int i=0;i< list.size();i++){
				InventoryStoreDrugIndentDetail indentDetail = list.get(i);
				indentDetail.setCreatedOn(date);
				indentDetail.setIndent(indent);
				inventoryService.saveStoreDrugIndentDetail(indentDetail);
			}
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			model.addAttribute("message", "Succesfully");
			model.addAttribute("urlS", "subStoreIndentDrugList.form");
		}else{
			model.addAttribute("message", "Sorry don't have any indents to save");
			model.addAttribute("urlS", "subStoreIndentDrug.form");
		}
	 return "/module/inventory/thickbox/success";
	}
}
