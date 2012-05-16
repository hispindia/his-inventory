package org.openmrs.module.inventory.web.controller.substoreItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreItem;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreItemProcessReceiptIndentController")
@RequestMapping("/module/inventory/subStoreItemProcessIndent.form")
public class SubStoreItemProcessReceiptIndentController {

	
	@RequestMapping(method = RequestMethod.GET)
	public String sendIndent( @RequestParam(value="indentId",required=false)  Integer id,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreItemIndent indent = inventoryService.getStoreItemIndentById(id);
		if(indent != null && indent.getSubStoreStatus() != 3 && indent.getMainStoreStatus() != 3){
			return "redirect:/module/inventory/subStoreIndentItemList.form";
		}
		List<InventoryStoreItemIndentDetail> listItemNeedProcess = inventoryService.listStoreItemIndentDetail(id);
		//Collection<Integer> formulationIds = new ArrayList<Integer>();
		//Collection<Integer> ItemIds = new ArrayList<Integer>();
		model.addAttribute("listItemNeedProcess", listItemNeedProcess);
		model.addAttribute("indent", indent);
		return "/module/inventory/substoreItem/subStoreItemProcessIndent";
		
		
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit( HttpServletRequest request, Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
 	 Integer indentId = NumberUtils.toInt(request.getParameter("indentId"));
	 InventoryStoreItemIndent indent =inventoryService.getStoreItemIndentById(indentId);
	 List<InventoryStoreItemIndentDetail> listIndentDetail = inventoryService.listStoreItemIndentDetail(indentId);
	 
	InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	List<InventoryStoreItemTransactionDetail> refundItemList = inventoryService.listStoreItemTransactionDetail(indent.getTransaction().getId());
	
	 if("1".equals(request.getParameter("refuse"))){
		if(indent != null){
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[3]);
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[3]);
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
		
		
		if(refundItemList != null && refundItemList.size() > 0){
			InventoryStoreItemTransaction transaction = new InventoryStoreItemTransaction();
			transaction.setStore(subStore.getParent());
			transaction.setDescription("REFUND ITEM BC SUBSTORE REFUSE "+DateUtils.getDDMMYYYY());
			transaction.setTypeTransaction(ActionValue.TRANSACTION[0]);
			transaction.setCreatedBy("System");
			transaction.setCreatedOn(new Date());
			transaction = inventoryService.saveStoreItemTransaction(transaction);
			 
			 
			for(InventoryStoreItemTransactionDetail refund : refundItemList){
				
				Date date = new Date();
				Integer sumTotalQuantity = inventoryService.sumStoreItemCurrentQuantity(subStore.getParent().getId(), refund.getItem().getId(), refund.getSpecification()!= null?refund.getSpecification().getId() : null);
				InventoryStoreItemTransactionDetail transDetail = new InventoryStoreItemTransactionDetail();
				transDetail.setTransaction(transaction);
				transDetail.setItem(refund.getItem());
				transDetail.setCompanyName(refund.getCompanyName());
				transDetail.setCreatedOn(date);
				transDetail.setDateManufacture(refund.getDateManufacture());
				transDetail.setSpecification(refund.getSpecification());
				transDetail.setUnitPrice(refund.getUnitPrice());
				transDetail.setVAT(refund.getVAT());
				transDetail.setParent(refund);
				transDetail.setReceiptDate(date);
				transDetail.setQuantity(refund.getIssueQuantity());
				/* Money moneyUnitPrice = new Money(refund.getUnitPrice());
				 Money vATUnitPrice = new Money(refund.getVAT());
				 Money m = moneyUnitPrice.plus(vATUnitPrice);
				 Money totl = m.times(refund.getIssueQuantity());
				 transDetail.setTotalPrice(totl.getAmount());*/
				/*
				 Money moneyUnitPrice = new Money(refund.getUnitPrice());
				 Money totl = moneyUnitPrice.times(refund.getIssueQuantity());
				
				totl = totl.plus(totl.times(refund.getVAT().divide(new BigDecimal(100))));
				*/
				BigDecimal moneyUnitPrice = refund.getUnitPrice().multiply(new BigDecimal(refund.getIssueQuantity()));
				moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(refund.getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setCurrentQuantity(refund.getIssueQuantity());
				transDetail.setIssueQuantity(0);
				transDetail.setOpeningBalance(sumTotalQuantity);
				transDetail.setClosingBalance(sumTotalQuantity + refund.getIssueQuantity());
				inventoryService.saveStoreItemTransactionDetail(transDetail);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return "redirect:/module/inventory/subStoreIndentItemList.form";
	 }
	 //save here 

	 InventoryStoreItemTransaction transaction = new InventoryStoreItemTransaction();
		transaction.setStore(subStore);
		transaction.setDescription("RECEIPT "+DateUtils.getDDMMYYYY());
		transaction.setTypeTransaction(ActionValue.TRANSACTION[0]);
		transaction.setCreatedBy("System");
		transaction.setCreatedOn(new Date());
		transaction = inventoryService.saveStoreItemTransaction(transaction);
		 
		 
		for(InventoryStoreItemTransactionDetail refund : refundItemList){
			
			Date date = new Date();
			Integer specificationId = refund.getSpecification() != null ? refund.getSpecification().getId() : null;
			Integer sumTotalQuantity = inventoryService.sumStoreItemCurrentQuantity(subStore.getId(), refund.getItem().getId(), specificationId);
			InventoryStoreItemTransactionDetail transDetail = new InventoryStoreItemTransactionDetail();
			transDetail.setTransaction(transaction);
			transDetail.setItem(refund.getItem());
			transDetail.setCompanyName(refund.getCompanyName());
			transDetail.setCreatedOn(date);
			transDetail.setDateManufacture(refund.getDateManufacture());
			transDetail.setSpecification(refund.getSpecification());
			transDetail.setUnitPrice(refund.getUnitPrice());
			transDetail.setVAT(refund.getVAT());
			transDetail.setParent(refund);
			transDetail.setQuantity(refund.getIssueQuantity());
			transDetail.setReceiptDate(date);
			
			 /*Money moneyUnitPrice = new Money(refund.getUnitPrice());
			 Money vATUnitPrice = new Money(refund.getVAT());
			 Money m = moneyUnitPrice.plus(vATUnitPrice);
			 Money totl = m.times(refund.getIssueQuantity());
			 transDetail.setTotalPrice(totl.getAmount());*/
			
			 /*Money moneyUnitPrice = new Money(refund.getUnitPrice());
			 Money totl = moneyUnitPrice.times(refund.getIssueQuantity());
			
			totl = totl.plus(totl.times(refund.getVAT().divide(new BigDecimal(100),2)));
			transDetail.setTotalPrice(totl.getAmount());*/
			
			BigDecimal moneyUnitPrice = refund.getUnitPrice().multiply(new BigDecimal(refund.getIssueQuantity()));
			moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(refund.getVAT().divide(new BigDecimal(100))));
			transDetail.setTotalPrice(moneyUnitPrice);
			
			
			 transDetail.setQuantity(refund.getIssueQuantity());
			transDetail.setCurrentQuantity(refund.getIssueQuantity());
			transDetail.setIssueQuantity(0);
			transDetail.setOpeningBalance(sumTotalQuantity);
			transDetail.setClosingBalance(sumTotalQuantity + refund.getIssueQuantity());
			inventoryService.saveStoreItemTransactionDetail(transDetail);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 //indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[2]);
	 indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[4]);
	 inventoryService.saveStoreItemIndent(indent);
	 return "redirect:/module/inventory/subStoreIndentItemList.form";
	 
 }
}
