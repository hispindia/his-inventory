package org.openmrs.module.inventory.web.controller.global;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.model.InventoryStoreItemAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.util.DateUtils;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("AjaxGlobalController")
public class AjaxController {
	
	@RequestMapping("/module/inventory/drugByCategory.form")
	public String drugByCategory(@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrug> drugs = inventoryService.listDrug(categoryId, null, 0, 0);
		model.addAttribute("drugs", drugs);
		return "/module/inventory/autocomplete/drugByCategory";
	}
	@RequestMapping("/module/inventory/drugByCategoryForIssue.form")
	public String drugByCategoryForIssue(@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrug> drugs = inventoryService.listDrug(categoryId, null, 0, 0);
		model.addAttribute("drugs", drugs);
		return "/module/inventory/autocomplete/drugByCategoryForIssue";
	}
	@RequestMapping("/module/inventory/itemBySubCategory.form")
	public String itemByCategory(@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItem> items = inventoryService.listItem(categoryId, null, 0, 0);
		model.addAttribute("items", items);
		return "/module/inventory/autocomplete/itemBySubCategory";
	}
	@RequestMapping("/module/inventory/itemBySubCategoryForIssue.form")
	public String itemBySubCategoryForIssue(@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItem> items = inventoryService.listItem(categoryId, null, 0, 0);
		model.addAttribute("items", items);
		return "/module/inventory/autocomplete/itemBySubCategoryForIssue";
	}
	@RequestMapping("/module/inventory/formulationByDrug.form")
	public String formulationByDrug(@RequestParam(value="drugId",required=false) Integer drugId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugById(drugId);
		if(drug != null){
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(drug.getFormulations());
			model.addAttribute("formulations", formulations);
		}
		return "/module/inventory/autocomplete/formulationByDrug";
	}
	@RequestMapping("/module/inventory/formulationByDrugName.form")
	public String formulationByDrugName(
			@RequestParam(value = "drugName", required = false) String drugName,
			Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugByName(drugName);
		if (drug != null) {
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(
					drug.getFormulations());
			model.addAttribute("formulations", formulations);
		}
		return "/module/inventory/autocomplete/formulationByDrug";
	}

	@RequestMapping("/module/inventory/specificationByItem.form")
	public String specificationByItem(@RequestParam(value="itemId",required=false) Integer itemId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryItem item = inventoryService.getItemById(itemId);
		if(item != null){
			List<InventoryItemSpecification> specifications = new ArrayList<InventoryItemSpecification>(item.getSpecifications());
			model.addAttribute("specifications", specifications);
		}
		return "/module/inventory/autocomplete/specificationByItem";
	}
	
