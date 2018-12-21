package org.openmrs.module.inventory.web.controller.drug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.concept.ConceptNode;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryDrugUnit;
import org.openmrs.module.hospitalcore.util.Action;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.InventoryDrugFormulationComparator;
import org.openmrs.module.inventory.web.controller.property.editor.DrugCategoryPropertyEditor;
import org.openmrs.module.inventory.web.controller.property.editor.DrugCorePropertyEditor;
import org.openmrs.module.inventory.web.controller.property.editor.DrugUnitPropertyEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller("DrugFormController")
@RequestMapping("/module/inventory/drug.form")
public class DrugFormController {
Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("drug") InventoryDrug drug, @RequestParam(value="drugId",required=false) Integer id, Model model) {
		if( id != null ){
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			int  countDrugInTransactionDetail = inventoryService.checkExistDrugTransactionDetail(id);
			int  countDrugInIndentDetail = inventoryService.checkExistDrugIndentDetail(id);
			
			if(countDrugInIndentDetail > 0 || countDrugInTransactionDetail >0){
				model.addAttribute("add", "You can edit this drug");
			}
			
			drug = Context.getService(InventoryService.class).getDrugById(id);
			model.addAttribute("drug",drug);
		}
		return "/module/inventory/drug/drug";
	}
	@ModelAttribute("categories")
	public List<InventoryDrugCategory> populateCategories() {
 
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrugCategory> categories = inventoryService.findDrugCategory("");
		return categories;
	}
	@ModelAttribute("formulations")
	public List<InventoryDrugFormulation> populateFormulation() {
 
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrugFormulation> formulations = inventoryService.findDrugFormulation("");
		List<InventoryDrugFormulation> form = new ArrayList(formulations);
		
	
		Collections.sort(form, new InventoryDrugFormulationComparator());
		return form;
	}
	@ModelAttribute("units")
	public List<InventoryDrugUnit> populateUnit() {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		List<InventoryDrugUnit> units = inventoryService.findDrugUnit("");
		return units;
	}
	@ModelAttribute("attributes")
	public List<Action> populateAttribute() {
		List<Action> attributes = ActionValue.getListDrugAttribute();
		return attributes;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Drug.class, new DrugCorePropertyEditor());
		binder.registerCustomEditor(InventoryDrugCategory.class, new DrugCategoryPropertyEditor());
		binder.registerCustomEditor(InventoryDrugUnit.class, new DrugUnitPropertyEditor());
		binder.registerCustomEditor(Set.class, "formulations",new CustomCollectionEditor(Set.class){
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			protected Object convertElement(Object element)
			    {
				  Integer formulationId = null;
			      if (element instanceof Integer)
			    	  formulationId = (Integer) element;
			      else if (element instanceof String)
			    	  formulationId = NumberUtils.toInt((String) element , 0);
			      return formulationId != null ? inventoryService.getDrugFormulationById(formulationId) : null;
			    }
		});
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("drug") InventoryDrug drug, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new DrugValidator().validate(drug, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/inventory/drug/drug";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			drug.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			drug.setCreatedOn(new Date());
			inventoryService.saveDrug(drug);
			status.setComplete();
			return "redirect:/module/inventory/drugList.form";
		}
	}
}
