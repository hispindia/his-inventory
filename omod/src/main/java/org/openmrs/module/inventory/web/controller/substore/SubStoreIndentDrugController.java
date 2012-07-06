package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrug;
import org.openmrs.module.inventory.model.InventoryDrugCategory;
import org.openmrs.module.inventory.model.InventoryDrugFormulation;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreIndentDrugController")
@RequestMapping("/module/inventory/subStoreIndentDrug.form")
public class SubStoreIndentDrugController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 if(categoryId != null && categoryId > 0){
		 List<InventoryDrug> drugs = inventoryService.findDrug(categoryId, null);
		 model.addAttribute("drugs",drugs);
		 
	 }
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 model.addAttribute("store",store);
	 model.addAttribute("date",new Date());
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "subStoreIndentDrug_"+userId;
	 List<InventoryStoreDrugIndentDetail> list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 model.addAttribute("listIndent", list);
	 
	 return "/module/inventory/substore/subStoreIndentDrug";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		 model.addAttribute("listCategory", listCategory);
	//	int category = NumberUtils.toInt(request.getParameter("category"),0);
		int formulation = NumberUtils.toInt(request.getParameter("formulation"),0);
		
		InventoryDrug drug = inventoryService.getDrugByName(request.getParameter("drugName"));
		if(drug == null){
			errors.add("inventory.indent.drug.required");
			
		}
		int drugId = drug.getId();
		
		int quantity = NumberUtils.toInt(request.getParameter("quantity"),0);
		
		
		InventoryDrugFormulation formulationO = inventoryService.getDrugFormulationById(formulation);
		if(formulationO == null)
		{
			errors.add("inventory.receiptDrug.formulation.required");
		}
		
	
		if(formulationO != null && drug != null && !drug.getFormulations().contains(formulationO))
		{
			errors.add("inventory.receiptDrug.formulation.notCorrect");
		}
		
		if(CollectionUtils.isNotEmpty(errors)){
		//	model.addAttribute("category", category);
			model.addAttribute("formulation", formulation);
			model.addAttribute("drugId", drugId);
			model.addAttribute("quantity", quantity);
			model.addAttribute("errors", errors);
			return "/module/inventory/substore/subStoreIndentDrug";
		}
		InventoryStoreDrugIndentDetail indentDetail = new InventoryStoreDrugIndentDetail();
		indentDetail.setDrug(drug);
		indentDetail.setFormulation(inventoryService.getDrugFormulationById(formulation));
		indentDetail.setQuantity(quantity);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "subStoreIndentDrug_"+userId;
		List<InventoryStoreDrugIndentDetail> list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		
		List<InventoryStoreDrugIndentDetail> listExt = null;
		if(list == null){
			listExt = list = new ArrayList<InventoryStoreDrugIndentDetail>();
		}else{
			listExt = new ArrayList<InventoryStoreDrugIndentDetail>(list);
		}
		for(int i=0;i< list.size();i ++ ){
			InventoryStoreDrugIndentDetail d = list.get(i);
			
			if(d.getDrug().getId().equals(indentDetail.getDrug().getId()) && 
					d.getFormulation().getId().equals(indentDetail.getFormulation().getId()))
			{
				indentDetail.setQuantity(indentDetail.getQuantity() + d.getQuantity());
				listExt.remove(i);
				break;
			}
		}
		
		listExt.add(indentDetail);
		StoreSingleton.getInstance().getHash().put(fowardParam, listExt);
		//model.addAttribute("listIndent", list);
	 return "redirect:/module/inventory/subStoreIndentDrug.form";
}
}