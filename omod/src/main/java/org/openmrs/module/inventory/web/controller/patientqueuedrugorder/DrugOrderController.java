/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Inventory module.
 *
 *  Inventory module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Inventory module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Inventory module.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  author: ghanshyam
 *  date: 15-june-2013
 *  issue no: #1636
 **/

package org.openmrs.module.inventory.web.controller.patientqueuedrugorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBill;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBillItem;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("DrugOrderController")
@RequestMapping("/module/inventory/drugorder.form")
public class DrugOrderController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(Model model,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam(value = "date", required = false) String dateStr, 
			@RequestParam(value = "patientType", required = false) String patientType) {
		InventoryService inventoryService = Context
				.getService(InventoryService.class);

		List<OpdDrugOrder> drugOrderList = inventoryService.listOfDrugOrder(
				patientId, encounterId);
		
		
		model.addAttribute("drugOrderList", drugOrderList);
		model.addAttribute("drugOrderSize", drugOrderList.size());
		model.addAttribute("patientId", patientId);
		model.addAttribute("encounterId", encounterId);
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = hospitalCoreService.getPatientByPatientId(patientId);
		model.addAttribute("patientSearch", patientSearch);
		model.addAttribute("patientType", patientType);
		model.addAttribute("date", dateStr);
		model.addAttribute("doctor", drugOrderList.get(0).getCreator().getGivenName());
		
		
		List<InventoryStoreDrugPatient> inventoryStoreDrugPatient = new ArrayList<InventoryStoreDrugPatient>();
		inventoryStoreDrugPatient=inventoryService.listPatientDetail();
		model.addAttribute("isdpdt", inventoryStoreDrugPatient.size()+1);
		
		
		model.addAttribute("pharmacist", Context.getAuthenticatedUser().getGivenName());
		
		List<OpdDrugOrder> drugOrderListAvailable = new ArrayList<OpdDrugOrder>();
		List<OpdDrugOrder> drugOrderListNotAvailable = new ArrayList<OpdDrugOrder>();
	
		for(OpdDrugOrder dol:drugOrderList){
		
			InventoryDrug drug = inventoryService.getDrugById(dol.getInventoryDrug().getId());
			Integer formulationId = dol.getInventoryDrugFormulation().getId();
			InventoryStore	store = inventoryService
				.getStoreByCollectionRole(new ArrayList<Role>(Context
						.getAuthenticatedUser().getAllRoles()));
		if (store != null && drug != null && formulationId != null) {
			List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService
					.listStoreDrugTransactionDetail(store.getId(),
							drug.getId(), formulationId, true);
		
			if(listReceiptDrug.size()!=0){
				drugOrderListAvailable.add(dol);
			}
			else{
				drugOrderListNotAvailable.add(dol);	
			}
		  }
		
		}
		
		
		model.addAttribute("drugOrderListAvailable", drugOrderListAvailable);
	
		model.addAttribute("drugOrderListNotAvailable", drugOrderListNotAvailable);
		
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		List<PersonAttribute> pas = hcs.getPersonAttributes(patientId);
		for (PersonAttribute pa : pas) {
			PersonAttributeType attributeType = pa.getAttributeType();
			if (attributeType.getPersonAttributeTypeId() == 14) {
				String patientCategory=pa.getValue();
				Integer patientCategoryConcept=Integer.parseInt(patientCategory);
				Concept concept=Context.getConceptService().getConcept(patientCategoryConcept);
				model.addAttribute("patientCategory", concept.getName());
			}
			if (attributeType.getPersonAttributeTypeId() == 31) {
				String patientCategory=pa.getValue();
				Integer patientCategoryConcept=Integer.parseInt(patientCategory);
				Concept concept=Context.getConceptService().getConcept(patientCategoryConcept);
				model.addAttribute("patientSubCategory", concept.getName());
			}
			if (attributeType.getPersonAttributeTypeId() == 29) {
				String dohId=pa.getValue();
				
				model.addAttribute("dohId", dohId);
			}
		}
		String hospitalName=GlobalPropertyUtil.getString("hospitalcore.hospitalParticularName", "Kollegal DVT Hospital");
		model.addAttribute("hospitalName", hospitalName);
		
                
		return "/module/inventory/queue/drugOrder";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(HttpServletRequest request,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam(value = "paymentMode", required = false) String paymentMode,
			@RequestParam(value = "patientType", required = false) String patientType,
			@RequestParam(value="avaiableId",required=false) String[] avaiableId,
			@RequestParam(value = "totalValue", required = false) Float totalValue,
			@RequestParam(value = "waiverPercentage", required = false) Float waiverPercentage,
            @RequestParam(value= "waiverComment", required = false) String waiverComment,
			@RequestParam(value = "totalAmountPayable", required = false) BigDecimal totalAmountPayable,
			@RequestParam(value = "amountGiven", required = false) Integer amountGiven,
			@RequestParam(value = "amountReturned", required = false) Integer amountReturned) throws Exception{
		
		
		PatientService  patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(patientId);
		InventoryService inventoryService = Context.getService(InventoryService.class);
		PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		
		Date date = new Date();
		Integer formulationId;
		String frequencyName;
		String comments;
		Integer quantity;
		Integer noOfDays;
		Integer avlId;
		BigDecimal Discount;
		String patientCategory = "";
		String patientSubcategory = "";
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		List<PersonAttribute> pas = hcs.getPersonAttributes(patient.getPatientId());
		InventoryStoreDrugPatient inventoryStoreDrugPatient = new InventoryStoreDrugPatient();
		inventoryStoreDrugPatient.setStore(store);
		inventoryStoreDrugPatient.setPatient(patient);
		inventoryStoreDrugPatient.setName(patient.getGivenName()+" "+patient.getFamilyName());
		inventoryStoreDrugPatient.setIdentifier(patient.getPatientIdentifier().getIdentifier());
		inventoryStoreDrugPatient.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
		inventoryStoreDrugPatient.setCreatedOn(date);
		 
	    for (PersonAttribute pa : pas) {
	    PersonAttributeType attributeType = pa.getAttributeType();
	    if(pa.getAttributeType().getId()==14){
	    patientCategory = pa.getValue();
	    inventoryStoreDrugPatient.setPatientCategoryf(patientCategory);
	    }
	    if(pa.getAttributeType().getId()==31){
	    patientSubcategory = pa.getValue();
	    inventoryStoreDrugPatient.setPatientSubcategoryf(patientSubcategory);
	     }
	    }
		inventoryStoreDrugPatient = inventoryService.saveStoreDrugPatient(inventoryStoreDrugPatient);
		
		InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
		transaction.setDescription("ISSUE DRUG TO PATIENT "+DateUtils.getDDMMYYYY());
		transaction.setStore(store);
		transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
		transaction.setCreatedOn(date);
		if(patient.getAttribute(14).getValue().equals("3164")||patient.getAttribute(14).getValue().equals("3165")||
				patient.getAttribute(14).getValue().equals("3166")||patient.getAttribute(14).getValue().equals("3167"))
		{
			transaction.setPaymentCategory("paying");
		}
		else
		{
			transaction.setPaymentCategory("non-paying");
		}
		transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
		
		transaction = inventoryService.saveStoreDrugTransaction(transaction);
		
		List<EncounterType> types = new ArrayList<EncounterType>();
		EncounterType eType = new EncounterType(10);
		types.add(eType);
		Encounter lastVisitEncounter = hcs.getLastVisitEncounter(patient, types);
		if(avaiableId!=null){
		for (String avId : avaiableId) {
			InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
			
			formulationId = Integer.parseInt(request.getParameter(avId
					+ "_fFormulationId"));
			quantity = Integer.parseInt(request.getParameter(avId
					+ "_fQuantity"));
			
			Discount =  NumberUtils.createBigDecimal(request.getParameter(avId
					+ "_fDiscount"));
			
			frequencyName = request.getParameter(avId + "_fFrequencyName");
			noOfDays = Integer.parseInt(request.getParameter(avId + "_fnoOfDays"));
			comments = request.getParameter(avId + "_fcomments");
			
			
			Concept fCon = Context.getConceptService().getConcept(frequencyName);
			
			if(quantity!=0){
			 avlId = Integer.parseInt(avId);
			 InventoryDrugFormulation inventoryDrugFormulation = inventoryCommonService.getDrugFormulationById(formulationId);
			
			 InventoryStoreDrugPatientDetail pDetail = new InventoryStoreDrugPatientDetail();
			 
			 InventoryStoreDrugTransactionDetail inventoryStoreDrugTransactionDetail = inventoryService.getStoreDrugTransactionDetailById(avlId);
			 Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(store.getId(),inventoryStoreDrugTransactionDetail.getDrug().getId(), inventoryDrugFormulation.getId());
			 int t = totalQuantity -quantity;
			 InventoryStoreDrugTransactionDetail drugTransactionDetail = inventoryService.getStoreDrugTransactionDetailById(inventoryStoreDrugTransactionDetail.getId());
			 inventoryStoreDrugTransactionDetail.setCurrentQuantity(drugTransactionDetail.getCurrentQuantity() - quantity);
             inventoryService.saveStoreDrugTransactionDetail(inventoryStoreDrugTransactionDetail);
				
			 //save transactiondetail first
             InventoryStoreDrugTransactionDetail transDetail = new InventoryStoreDrugTransactionDetail();
			 transDetail.setTransaction(transaction);
			 transDetail.setCurrentQuantity(0);
			 transDetail.setIssueQuantity(quantity);
			 transDetail.setOpeningBalance(totalQuantity);
			 transDetail.setClosingBalance(t);
			 transDetail.setQuantity(0);
			 transDetail.setVAT(inventoryStoreDrugTransactionDetail.getVAT());
			 transDetail.setCostToPatient(inventoryStoreDrugTransactionDetail.getMrpPrice());
			 transDetail.setMrpPrice(inventoryStoreDrugTransactionDetail.getMrpPrice());
			 transDetail.setDrug(inventoryStoreDrugTransactionDetail.getDrug());
			 transDetail.setReorderPoint(inventoryStoreDrugTransactionDetail.getDrug().getReorderQty());
			 transDetail.setAttribute(inventoryStoreDrugTransactionDetail.getDrug().getAttributeName());
			 transDetail.setFormulation(inventoryDrugFormulation);
			 transDetail.setBatchNo(inventoryStoreDrugTransactionDetail.getBatchNo());
			 transDetail.setCompanyName(inventoryStoreDrugTransactionDetail.getCompanyName());
			 transDetail.setDateManufacture(inventoryStoreDrugTransactionDetail.getDateManufacture());
			 transDetail.setDateExpiry(inventoryStoreDrugTransactionDetail.getDateExpiry());
			 transDetail.setReceiptDate(inventoryStoreDrugTransactionDetail.getReceiptDate());
			 transDetail.setCreatedOn(date);
			 transDetail.setPatientType(patientType);
			 transDetail.setEncounter(Context.getEncounterService().getEncounter(encounterId));
			
			 transDetail.setFrequency(fCon);
			 transDetail.setNoOfDays(noOfDays);
			 transDetail.setComments(comments);
			
			 BigDecimal moneyUnitPrice = inventoryStoreDrugTransactionDetail.getMrpPrice().multiply(new BigDecimal(quantity));
			// moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(inventoryStoreDrugTransactionDetail.getVAT().divide(new BigDecimal(100))));
			 transDetail.setTotalPrice(moneyUnitPrice);
				
			 transDetail.setTotalAmount(totalValue);
			 transDetail.setWaiverPercentage(waiverPercentage);
			 Float waiverAmount=null;
			 
			 if(waiverPercentage!=null)
			 {
			 waiverAmount=totalValue*waiverPercentage/100;
			
			 
			 transDetail.setWaiverAmount(waiverAmount);
			 }
			 else
			 {
				 transDetail.setWaiverAmount(waiverAmount);
			 }
			 
			 transDetail.setAmountPayable(totalAmountPayable);
			 if(amountGiven==null)
			 {
			 transDetail.setAmountCredit(totalAmountPayable);
			 }
			
			 transDetail.setAmountGiven(amountGiven);
			 transDetail.setAmountReturned(amountReturned);
			 transDetail.setParent(inventoryStoreDrugTransactionDetail);
			 transDetail = inventoryService.saveStoreDrugTransactionDetail(transDetail);
				
			 pDetail.setQuantity(quantity);
			 
			 pDetail.setStoreDrugPatient(inventoryStoreDrugPatient);
			 pDetail.setTransactionDetail(transDetail);
			 
			 //save issue to patient detail
			 inventoryService.saveStoreDrugPatientDetail(pDetail);
			
			 BillingService billingService = Context.getService(BillingService.class);
				IndoorPatientServiceBill bill = new IndoorPatientServiceBill();
				bill.setActualAmount(moneyUnitPrice);
				bill.setAmount(moneyUnitPrice);
				
				bill.setEncounter(lastVisitEncounter);
				bill.setCreatedDate(new Date());
				bill.setPatient(patient);
				bill.setCreator(Context.getAuthenticatedUser());

				
				IndoorPatientServiceBillItem item = new IndoorPatientServiceBillItem();
			//	System.out.println("pDetail.getTransactionDetail().getCostToPatient():"+pDetail.getTransactionDetail().getCostToPatient());
				item.setUnitPrice(pDetail.getTransactionDetail().getCostToPatient());
				item.setAmount(moneyUnitPrice);
				item.setQuantity(pDetail.getQuantity());
				
		    //  System.out.println("pDetail.getQuantity():"+pDetail.getQuantity());
				
				item.setName(pDetail.getTransactionDetail().getDrug().getName());
				item.setCreatedDate(new Date());
				item.setIndoorPatientServiceBill(bill);
				item.setActualAmount(moneyUnitPrice);
				//item.setOrderType("DRUG");
				bill.addBillItem(item);
				bill = billingService.saveIndoorPatientServiceBill(bill);
				
			 OpdDrugOrder opdDrugOrder = inventoryService.getOpdDrugOrder(patientId,encounterId,
					 inventoryStoreDrugTransactionDetail.getDrug().getId(),formulationId);
			 opdDrugOrder.setOrderStatus(1);
			 patientDashboardService.saveOrUpdateOpdDrugOrder(opdDrugOrder);
		    }
				
		  }
		  
		}
		List<OpdDrugOrder> drugOrderList = inventoryService.listOfDrugOrder(patientId, encounterId);
		
		for(OpdDrugOrder dol:drugOrderList){
		InventoryDrug drug = inventoryService.getDrugById(dol.getInventoryDrug().getId());
		Integer formulationIdd = dol.getInventoryDrugFormulation().getId();
		InventoryStore storee = inventoryService
				.getStoreByCollectionRole(new ArrayList<Role>(Context
						.getAuthenticatedUser().getAllRoles()));
		if (storee != null && drug != null && formulationIdd != null) {
			List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService
					.listStoreDrugTransactionDetail(storee.getId(),
							drug.getId(), formulationIdd, true);
			if(listReceiptDrug.size()!=0){
		
			}
			else{
				 dol.setCancelStatus(1);
				 patientDashboardService.saveOrUpdateOpdDrugOrder(dol);
			}
		  }
		}
		return "redirect:/module/inventory/patientQueueDrugOrder.form";
	}
}
