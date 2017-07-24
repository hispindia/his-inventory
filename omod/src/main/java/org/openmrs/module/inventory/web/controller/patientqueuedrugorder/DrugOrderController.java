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

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.PatientService;
//new
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBill;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBillItem;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.ActionValue;
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
		
		
		InventoryStoreDrugPatient inventoryStoreDrugPatient = new InventoryStoreDrugPatient();
		model.addAttribute("pharmacist", Context.getAuthenticatedUser().getGivenName());
                
                
		return "/module/inventory/queue/drugOrder";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(HttpServletRequest request,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam(value = "paymentMode", required = false) String paymentMode,
			@RequestParam(value = "patientType", required = false) String patientType,
			//ghanshyam,4-july-2013, issue no # 1984, User can issue drugs only from the first indent
			@RequestParam(value="avaiableId",required=false) String[] avaiableId) throws Exception{
		PatientService  patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(patientId);
		InventoryService inventoryService = Context.getService(InventoryService.class);
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		
		Date date = new Date();
		Integer formulationId;
		String frequencyName;
		String comments;
		Integer quantity;
		Integer noOfDays;
		Integer avlId;
		
		InventoryStoreDrugPatient inventoryStoreDrugPatient = new InventoryStoreDrugPatient();
		inventoryStoreDrugPatient.setStore(store);
		inventoryStoreDrugPatient.setPatient(patient);
		if(patient.getMiddleName()!=null){
			inventoryStoreDrugPatient.setName(patient.getGivenName()+" "+patient.getFamilyName()+" "+patient.getMiddleName().replace(",", " "));
		}
		else{
			inventoryStoreDrugPatient.setName(patient.getGivenName()+" "+patient.getFamilyName());
		}
		inventoryStoreDrugPatient.setIdentifier(patient.getPatientIdentifier().getIdentifier());
		inventoryStoreDrugPatient.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
		inventoryStoreDrugPatient.setCreatedOn(date);
		inventoryStoreDrugPatient = inventoryService.saveStoreDrugPatient(inventoryStoreDrugPatient);
		
		InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
		transaction.setDescription("ISSUE DRUG TO PATIENT "+DateUtils.getDDMMYYYY());
		transaction.setStore(store);
		transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
		transaction.setCreatedOn(date);
		//transaction.setPaymentMode(paymentMode);
		System.out.println("gsdgshgds"+patient.getAttribute(14).getValue());
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
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		Encounter lastVisitEncounter = hcs.getLastVisitEncounter(patient, types);
		if(avaiableId!=null){
		for (String avId : avaiableId) {
			InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
			
			formulationId = Integer.parseInt(request.getParameter(avId
					+ "_fFormulationId"));
			quantity = Integer.parseInt(request.getParameter(avId
					+ "_fQuantity"));
			
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
			 transDetail.setCostToPatient(inventoryStoreDrugTransactionDetail.getCostToPatient());
			 transDetail.setUnitPrice(inventoryStoreDrugTransactionDetail.getUnitPrice());
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
			
			 System.out.println("quantity"+quantity);
			 BigDecimal moneyUnitPrice = inventoryStoreDrugTransactionDetail.getCostToPatient().multiply(new BigDecimal(quantity));
			// moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(inventoryStoreDrugTransactionDetail.getVAT().divide(new BigDecimal(100))));
			 transDetail.setTotalPrice(moneyUnitPrice);
				
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
				item.setOrderType("DRUG");
				bill.addBillItem(item);
				bill = billingService.saveIndoorPatientServiceBill(bill);
				
			 OpdDrugOrder opdDrugOrder = inventoryService.getOpdDrugOrder(patientId,encounterId,
					 inventoryStoreDrugTransactionDetail.getDrug().getId(),formulationId);
			 PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
			 opdDrugOrder.setOrderStatus(1);
			 patientDashboardService.saveOrUpdateOpdDrugOrder(opdDrugOrder);
		    }
				
		  }
		  
		}
		return "redirect:/module/inventory/patientQueueDrugOrder.form";
	}
}
