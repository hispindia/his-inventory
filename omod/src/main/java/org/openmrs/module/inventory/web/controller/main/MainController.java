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
package org.openmrs.module.inventory.web.controller.main;

import java.util.ArrayList;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *
 */
@Controller("mainInventoryController")
@RequestMapping("/module/inventory/main.form")
public class MainController {

	 @RequestMapping(method = RequestMethod.GET)
		public String firstView( Model model) {
		 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 try {
			 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
			 if(store != null){
				 if( store.getParent() == null && !store.getRetired()){
					 return "redirect:/module/inventory/viewStockBalance.form";
				 }else if( store.getParent() != null && store.getIsDrug() == 1 && !store.getRetired() ){
					 return "redirect:/module/inventory/subStoreIssueDrugList.form";
				 }else if( store.getParent() != null && store.getIsDrug() == 2 && !store.getRetired() ){
					 return "redirect:/module/inventory/itemViewStockBalanceSubStore.form";
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return "";
	}
}
