package org.openmrs.module.inventory.web.controller.autocomplete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrug;
import org.openmrs.module.inventory.model.InventoryItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller("AutoCompleteController")
public class AutoCompleteController {
		@RequestMapping("/module/inventory/autoCompleteDrugCoreList.form")
		public String drugCore(@RequestParam(value="term",required=false) String name, Model model) {
			List<Drug> drugs = new ArrayList<Drug>();
			if(!StringUtils.isBlank(name)){
				drugs = Context.getConceptService().getDrugs(name);
			}else{
				drugs = Context.getConceptService().getAllDrugs();
			}
				model.addAttribute("drugs",drugs);
			return "/module/inventory/autocomplete/autoCompleteDrugCoreList";
		}
		@RequestMapping("/module/inventory/autoCompleteDrugList.form")
		public String drug(@RequestParam(value="term",required=false) String name,@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			List<InventoryDrug> drugs = inventoryService.findDrug(categoryId, name);
			model.addAttribute("drugs",drugs);
			return "/module/inventory/autocomplete/autoCompleteDrugList";
		}
		@RequestMapping("/module/inventory/autoCompleteItemList.form")
		public String item(@RequestParam(value="term",required=false) String name,@RequestParam(value="categoryId",required=false) Integer categoryId, Model model) {
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			List<InventoryItem> items = inventoryService.findItem(categoryId, name);
			model.addAttribute("items",items);
			return "/module/inventory/autocomplete/autoCompleteItemList";
		}
		
		@RequestMapping("/module/inventory/checkSession.form")
		public String checkSession(HttpServletRequest request,Model model) {
			 if( Context.getAuthenticatedUser() != null &&  Context.getAuthenticatedUser().getId() != null){
				 model.addAttribute("session","1");
			 }else{
				 model.addAttribute("session","0");
			 }
			
			return "/module/inventory/session/checkSession";
		}

	public static void main(String[] args) {
		Date date = DateUtils.addDays(new Date(), 93);
		System.out.println("date: "+date);
		Date currentDate = new Date();
		Date date3Month = DateUtils.addMonths(currentDate, 3);
		System.out.println(date3Month);
		if(date.before(date3Month)){
			System.out.println("Can alert");
		}else{
			
		}
	}	
	}
