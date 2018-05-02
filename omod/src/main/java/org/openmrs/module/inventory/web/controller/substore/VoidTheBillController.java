package org.openmrs.module.inventory.web.controller.substore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("VoidTheBillController")
@RequestMapping("/module/inventory/voidTheBill.form")
public class VoidTheBillController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@RequestParam(value="billNo",required=false) Integer billNo, Model model) {
		InventoryCommonService inventoryCommonService = (InventoryCommonService) Context.getService(InventoryCommonService.class);
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		InventoryStoreDrugPatient inventoryStoreDrugPatient=inventoryCommonService.getInventoryStoreDrugPatient(billNo);
		List<InventoryStoreDrugPatientDetail> inventoryStoreDrugPatientDetailList=inventoryCommonService.getInventoryStoreDrugPatientDetail(inventoryStoreDrugPatient);
		List<InventoryStoreDrugTransactionDetail> inventoryStoreDrugTransactionDetail=new ArrayList<InventoryStoreDrugTransactionDetail>();
		Map<String,Integer> quantityMap=new LinkedHashMap<String,Integer>();
		List<Integer> transactionDetailId=new ArrayList<Integer>();
		for(InventoryStoreDrugPatientDetail isdpd:inventoryStoreDrugPatientDetailList){
			InventoryStoreDrugTransactionDetail isdtd=inventoryCommonService.getInventoryStoreDrugTransactionDetail(isdpd.getTransactionDetail().getId());
			inventoryStoreDrugTransactionDetail.add(isdtd);
		}
		
