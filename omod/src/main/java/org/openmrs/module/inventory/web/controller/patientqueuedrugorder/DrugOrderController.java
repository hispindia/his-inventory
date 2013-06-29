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
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
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
			@RequestParam(value = "date", required = false) String dateStr) {
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
		model.addAttribute("date", dateStr);

		return "/module/inventory/queue/drugOrder";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(HttpServletRequest request,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam(value="drugOrder",required=false) String[] drugOrder) throws Exception{

		PatientService  patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(patientId);
		Integer formulationId;
		Integer quantity;
		Integer avaiableId;
		if(drugOrder!=null){
		for (String drugName : drugOrder) {
			InventoryCommonService inventoryCommonService = Context.getService(InventoryCommonService.class);
			InventoryService inventoryService = Context.getService(InventoryService.class);
			InventoryDrug inventoryDrug = inventoryCommonService.getDrugByName(drugName);
			if(inventoryDrug!=null){
			formulationId = Integer.parseInt(request.getParameter(drugName
					+ "_formulationId"));
			quantity = Integer.parseInt(request.getParameter(drugName
					+ "_quantity"));
			avaiableId = Integer.parseInt(request.getParameter(drugName
					+ "_avaiableId"));
			InventoryDrugFormulation inventoryDrugFormulation = inventoryCommonService.getDrugFormulationById(formulationId);
			OpdDrugOrder opdDrugOrder = inventoryService.getOpdDrugOrder(patientId,encounterId,inventoryDrug.getId(),formulationId);
			
			InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
			
			Date date = new Date();
			
			//create transaction issue from substore
			 InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
			 transaction.setDescription("ISSUE DRUG TO PATIENT "+DateUtils.getDDMMYYYY());
			 transaction.setStore(store);
			 transaction.setTypeTransaction(ActionValue.TRANSACTION[1]);
			 transaction.setCreatedOn(date);
			 transaction.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			 transaction = inventoryService.saveStoreDrugTransaction(transaction);
			 
			 
			 InventoryStoreDrugPatientDetail pDetail = new InventoryStoreDrugPatientDetail();
			 InventoryStoreDrugPatient inventoryStoreDrugPatient = new InventoryStoreDrugPatient();
			 
			 inventoryStoreDrugPatient.setStore(store);
			 inventoryStoreDrugPatient.setPatient(patient);
			 inventoryStoreDrugPatient.setName(patient.getGivenName()+""+patient.getMiddleName()+""+patient.getFamilyName());
			 inventoryStoreDrugPatient.setIdentifier(patient.getPatientIdentifier().getIdentifier());
			 inventoryStoreDrugPatient.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			 inventoryStoreDrugPatient.setCreatedOn(date);
			 
			 //inventoryStoreDrugPatientDetail.set
			 
			 inventoryStoreDrugPatient = inventoryService.saveStoreDrugPatient(inventoryStoreDrugPatient);
			 
			 InventoryStoreDrugTransactionDetail inventoryStoreDrugTransactionDetail = inventoryService.getStoreDrugTransactionDetailById(avaiableId);
			 Integer totalQuantity = inventoryService.sumCurrentQuantityDrugOfStore(store.getId(),inventoryDrug.getId(), inventoryDrugFormulation.getId());
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
				transDetail.setUnitPrice(inventoryStoreDrugTransactionDetail.getUnitPrice());
				transDetail.setDrug(inventoryDrug);
				transDetail.setFormulation(inventoryDrugFormulation);
				transDetail.setBatchNo(inventoryStoreDrugTransactionDetail.getBatchNo());
				transDetail.setCompanyName(inventoryStoreDrugTransactionDetail.getCompanyName());
				transDetail.setDateManufacture(inventoryStoreDrugTransactionDetail.getDateManufacture());
				transDetail.setDateExpiry(inventoryStoreDrugTransactionDetail.getDateExpiry());
				transDetail.setReceiptDate(inventoryStoreDrugTransactionDetail.getReceiptDate());
				transDetail.setCreatedOn(date);
				
				BigDecimal moneyUnitPrice = inventoryStoreDrugTransactionDetail.getUnitPrice().multiply(new BigDecimal(quantity));
				moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(inventoryStoreDrugTransactionDetail.getVAT().divide(new BigDecimal(100))));
				transDetail.setTotalPrice(moneyUnitPrice);
				
				transDetail.setParent(inventoryStoreDrugTransactionDetail);
				transDetail = inventoryService.saveStoreDrugTransactionDetail(transDetail);
				
				pDetail.setQuantity(quantity);
				pDetail.setStoreDrugPatient(inventoryStoreDrugPatient);
				pDetail.setTransactionDetail(transDetail);
				//save issue to patient detail
				inventoryService.saveStoreDrugPatientDetail(pDetail);
				PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
				opdDrugOrder.setOrderStatus(1);
				patientDashboardService.saveOrUpdateOpdDrugOrder(opdDrugOrder);
			
			}
		  }
		}
		return "redirect:/module/inventory/patientQueueDrugOrder.form";
	}
}
