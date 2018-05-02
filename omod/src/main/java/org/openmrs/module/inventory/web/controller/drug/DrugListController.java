package org.openmrs.module.inventory.web.controller.drug;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("DrugListController")
@RequestMapping("/module/inventory/drugList.form")
public class DrugListController {
	Log log = LogFactory.getLog(this.getClass());
	@RequestMapping(method=RequestMethod.POST)
    public String delete(@RequestParam("ids") String[] ids,HttpServletRequest request){
		String temp = "";
    	HttpSession httpSession = request.getSession();
		try{
			InventoryService inventoryService = (InventoryService)Context.getService(InventoryService.class);
			if( ids != null && ids.length > 0 ){
				for(String sId : ids )
				{
					InventoryDrug drug = inventoryService.getDrugById( NumberUtils.toInt(sId));
					int  countDrugInTransactionDetail = inventoryService.checkExistDrugTransactionDetail(drug.getId());
					int  countDrugInIndentDetail = inventoryService.checkExistDrugIndentDetail(drug.getId());
					if( drug!= null && countDrugInTransactionDetail == 0 && countDrugInIndentDetail == 0)
					{
						inventoryService.deleteDrug(drug);
					}else{
						//temp += "We can't delete drug="+drug.getName()+" because that drug is using please check <br/>";
						temp = "This drug/drugs cannot be deleted as it is in use";
					}
				}
			}
		}catch (Exception e) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			"Can not delete drug ");
			log.error(e);
		}
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,StringUtils.isBlank(temp) ?  "drug.deleted" : temp);
    	
    	return "redirect:/module/inventory/drugList.form";
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public String list( @RequestParam(value="searchName",required=false)  String searchName, 
							 @RequestParam(value="categoryId",required=false)  Integer categoryId, 
							 @RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                         @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                         Map<String, Object> model, HttpServletRequest request){
		
		InventoryService inventoryService = Context.getService(InventoryService.class);
		
		int total = inventoryService.countListDrug(categoryId ,searchName);
		String temp = "";
		if(!StringUtils.isBlank(searchName)){	
				temp = "?searchName="+searchName;
		}
		if(categoryId != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?categoryId="+categoryId;
			}else{
				temp +="&categoryId="+categoryId;
			}
	}
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		
		List<InventoryDrug> drugs = inventoryService.listDrug(categoryId , searchName, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryDrugCategory> categories = inventoryService.listDrugCategory("", 0, 0);
		model.put("drugs", drugs );
		model.put("categories", categories );
		model.put("categoryId", categoryId );
		model.put("searchName", searchName);
		model.put("pagingUtil", pagingUtil);
		
		return "/module/inventory/drug/drugList";
	}
}
