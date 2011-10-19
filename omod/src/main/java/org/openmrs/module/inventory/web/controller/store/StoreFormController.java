package org.openmrs.module.inventory.web.controller.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryConstants;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.util.Action;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.web.controller.property.editor.RolePropertyEditor;
import org.openmrs.module.inventory.web.controller.property.editor.StorePropertyEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
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

@Controller("storeFormController")
@RequestMapping("/module/inventory/store.form")
public class StoreFormController {
	 Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("store") InventoryStore store, @RequestParam(value="storeId",required=false) Integer id, Model model) {
		if( id != null ){
			store = Context.getService(InventoryService.class).getStoreById(id);
			model.addAttribute("store",store);
		}
		List<Action> listIsDrug = ActionValue.getListIsDrug();
		model.addAttribute("listIsDrug",listIsDrug);
		return "/module/inventory/store/form";
	}
	
	@ModelAttribute("roles")
	public List<Role> populateRoles(HttpServletRequest request) {
 
	
		//List<Role> listRole = Context.getUserService().getAllRoles();
		
		//return listRole;
		
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		String storeId = request.getParameter("storeId");
		String role = "";
		InventoryStore store = null;
		if( storeId != null )
		{
			store = inventoryService.getStoreById(NumberUtils.toInt(storeId));
			if(store != null){
				role = store.getRole() != null ? store.getRole().getRole() : "";
			}
			
		}
		List<Role> roles = Context.getUserService().getAllRoles();
		List<Role> listRole = new ArrayList<Role>();
		listRole.addAll(roles);
	    List<InventoryStore> stores = inventoryService.listAllInventoryStore();
	    //System.out.println("stores: "+stores);
	    //System.out.println("users: "+users);
	    if(!CollectionUtils.isEmpty(roles) && !CollectionUtils.isEmpty(stores)){
		    for( Role roleX : roles ){
		    	for( InventoryStore s : stores )	{
		    		if( s.getRole() != null && s.getRole().getRole().equals(roleX.getRole()) && !roleX.getRole().equals(role)){
		    			listRole.remove(roleX);
		    		}
		    	}
		    }
	    }
		return listRole;
		
		
		
	}
	
	@ModelAttribute("parents")
	public List<InventoryStore> populateParents(HttpServletRequest request) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	    List<InventoryStore> stores = inventoryService.listAllInventoryStore();
	    List<InventoryStore> listParents = new ArrayList<InventoryStore>();
	    listParents.addAll(stores);
	    //System.out.println("parents: "+stores);
	    for( InventoryStore storeV : stores ){
	    	if(storeV.getParent() != null){
	    		listParents.remove(storeV);
	    	}
	    
	    }
		return listParents;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.lang.Integer.class,new CustomNumberEditor(java.lang.Integer.class, true));
		//binder.registerCustomEditor(java.util.List.class,new CustomCollectionEditor(java.util.List.class, true));
		binder.registerCustomEditor(InventoryStore.class, new StorePropertyEditor());
		binder.registerCustomEditor(Role.class, new RolePropertyEditor());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("store") InventoryStore store, BindingResult bindingResult, HttpServletRequest request, SessionStatus status) {
		new StoreValidator().validate(store, bindingResult);
		//storeValidator.validate(store, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "/module/inventory/store/form";
			
		}else{
			InventoryService inventoryService = (InventoryService) Context
			.getService(InventoryService.class);
			//save store
			store.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			store.setCreatedOn(new Date());
			inventoryService.saveStore(store);
			
			status.setComplete();
			return "redirect:/module/inventory/storeList.form";
		}
	}
	
}
