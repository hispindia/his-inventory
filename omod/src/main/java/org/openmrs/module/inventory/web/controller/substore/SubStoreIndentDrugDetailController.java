package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("SubStoreIndentDrugDetailController")
@RequestMapping("/module/inventory/subStoreIndentDrugDetail.form")
public class SubStoreIndentDrugDetailController {
	@RequestMapping(method = RequestMethod.GET)
	public String list( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="categoryId",required=false)  Integer categoryId,
            @RequestParam(value="indentName",required=false)  String indentName,
            @RequestParam(value="drugName",required=false)  String drugName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            Map<String, Object> model, HttpServletRequest request
	) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 
	 int total = inventoryService.countStoreDrugIndentDetail(store.getId(), categoryId, indentName, drugName, fromDate, toDate);
	 
	 
	 String temp = "";
		if(categoryId != null){	
				temp = "?categoryId="+categoryId;
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
		if(drugName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?drugName="+drugName;
			}else{
				temp +="&drugName="+drugName;
			}
	}
		if(indentName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?indentName="+indentName;
			}else{
				temp +="&indentName="+indentName;
			}
	}
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		List<InventoryStoreDrugIndentDetail> indents = inventoryService.listStoreDrugIndentDetail(store.getId(), categoryId, indentName, drugName, fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryDrugCategory> listCategory = inventoryService.listDrugCategory("", 0, 0);
		model.put("categoryId", categoryId );
		model.put("drugName", drugName );
		model.put("indentName", indentName );
		model.put("fromDate", fromDate );
		model.put("toDate", toDate );
		model.put("pagingUtil", pagingUtil );
		model.put("indents", indents );
		model.put("listCategory", listCategory );
	 return "/module/inventory/substore/subStoreIndentDrugDetail";
	 
	}
}
