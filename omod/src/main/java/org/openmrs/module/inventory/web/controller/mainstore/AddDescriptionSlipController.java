/**
 * <p> File: org.openmrs.module.inventory.web.controller.mainstore.AddDescriptionSlipController.java </p>
 * <p> Project: inventory-omod </p>
 * <p> Copyright (c) 2011 CHT Technologies. </p>
 * <p> All rights reserved. </p>
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Jan 6, 2011 1:37:59 PM </p>
 * <p> Update date: Jan 6, 2011 1:37:59 PM </p>
 **/

package org.openmrs.module.inventory.web.controller.mainstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreDrug;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransaction;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p> Class: AddDescriptionSlipController </p>
 * <p> Package: org.openmrs.module.inventory.web.controller.mainstore </p> 
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Jan 6, 2011 1:37:59 PM </p>
 * <p> Update date: Jan 6, 2011 1:37:59 PM </p>
 **/

@Controller("AddDescriptionSlipController")
@RequestMapping("/module/inventory/addDescriptionReceiptSlip.form")
public class AddDescriptionSlipController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(Model model) {
		return "/module/inventory/mainstore/addDescriptionReceiptSlip";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		String description = request.getParameter("description");
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		Date date = new Date();
		int userId = Context.getAuthenticatedUser().getId();
		InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));;
		
		InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
		transaction.setDescription(description);
		transaction.setCreatedOn(date);
		transaction.setStore(store);
		transaction.setTypeTransaction(ActionValue.TRANSACTION[0]);
		transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
		transaction = inventoryService.saveStoreDrugTransaction(transaction);
		
		String fowardParam = "reipt_"+userId;
		List<InventoryStoreDrugTransactionDetail> list = (List<InventoryStoreDrugTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list != null && list.size() > 0){
			for(int i=0;i< list.size();i++){
				InventoryStoreDrugTransactionDetail transactionDetail = list.get(i);
				//save total first
				//System.out.println("transactionDetail.getDrug(): "+transactionDetail.getDrug());
				//System.out.println("transactionDetail.getFormulation(): "+transactionDetail.getFormulation());
				InventoryStoreDrug storeDrug = inventoryService.getStoreDrug(store.getId(), transactionDetail.getDrug().getId(), transactionDetail.getFormulation().getId());
				if(storeDrug == null){
					storeDrug = new InventoryStoreDrug();
					storeDrug.setCurrentQuantity(transactionDetail.getQuantity());
					storeDrug.setReceiptQuantity(transactionDetail.getQuantity());
					storeDrug.setDrug(transactionDetail.getDrug());
					storeDrug.setFormulation(transactionDetail.getFormulation());
					storeDrug.setStore(store);
					storeDrug.setStatusIndent(0);
					storeDrug.setReorderQty(0);
					storeDrug.setOpeningBalance(0);
					storeDrug.setClosingBalance(transactionDetail.getQuantity());
					storeDrug.setStatus(0);
					storeDrug.setReorderQty(transactionDetail.getDrug().getReorderQty());
					storeDrug = inventoryService.saveStoreDrug(storeDrug);
					
				}else{
					storeDrug.setOpeningBalance(storeDrug.getClosingBalance());
					storeDrug.setClosingBalance(storeDrug.getClosingBalance()+transactionDetail.getQuantity());
					storeDrug.setCurrentQuantity(storeDrug.getCurrentQuantity() + transactionDetail.getQuantity());
					storeDrug.setReceiptQuantity(transactionDetail.getQuantity());
					storeDrug.setReorderQty(transactionDetail.getDrug().getReorderQty());
					storeDrug = inventoryService.saveStoreDrug(storeDrug);
				}
				//save transactionDetail
				transactionDetail.setOpeningBalance(storeDrug.getOpeningBalance());
				transactionDetail.setClosingBalance(storeDrug.getClosingBalance());
				transactionDetail.setTransaction(transaction);
				inventoryService.saveStoreDrugTransactionDetail(transactionDetail);
			}
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			model.addAttribute("message", "Succesfully");
			model.addAttribute("urlS", "receiptsToGeneralStoreList.form");
		}else{
			model.addAttribute("message", "Sorry don't have any receipt to save");
			model.addAttribute("urlS", "receiptsToGeneralStore.form");
		}
	 return "/module/inventory/thickbox/success";
	}
}