	@RequestMapping("/module/inventory/clearSlip.form")
	public String clearSlip(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "reipt_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/receiptsToGeneralStore.form";
		}
		return "/module/inventory/mainstore/addDescriptionReceiptSlip";
	}
	
	@RequestMapping("/module/inventory/itemClearSlip.form")
	public String clearSlipItem(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "itemReceipt_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/itemReceiptsToGeneralStore.form";
		}
		return "/module/inventory/mainstoreItem/itemAddDescriptionReceiptSlip";
	}
	
	@RequestMapping("/module/inventory/clearPurchaseOrder.form")
	public String clearPurchase(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "purchase_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/purchaseOrderForGeneralStore.form";
		}
		return "/module/inventory/mainstore/purchaseOrderForGeneralStore";
	}
	
	@RequestMapping("/module/inventory/itemClearPurchaseOrder.form")
	public String itemClearPurchase(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "itemPurchase_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/itemPurchaseOrderForGeneralStore.form";
		}
		return "/module/inventory/mainstoreItem/itemPurchaseOrderForGeneralStore";
	}
	
	@RequestMapping("/module/inventory/clearSubStoreIndent.form")
	public String clearIndent(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "subStoreIndentDrug_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/subStoreIndentDrug.form";
		}
		return "/module/inventory/substore/subStoreIndentDrug";
	}
	
	@RequestMapping("/module/inventory/itemClearSubStoreIndent.form")
	public String itemClearIndent(@RequestParam(value="action",required=false) String name, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "subStoreIndentItem_"+userId;
		if("1".equals(name)){
			//Clear slip 
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			return "redirect:/module/inventory/subStoreIndentItem.form";
		}
		return "/module/inventory/substoreItem/subStoreIndentItem";
	}
	
	@RequestMapping("/module/inventory/indentDrugDetail.form")
	public String detailSubStoreDrugIndent(@RequestParam(value="indentId",required=false) Integer indentId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreDrugIndent indent = inventoryService.getStoreDrugIndentById(indentId);
		List<InventoryStoreDrugIndentDetail> listIndentDetail = inventoryService.listStoreDrugIndentDetail(indentId);
		model.addAttribute("listIndentDetail", listIndentDetail);
		if(indent != null && indent.getTransaction() != null){
			List<InventoryStoreDrugTransactionDetail> listTransactionDetail = inventoryService.listTransactionDetail(indent.getTransaction().getId());
			model.addAttribute("listTransactionDetail", listTransactionDetail);
		}
		model.addAttribute("store", !CollectionUtils.isEmpty(listIndentDetail) ?listIndentDetail.get(0).getIndent().getStore() : null );
		model.addAttribute("date", !CollectionUtils.isEmpty(listIndentDetail) ?listIndentDetail.get(0).getIndent().getCreatedOn() : null );
		return "/module/inventory/autocomplete/indentDrugDetail";
	}
	
	@RequestMapping("/module/inventory/indentItemDetail.form")
	public String detailSubStoreItemIndent(@RequestParam(value="indentId",required=false) Integer indentId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreItemIndentDetail> listIndentDetail = inventoryService.listStoreItemIndentDetail(indentId);
		//InventoryStoreItemIndent indent = inventoryService.getStoreItemIndentById(indentId);
		model.addAttribute("listIndentDetail", listIndentDetail);
		/*if(indent != null && indent.getTransaction() != null){
			List<InventoryStoreItemTransactionDetail> listTransactionDetail = inventoryService.listStoreItemTransactionDetail(indent.getTransaction().getId());
			model.addAttribute("listTransactionDetail", listTransactionDetail);
		}*/
		model.addAttribute("store", !CollectionUtils.isEmpty(listIndentDetail) ?listIndentDetail.get(0).getIndent().getStore() : null );
		model.addAttribute("date", !CollectionUtils.isEmpty(listIndentDetail) ?listIndentDetail.get(0).getIndent().getCreatedOn() : null );
		return "/module/inventory/autocomplete/indentItemDetail";
	}
	
	@RequestMapping("/module/inventory/sentDrugIndentToMainStore.form")
	public String sendDrugIndentToMainStore(@RequestParam(value="indentId",required=false) Integer indentId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreDrugIndent indent = inventoryService.getStoreDrugIndentById(indentId);
		if(indent != null){
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[1]);
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[0]);
			inventoryService.saveStoreDrugIndent(indent);
		}
		return "redirect:/module/inventory/subStoreIndentDrugList.form";
	}
	
	@RequestMapping("/module/inventory/sentItemIndentToMainStore.form")
	public String sendItemIndentToMainStore(@RequestParam(value="indentId",required=false) Integer indentId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreItemIndent indent = inventoryService.getStoreItemIndentById(indentId);
		if(indent != null){
			indent.setSubStoreStatus(ActionValue.INDENT_SUBSTORE[1]);
			indent.setMainStoreStatus(ActionValue.INDENT_MAINSTORE[0]);
			inventoryService.saveStoreItemIndent(indent);
		}
		return "redirect:/module/inventory/subStoreIndentItemList.form";
	}
	
	@RequestMapping("/module/inventory/listReceiptDrug.form")
	public String listReceiptDrugAvailable(@RequestParam(value="drugId",required=false) Integer drugId,@RequestParam(value="formulationId",required=false) Integer formulationId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugById(drugId);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(store != null && drug != null && formulationId != null){
			List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService.listStoreDrugTransactionDetail(store.getId(), drug.getId(), formulationId, true);
			//check that drug is issued before
			int userId = Context.getAuthenticatedUser().getId();
			
			String fowardParam = "issueDrugAccountDetail_"+userId;
			String fowardParamDrug = "issueDrugDetail_"+userId;
			 List<InventoryStoreDrugPatientDetail> listDrug = (List<InventoryStoreDrugPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParamDrug);
			 List<InventoryStoreDrugAccountDetail> listDrugAccount = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			 List<InventoryStoreDrugTransactionDetail> listReceiptDrugReturn = new ArrayList<InventoryStoreDrugTransactionDetail>();
			 boolean check = false;
			if(CollectionUtils.isNotEmpty(listDrug)){ 
				if(CollectionUtils.isNotEmpty(listReceiptDrug)){
					for(InventoryStoreDrugTransactionDetail drugDetail : listReceiptDrug){
						for(InventoryStoreDrugPatientDetail drugPatient : listDrug)
						{
							if(drugDetail.getId().equals(drugPatient.getTransactionDetail().getId())){
								drugDetail.setCurrentQuantity(drugDetail.getCurrentQuantity() - drugPatient.getQuantity());
							}
							
						}
						if(drugDetail.getCurrentQuantity() > 0){
							listReceiptDrugReturn.add(drugDetail);
							check = true;
						}
					}
				}
			}
			
			if(CollectionUtils.isNotEmpty(listDrugAccount)){ 
				if(CollectionUtils.isNotEmpty(listReceiptDrug)){
					for(InventoryStoreDrugTransactionDetail drugDetail : listReceiptDrug){
						for(InventoryStoreDrugAccountDetail drugAccount : listDrugAccount)
						{
							if(drugDetail.getId().equals(drugAccount.getTransactionDetail().getId())){
								drugDetail.setCurrentQuantity(drugDetail.getCurrentQuantity() - drugAccount.getQuantity());
							}
						}
						if(drugDetail.getCurrentQuantity() > 0 && !check){
							listReceiptDrugReturn.add(drugDetail);
						}
					}
				}
			}
			if(CollectionUtils.isEmpty(listReceiptDrugReturn) && CollectionUtils.isNotEmpty(listReceiptDrug))
			{
				listReceiptDrugReturn.addAll(listReceiptDrug);
			}
			
			model.addAttribute("listReceiptDrug", listReceiptDrugReturn);
		}
		
		return "/module/inventory/autocomplete/listReceiptDrug";
	}
	
	@RequestMapping("/module/inventory/listReceiptItem.form")
	public String listReceiptItemAvailable(@RequestParam(value="itemId",required=false) Integer itemId,@RequestParam(value="specificationId",required=false) Integer specificationId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryItem item = inventoryService.getItemById(itemId);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(store != null && item != null ){
			Integer sumReceiptItem = inventoryService.sumStoreItemCurrentQuantity(store.getId(), item.getId(), specificationId);
			
			 int userId = Context.getAuthenticatedUser().getId();
			 String fowardParam = "issueItemDetail_"+userId;
			 List<InventoryStoreItemAccountDetail> list = (List<InventoryStoreItemAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			 if(CollectionUtils.isNotEmpty(list)){
				 for(InventoryStoreItemAccountDetail itemAccount : list)
				 {
					 if(itemAccount.getTransactionDetail().getItem().getId().equals(itemId)){
						 if(specificationId != null)
						 {
							 if(itemAccount.getTransactionDetail().getSpecification() != null && itemAccount.getTransactionDetail().getSpecification().getId().equals(specificationId) )
							 {
								 sumReceiptItem -= itemAccount.getQuantity();
							 }
						 }else{
							 if(itemAccount.getTransactionDetail().getSpecification() == null )
							 {
								 sumReceiptItem -= itemAccount.getQuantity();
							 }
						 }
					 }
				 }
			 }
			
			model.addAttribute("sumReceiptItem", sumReceiptItem);
		}
		
		return "/module/inventory/autocomplete/listReceiptItem";
	}
	
	@RequestMapping("/module/inventory/formulationByDrugForIssue.form")
	public String formulationByDrugForIssueDrug(@RequestParam(value="drugId",required=false) Integer drugId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugById(drugId);
		if(drug != null){
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(drug.getFormulations());
			model.addAttribute("formulations", formulations);
			
		}
		return "/module/inventory/autocomplete/formulationByDrugForIssue";
	}
	@RequestMapping("/module/inventory/formulationByDrugNameForIssue.form")
	public String formulationByDrugNameForIssueDrug(@RequestParam(value="drugName",required=false)String drugName, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugByName(drugName);
		if(drug != null){
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(drug.getFormulations());
			model.addAttribute("formulations", formulations);
			model.addAttribute("drugId", drug.getId());
		}
		return "/module/inventory/autocomplete/formulationByDrugForIssue";
	}
	@RequestMapping("/module/inventory/specificationByItemForIssue.form")
	public String specificationByItemForIssueItem(@RequestParam(value="itemId",required=false) Integer itemId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryItem item= inventoryService.getItemById(itemId);
		if(item != null ){
			if(item.getSpecifications() != null && item.getSpecifications().size() > 0){
				List<InventoryItemSpecification> specifications = new ArrayList<InventoryItemSpecification>(item.getSpecifications());
				model.addAttribute("specifications", specifications);
				return "/module/inventory/autocomplete/specificationByItemForIssue";
			}else{
				InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
				if(store != null){
					Integer sumReceiptItem = inventoryService.sumStoreItemCurrentQuantity(store.getId(), item.getId(), null);
					
					 int userId = Context.getAuthenticatedUser().getId();
					 String fowardParam = "issueItemDetail_"+userId;
					 List<InventoryStoreItemAccountDetail> list = (List<InventoryStoreItemAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
					 if(CollectionUtils.isNotEmpty(list)){
						 for(InventoryStoreItemAccountDetail itemAccount : list)
						 {
							 if(itemAccount.getTransactionDetail().getItem().getId().equals(itemId)){
								 if(itemAccount.getTransactionDetail().getSpecification() == null )
								 {
									 sumReceiptItem -= itemAccount.getQuantity();
								 }
								 
							 }
						 }
					 }
					
					model.addAttribute("sumReceiptItem", sumReceiptItem);
				}
				return "/module/inventory/autocomplete/listReceiptItem";
			}
		}
		
		
		return "/module/inventory/autocomplete/specificationByItemForIssue";
	}
	
	@RequestMapping("/module/inventory/autoCompletePatientList.form")
	public String showPatientList( @RequestParam(value="searchPatient",required=false)  String identifier,Model model) {
		
		
		if( StringUtils.isNotBlank(identifier) )
		{
			String prefix = Context.getAdministrationService().getGlobalProperty("registration.identifier_prefix");
			if( identifier.contains("-") && !identifier.contains(prefix)){
				identifier = prefix+identifier;
	    	}
			List<Patient> patientsList = null;
			try {
				patientsList = Context.getPatientService().getPatients( identifier.trim() );
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("patients", patientsList);
		}
		
		return "/module/inventory/autocomplete/autoCompletePatientList";
	}
	
	@RequestMapping("/module/inventory/processIssueDrug.form")
	public String processIssueDrug( @RequestParam(value="action",required=false)  Integer action,Model model,HttpServletRequest request,
			@RequestParam(value = "totalValue", required = false) Float totalValue,
			@RequestParam(value = "waiverPercentage", required = false) Float waiverPercentage,
            @RequestParam(value= "waiverComment", required = false) String waiverComment,
			@RequestParam(value = "totalAmountPayable", required = false) BigDecimal totalAmountPayable,
			@RequestParam(value = "amountGiven", required = false) Integer amountGiven,
			@RequestParam(value = "amountReturned", required = false) Integer amountReturned) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "issueDrugDetail_"+userId;
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(action == 1){
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueDrug_"+userId);
			return "redirect:/module/inventory/subStoreIssueDrugForm.form";
		}
		List<InventoryStoreDrugPatientDetail> list = (List<InventoryStoreDrugPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		InventoryStoreDrugPatient issueDrugPatient = (InventoryStoreDrugPatient )StoreSingleton.getInstance().getHash().get("issueDrug_"+userId);
		if(issueDrugPatient != null && list != null && list.size() > 0){
			
			Date date = new Date();
			//create transaction issue from substore
			 InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
			 transaction.setDescription("ISSUE DRUG TO PATIENT "+DateUtils.getDDMMYYYY());
			 transaction.setStore(store);
			 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
			 transaction.setCreatedOn(date);
			 transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			 transaction = inventoryService.saveStoreDrugTransaction(transaction);
			
			issueDrugPatient = inventoryService.saveStoreDrugPatient(issueDrugPatient);
			for(InventoryStoreDrugPatientDetail pDetail : list){
				Date date1 = new Date();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(store.getId(),pDetail.getTransactionDetail().getDrug().getId(), pDetail.getTransactionDetail().getFormulation().getId());
				int t = totalQuantity - pDetail.getQuantity();
				InventoryStoreDrugTransactionDetail drugTransactionDetail = inventoryService.getStoreDrugTransactionDetailById(pDetail.getTransactionDetail().getId());
				pDetail.getTransactionDetail().setCurrentQuantity(drugTransactionDetail.getCurrentQuantity() - pDetail.getQuantity());
				inventoryService.saveStoreDrugTransactionDetail(pDetail.getTransactionDetail());
				
				//save transactiondetail first
				InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
				transDetail.setTransaction(transaction);
				transDetail.setCurrentQuantity(0);
				transDetail.setIssueQuantity(pDetail.getQuantity());
				transDetail.setOpeningBalance(totalQuantity);
				transDetail.setClosingBalance(t);
				transDetail.setQuantity(0);
				transDetail.setVAT(pDetail.getTransactionDetail().getVAT());
				transDetail.setCostToPatient(drugTransactionDetail.getUnitPrice());
				transDetail.setUnitPrice(pDetail.getTransactionDetail().getUnitPrice());
				transDetail.setDrug(pDetail.getTransactionDetail().getDrug());
				transDetail.setFormulation(pDetail.getTransactionDetail().getFormulation());
				transDetail.setBatchNo(pDetail.getTransactionDetail().getBatchNo());
				transDetail.setCompanyName(pDetail.getTransactionDetail().getCompanyName());
				transDetail.setDateManufacture(pDetail.getTransactionDetail().getDateManufacture());
				transDetail.setDateExpiry(pDetail.getTransactionDetail().getDateExpiry());
				transDetail.setReceiptDate(pDetail.getTransactionDetail().getReceiptDate());
				transDetail.setCreatedOn(date1);
				/*Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				Money vATUnitPrice = new Money(pDetail.getTransactionDetail().getVAT());
				Money m = moneyUnitPrice.plus(vATUnitPrice);
				Money totl = m.times( pDetail.getQuantity());
				transDetail.setTotalPrice(totl.getAmount());*/
				
				/* Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				 Money totl = moneyUnitPrice.times(pDetail.getQuantity());
				
				totl = totl.plus(totl.times((double)pDetail.getTransactionDetail().getVAT()/100));
				transDetail.setTotalPrice(totl.getAmount());*/
				
				BigDecimal moneyUnitPrice = pDetail.getTransactionDetail().getUnitPrice().multiply(new BigDecimal(pDetail.getQuantity()));
				//moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(pDetail.getTransactionDetail().getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setTotalAmount(totalValue);
				transDetail.setWaiverPercentage(waiverPercentage);
				Float waiverAmount=totalValue*waiverPercentage/100;
				transDetail.setWaiverAmount(waiverAmount);
				transDetail.setAmountPayable(totalAmountPayable);
				transDetail.setAmountGiven(amountGiven);
				transDetail.setAmountReturned(amountReturned);
				
				transDetail.setParent(pDetail.getTransactionDetail());
				transDetail = inventoryService.saveStoreDrugTransactionDetail(transDetail);
				
				pDetail.setStoreDrugPatient(issueDrugPatient);
				pDetail.setTransactionDetail(transDetail);
				//save issue to patient detail
				inventoryService.saveStoreDrugPatientDetail(pDetail);
				//save issues transaction detail
				
			}
			
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueDrug_"+userId);
		}
		
		return "redirect:/module/inventory/subStoreIssueDrugList.form";
	}
	
	@RequestMapping("/module/inventory/processIssueDrugAccount.form")
	public String processIssueDrugAccount( @RequestParam(value="action",required=false)  Integer action,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "issueDrugAccountDetail_"+userId;
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(action == 1){
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueDrugAccount_"+userId);
			return "redirect:/module/inventory/subStoreIssueDrugAccountForm.form";
		}
		List<InventoryStoreDrugAccountDetail> list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		InventoryStoreDrugAccount issueDrugAccount = (InventoryStoreDrugAccount )StoreSingleton.getInstance().getHash().get("issueDrugAccount_"+userId);
		if(issueDrugAccount != null && list != null && list.size() > 0){
			
			Date date = new Date();
			//create transaction issue from substore
			 InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
			 transaction.setDescription("ISSUE DRUG TO ACCOUNT "+DateUtils.getDDMMYYYY());
			 transaction.setStore(store);
			 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
			 transaction.setCreatedOn(date);
			 transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			 transaction = inventoryService.saveStoreDrugTransaction(transaction);
			 
			 
			
			issueDrugAccount = inventoryService.saveStoreDrugAccount(issueDrugAccount);
			for(InventoryStoreDrugAccountDetail pDetail : list){
				Date date1 = new Date();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(store.getId(),pDetail.getTransactionDetail().getDrug().getId(), pDetail.getTransactionDetail().getFormulation().getId());
				int t = totalQuantity - pDetail.getQuantity();
				
				InventoryStoreDrugTransactionDetail drugTransactionDetail = inventoryService.getStoreDrugTransactionDetailById(pDetail.getTransactionDetail().getId());
				pDetail.getTransactionDetail().setCurrentQuantity(drugTransactionDetail.getCurrentQuantity() - pDetail.getQuantity());
				inventoryService.saveStoreDrugTransactionDetail(pDetail.getTransactionDetail());
					
				
				//save transactiondetail first
				InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
				transDetail.setTransaction(transaction);
				transDetail.setCurrentQuantity(0);
				transDetail.setIssueQuantity(pDetail.getQuantity());
				transDetail.setOpeningBalance(totalQuantity);
				transDetail.setClosingBalance(t);
				transDetail.setQuantity(0);
				transDetail.setVAT(pDetail.getTransactionDetail().getVAT());
				transDetail.setUnitPrice(pDetail.getTransactionDetail().getUnitPrice());
				transDetail.setDrug(pDetail.getTransactionDetail().getDrug());
				transDetail.setFormulation(pDetail.getTransactionDetail().getFormulation());
				transDetail.setBatchNo(pDetail.getTransactionDetail().getBatchNo());
				transDetail.setCompanyName(pDetail.getTransactionDetail().getCompanyName());
				transDetail.setDateManufacture(pDetail.getTransactionDetail().getDateManufacture());
				transDetail.setDateExpiry(pDetail.getTransactionDetail().getDateExpiry());
				transDetail.setReceiptDate(pDetail.getTransactionDetail().getReceiptDate());
				transDetail.setCreatedOn(date1);
				/*Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				Money vATUnitPrice = new Money(pDetail.getTransactionDetail().getVAT());
				Money m = moneyUnitPrice.plus(vATUnitPrice);
				Money totl = m.times( pDetail.getQuantity());
				transDetail.setTotalPrice(totl.getAmount());*/
				
				/* Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				 Money totl = moneyUnitPrice.times(pDetail.getQuantity());
				
				totl = totl.plus(totl.times((double)pDetail.getTransactionDetail().getVAT()/100));
				transDetail.setTotalPrice(totl.getAmount());*/
				BigDecimal moneyUnitPrice = pDetail.getTransactionDetail().getUnitPrice().multiply(new BigDecimal(pDetail.getQuantity()));
				moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(pDetail.getTransactionDetail().getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setParent(pDetail.getTransactionDetail());
				transDetail = inventoryService.saveStoreDrugTransactionDetail(transDetail);
				
				pDetail.setDrugAccount(issueDrugAccount);
				pDetail.setTransactionDetail(transDetail);
				//save issue to patient detail
				inventoryService.saveStoreDrugAccountDetail(pDetail);
				//save issues transaction detail
				
			}
			
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueDrugAccount_"+userId);
		}
		
		return "redirect:/module/inventory/subStoreIssueDrugAccountList.form";
	}
	
	
	@RequestMapping("/module/inventory/processIssueItem.form")
	public String processIssueItem( @RequestParam(value="action",required=false)  Integer action,Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "issueItemDetail_"+userId;
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		if(action == 1){
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueItem_"+userId);
			return "redirect:/module/inventory/subStoreIssueItemForm.form";
		}
		List<InventoryStoreItemAccountDetail> list = (List<InventoryStoreItemAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		InventoryStoreItemAccount issueItemAccount = (InventoryStoreItemAccount )StoreSingleton.getInstance().getHash().get("issueItem_"+userId);
		if(issueItemAccount != null && list != null && list.size() > 0){
			
			Date date = new Date();
			//create transaction issue from substore
			 InventoryStoreItemTransaction transaction = new InventoryStoreItemTransaction();
			 transaction.setDescription("ISSUE ITEM "+DateUtils.getDDMMYYYY());
			 transaction.setStore(store);
			 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
			 transaction.setCreatedOn(date);
			 transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			 transaction = inventoryService.saveStoreItemTransaction(transaction);
			 
			 
			
			issueItemAccount = inventoryService.saveStoreItemAccount(issueItemAccount);
			for(InventoryStoreItemAccountDetail pDetail : list){
				Date date1 = new Date();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer specificationId =  pDetail.getTransactionDetail().getSpecification() != null? pDetail.getTransactionDetail().getSpecification().getId() : null;
				Integer totalQuantity = inventoryService.sumStoreItemCurrentQuantity(store.getId(),pDetail.getTransactionDetail().getItem().getId(), specificationId);
				int t = totalQuantity - pDetail.getQuantity();
				InventoryStoreItemTransactionDetail itemTransactionDetail = inventoryService.getStoreItemTransactionDetailById(pDetail.getTransactionDetail().getId());
				pDetail.getTransactionDetail().setCurrentQuantity(itemTransactionDetail.getCurrentQuantity() - pDetail.getQuantity());
				//System.out.println("get current quantity: "+pDetail.getTransactionDetail().getCurrentQuantity());
				//System.out.println("total quantity: "+totalQuantity);
				inventoryService.saveStoreItemTransactionDetail(pDetail.getTransactionDetail());
				
				//save transactiondetail first
				InventoryStoreItemTransactionDetail transDetail = new InventoryStoreItemTransactionDetail();
				transDetail.setTransaction(transaction);
				transDetail.setCurrentQuantity(0);
				transDetail.setIssueQuantity(pDetail.getQuantity());
				transDetail.setOpeningBalance(totalQuantity);
				transDetail.setClosingBalance(t);
				transDetail.setQuantity(0);
				transDetail.setVAT(pDetail.getTransactionDetail().getVAT());
				transDetail.setUnitPrice(pDetail.getTransactionDetail().getUnitPrice());
				transDetail.setItem(pDetail.getTransactionDetail().getItem());
				transDetail.setSpecification(pDetail.getTransactionDetail().getSpecification());
				transDetail.setCompanyName(pDetail.getTransactionDetail().getCompanyName());
				transDetail.setDateManufacture(pDetail.getTransactionDetail().getDateManufacture());
				transDetail.setReceiptDate(pDetail.getTransactionDetail().getReceiptDate());
				transDetail.setCreatedOn(date1);
				
				//-------------
				//Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				//Money vATUnitPrice = new Money(pDetail.getTransactionDetail().getVAT());
				//Money m = moneyUnitPrice.plus(vATUnitPrice);
				//Money totl = m.times( pDetail.getQuantity());
				//transDetail.setTotalPrice(totl.getAmount());
				//-----------------
				

				 /*Money moneyUnitPrice = new Money(pDetail.getTransactionDetail().getUnitPrice());
				 Money totl = moneyUnitPrice.times(pDetail.getQuantity());
				
				totl = totl.plus(totl.times(pDetail.getTransactionDetail().getVAT().divide(new BigDecimal(100),2)));
				transDetail.setTotalPrice(totl.getAmount());*/
				
				BigDecimal moneyUnitPrice = pDetail.getTransactionDetail().getUnitPrice().multiply(new BigDecimal(pDetail.getQuantity()));
				moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(pDetail.getTransactionDetail().getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setParent(pDetail.getTransactionDetail());
				transDetail = inventoryService.saveStoreItemTransactionDetail(transDetail);
				
				pDetail.setItemAccount(issueItemAccount);
				pDetail.setTransactionDetail(transDetail);
				//save issue to patient detail
				inventoryService.saveStoreItemAccountDetail(pDetail);
				//save issues transaction detail
				
			}
			
			StoreSingleton.getInstance().getHash().remove(fowardParam);
			StoreSingleton.getInstance().getHash().remove("issueItem_"+userId);
		}
		
		return "redirect:/module/inventory/subStoreIssueItemList.form";
	}
	
	@RequestMapping("/module/inventory/viewStockBalanceDetail.form")
	public String viewStockBalanceDetail( @RequestParam(value="drugId",required=false)  Integer drugId,@RequestParam(value="formulationId",required=false)  Integer formulationId,@RequestParam(value="expiry",required=false)  Integer expiry, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreDrugTransactionDetail> listViewStockBalance = inventoryService.listStoreDrugTransactionDetail(store.getId(), drugId, formulationId , expiry);
		model.addAttribute("listViewStockBalance", listViewStockBalance);
		return "/module/inventory/mainstore/viewStockBalanceDetail";
	}
	@RequestMapping("/module/inventory/itemViewStockBalanceDetail.form")
	public String itemViewStockBalanceDetail( @RequestParam(value="itemId",required=false)  Integer itemId,@RequestParam(value="specificationId",required=false)  Integer specificationId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreItemTransactionDetail> listViewStockBalance = inventoryService.listStoreItemTransactionDetail(store.getId(), itemId, specificationId,0,0);
		model.addAttribute("listViewStockBalance", listViewStockBalance);
		return "/module/inventory/mainstoreItem/itemViewStockBalanceDetail";
	}
	@RequestMapping("/module/inventory/viewStockBalanceSubStoreDetail.form")
	public String viewStockBalanceSubStoreDetail( @RequestParam(value="drugId",required=false)  Integer drugId,@RequestParam(value="formulationId",required=false)  Integer formulationId ,@RequestParam(value="expiry",required=false)  Integer expiry, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreDrugTransactionDetail> listViewStockBalance = inventoryService.listStoreDrugTransactionDetail(store.getId(), drugId, formulationId, expiry);
		model.addAttribute("listViewStockBalance", listViewStockBalance);
		return "/module/inventory/substore/viewStockBalanceDetail";
	}
	@RequestMapping("/module/inventory/itemViewStockBalanceSubStoreDetail.form")
	public String itemViewStockBalanceSubStoreDetail( @RequestParam(value="itemId",required=false)  Integer itemId,@RequestParam(value="specificationId",required=false)  Integer specificationId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		List<InventoryStoreItemTransactionDetail> listViewStockBalance = inventoryService.listStoreItemTransactionDetail(store.getId(), itemId, specificationId, 0, 0);
		model.addAttribute("listViewStockBalance", listViewStockBalance);
		return "/module/inventory/substoreItem/itemViewStockBalanceDetail";
	}
	
	@RequestMapping("/module/inventory/subStoreIssueDrugDettail.form")
	public String viewDetailIssueDrug( @RequestParam(value="issueId",required=false)  Integer issueId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreDrugPatientDetail> listDrugIssue = inventoryService.listStoreDrugPatientDetail(issueId);
		InventoryStoreDrugPatient inventoryStoreDrugPatient = new InventoryStoreDrugPatient();
		model.addAttribute("listDrugIssue", listDrugIssue);
		if(CollectionUtils.isNotEmpty(listDrugIssue)){
			inventoryStoreDrugPatient=listDrugIssue.get(0).getStoreDrugPatient();
			model.addAttribute("issueDrugPatient", listDrugIssue.get(0).getStoreDrugPatient());
			model.addAttribute("date", listDrugIssue.get(0).getStoreDrugPatient().getCreatedOn());
		}
		if(inventoryStoreDrugPatient!=null){
			Integer patientCategoryConcept=Integer.parseInt(inventoryStoreDrugPatient.getPatientCategory());
			Concept concept=Context.getConceptService().getConcept(patientCategoryConcept);
			model.addAttribute("patientCategory", concept.getName());
		}
		if(CollectionUtils.isNotEmpty(listDrugIssue)){
		for(InventoryStoreDrugPatientDetail issue:listDrugIssue){
		model.addAttribute("totalAmount", issue.getTransactionDetail().getTotalPrice());
		model.addAttribute("discount", issue.getTransactionDetail().getWaiverPercentage());
		model.addAttribute("totalAmountPayable", issue.getTransactionDetail().getAmountPayable());
		model.addAttribute("amountGiven", issue.getTransactionDetail().getAmountGiven());
		model.addAttribute("amountReturned", issue.getTransactionDetail().getAmountReturned());
		}	
		}
		return "/module/inventory/substore/subStoreIssueDrugDettail";
	}
	
	@RequestMapping("/module/inventory/subStoreIssueDrugAccountDettail.form")
	public String viewDetailIssueDrugAccount( @RequestParam(value="issueId",required=false)  Integer issueId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreDrugAccountDetail> listDrugIssue = inventoryService.listStoreDrugAccountDetail(issueId);
		model.addAttribute("listDrugIssue", listDrugIssue);
		if(CollectionUtils.isNotEmpty(listDrugIssue)){
			model.addAttribute("issueDrugAccount", listDrugIssue.get(0).getDrugAccount());
			model.addAttribute("date", listDrugIssue.get(0).getDrugAccount().getCreatedOn());
		}
		return "/module/inventory/substore/subStoreIssueDrugAccountDettail";
	}
	
	@RequestMapping("/module/inventory/subStoreIssueItemDettail.form")
	public String viewDetailIssueItem( @RequestParam(value="issueId",required=false)  Integer issueId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreItemAccountDetail> listItemIssue = inventoryService.listStoreItemAccountDetail(issueId);
		model.addAttribute("listItemIssue", listItemIssue);
		if(CollectionUtils.isNotEmpty(listItemIssue)){
			model.addAttribute("issueItemAccount", listItemIssue.get(0).getItemAccount());
			model.addAttribute("date", listItemIssue.get(0).getItemAccount().getCreatedOn());
		}
		return "/module/inventory/substoreItem/subStoreIssueItemDettail";
	}
	
	@RequestMapping("/module/inventory/subCatByCat.form")
	public String getSubCatByCat( @RequestParam(value="categoryId",required=false)  Integer categoryId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryItemSubCategory> subCategories = inventoryService.listSubCatByCat(categoryId);
		model.addAttribute("subCategories", subCategories);
		return "/module/inventory/item/subCatByCat";
	}
	
	@RequestMapping("/module/inventory/drugReceiptDetail.form")
	public String drugReceiptDetail( @RequestParam(value="receiptId",required=false)  Integer receiptId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreDrugTransactionDetail> transactionDetails = inventoryService.listTransactionDetail(receiptId);
		if(!CollectionUtils.isEmpty(transactionDetails)){
			model.addAttribute("store", transactionDetails.get(0).getTransaction().getStore());
			model.addAttribute("date", transactionDetails.get(0).getTransaction().getCreatedOn());
		}
		model.addAttribute("transactionDetails", transactionDetails);
		return "/module/inventory/mainstore/receiptsToGeneralStoreDetail";
	}
	@RequestMapping("/module/inventory/itemReceiptDetail.form")
	public String itemReceiptDetail( @RequestParam(value="receiptId",required=false)  Integer receiptId, Model model) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryStoreItemTransactionDetail> transactionDetails = inventoryService.listStoreItemTransactionDetail(receiptId);
		if(!CollectionUtils.isEmpty(transactionDetails)){
			model.addAttribute("store", transactionDetails.get(0).getTransaction().getStore());
			model.addAttribute("date", transactionDetails.get(0).getTransaction().getCreatedOn());
		}
		model.addAttribute("transactionDetails", transactionDetails);
		return "/module/inventory/mainstoreItem/itemReceiptsToGeneralStoreDetail";
	}
	
	
	@RequestMapping(value="/module/inventory/viewStockBalanceExpiry.form",method = RequestMethod.GET)
	public String viewStockBalanceExpiry( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="categoryId",required=false)  Integer categoryId,
            @RequestParam(value="drugName",required=false)  String drugName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            Map<String, Object> model, HttpServletRequest request
	) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 
	 int total = inventoryService.countViewStockBalance(store.getId(), categoryId, drugName,  fromDate, toDate , true);
	 String temp = "";
		if(categoryId != null){	
				temp = "?categoryId="+categoryId;
		}
		
		if(drugName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?drugName="+drugName;
			}else{
				temp +="&drugName="+drugName;
			}
		}
		if(fromDate != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?fromDate="+fromDate;
			}else{
				temp +="&fromDate="+fromDate;
			}
		}
		if(toDate != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?toDate="+toDate;
			}else{
				temp +="&toDate="+toDate;
			}
		}
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		List<InventoryStoreDrugTransactionDetail> stockBalances = inventoryService.listViewStockBalance(store.getId(), categoryId, drugName,  fromDate, toDate, true, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryDrugCategory> listCategory = inventoryService.listDrugCategory("", 0, 0);
		if (stockBalances!=null){
		Collections.sort(stockBalances);
		}
		
		model.put("categoryId", categoryId );
		model.put("drugName", drugName );
		model.put("fromDate", fromDate );
		model.put("toDate", toDate );
		model.put("pagingUtil", pagingUtil );
		model.put("stockBalances", stockBalances );
		model.put("listCategory", listCategory );
		model.put("store", store );
		if(store != null && store.getParent() == null){
			return "/module/inventory/mainstore/viewStockBalanceExpiry";
		}else
		{
			return "/module/inventory/substore/viewStockBalanceExpiry";
		}
	 
	}
	
	@RequestMapping(value="/module/inventory/expireDrug.form",method = RequestMethod.GET)
	public String expireDrug(@RequestParam(value="drugIdList",required=false)  String drugIdList,
			@RequestParam(value="storeId",required=false)  Integer storeId){
	InventoryCommonService inventoryCommonService = (InventoryCommonService) Context.getService(InventoryCommonService.class);
	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	for(String drugIdAndFormulationId:drugIdList.split(",")){
	String[] drugAndFormulation=drugIdAndFormulationId.split("-");
	List<InventoryStoreDrugTransactionDetail> isdtds=inventoryService.getStoreDrugTransactionDetailByIdAndFormulation(Integer.parseInt(drugAndFormulation[0]),Integer.parseInt(drugAndFormulation[1]),storeId);
	for(InventoryStoreDrugTransactionDetail isdt:isdtds){
	isdt.setExpireStatus(1);
	InventoryStoreDrugTransactionDetail ip=inventoryCommonService.expireInventoryStoreDrugTransactionDetail(isdt);
	}
	}
	return "redirect:/module/inventory/viewStockBalanceExpiry.form";
	}
			//order from opd
	@RequestMapping("/module/inventory/processDrugOrder.form")
	public String listReceiptDrugAvailablee(
			@RequestParam(value = "drugId", required = false) Integer drugId,
			@RequestParam(value = "formulationId", required = false) Integer formulationId,
			@RequestParam(value = "frequencyName", required = false) String frequencyName,
			@RequestParam(value = "days", required = false) Integer days,
			@RequestParam(value = "comments", required = false) String comments,
			Model model) {

		InventoryService inventoryService = (InventoryService) Context
				.getService(InventoryService.class);
		InventoryDrug drug = inventoryService.getDrugById(drugId);
		InventoryStore store = inventoryService
				.getStoreByCollectionRole(new ArrayList<Role>(Context
						.getAuthenticatedUser().getAllRoles()));
		if (store != null && drug != null && formulationId != null) {
			List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService
					.listStoreDrugTransactionDetail(store.getId(),
							drug.getId(), formulationId, true);
			// check that drug is issued before
			int userId = Context.getAuthenticatedUser().getId();

			String fowardParam = "issueDrugAccountDetail_" + userId;
			String fowardParamDrug = "issueDrugDetail_" + userId;
			List<InventoryStoreDrugPatientDetail> listDrug = (List<InventoryStoreDrugPatientDetail>) StoreSingleton
					.getInstance().getHash().get(fowardParamDrug);
			List<InventoryStoreDrugAccountDetail> listDrugAccount = (List<InventoryStoreDrugAccountDetail>) StoreSingleton
					.getInstance().getHash().get(fowardParam);
			List<InventoryStoreDrugTransactionDetail> listReceiptDrugReturn = new ArrayList<InventoryStoreDrugTransactionDetail>();
			boolean check = false;
			if (CollectionUtils.isNotEmpty(listDrug)) {
				if (CollectionUtils.isNotEmpty(listReceiptDrug)) {
					for (InventoryStoreDrugTransactionDetail drugDetail : listReceiptDrug) {
						for (InventoryStoreDrugPatientDetail drugPatient : listDrug) {
							if (drugDetail.getId().equals(
									drugPatient.getTransactionDetail().getId())) {
								drugDetail.setCurrentQuantity(drugDetail
										.getCurrentQuantity()
										- drugPatient.getQuantity());
							}

						}
						if (drugDetail.getCurrentQuantity() > 0) {
							listReceiptDrugReturn.add(drugDetail);
							check = true;
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(listDrugAccount)) {
				if (CollectionUtils.isNotEmpty(listReceiptDrug)) {
					for (InventoryStoreDrugTransactionDetail drugDetail : listReceiptDrug) {
						for (InventoryStoreDrugAccountDetail drugAccount : listDrugAccount) {
							if (drugDetail.getId().equals(
									drugAccount.getTransactionDetail().getId())) {
								drugDetail.setCurrentQuantity(drugDetail
										.getCurrentQuantity()
										- drugAccount.getQuantity());
							}
						}
						if (drugDetail.getCurrentQuantity() > 0 && !check) {
							listReceiptDrugReturn.add(drugDetail);
						}
					}
				}
			}
			if (CollectionUtils.isEmpty(listReceiptDrugReturn)
					&& CollectionUtils.isNotEmpty(listReceiptDrug)) {
				listReceiptDrugReturn.addAll(listReceiptDrug);
			}

			model.addAttribute("listReceiptDrug", listReceiptDrugReturn);

			
			String listOfDrugQuantity = "";
			for (InventoryStoreDrugTransactionDetail lrdr : listReceiptDrugReturn) {
				listOfDrugQuantity = listOfDrugQuantity
						+ lrdr.getId().toString() + ".";
			}

			model.addAttribute("listOfDrugQuantity", listOfDrugQuantity);
			model.addAttribute("frequencyName", frequencyName);
			model.addAttribute("noOfDays", days);
			model.addAttribute("comments", comments);
		}

		return "/module/inventory/queue/processDrugOrder";
	}
	
	@RequestMapping("/module/inventory/removeObjectFromList.form")
	public String removeObjectFromList( @RequestParam(value="position")  Integer position,@RequestParam(value="check")  Integer check, Model model) {
		int userId = Context.getAuthenticatedUser().getId();
		
		String fowardParam1 = "issueItemDetail_"+userId;
		String fowardParam2 = "subStoreIndentItem_"+userId;
		String fowardParam3 = "subStoreIndentDrug_"+userId;
		String fowardParam4 = "issueDrugAccountDetail_"+userId;
		String fowardParam5 = "issueDrugDetail_"+userId;
		String fowardParam6 = "itemReceipt_"+userId;
		String fowardParam7 = "reipt_"+userId;
		List list = null;
		switch (check){
		case 1:
			//process fowardParam1
			list = (List<InventoryStoreItemAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam1);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreItemAccountDetail a = (InventoryStoreItemAccountDetail)list.get(position);
				//System.out.println("a fowardParam1: "+a.getTransactionDetail().getItem().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam1, list);
			return "redirect:/module/inventory/subStoreIssueItemForm.form";
		case 2:
			//process fowardParam2
			list = (List<InventoryStoreItemIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam2);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreItemIndentDetail a = (InventoryStoreItemIndentDetail)list.get(position);
				//System.out.println("a fowardParam2: "+a.getItem().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam2, list);
			return "redirect:/module/inventory/subStoreIndentItem.form";
		case 3:
			//process fowardParam3
			list = (List<InventoryStoreDrugIndentDetail> )StoreSingleton.getInstance().getHash().get(fowardParam3);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreDrugIndentDetail a = (InventoryStoreDrugIndentDetail)list.get(position);
				//System.out.println("fowardParam3 a drug : "+a.getDrug().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam3, list);
			return "redirect:/module/inventory/subStoreIndentDrug.form";
		case 4:
			//process fowardParam4
			list = (List<InventoryStoreDrugAccountDetail> )StoreSingleton.getInstance().getHash().get(fowardParam4);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreDrugAccountDetail a = (InventoryStoreDrugAccountDetail)list.get(position);
				//System.out.println("fowardParam4 a drug : "+a.getTransactionDetail().getDrug().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam4, list);
			return "redirect:/module/inventory/subStoreIssueDrugAccountForm.form";
		case 5:
			//process fowardParam5
			list = (List<InventoryStoreDrugPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam5);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreDrugPatientDetail a = (InventoryStoreDrugPatientDetail)list.get(position);
				//System.out.println("fowardParam5 a drug : "+a.getTransactionDetail().getDrug().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam5, list);
			return "redirect:/module/inventory/subStoreIssueDrugForm.form";
		case 6:
			//process fowardParam6
			list = (List<InventoryStoreItemTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam6);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreItemTransactionDetail a = (InventoryStoreItemTransactionDetail)list.get(position);
				//System.out.println("fowardParam6 a item : "+a.getItem().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam6, list);
			return "redirect:/module/inventory/itemReceiptsToGeneralStore.form";
		case 7:
			//process fowardParam7
			list = (List<InventoryStoreDrugTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam7);
			if(CollectionUtils.isNotEmpty(list)){
				InventoryStoreDrugTransactionDetail a = (InventoryStoreDrugTransactionDetail)list.get(position);
				//System.out.println("fowardParam7 a drug : "+a.getDrug().getName());
				list.remove(a);
			}
			StoreSingleton.getInstance().getHash().put(fowardParam7, list);
			return "redirect:/module/inventory/receiptsToGeneralStore.form";
		default: 
		}
		
		
		return "redirect:/module/inventory/main.form";
	}
}
