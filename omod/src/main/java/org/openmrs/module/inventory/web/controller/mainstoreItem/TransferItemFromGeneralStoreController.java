package org.openmrs.module.inventory.web.controller.mainstoreItem;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.util.Action;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("TransferItemFromGeneralStoreController")
@RequestMapping("/module/inventory/transferItemFromGeneralStore.form")
public class TransferItemFromGeneralStoreController {
	@RequestMapping(method = RequestMethod.GET)
	public String showAttributeList( 
			@RequestParam(value="indentId",required=false)  Integer id,
			@RequestParam(value="storeId",required=false)  Integer storeId,
			@RequestParam(value="statusId",required=false)  Integer statusId,
			@RequestParam(value="indentName",required=false)  String indentName,
			 @RequestParam(value="fromDate",required=false)  String fromDate,
             @RequestParam(value="toDate",required=false)  String toDate,
			@RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
			Model model,
			HttpServletRequest request
			) {
		InventoryService inventoryService = Context.getService(InventoryService.class);
		InventoryStore mainStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		//System.out.println("id main store controller: "+mainStore.getId());
		//System.out.println("name main store controller: "+mainStore.getName());
		int total = inventoryService.countMainStoreItemIndent(id, mainStore.getId(), storeId, indentName, statusId, fromDate, toDate);
		
		String temp = "";
		if(!StringUtils.isBlank(indentName)){	
				temp = "?indentName="+indentName;
		}
		
		if(storeId != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?storeId="+storeId;
			}else{
				temp +="&storeId="+storeId;
			}
		}
		if(statusId != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?statusId="+statusId;
			}else{
				temp +="&statusId="+statusId;
			}
		}
		if(!StringUtils.isBlank(fromDate)){	
			if(StringUtils.isBlank(temp)){
				temp = "?fromDate="+fromDate;
			}else{
				temp +="&fromDate="+fromDate;
			}
	}
		if(!StringUtils.isBlank(toDate)){	
			if(StringUtils.isBlank(temp)){
				temp = "?toDate="+toDate;
			}else{
				temp +="&toDate="+toDate;
			}
	}
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		List<InventoryStoreItemIndent> listIndent = inventoryService.listMainStoreItemIndent(id ,mainStore.getId(), storeId, indentName, statusId, fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryStore> listStore = inventoryService.listStoreByMainStore(mainStore.getId(),false);
		List<Action> listMainStoreStatus = ActionValue.getListIndentMainStore();
		model.addAttribute("listMainStoreStatus", listMainStoreStatus);
		model.addAttribute("listIndent", listIndent);
		model.addAttribute("listStore", listStore);
		model.addAttribute("indentName", indentName);
		model.addAttribute("statusId", statusId);
		model.addAttribute("storeId", storeId);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("pagingUtil", pagingUtil);
		
		return "/module/inventory/mainstoreItem/transferItemFromGeneralStore";
	}

}
