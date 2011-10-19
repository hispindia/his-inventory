package org.openmrs.module.inventory.web.controller.mainstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrug;
import org.openmrs.module.inventory.model.InventoryDrugCategory;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("PurchaseOrderFormController")
@RequestMapping("/module/inventory/purchaseOrderForGeneralStore.form")
public class PurchaseOrderFormController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "purchase_"+userId;
	 List<InventoryStoreDrugIndentDetail> list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 model.addAttribute("listPurchase", list);
	 
	 return "/module/inventory/mainstore/purchaseOrderForGeneralStore";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		 model.addAttribute("listCategory", listCategory);
		int category = NumberUtils.toInt(request.getParameter("category"),0);
		int formulation = NumberUtils.toInt(request.getParameter("formulation"),0);
		String drugName = request.getParameter("drugName");
		int quantity = NumberUtils.toInt(request.getParameter("quantity"),0);
		
		InventoryDrug drug = inventoryService.getDrugByName(drugName);
		if(drug == null){
			errors.add("inventory.purchase.drug.required");
			model.addAttribute("category", category);
			model.addAttribute("formulation", formulation);
			model.addAttribute("drugName", drugName);
			model.addAttribute("quantity", quantity);
		
			return "/module/inventory/mainstore/purchaseOrderForGeneralStore";
		}
		
		InventoryStoreDrugIndentDetail indentDetail = new InventoryStoreDrugIndentDetail();
		indentDetail.setDrug(drug);
		indentDetail.setFormulation(inventoryService.getDrugFormulationById(formulation));
		indentDetail.setQuantity(quantity);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "purchase_"+userId;
		List<InventoryStoreDrugIndentDetail> list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list == null){
			list = new ArrayList<InventoryStoreDrugIndentDetail>();
		}
		list.add(indentDetail);
		StoreSingleton.getInstance().getHash().put(fowardParam, list);
		model.addAttribute("listPurchase", list);
	 return "/module/inventory/mainstore/purchaseOrderForGeneralStore";
	}
}
