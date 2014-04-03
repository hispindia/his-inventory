package org.openmrs.module.inventory.web.controller.substore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreDrug;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreDrugProcessReceiptIndentController")
@RequestMapping("/module/inventory/subStoreDrugProcessIndent.form")
public class SubStoreDrugProcessReceiptIndentController {

	
	@RequestMapping(method = RequestMethod.GET)
	public String sendIndent( @RequestParam(value="indentId",required=false)  Integer id,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreDrugIndent indent = inventoryService.getStoreDrugIndentById(id);
		if(indent != null && indent.getSubStoreStatus() != 3 && indent.getMainStoreStatus() != 3){
			return "redirect:/module/inventory/subStoreIndentDrugList.form";
		}
		List<InventoryStoreDrugIndentDetail> listDrugNeedProcess = inventoryService.listStoreDrugIndentDetail(id);
		//Collection<Integer> formulationIds = new ArrayList<Integer>();
		//Collection<Integer> drugIds = new ArrayList<Integer>();
		model.addAttribute("listDrugNeedProcess", listDrugNeedProcess);
		model.addAttribute("indent", indent);
		return "/module/inventory/substore/subStoreDrugProcessIndent";
		
		
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit( HttpServletRequest request, Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
 	 Integer indentId = NumberUtils.toInt(request.getParameter("indentId"));
	 InventoryStoreDrugIndent indent =inventoryService.getStoreDrugIndentById(indentId);
	 List<InventoryStoreDrugIndentDetail> listIndentDetail = inventoryService.listStoreDrugIndentDetail(indentId);
	 
	InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	List<InventoryStoreDrugTransactionDetail> refundDrugList = inventoryService.listTransactionDetail(indent.getTransaction().getId());
	 if("1".equals(request.getParameter("refuse"))){
		if(indent != null){
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[3]);
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[3]);
			inventoryService.saveStoreDrugIndent(indent);
			
			for(InventoryStoreDrugIndentDetail t : listIndentDetail){
				InventoryStoreDrug storeDrug = inventoryService.getStoreDrug(indent.getStore().getId(), t.getDrug().getId(),t.getFormulation().getId());
				if(storeDrug != null ){
					storeDrug.setStatusIndent(0);
					inventoryService.saveStoreDrug(storeDrug);
				}
				
			}
		}
		
		
		if(refundDrugList != null && refundDrugList.size() > 0){
			InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
			//transaction.setStore(subStore.getParent());
			/*
			 * luan- should update code here as new requirement for multiple main stores
			 */
			transaction.setDescription("REFUND BC SUBSTORE REFUSE "+DateUtils.getDDMMYYYY());
			transaction.setTypeTransaction(ActionValue.TRANSACTION[0]);
			transaction.setCreatedBy("System");
			transaction.setCreatedOn(new Date());
			transaction = inventoryService.saveStoreDrugTransaction(transaction);
			 
			 
			for(InventoryStoreDrugTransactionDetail refund : refundDrugList){
				
				Date date = new Date();
				//Integer sumTotalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(subStore.getParent().getId(), refund.getDrug().getId(), refund.getFormulation().getId());
				/*
				 * luan- should update code here as new requirement for multiple main stores
				 */
				Integer sumTotalQuantity = 0; //just for testing. Remember to remove it
				InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
				transDetail.setTransaction(transaction);
				transDetail.setDrug(refund.getDrug());
				transDetail.setDateExpiry(refund.getDateExpiry());
				transDetail.setBatchNo(refund.getBatchNo());
				transDetail.setCompanyName(refund.getCompanyName());
				transDetail.setCreatedOn(new Date());
				transDetail.setDateManufacture(refund.getDateManufacture());
				transDetail.setFormulation(refund.getFormulation());
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
				
				 /*Money moneyUnitPrice = new Money(refund.getUnitPrice());
				 Money totl = moneyUnitPrice.times(refund.getIssueQuantity());
				
				totl = totl.plus(totl.times((double)refund.getVAT()/100));
				transDetail.setTotalPrice(totl.getAmount());*/
				BigDecimal moneyUnitPrice = refund.getUnitPrice().multiply(new BigDecimal(refund.getIssueQuantity()));
				moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(refund.getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setCurrentQuantity(refund.getIssueQuantity());
				transDetail.setIssueQuantity(0);
				transDetail.setOpeningBalance(sumTotalQuantity);
				transDetail.setClosingBalance(sumTotalQuantity + refund.getIssueQuantity());
				inventoryService.saveStoreDrugTransactionDetail(transDetail);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return "redirect:/module/inventory/subStoreIndentDrugList.form";
	 }
	 //save here 

	 InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
		transaction.setStore(subStore);
		transaction.setDescription("RECEIPT "+DateUtils.getDDMMYYYY());
		transaction.setTypeTransaction(ActionValue.TRANSACTION[0]);
		transaction.setCreatedBy("System");
		transaction.setCreatedOn(new Date());
		transaction = inventoryService.saveStoreDrugTransaction(transaction);
		 
		 
		for(InventoryStoreDrugTransactionDetail refund : refundDrugList){
			
			Date date = new Date();
			Integer sumTotalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(subStore.getId(), refund.getDrug().getId(), refund.getFormulation().getId());
			InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
			transDetail.setTransaction(transaction);
			transDetail.setDrug(refund.getDrug());
			transDetail.setDateExpiry(refund.getDateExpiry());
			transDetail.setBatchNo(refund.getBatchNo());
			transDetail.setCompanyName(refund.getCompanyName());
			transDetail.setCreatedOn(date);
			transDetail.setDateManufacture(refund.getDateManufacture());
			transDetail.setFormulation(refund.getFormulation());
			transDetail.setUnitPrice(refund.getUnitPrice());
			transDetail.setVAT(refund.getVAT());
			transDetail.setParent(refund);
			transDetail.setReceiptDate(date);
			
			/* Money moneyUnitPrice = new Money(refund.getUnitPrice());
			 Money vATUnitPrice = new Money(refund.getVAT());
			 Money m = moneyUnitPrice.plus(vATUnitPrice);
			 Money totl = m.times(refund.getIssueQuantity());
			 transDetail.setTotalPrice(totl.getAmount());*/
			
			/* Money moneyUnitPrice = new Money(refund.getUnitPrice());
			 Money totl = moneyUnitPrice.times(refund.getIssueQuantity());
			
			totl = totl.plus(totl.times((double)refund.getVAT()/100));
			transDetail.setTotalPrice(totl.getAmount());*/
			BigDecimal moneyUnitPrice = refund.getUnitPrice().multiply(new BigDecimal(refund.getIssueQuantity()));
			moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(refund.getVAT().divide(new BigDecimal(100))));
			transDetail.setTotalPrice(moneyUnitPrice);
			
			transDetail.setQuantity(refund.getIssueQuantity());
			transDetail.setCurrentQuantity(refund.getIssueQuantity());
			transDetail.setIssueQuantity(0);
			transDetail.setOpeningBalance(sumTotalQuantity);
			transDetail.setClosingBalance(sumTotalQuantity + refund.getIssueQuantity());
			inventoryService.saveStoreDrugTransactionDetail(transDetail);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 //indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[2]);
	 indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[4]);
	 inventoryService.saveStoreDrugIndent(indent);
	 return "redirect:/module/inventory/subStoreIndentDrugList.form";
	 
 }
}
