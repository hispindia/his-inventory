package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("IssueDrugAccountListController")
@RequestMapping("/module/inventory/subStoreIssueDrugAccountList.form")
public class IssueDrugAccountListController {
	@RequestMapping(method = RequestMethod.GET)
	public String list( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="issueName",required=false)  String issueName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            Map<String, Object> model, HttpServletRequest request
	) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	//check is it pharmacy ?
	/*if(store != null && store.getParent() != null && store.getIsDrug() == 1){
		return "redirect:/module/inventory/subStoreIssueDrugList.form";
	}*/
	 
	 int total = inventoryService.countStoreDrugAccount(store.getId(), issueName, fromDate, toDate);
	 String temp = "";
		
		if(issueName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?issueName="+issueName;
			}else{
				temp +="&issueName="+issueName;
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
		List<InventoryStoreDrugAccount> listIssue = inventoryService.listStoreDrugAccount(store.getId(), issueName,fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		model.put("issueName", issueName );
		model.put("toDate", toDate );
		model.put("fromDate", fromDate );
		model.put("pagingUtil", pagingUtil );
		model.put("listIssue", listIssue );
		model.put("store", store );
	 return "/module/inventory/substore/subStoreIssueDrugAccountList";
	 
	}
}
