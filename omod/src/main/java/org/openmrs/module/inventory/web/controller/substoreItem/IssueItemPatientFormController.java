package org.openmrs.module.inventory.web.controller.substoreItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.model.InventoryStoreItemPatient;
import org.openmrs.module.inventory.model.InventoryStoreItemPatientDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("IssueItemPatientFormController")
@RequestMapping("/module/inventory/subStoreIssueItemPatientForm.form")
public class IssueItemPatientFormController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
		
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 if(categoryId != null && categoryId > 0){
		 List<InventoryItem> items = inventoryService.findItem(categoryId, null);
		 model.addAttribute("items",items);
			
	 }
	 model.addAttribute("date",new Date());
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "issueItemDetailPatient_"+userId;
	 List<InventoryStoreItemPatientDetail> list = (List<InventoryStoreItemPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 InventoryStoreItemPatient issueItemPatient = (InventoryStoreItemPatient )StoreSingleton.getInstance().getHash().get("issueItemPatient_"+userId);
	 
	if (issueItemPatient != null) {
		PatientService ps = (PatientService) Context.getService(PatientService.class);
		model.addAttribute("patientCategory",
		    ps.getPatient(issueItemPatient.getPatient().getId()).getAttribute(PatientUtils.PATIENT_ATTRIBUTE_CATEGORY)
		            .getValue());
		
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		String patientType = hcs.getPatientType(issueItemPatient.getPatient());
		model.addAttribute("patientType", patientType);
		
		List<PersonAttribute> pas = hcs.getPersonAttributes(issueItemPatient.getPatient().getId());
        for (PersonAttribute pa : pas) {
            PersonAttributeType attributeType = pa.getAttributeType(); 
            PersonAttributeType personAttributePCT=hcs.getPersonAttributeTypeByName("Paying Category Type");
            PersonAttributeType personAttributeNPCT=hcs.getPersonAttributeTypeByName("Non-Paying Category Type");
            PersonAttributeType personAttributeSSCT=hcs.getPersonAttributeTypeByName("Special Scheme Category Type");
            if(attributeType.getPersonAttributeTypeId()==personAttributePCT.getPersonAttributeTypeId()){
            	model.addAttribute("paymentSubCategory",pa.getValue()); 
            }
            else if(attributeType.getPersonAttributeTypeId()==personAttributeNPCT.getPersonAttributeTypeId()){
            	 model.addAttribute("paymentSubCategory",pa.getValue()); 
            }
            else if(attributeType.getPersonAttributeTypeId()==personAttributeSSCT.getPersonAttributeTypeId()){
            	model.addAttribute("paymentSubCategory",pa.getValue()); 
            }
        }
		
		
	}
	 model.addAttribute("listItemDetail", list);
	 model.addAttribute("issueItemPatient", issueItemPatient);
	 return "/module/inventory/substoreItem/subStoreIssueItemPatientForm";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
		 model.addAttribute("listCategory", listCategory);
		int category = NumberUtils.toInt(request.getParameter("category"),0);
		Integer specification = NumberUtils.toInt(request.getParameter("specification"),0);
		Integer itemId = NumberUtils.toInt(request.getParameter("itemId"), 0);
		int userId = Context.getAuthenticatedUser().getId();
		Integer issueItemQuantity = NumberUtils.toInt(request.getParameter("issueItemQuantity"),0);
		InventoryItem item = inventoryService.getItemById(itemId);
		
		if(item == null || (item.getSpecifications() != null && item.getSpecifications().size() > 0 && specification <=0)){
			errors.add("inventory.issueItem.quantity.required");
			model.addAttribute("errors", errors);
			model.addAttribute("category", category);
			String fowardParam = "issueItemDetailPatient_"+userId;
			List<InventoryStoreItemPatientDetail> list = (List<InventoryStoreItemPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			 InventoryStoreItemAccount issueItemAccount = (InventoryStoreItemAccount )StoreSingleton.getInstance().getHash().get("issueItem_"+userId);
			 model.addAttribute("issueItemAccount", issueItemAccount);
			model.addAttribute("listPatientDetail", list);
			return "/module/inventory/substoreItem/subStoreIssueItemPatientForm";
		}
		
		
		InventoryStore store =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		
		Integer sumCurrentOfItem = inventoryService.sumStoreItemCurrentQuantity(store.getId(), item.getId(), specification);
		if(sumCurrentOfItem == 0 || issueItemQuantity <= 0 ){
			errors.add("inventory.issueItem.quantity.required");
		}
		if(sumCurrentOfItem < issueItemQuantity){
			errors.add("inventory.issueItem.quantity.lessthanQuantity.required");
		}
		
		if(errors != null && errors.size() > 0){
			String fowardParam = "issueItemDetailPatient_"+userId;
			List<InventoryStoreItemPatientDetail> list = (List<InventoryStoreItemPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
			 InventoryStoreItemAccount issueItemAccount = (InventoryStoreItemAccount )StoreSingleton.getInstance().getHash().get("issueItem_"+userId);
			 model.addAttribute("issueItemAccount", issueItemAccount);
			model.addAttribute("listAccountDetail", list);
			model.addAttribute("category", category);
			model.addAttribute("specification", specification);
			model.addAttribute("issueItemQuantity", issueItemQuantity);
			model.addAttribute("itemId", itemId);
			model.addAttribute("errors", errors);
			return "/module/inventory/substoreItem/subStoreIssueItemPatientForm";
		}
		
		
		String fowardParam = "issueItemDetailPatient_"+userId;
		List<InventoryStoreItemPatientDetail> list = (List<InventoryStoreItemPatientDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		
		List<InventoryStoreItemTransactionDetail> listReceiptItem = inventoryService.listStoreItemTransactionDetail(store.getId(), item.getId(), specification, true);
		if(list == null){
			list = new ArrayList<InventoryStoreItemPatientDetail>();
		}
		List<InventoryStoreItemPatientDetail> listExt = new ArrayList<InventoryStoreItemPatientDetail>(list); 
		if(CollectionUtils.isNotEmpty(list)){
			for(InventoryStoreItemPatientDetail tDetail : list){
				if(tDetail.getTransactionDetail().getItem().getId().equals(item.getId()) ){
					if(tDetail.getTransactionDetail().getSpecification() != null && specification != null  ){
						if(tDetail.getTransactionDetail().getSpecification().getId().equals(specification)){
							issueItemQuantity += tDetail.getQuantity();
							listExt.remove(tDetail);
						}
					}else{
						if(tDetail.getTransactionDetail().getSpecification() == null ){
							issueItemQuantity += tDetail.getQuantity();
							listExt.remove(tDetail);
						}
					}
				}
			}
		}
		
		
		
		for(InventoryStoreItemTransactionDetail t: listReceiptItem){
			InventoryStoreItemPatientDetail issueItemDetail = new InventoryStoreItemPatientDetail();
			//ghanshyam 7-august-2013 code review bug
			if(t.getItem().getId().equals(item.getId())){
				if(t.getCurrentQuantity() >= issueItemQuantity){
					issueItemDetail.setTransactionDetail(t);
					issueItemDetail.setQuantity(issueItemQuantity);
					listExt.add(issueItemDetail);
					break;
				}else{
					issueItemDetail.setTransactionDetail(t);
					issueItemDetail.setQuantity(t.getCurrentQuantity());
					issueItemQuantity -= t.getCurrentQuantity();
					listExt.add(issueItemDetail);
					
				}
				
			}
		}
		StoreSingleton.getInstance().getHash().put(fowardParam, listExt);
		InventoryStoreItemPatientDetail issueItemAccount = (InventoryStoreItemPatientDetail )StoreSingleton.getInstance().getHash().get("issueItem_"+userId);
		// model.addAttribute("issueItemAccount", issueItemAccount);
		//model.addAttribute("listAccountDetail", list);
	 return "redirect:/module/inventory/subStoreIssueItemPatientForm.form";
	}
}
