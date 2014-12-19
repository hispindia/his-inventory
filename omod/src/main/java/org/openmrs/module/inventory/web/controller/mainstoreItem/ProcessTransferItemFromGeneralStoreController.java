package org.openmrs.module.inventory.web.controller.mainstoreItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreItem;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ProcessTransferItemFromGeneralStoreController")
@RequestMapping("/module/inventory/mainStoreItemProcessIndent.form")
public class ProcessTransferItemFromGeneralStoreController {
	@RequestMapping(method = RequestMethod.GET)
	public String sendIndent( @RequestParam(value="indentId",required=false)  Integer id,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreItemIndent indent = inventoryService.getStoreItemIndentById(id);
		InventoryStore mainStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(indent != null && indent.getSubStoreStatus() == 2 && indent.getMainStoreStatus() == 1){
			List<InventoryStoreItemIndentDetail> listItemNeedProcess = inventoryService.listStoreItemIndentDetail(id);
			Collection<Integer> specificationIds = new ArrayList<Integer>();
			Collection<Integer> itemIds = new ArrayList<Integer>();
			for(InventoryStoreItemIndentDetail t : listItemNeedProcess){
				if(t.getSpecification() != null){
					specificationIds.add(t.getSpecification().getId());
				}
				itemIds.add(t.getItem().getId());
			}
			//need change it in future
			List<InventoryStoreItemTransactionDetail> transactionAvaiableOfMainStore = inventoryService.listStoreItemAvaiable(mainStore.getId(), itemIds, null);
			//System.out.println("transactionAvaiableOfMainStore: "+transactionAvaiableOfMainStore);
			List<InventoryStoreItemIndentDetail> listItemTP = new ArrayList<InventoryStoreItemIndentDetail>(); 
			for(InventoryStoreItemIndentDetail t : listItemNeedProcess){
				if(transactionAvaiableOfMainStore != null && transactionAvaiableOfMainStore.size() > 0){
					for(InventoryStoreItemTransactionDetail trDetail : transactionAvaiableOfMainStore ){
						if( t.getSpecification() != null){
							if(trDetail.getSpecification() != null){
								if(t.getItem().getId() == trDetail.getItem().getId() && t.getSpecification().getId() == trDetail.getSpecification().getId()){
									t.setMainStoreTransfer(trDetail.getCurrentQuantity());
								}
							}else{
								continue;
							}
						}else{
							//if(t.getItem().getId() == trDetail.getItem().getId() && trDetail.getSpecification() == null){
							//ghanshyam 28/06/2012 tag RC_REF_COMPARISON
								if(t.getItem().getId().equals(trDetail.getItem().getId()) && trDetail.getSpecification() == null){
								t.setMainStoreTransfer(trDetail.getCurrentQuantity());
							}
						}
					}
				}else{
					t.setMainStoreTransfer(0);
				}
				listItemTP.add(t);
			}
			//System.out.println("listItemTP: "+listItemTP);
			//need change in future this
			//System.out.println("truong hop bo het: "+inventoryService.listStoreDrugAvaiable(mainStore.getId(), null, null));
			model.addAttribute("listItemNeedProcess", listItemTP);
			model.addAttribute("indent", indent);
			return "/module/inventory/mainstoreItem/mainStoreItemProcessIndent";
		}
		
		return "redirect:/module/inventory/transferItemFromGeneralStore.form";
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit( HttpServletRequest request, Model model) {
	 List<String> errors = new ArrayList<String>();
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
 	 Integer indentId = NumberUtils.toInt(request.getParameter("indentId"));
	 InventoryStoreItemIndent indent =inventoryService.getStoreItemIndentById(indentId);
	 List<InventoryStoreItemIndentDetail> listIndentDetail = inventoryService.listStoreItemIndentDetail(indentId);
	 if("1".equals(request.getParameter("refuse"))){
		if(indent != null){
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[1]);
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[5]);
			inventoryService.saveStoreItemIndent(indent);
			
			for(InventoryStoreItemIndentDetail t : listIndentDetail){
				 Integer specificationId = t.getSpecification() != null ? t.getSpecification().getId() : null;
				InventoryStoreItem storeItem = inventoryService.getStoreItem(indent.getStore().getId(), t.getItem().getId(),specificationId);
				if(storeItem != null ){
					storeItem.setStatusIndent(0);
					inventoryService.saveStoreItem(storeItem);
				}
				
			}
		}
		return "redirect:/module/inventory/transferItemFromGeneralStore.form";
	 }
	 //validate here 
	 
