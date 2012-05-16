package org.openmrs.module.inventory.web.controller.mainstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransaction;
import org.openmrs.module.inventory.util.ActionValue;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ReceiptToGeneralStoreListController")
@RequestMapping("/module/inventory/receiptsToGeneralStoreList.form")
public class ReceiptToGeneralStoreListController {
	@RequestMapping(method = RequestMethod.GET)
	public String list( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="receiptName",required=false)  String receiptName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            Map<String, Object> model, HttpServletRequest request
	) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 
	 int total = inventoryService.countStoreDrugTransaction(ActionValue.TRANSACTION[0], store.getId(), receiptName, fromDate, toDate);
	 String temp = "";
	 if(receiptName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?receiptName="+receiptName;
			}else{
				temp +="&receiptName="+receiptName;
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
		List<InventoryStoreDrugTransaction> transactions = inventoryService.listStoreDrugTransaction(ActionValue.TRANSACTION[0], store.getId(), receiptName, fromDate, toDate,pagingUtil.getStartPos(), pagingUtil.getPageSize());
		model.put("receiptName", receiptName );
		model.put("fromDate", fromDate );
		model.put("toDate", toDate );
		model.put("pagingUtil", pagingUtil );
		model.put("transactions", transactions );
	 return "/module/inventory/mainstore/receiptsToGeneralStoreList";
	 
	}
}
