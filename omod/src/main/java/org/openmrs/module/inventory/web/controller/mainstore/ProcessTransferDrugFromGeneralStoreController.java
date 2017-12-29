package org.openmrs.module.inventory.web.controller.mainstore;

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

@Controller("ProcessTransferDrugFromGeneralStoreController")
@RequestMapping("/module/inventory/mainStoreDrugProcessIndent.form")
public class ProcessTransferDrugFromGeneralStoreController {
	@RequestMapping(method = RequestMethod.GET)
	public String sendIndent( @RequestParam(value="indentId",required=false)  Integer id,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreDrugIndent indent = inventoryService.getStoreDrugIndentById(id);
		InventoryStore mainStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(indent != null && indent.getSubStoreStatus() == 2 && indent.getMainStoreStatus() == 1){
			List<InventoryStoreDrugIndentDetail> listDrugNeedProcess = inventoryService.listStoreDrugIndentDetail(id);
			Collection<Integer> formulationIds = new ArrayList<Integer>();
			Collection<Integer> drugIds = new ArrayList<Integer>();
			for(InventoryStoreDrugIndentDetail t : listDrugNeedProcess){
				//System.out.println("formulation: "+t.getFormulation().getId());
				//System.out.println("t.getDrug().getId(): "+t.getDrug().getId());
				formulationIds.add(t.getFormulation().getId());
				drugIds.add(t.getDrug().getId());
			}
			//System.out.println("main store: "+mainStore.getName());
			//System.out.println("main store id: "+mainStore.getId());
			//need change it in future
			List<InventoryStoreDrugTransactionDetail> transactionAvaiableOfMainStore = inventoryService.listStoreDrugAvaiable(mainStore.getId(), drugIds, formulationIds);
			//System.out.println("transactionAvaiableOfMainStore: "+transactionAvaiableOfMainStore);
			List<InventoryStoreDrugIndentDetail> listDrugTP = new ArrayList<InventoryStoreDrugIndentDetail>(); 
			for(InventoryStoreDrugIndentDetail t : listDrugNeedProcess){
				if(transactionAvaiableOfMainStore != null && transactionAvaiableOfMainStore.size() > 0){
					for(InventoryStoreDrugTransactionDetail trDetail : transactionAvaiableOfMainStore ){
						//if(t.getDrug().getId() == trDetail.getDrug().getId() && t.getFormulation().getId() == trDetail.getFormulation().getId()){
						//ghanshyam 28/06/2012 tag RC_REF_COMPARISON
							if(t.getDrug().getId().equals(trDetail.getDrug().getId()) && t.getFormulation().getId().equals(trDetail.getFormulation().getId())){
							//System.out.println("quantity check hre: "+trDetail.getCurrentQuantity());
							t.setMainStoreTransfer(trDetail.getCurrentQuantity());
						}
						
					}
				}else{
					//System.out.println("sao lai vao day nhi");
					t.setMainStoreTransfer(0);
				}
				listDrugTP.add(t);
			}
			//System.out.println("listDrugTP: "+listDrugTP);
			//need change in future this
			//System.out.println("truong hop bo het: "+inventoryService.listStoreDrugAvaiable(mainStore.getId(), null, null));
			model.addAttribute("listDrugNeedProcess", listDrugTP);
			model.addAttribute("indent", indent);
			
			return "/module/inventory/mainstore/mainStoreDrugProcessIndent";
		}
		
		return "redirect:/module/inventory/transferDrugFromGeneralStore.form";
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit( HttpServletRequest request, Model model) {
	 List<String> errors = new ArrayList<String>();
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
 	 Integer indentId = NumberUtils.toInt(request.getParameter("indentId"));
	 InventoryStoreDrugIndent indent =inventoryService.getStoreDrugIndentById(indentId);
	 List<InventoryStoreDrugIndentDetail> listIndentDetail = inventoryService.listStoreDrugIndentDetail(indentId);
	 if("1".equals(request.getParameter("refuse"))){
		if(indent != null){
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[1]);
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[5]);
			inventoryService.saveStoreDrugIndent(indent);
			
			for(InventoryStoreDrugIndentDetail t : listIndentDetail){
				InventoryStoreDrug storeDrug = inventoryService.getStoreDrug(indent.getStore().getId(), t.getDrug().getId(),t.getFormulation().getId());
				if(storeDrug != null ){
					storeDrug.setStatusIndent(0);
					inventoryService.saveStoreDrug(storeDrug);
				}
				
			}
		}
		return "redirect:/module/inventory/transferDrugFromGeneralStore.form";
	 }
	 //validate here 
	 
