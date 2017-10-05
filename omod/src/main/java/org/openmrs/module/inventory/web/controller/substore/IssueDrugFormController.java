/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.inventory.web.controller.substore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Concept;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("IssueDrugFormController")
@RequestMapping("/module/inventory/subStoreIssueDrugForm.form")
public class IssueDrugFormController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model,
			HttpServletRequest request) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		//InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		/*if(store != null && store.getParent() != null && store.getIsDrug() != 1){
			return "redirect:/module/inventory/subStoreIssueDrugAccountForm.form";
		}*/
		
		List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("categoryId", categoryId);
		
		/**
		 * @support feature#174
		 * @author Thai Chuong
		 * @date <dd/mm/yyyy>08/05/2012
		 * @support feature #174 - 'Testing Result Git Punjab' project
		 */
		if (categoryId != null && categoryId > 0) {
			List<InventoryDrug> drugs = inventoryService.findDrug(categoryId, null);
			model.addAttribute("drugs", drugs);
			
		} else {
			List<InventoryDrug> drugs = inventoryService.getAllDrug();
			model.addAttribute("drugs", drugs);
		}
		
		model.addAttribute("date", new Date());
		
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "issueDrugDetail_" + userId;
		List<InventoryStoreDrugPatientDetail> list = (List<InventoryStoreDrugPatientDetail>) StoreSingleton.getInstance()
		        .getHash().get(fowardParam);
		InventoryStoreDrugPatient issueDrugPatient = (InventoryStoreDrugPatient) StoreSingleton.getInstance().getHash()
		        .get("issueDrug_" + userId);
		model.addAttribute("listPatientDetail", list);
		model.addAttribute("issueDrugPatient", issueDrugPatient);
		String waiverPercentage=request.getParameter("waiverPercentage");
		Float totalValu=Float.parseFloat("0");
		Float totalAmountPy=Float.parseFloat("0");
		if(list!=null){
		for(InventoryStoreDrugPatientDetail lst:list){
			Float unitPrice=lst.getTransactionDetail().getUnitPrice().floatValue();
			Integer quantity=lst.getQuantity();
			totalValu=totalValu+quantity*unitPrice;
		}
		Float waiverPercentge=Float.parseFloat("0");
		if(waiverPercentage!=null){
		waiverPercentge=Float.parseFloat(waiverPercentage);
		}
		totalAmountPy=totalValu-(totalValu*waiverPercentge)/100;	
		}
		model.addAttribute("total", totalValu);
		model.addAttribute("totalAmountPayable", Math.round(totalAmountPy));
		if(waiverPercentage!=null){
			model.addAttribute("waiverPercentage", Float.parseFloat(waiverPercentage));	
		}
		else{
			model.addAttribute("waiverPercentage", Float.parseFloat("0"));
		}
		
		if(issueDrugPatient!=null){
		Integer patientCategoryConcept=Integer.parseInt(issueDrugPatient.getPatientCategory());
		Concept concept=Context.getConceptService().getConcept(patientCategoryConcept);
		model.addAttribute("patientCategory", concept.getName());
		}
	
		return "/module/inventory/substore/subStoreIssueDrugForm";
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		int drugId=0;
		String drugN="",drugIdStr="";
		InventoryDrug drug=null;
		
		int userId = Context.getAuthenticatedUser().getId();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		model.addAttribute("listCategory", listCategory);
		int category = NumberUtils.toInt(request.getParameter("category"), 0);
		Integer formulation = NumberUtils.toInt(request.getParameter("formulation"), 0);
		
		if (request.getParameter("drugName")!=null)
			drugN=request.getParameter("drugName");
			if (request.getParameter("drugId")!=null)
		 drugIdStr=request.getParameter("drugId");
			
			if (!drugN.equalsIgnoreCase("")){
				
				 drug=inventoryService.getDrugByName(drugN);
			}else if (!drugIdStr.equalsIgnoreCase("")){
				drugId=Integer.parseInt(drugIdStr);
				 drug=inventoryService.getDrugById(drugId);
			}
			
			if(drug == null){
				errors.add("inventory.receiptDrug.drug.required");
			}else{
			 drugId = drug.getId();
			}
		
		InventoryDrugFormulation formulationO = inventoryService.getDrugFormulationById(formulation);
		if (formulationO == null) {
			errors.add("inventory.receiptDrug.formulation.required");
		}
		if (formulationO != null && drug != null && !drug.getFormulations().contains(formulationO)) {
			errors.add("inventory.receiptDrug.formulation.notCorrect");
		}
		if (CollectionUtils.isNotEmpty(errors)) {
			
			model.addAttribute("category", category);
			model.addAttribute("errors", errors);
			String fowardParam = "issueDrugDetail_" + userId;
			List<InventoryStoreDrugPatientDetail> list = (List<InventoryStoreDrugPatientDetail>) StoreSingleton
			        .getInstance().getHash().get(fowardParam);
			StoreSingleton.getInstance().getHash().put(fowardParam, list);
			InventoryStoreDrugPatient issueDrugPatient = (InventoryStoreDrugPatient) StoreSingleton.getInstance().getHash()
			        .get("issueDrug_" + userId);
			model.addAttribute("issueDrugPatient", issueDrugPatient);
			model.addAttribute("listPatientDetail", list);
			return "/module/inventory/substore/subStoreIssueDrugForm";
		}
		
		InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser()
		        .getAllRoles()));
		List<Integer> listIssueQty = new ArrayList<Integer>();
		List<InventoryStoreDrugTransactionDetail> listReceiptDrug = inventoryService.listStoreDrugTransactionDetail(
		    store.getId(), drug.getId(), formulation, true);
		boolean checkCorrect = true;
		if (listReceiptDrug != null) {
			model.addAttribute("listReceiptDrug", listReceiptDrug);
			for (InventoryStoreDrugTransactionDetail t : listReceiptDrug) {
				
				Integer temp = NumberUtils.toInt(request.getParameter(t.getId() + ""), 0);
				
				if (temp > 0) {
					checkCorrect = false;
				} else {
					temp = 0;
				}
				listIssueQty.add(temp);
				if (temp > t.getCurrentQuantity()) {
					errors.add("inventory.issueDrug.quantity.lessthanQuantity.required");
				}
			}
		} else {
			errors.add("inventory.issueDrug.drug.required");
		}
		if (checkCorrect) {
			errors.add("inventory.issueDrug.quantity.required");
		}
		if (errors != null && errors.size() > 0) {
			
			model.addAttribute("category", category);
			model.addAttribute("formulation", formulation);
			model.addAttribute("listIssueQty", listIssueQty);
			model.addAttribute("drugId", drugId);
			model.addAttribute("errors", errors);
			String fowardParam = "issueDrugDetail_" + userId;
			List<InventoryStoreDrugPatientDetail> list = (List<InventoryStoreDrugPatientDetail>) StoreSingleton
			        .getInstance().getHash().get(fowardParam);
			StoreSingleton.getInstance().getHash().put(fowardParam, list);
			InventoryStoreDrugPatient issueDrugPatient = (InventoryStoreDrugPatient) StoreSingleton.getInstance().getHash()
			        .get("issueDrug_" + userId);
			model.addAttribute("issueDrugPatient", issueDrugPatient);
			model.addAttribute("listPatientDetail", list);
			return "/module/inventory/substore/subStoreIssueDrugForm";
		}
		
		String fowardParam = "issueDrugDetail_" + userId;
		List<InventoryStoreDrugPatientDetail> list = (List<InventoryStoreDrugPatientDetail>) StoreSingleton.getInstance()
		        .getHash().get(fowardParam);
		List<InventoryStoreDrugPatientDetail> listExt = null;
		if (list == null) {
			listExt = list = new ArrayList<InventoryStoreDrugPatientDetail>();
		} else {
			listExt = new ArrayList<InventoryStoreDrugPatientDetail>(list);
		}
		for (InventoryStoreDrugTransactionDetail t : listReceiptDrug) {
			Integer temp = NumberUtils.toInt(request.getParameter(t.getId() + ""), 0);
			if (temp > 0) {
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						InventoryStoreDrugPatientDetail dtail = list.get(i);
						if (t.getId().equals(dtail.getTransactionDetail().getId())) {
							listExt.remove(i);
							temp += dtail.getQuantity();
							break;
						}
					}
				}
			
				InventoryStoreDrugPatientDetail issueDrugDetail = new InventoryStoreDrugPatientDetail();
				issueDrugDetail.setTransactionDetail(t);
				issueDrugDetail.setQuantity(temp);
				listExt.add(issueDrugDetail);
			}
		}
		StoreSingleton.getInstance().getHash().put(fowardParam, listExt);
		InventoryStoreDrugPatient issueDrugPatient = (InventoryStoreDrugPatient) StoreSingleton.getInstance().getHash()
		        .get("issueDrug_" + userId);
		model.addAttribute("issueDrugPatient", issueDrugPatient);
		//model.addAttribute("listPatientDetail", list);
		String waiverPercentage=request.getParameter("waiverPercentage");
		Float waiverPercentge=Float.parseFloat(waiverPercentage);
		return "redirect:/module/inventory/subStoreIssueDrugForm.form?waiverPercentage=" + waiverPercentge;
	}
}