		if(CollectionUtils.isNotEmpty(inventoryStoreDrugPatientDetailList)){
			inventoryStoreDrugPatient=inventoryStoreDrugPatientDetailList.get(0).getStoreDrugPatient();
			model.addAttribute("issueDrugPatient", inventoryStoreDrugPatientDetailList.get(0).getStoreDrugPatient());
			model.addAttribute("date", inventoryStoreDrugPatientDetailList.get(0).getStoreDrugPatient().getCreatedOn());
		}
		if(inventoryStoreDrugPatient!=null){
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			Integer patientCategoryConcept=Integer.parseInt(inventoryStoreDrugPatient.getPatientCategory());
			Concept concept=Context.getConceptService().getConcept(patientCategoryConcept);
			model.addAttribute("patientCategory", concept.getName());
			List<PersonAttribute> pas = hcs.getPersonAttributes(inventoryStoreDrugPatient.getPatient().getId());
			for (PersonAttribute pa : pas) {
				PersonAttributeType attributeType = pa.getAttributeType();
			if (attributeType.getPersonAttributeTypeId() == 31) {
				String patientCategory=pa.getValue();
				Integer patientSubCategoryConcept=Integer.parseInt(patientCategory);
				Concept subconcept=Context.getConceptService().getConcept(patientSubCategoryConcept);
				model.addAttribute("patientSubCategory", subconcept.getName());
			}
			if (attributeType.getPersonAttributeTypeId() == 29) {
				String dohId=pa.getValue();
				model.addAttribute("dohId", dohId);
			}
			}
			model.addAttribute("billNo",inventoryStoreDrugPatient.getId());
		}
		model.addAttribute("storeDrugTransactionDetailList",inventoryStoreDrugTransactionDetail);
		Integer expired=0;
		for(InventoryStoreDrugTransactionDetail isdtd:inventoryStoreDrugTransactionDetail){
			quantityMap.put(isdtd.getId().toString(), isdtd.getIssueQuantity());
			transactionDetailId.add(isdtd.getId());	
		    Date date1 = new Date();
		    Date date2 = isdtd.getDateExpiry();
	        if (expired==0 && date1.compareTo(date2) > 0) {
	        	expired=1;
	        }
		}
		model.addAttribute("expired",expired);
		model.addAttribute("quantityMap",quantityMap);
		model.addAttribute("transactionDetailId",transactionDetailId);
		return "/module/inventory/substore/voidTheBill";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model,
			 @RequestParam(value="billNo",required=false)  Integer billNo,
			 @RequestParam(value = "tranDetail", required = false) String[] tranDetailList,
			 @RequestParam(value="voidedReason",required=false)  String voidedReason,
			 @RequestParam(value="cashReturned",required=false)  Integer cashReturned) {
	InventoryCommonService inventoryCommonService = (InventoryCommonService) Context.getService(InventoryCommonService.class);
	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	InventoryStoreDrugPatient inventoryStoreDrugPatient=inventoryCommonService.getInventoryStoreDrugPatient(billNo);
	List<InventoryStoreDrugPatientDetail> inventoryStoreDrugPatientDetailList=inventoryCommonService.getInventoryStoreDrugPatientDetail(inventoryStoreDrugPatient);
	
	InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	//totalAmountAferVoid will be zero as complete bill is voided
	Float totalAmountAferVoid=0f;
	Float waiverAmountAferVoid=0f;
	for(InventoryStoreDrugPatientDetail isdpd:inventoryStoreDrugPatientDetailList){
		InventoryStoreDrugTransaction inventoryStoreDrugTransaction=new InventoryStoreDrugTransaction();
		InventoryStoreDrugTransactionDetail inventoryStoreDrugTransactionDetail=new InventoryStoreDrugTransactionDetail();
		InventoryStoreDrugTransactionDetail isdtd=inventoryCommonService.getInventoryStoreDrugTransactionDetail(isdpd.getTransactionDetail().getId());
		String quantity = request.getParameter("quantity"+isdpd.getTransactionDetail().getId());
     
        if (quantity != null) {
        Integer quantityInInteger=Integer.parseInt(quantity);
        if (quantityInInteger != 0) {
        
        //totalAmountAferVoid=totalAmountAferVoid+isdtd.getMrpPrice().floatValue()*quantityInInteger;
        
        inventoryStoreDrugTransaction.setStore(store);
		inventoryStoreDrugTransaction.setTypeTransaction(1);
		inventoryStoreDrugTransaction.setStatus(0);
		inventoryStoreDrugTransaction.setDescription("RECEIPT FROM PATIENT"+" "+DateUtils.getDDMMYYYY());
		inventoryStoreDrugTransaction.setCreatedOn(new Date());
		inventoryStoreDrugTransaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
		inventoryStoreDrugTransaction=inventoryService.saveStoreDrugTransaction(inventoryStoreDrugTransaction);
		
		InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(subStore.getId(), isdtd.getDrug().getId(), isdtd.getFormulation().getId());
		inventoryStoreDrugTransactionDetail.setTransaction(inventoryStoreDrugTransaction);
		inventoryStoreDrugTransactionDetail.setDrug(isdtd.getDrug());
		inventoryStoreDrugTransactionDetail.setFormulation(isdtd.getFormulation());
		inventoryStoreDrugTransactionDetail.setQuantity(quantityInInteger);
		inventoryStoreDrugTransactionDetail.setCurrentQuantity(0);
		inventoryStoreDrugTransactionDetail.setIssueQuantity(0);
		inventoryStoreDrugTransactionDetail.setMrpPrice(isdtd.getMrpPrice());
		inventoryStoreDrugTransactionDetail.setVAT(isdtd.getVAT());
		inventoryStoreDrugTransactionDetail.setTotalPrice(isdtd.getMrpPrice().multiply(new BigDecimal(quantityInInteger)));
		inventoryStoreDrugTransactionDetail.setBatchNo(isdtd.getBatchNo());
		inventoryStoreDrugTransactionDetail.setCompanyName(isdtd.getCompanyName());
		inventoryStoreDrugTransactionDetail.setDateManufacture(isdtd.getDateManufacture());
		inventoryStoreDrugTransactionDetail.setDateExpiry(isdtd.getDateExpiry());
		inventoryStoreDrugTransactionDetail.setReceiptDate(isdtd.getReceiptDate());
		inventoryStoreDrugTransactionDetail.setCreatedOn(new Date());
		inventoryStoreDrugTransactionDetail.setOpeningBalance(totalQuantity);
		inventoryStoreDrugTransactionDetail.setClosingBalance(totalQuantity+quantityInInteger);
		inventoryStoreDrugTransactionDetail.setParent(isdtd.getParent());
		inventoryStoreDrugTransactionDetail.setExpireStatus(0);
		inventoryStoreDrugTransactionDetail.setCostToPatient(isdtd.getCostToPatient());
		inventoryStoreDrugTransactionDetail.setRate(isdtd.getRate());
		inventoryStoreDrugTransactionDetail.setCgst(isdtd.getCgst());
		inventoryStoreDrugTransactionDetail.setSgst(isdtd.getSgst());
		inventoryStoreDrugTransactionDetail.setSgstAmount(isdtd.getSgstAmount());
		inventoryStoreDrugTransactionDetail.setUnitPrice(isdtd.getUnitPrice());
		inventoryStoreDrugTransactionDetail.setTotalAmountAfterGst(isdtd.getTotalAmountAfterGst());
		inventoryService.saveStoreDrugTransactionDetail(inventoryStoreDrugTransactionDetail);
		
		isdtd.setQuantityAfterReturn(isdtd.getIssueQuantity()-quantityInInteger);
		isdtd.setVoided(1);
		isdtd.setTotalAmountAferVoid(totalAmountAferVoid);
		//isdtd.setWaiverAmountAferVoid(waiverAmountAferVoid);
		isdtd.setCashReturned(cashReturned);
		//isdtd.setAmountCreditAfterVoid();
		inventoryService.saveOrUpdateStoreDrugTransactionDetail(isdtd);
		
		Integer currentQuantity=isdtd.getParent().getCurrentQuantity();
		InventoryStoreDrugTransactionDetail isdtdcurrentQuantity=isdtd.getParent();
		isdtdcurrentQuantity.setCurrentQuantity(currentQuantity+quantityInInteger);
		inventoryService.saveOrUpdateStoreDrugTransactionDetail(isdtdcurrentQuantity);
		
		inventoryStoreDrugPatient.setVoided(1);
		inventoryStoreDrugPatient.setVoidedDate(new Date());
		inventoryStoreDrugPatient.setVoidedBy(Context.getAuthenticatedUser().getGivenName());
		inventoryStoreDrugPatient.setVoidedReason(voidedReason);
		inventoryService.saveStoreDrugPatient(inventoryStoreDrugPatient);
         }
        }
	}
	return "/module/inventory/substore/voidTheBillClose";
}
}