	Collection<Integer> specificationIds = new ArrayList<Integer>();
		Collection<Integer> itemIds = new ArrayList<Integer>();
		for(InventoryStoreItemIndentDetail t : listIndentDetail){
			if(t.getSpecification() != null){
				specificationIds.add(t.getSpecification().getId());
			}
			itemIds.add(t.getItem().getId());
		}
		InventoryStore mainStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreItemTransactionDetail> transactionAvaiableOfMainStore = inventoryService.listStoreItemAvaiable(mainStore.getId(), itemIds, null);
	
	 
	 boolean passTransfer = true;
	 List<Integer> quantityTransfers = new ArrayList<Integer>(); 
	 //get available quantity mainstore have on hand
	 List<InventoryStoreItemIndentDetail> listItemTP = new ArrayList<InventoryStoreItemIndentDetail>(); 
	for(InventoryStoreItemIndentDetail t : listIndentDetail){
		int temp = NumberUtils.toInt(request.getParameter(t.getId()+"") );
		//get to return view value
		quantityTransfers.add(temp);
		if(transactionAvaiableOfMainStore != null && transactionAvaiableOfMainStore.size() > 0){
		for(InventoryStoreItemTransactionDetail trDetail : transactionAvaiableOfMainStore ){
			
			if(t.getSpecification() != null){
				if(trDetail.getSpecification() != null){
					//ghanshyam 7-august-2013 code review bug
					if(t.getItem().getId().equals(trDetail.getItem().getId()) && t.getSpecification().getId().equals(trDetail.getSpecification().getId())){
						t.setMainStoreTransfer(trDetail.getCurrentQuantity());
						if(temp > trDetail.getCurrentQuantity() || temp < 0 ){
							//System.out.println("nhay avo day 1");
							errors.add("inventory.indent.error.quantity");
							break;
						}
					}
				}else{
					continue;
				}
			}else{
				//ghanshyam 7-august-2013 code review bug
				if(t.getItem().getId().equals(trDetail.getItem().getId()) && trDetail.getSpecification() == null){
					t.setMainStoreTransfer(trDetail.getCurrentQuantity());
					if(temp > trDetail.getCurrentQuantity() || temp < 0 ){
						//System.out.println("temp: "+temp+" itemName: "+t.getItem().getName()+" current quantity: "+trDetail.getCurrentQuantity());
						//System.out.println("nhay avo day 2");
						errors.add("inventory.indent.error.quantity");
						break;
					}
				}
			}
			
			
			
		}
		}else{
			//System.out.println("nhay avo day 3");
			errors.add("inventory.indent.error.quantity");
			break;
		}
		 if(temp > 0){
			 passTransfer = false;
		 }
		listItemTP.add(t);
	}
	 if(passTransfer){
		// System.out.println("nhay avo day 4");
		 errors.add("inventory.indent.error.transfer");
	 }
	 if(errors != null && errors.size() > 0){
			model.addAttribute("listItemNeedProcess", listItemTP);
			model.addAttribute("indent", indent);
			model.addAttribute("errors", errors);
			model.addAttribute("quantityTransfers", quantityTransfers);
		 return "/module/inventory/mainstoreItem/mainStoreItemProcessIndent";
	 }
	 
	 //create transaction
	 InventoryStoreItemTransaction transaction = new InventoryStoreItemTransaction();
	 transaction.setDescription("TRANSFER ITEM SYSTEM AUTO "+DateUtils.getDDMMYYYY());
	 transaction.setStore(mainStore);
	 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
	 transaction.setCreatedOn(new Date());
	 transaction.setCreatedBy("System");
	 transaction = inventoryService.saveStoreItemTransaction(transaction);
	 
	 
	 