	 Collection<Integer> formulationIds = new ArrayList<Integer>();
		Collection<Integer> drugIds = new ArrayList<Integer>();
		for(InventoryStoreDrugIndentDetail t : listIndentDetail){
			formulationIds.add(t.getFormulation().getId());
			drugIds.add(t.getDrug().getId());
		}
		InventoryStore mainStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreDrugTransactionDetail> transactionAvaiableOfMainStore = inventoryService.listStoreDrugAvaiable(mainStore.getId(), drugIds, formulationIds);
	
	 
	 boolean passTransfer = true;
	 List<Integer> quantityTransfers = new ArrayList<Integer>(); 
	 //get available quantity mainstore have on hand
	 List<InventoryStoreDrugIndentDetail> listDrugTP = new ArrayList<InventoryStoreDrugIndentDetail>(); 
	for(InventoryStoreDrugIndentDetail t : listIndentDetail){
		int temp = NumberUtils.toInt(request.getParameter(t.getId()+""));
		//get to return view value
		quantityTransfers.add(temp);
		if(transactionAvaiableOfMainStore != null && transactionAvaiableOfMainStore.size() > 0){
		for(InventoryStoreDrugTransactionDetail trDetail : transactionAvaiableOfMainStore ){
			if(t.getDrug().getId() == trDetail.getDrug().getId() && t.getFormulation().getId() == trDetail.getFormulation().getId()){
				t.setMainStoreTransfer(trDetail.getCurrentQuantity());
				if(temp > trDetail.getCurrentQuantity() || temp < 0 ){
					errors.add("inventory.indent.error.quantity");
				}
			}
			
			
		}
		}else{
			errors.add("inventory.indent.error.quantity");
		}
		 if(temp > 0){
			 passTransfer = false;
		 }
		listDrugTP.add(t);
	}
	 if(passTransfer){
		 errors.add("inventory.indent.error.transfer");
	 }
	 if(errors != null && errors.size() > 0){
			model.addAttribute("listDrugNeedProcess", listDrugTP);
			model.addAttribute("indent", indent);
			model.addAttribute("errors", errors);
			model.addAttribute("quantityTransfers", quantityTransfers);
		 return "/module/inventory/mainstore/mainStoreDrugProcessIndent";
	 }
	 
	 //create transaction
	 InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
	 transaction.setDescription("TRANSFER SYSTEM AUTO "+DateUtils.getDDMMYYYY());
	 transaction.setStore(mainStore);
	 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
	 transaction.setCreatedOn(new Date());
	 
	// transaction.setCreatedBy("System");
	// Sagar Bele - 07-08-2012 Bug #326 [INVENTORY] Transaction Stored with different user name
	 transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
	 transaction = inventoryService.saveStoreDrugTransaction(transaction);
	 
	 
	 
