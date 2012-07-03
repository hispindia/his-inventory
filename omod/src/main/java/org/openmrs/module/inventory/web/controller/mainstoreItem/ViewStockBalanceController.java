package org.openmrs.module.inventory.web.controller.mainstoreItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("itemViewStockBalanceController")
@RequestMapping("/module/inventory/itemViewStockBalance.form")
public class ViewStockBalanceController {
	@RequestMapping(method = RequestMethod.GET)
	public String list( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="categoryId",required=false)  Integer categoryId,
            @RequestParam(value="itemName",required=false)  String itemName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            Map<String, Object> model, HttpServletRequest request
	) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 
	 int total = inventoryService.countStoreItemViewStockBalance(store.getId(), categoryId, itemName,  fromDate, toDate);
	 String temp = "";
		if(categoryId != null){	
				temp = "?categoryId="+categoryId;
		}
		
		if(itemName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?itemName="+itemName;
			}else{
				temp +="&itemName="+itemName;
			}
	}
		if(fromDate != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?fromDate="+fromDate;
			}else{
				temp +="&fromDate="+fromDate;
			}
	}
		if(toDate != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?toDate="+toDate;
			}else{
				temp +="&toDate="+toDate;
			}
	}
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		List<InventoryStoreItemTransactionDetail> stockBalances = inventoryService.listStoreItemViewStockBalance(store.getId(), categoryId, itemName,  fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
		//03/07/2012: Kesavulu:sort Item Names  #300 
		Collections.sort(stockBalances);
		model.put("categoryId", categoryId );
		model.put("itemName", itemName );
		model.put("fromDate", fromDate );
		model.put("toDate", toDate );
		model.put("pagingUtil", pagingUtil );
		model.put("stockBalances", stockBalances );
		model.put("listCategory", listCategory );
	 return "/module/inventory/mainstoreItem/itemViewStockBalance";
	 
	}
}