	 for(InventoryStoreItemIndentDetail t : listIndentDetail){
		 int temp = NumberUtils.toInt(request.getParameter(t.getId()+""), 0);
		 //System.out.println("temp : "+temp);
		 t.setMainStoreTransfer(temp);
		 if(temp > 0){
			//sum currentQuantity of drugId, formulationId of store
			 Integer specificationId = t.getSpecification() != null ? t.getSpecification().getId() : null;
			 Integer totalQuantity = inventoryService.sumStoreItemCurrentQuantity(mainStore.getId(),t.getItem().getId(), specificationId);
			 //list all transaction detail with condition dateExpiry > now() , drugId = ? , formulationId = ? of mainstore
			 List<InventoryStoreItemTransactionDetail> listTransactionAvailableMS = inventoryService.listStoreItemTransactionDetail(mainStore.getId(), t.getItem().getId(), specificationId, true);
			 InventoryStoreItemTransactionDetail transDetail = new InventoryStoreItemTransactionDetail();
			 transDetail.setTransaction(transaction);
			 InventoryStoreItem storeItem = inventoryService.getStoreItem(mainStore.getId(), t.getItem().getId(), specificationId);
			 for(InventoryStoreItemTransactionDetail trDetail : listTransactionAvailableMS ){
				 Integer x = trDetail.getCurrentQuantity() - temp;
					if( x >= 0){
						Date date = new Date();
						//update current quantity of mainstore in transactionDetail
						trDetail.setCurrentQuantity(x);
						inventoryService.saveStoreItemTransactionDetail(trDetail);
						
						transDetail.setItem(trDetail.getItem());
						transDetail.setCompanyName(trDetail.getCompanyName());
						transDetail.setCreatedOn(date);
						transDetail.setDateManufacture(trDetail.getDateManufacture());
						transDetail.setSpecification(trDetail.getSpecification());
						transDetail.setUnitPrice(trDetail.getUnitPrice());
						transDetail.setVAT(trDetail.getVAT());
						transDetail.setCostToPatient(trDetail.getCostToPatient());
						transDetail.setParent(trDetail);
						transDetail.setReceiptDate(date);
						
						/* Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money vATUnitPrice = new Money(trDetail.getVAT());
						 Money m = moneyUnitPrice.plus(vATUnitPrice);
						 Money totl = m.times(temp);
						 transDetail.setTotalPrice(totl.getAmount());*/
						
						/* Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money totl = moneyUnitPrice.times(temp);
						
						totl = totl.plus(totl.times(trDetail.getVAT().divide(new BigDecimal(100))));
						transDetail.setTotalPrice(totl.getAmount());*/
						
						BigDecimal moneyUnitPrice = trDetail.getCostToPatient().multiply(new BigDecimal(temp));
						//moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(trDetail.getVAT().divide(new BigDecimal(100))));
						transDetail.setTotalPrice(moneyUnitPrice);
						
						 transDetail.setQuantity(0);
						transDetail.setCurrentQuantity(0);
						transDetail.setIssueQuantity(temp);
						transDetail.setOpeningBalance(totalQuantity);
						transDetail.setClosingBalance(totalQuantity - temp);
						
						transDetail.setAttribute(trDetail.getItem().getAttributeName());
						
						inventoryService.saveStoreItemTransactionDetail(transDetail);
						
						//save last to StoreDrug
						storeItem.setOpeningBalance(totalQuantity);
						storeItem.setClosingBalance(totalQuantity - temp);
						storeItem.setIssueQuantity(storeItem.getIssueQuantity() + temp);
						storeItem.setCurrentQuantity(totalQuantity - temp);
						inventoryService.saveStoreItem(storeItem);
						
						//create transactionDetail for transfer
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}else{
						Date date = new Date();
						//truong hop mot transactionDetail be hon cai can transfer.
						transDetail.setIssueQuantity(trDetail.getCurrentQuantity());
						trDetail.setCurrentQuantity(0);
						inventoryService.saveStoreItemTransactionDetail(trDetail);
						
						 /*Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money vATUnitPrice = new Money(trDetail.getVAT());
						 Money m = moneyUnitPrice.plus(vATUnitPrice);
						 Money totl = m.times(transDetail.getIssueQuantity());
						 transDetail.setTotalPrice(totl.getAmount());*/
						/*
						 Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money totl = moneyUnitPrice.times(temp);
						
						totl = totl.plus(totl.times(trDetail.getVAT().divide(new BigDecimal(100))));
						transDetail.setTotalPrice(totl.getAmount());*/
						
						BigDecimal moneyUnitPrice = trDetail.getCostToPatient().multiply(new BigDecimal(transDetail.getIssueQuantity()));
						/*moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(trDetail.getVAT().divide(new BigDecimal(100))));*/
						transDetail.setTotalPrice(moneyUnitPrice);
						 
						transDetail.setCurrentQuantity(0);
						transDetail.setOpeningBalance(totalQuantity);
						
						transDetail.setItem(trDetail.getItem());
						transDetail.setCompanyName(trDetail.getCompanyName());
						transDetail.setCreatedOn(date);
						transDetail.setDateManufacture(trDetail.getDateManufacture());
						transDetail.setSpecification(trDetail.getSpecification());
						transDetail.setUnitPrice(trDetail.getUnitPrice());
						transDetail.setVAT(trDetail.getVAT());
						transDetail.setCostToPatient(trDetail.getCostToPatient());
						transDetail.setParent(trDetail);
						transDetail.setReceiptDate(date);
						//NEW
						transDetail.setAttribute(trDetail.getItem().getAttributeName());
						//
						
						
						transDetail.setClosingBalance(totalQuantity - transDetail.getIssueQuantity());
						inventoryService.saveStoreItemTransactionDetail(transDetail);
						totalQuantity -=  transDetail.getIssueQuantity();
						temp -=  transDetail.getIssueQuantity();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//create transactionDetail for transfer
					}
				 
				}
			 
		 }
		 inventoryService.saveStoreItemIndentDetail(t);
	 }
	 //System.out.println("den day roi");
	 //add issue transaction from general store 
	 indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[2]);
	 indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[2]);
	 indent.setTransaction(transaction);
	 inventoryService.saveStoreItemIndent(indent);
	 return "redirect:/module/inventory/transferItemFromGeneralStore.form";
	 
 }
}