	 for(InventoryStoreDrugIndentDetail t : listIndentDetail){
		 int temp = NumberUtils.toInt(request.getParameter(t.getId()+""), 0);
		 //System.out.println("temp : "+temp);
		 t.setMainStoreTransfer(temp);
		 if(temp > 0){
			//sum currentQuantity of drugId, formulationId of store
			 Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(mainStore.getId(),t.getDrug().getId(), t.getFormulation().getId());
			 //list all transaction detail with condition dateExpiry > now() , drugId = ? , formulationId = ? of mainstore
			 List<InventoryStoreDrugTransactionDetail> listTransactionAvailableMS = inventoryService.listStoreDrugTransactionDetail(mainStore.getId(), t.getDrug().getId(), t.getFormulation().getId(), true);
			 InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
			 transDetail.setTransaction(transaction);
			 InventoryStoreDrug storeDrug = inventoryService.getStoreDrug(mainStore.getId(), t.getDrug().getId(), t.getFormulation().getId());
			 for(InventoryStoreDrugTransactionDetail trDetail : listTransactionAvailableMS ){
				 Integer x = trDetail.getCurrentQuantity() - temp;
					if( x >= 0){
						Date date = new Date();
						//update current quantity of mainstore in transactionDetail
						trDetail.setCurrentQuantity(x);
						inventoryService.saveStoreDrugTransactionDetail(trDetail);
						
						transDetail.setDrug(trDetail.getDrug());
						transDetail.setDateExpiry(trDetail.getDateExpiry());
						transDetail.setBatchNo(trDetail.getBatchNo());
						transDetail.setCompanyName(trDetail.getCompanyName());
						transDetail.setCreatedOn(date);
						transDetail.setDateManufacture(trDetail.getDateManufacture());
						transDetail.setFormulation(trDetail.getFormulation());
						transDetail.setMrpPrice(trDetail.getMrpPrice());
						transDetail.setCostToPatient(trDetail.getCostToPatient());
						transDetail.setVAT(trDetail.getVAT());
						transDetail.setParent(trDetail);
						transDetail.setReceiptDate(date);
						//------------
						 //Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						// Money vATUnitPrice = new Money(trDetail.getVAT());
						// Money m = moneyUnitPrice.plus(vATUnitPrice);
						// Money totl = m.times(temp);
						 //--------
						/* Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money totl = moneyUnitPrice.times(temp);
						
						totl = totl.plus(totl.times((double)trDetail.getVAT()/100));
						transDetail.setTotalPrice(totl.getAmount());
						transDetail.setTotalPrice(totl.getAmount());
						
						*/
						BigDecimal moneyUnitPrice = trDetail.getMrpPrice().multiply(new BigDecimal(temp));
					//	moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(trDetail.getVAT().divide(new BigDecimal(100))));
						transDetail.setTotalPrice(moneyUnitPrice);
						
						 transDetail.setQuantity(0);
						transDetail.setCurrentQuantity(0);
						transDetail.setIssueQuantity(temp);
						transDetail.setOpeningBalance(totalQuantity);
						transDetail.setClosingBalance(totalQuantity - temp);
						inventoryService.saveStoreDrugTransactionDetail(transDetail);
						
						//save last to StoreDrug
						storeDrug.setOpeningBalance(totalQuantity);
						storeDrug.setClosingBalance(totalQuantity - temp);
						storeDrug.setIssueQuantity(storeDrug.getIssueQuantity() + temp);
						storeDrug.setCurrentQuantity(totalQuantity - temp);
						inventoryService.saveStoreDrug(storeDrug);
						
						//create transactionDetail for transfer
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}else{
						//truong hop mot transactionDetail be hon cai can transfer.
						Date date = new Date();
						transDetail.setIssueQuantity(trDetail.getCurrentQuantity());
						trDetail.setCurrentQuantity(0);
						trDetail.setQuantity(0);
						inventoryService.saveStoreDrugTransactionDetail(trDetail);
						
/*						 Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money vATUnitPrice = new Money(trDetail.getVAT());
						 Money m = moneyUnitPrice.plus(vATUnitPrice);
						 Money totl = m.times(transDetail.getIssueQuantity());
						 transDetail.setTotalPrice(totl.getAmount());*/
						 /*Money moneyUnitPrice = new Money(trDetail.getUnitPrice());
						 Money totl = moneyUnitPrice.times(temp);
						
						totl = totl.plus(totl.times((double)trDetail.getVAT()/100));
						transDetail.setTotalPrice(totl.getAmount());
						 */
						BigDecimal moneyUnitPrice = trDetail.getMrpPrice().multiply(new BigDecimal(transDetail.getIssueQuantity()));
						moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(trDetail.getVAT().divide(new BigDecimal(100))));
						transDetail.setTotalPrice(moneyUnitPrice);
						
						transDetail.setCurrentQuantity(0);
						transDetail.setOpeningBalance(totalQuantity);
						
						transDetail.setDrug(trDetail.getDrug());
						transDetail.setDateExpiry(trDetail.getDateExpiry());
						transDetail.setBatchNo(trDetail.getBatchNo());
						transDetail.setCompanyName(trDetail.getCompanyName());
						transDetail.setCreatedOn(date);
						transDetail.setDateManufacture(trDetail.getDateManufacture());
						transDetail.setFormulation(trDetail.getFormulation());
						transDetail.setMrpPrice(trDetail.getMrpPrice());
						transDetail.setCostToPatient(trDetail.getCostToPatient());
						transDetail.setVAT(trDetail.getVAT());
						transDetail.setParent(trDetail);
						transDetail.setReceiptDate(date);
						
						transDetail.setClosingBalance(totalQuantity - transDetail.getIssueQuantity());
						inventoryService.saveStoreDrugTransactionDetail(transDetail);
						totalQuantity -=  transDetail.getIssueQuantity();
						temp -=  transDetail.getIssueQuantity();
						//create transactionDetail for transfer
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				 
				}
			 
		 }
		 inventoryService.saveStoreDrugIndentDetail(t);
	 }
	//System.out.println("den day roi");
	 //add issue transaction from general store 
	 indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[2]);
	 indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[2]);
	 indent.setTransaction(transaction);
	 inventoryService.saveStoreDrugIndent(indent);
	 return "redirect:/module/inventory/transferDrugFromGeneralStore.form?viewIndent="+indentId;
	 
 }
}
