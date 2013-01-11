package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.hospitalcore.util.Action;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreIndentDrugListController")
@RequestMapping("/module/inventory/subStoreIndentDrugList.form")
public class SubStoreIndentDrugListController {
	@RequestMapping(method = RequestMethod.GET)
	public String showList( 
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
		InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		//System.out.println("id main store controller: "+subStore.getId());
		//System.out.println("name main store controller: "+subStore.getName());
		int total = inventoryService.countSubStoreIndent(subStore.getId(), indentName, statusId, fromDate, toDate);
		
		String temp = "";
		if(!StringUtils.isBlank(indentName)){	
				temp = "?indentName="+indentName;
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
		List<InventoryStoreDrugIndent> listIndent = inventoryService.listSubStoreIndent(subStore.getId(), indentName, statusId, fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<Action> listSubStoreStatus = ActionValue.getListIndentSubStore();
		model.addAttribute("listSubStoreStatus", listSubStoreStatus);
		model.addAttribute("listIndent", listIndent);
		model.addAttribute("indentName", indentName);
		model.addAttribute("statusId", statusId);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("pagingUtil", pagingUtil);
		model.addAttribute("store", subStore );
		return "/module/inventory/substore/subStoreIndentDrugList";
	}
}
