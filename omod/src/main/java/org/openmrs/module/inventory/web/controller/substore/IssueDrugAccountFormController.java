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
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("IssueDrugAccountFormController")
@RequestMapping("/module/inventory/subStoreIssueDrugAccountForm.form")
public class IssueDrugAccountFormController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
		
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		/*InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(store != null && store.getParent() != null && store.getIsDrug() == 1){
			return "redirect:/module/inventory/subStoreIssueDrugForm.form";
		}*/	
	 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 
	 if(categoryId != null && categoryId > 0){
		 List<InventoryDrug> drugs = inventoryService.findDrug(categoryId, null);
		 model.addAttribute("drugs",drugs);
		 
	 }
	 model.addAttribute("date",new Date());
	 
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "issueDrugAccountDetail_"+userId;
	 List<InventoryStoreDrugAccountDetail> list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 InventoryStoreDrugAccount issueDrugAccount = (InventoryStoreDrugAccount )StoreSingleton.getInstance().getHash().get("issueDrugAccount_"+userId);
	 model.addAttribute("listDrugAccountDetail", list);
	 model.addAttribute("issueDrugAccount", issueDrugAccount);
	 return "/module/inventory/substore/subStoreIssueDrugAccountForm";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		int userId = Context.getAuthenticatedUser().getId();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		 model.addAttribute("listCategory", listCategory);
		//int category = NumberUtils.toInt(request.getParameter("category"),0);
		Integer formulation = NumberUtils.toInt(request.getParameter("formulation"),0);
		
		InventoryDrug drug = inventoryService.getDrugByName(request.getParameter("drugName"));
		
		if(drug == null){
			errors.add("inventory.issueDrug.drug.required");
			
			//return "/module/inventory/substore/subStoreIssueDrugAccountForm";
		}
		int drugId= drug.getId();
			
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
			model.addAttribute("errors", errors);
			String fowardParam = "issueDrugAccountDetail_"+userId;
			List<InventoryStoreDrugAccountDetail> list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			StoreSingleton.getInstance().getHash().put(fowardParam, list);
			InventoryStoreDrugAccount issueDrugPatient = (InventoryStoreDrugAccount )StoreSingleton.getInstance().getHash().get("issueDrugAccount_"+userId);
			 model.addAttribute("issueDrugAccount", issueDrugPatient);
			model.addAttribute("listDrugAccountDetail", list);
			return "/module/inventory/substore/subStoreIssueDrugAccountForm";
		}
		
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<Integer> listIssueQty = new ArrayList<Integer>();
		List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService.listStoreDrugTransactionDetail(store.getId(), drug.getId(), formulation, true);
		boolean checkCorrect = true;
		if(listReceiptDrug != null){
			model.addAttribute("listReceiptDrug", listReceiptDrug);
			for(InventoryStoreDrugTransactionDetail t: listReceiptDrug){
				
				Integer temp = NumberUtils.toInt(request.getParameter(t.getId()+"") , 0);
				//System.out.println(" transaction detail "+t.getId() +" : "+temp);
				if(temp > 0){
					checkCorrect = false;
				}else{
					temp = 0;
				}
				listIssueQty.add(temp);
				if(temp > t.getCurrentQuantity()){
					errors.add("inventory.issueDrug.quantity.lessthanQuantity.required");
				}
			}
		}else{
			errors.add("inventory.issueDrug.drug.required");
		}
		if(checkCorrect){
			errors.add("inventory.issueDrug.quantity.required");
		}
		if(errors != null && errors.size() > 0){
			
		//	model.addAttribute("category", category);
			model.addAttribute("formulation", formulation);
			model.addAttribute("listIssueQty", listIssueQty);
			model.addAttribute("drugId", drugId);
			model.addAttribute("errors", errors);
			String fowardParam = "issueDrugAccountDetail_"+userId;
			List<InventoryStoreDrugAccountDetail> list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			StoreSingleton.getInstance().getHash().put(fowardParam, list);
			 InventoryStoreDrugAccount issueDrugPatient = (InventoryStoreDrugAccount )StoreSingleton.getInstance().getHash().get("issueDrugAccount_"+userId);
			 model.addAttribute("issueDrugAccount", issueDrugPatient);
			model.addAttribute("listDrugAccountDetail", list);
			return "/module/inventory/substore/subStoreIssueDrugAccountForm";
		}
		//System.out.println("COME HERE!!!!!!!!!!!!!!            ");
		String fowardParam = "issueDrugAccountDetail_"+userId;
		List<InventoryStoreDrugAccountDetail> list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		List<InventoryStoreDrugAccountDetail> listExt = null;
		if(list == null){
			listExt = list = new ArrayList<InventoryStoreDrugAccountDetail>();
		}else{
			listExt = new ArrayList<InventoryStoreDrugAccountDetail>(list);
		}
		for(InventoryStoreDrugTransactionDetail t: listReceiptDrug){
			Integer temp = NumberUtils.toInt(request.getParameter(t.getId()+"") , 0);
			if(temp > 0){
				
				if(CollectionUtils.isNotEmpty(list)){
					for(int i=0;i<list.size();i++){
						InventoryStoreDrugAccountDetail dtail = list.get(i);
						if(t.getId().equals(dtail.getTransactionDetail().getId())){
							listExt.remove(i);
							temp += dtail.getQuantity();
							break;
						}
					}
				}
				//System.out.println("temp add vao issue : "+temp);
				InventoryStoreDrugAccountDetail issueDrugDetail = new InventoryStoreDrugAccountDetail();
				issueDrugDetail.setTransactionDetail(t);
				issueDrugDetail.setQuantity(temp);
				listExt.add(issueDrugDetail);
			}
		}
		StoreSingleton.getInstance().getHash().put(fowardParam, listExt);
		InventoryStoreDrugAccount issueDrugAccount = (InventoryStoreDrugAccount )StoreSingleton.getInstance().getHash().get("issueDrugAccount_"+userId);
		//model.addAttribute("issueDrugAccount", issueDrugAccount);
		//model.addAttribute("listDrugAccountDetail", list);
	 return "redirect:/module/inventory/subStoreIssueDrugAccountForm.form";
	}
}
