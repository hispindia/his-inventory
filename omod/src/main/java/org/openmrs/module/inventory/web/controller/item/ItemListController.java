package org.openmrs.module.inventory.web.controller.item;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("ItemListController")
@RequestMapping("/module/inventory/itemList.form")
public class ItemListController {
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
					InventoryItem item = inventoryService.getItemById( NumberUtils.toInt(sId));
					//ghanshyam 7-august-2013 code review bug
					if( item!= null ){
					int  countItemInTransactionDetail = inventoryService.checkExistItemTransactionDetail(item.getId());
					int  countItemInIndentDetail = inventoryService.checkExistItemIndentDetail(item.getId());
				
					if( countItemInIndentDetail == 0 && countItemInTransactionDetail == 0 )
					{
						inventoryService.deleteItem(item);
					}else{
						//temp += "We can't delete item="+item.getName()+" because that item is using please check <br/>";
						temp = "This item/items cannot be deleted as it is in use";
					}
				  }	
				}
			}
		}catch (Exception e) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Can not delete item ");
			log.error(e);
		}
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, StringUtils.isBlank(temp) ?  "item.deleted" : temp);
    	
    	return "redirect:/module/inventory/itemList.form";
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public String list( @RequestParam(value="searchName",required=false)  String searchName, 
							@RequestParam(value="categoryId",required=false)  Integer categoryId, 
							 @RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                         @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                         Map<String, Object> model, HttpServletRequest request){
		
		InventoryService inventoryService = Context.getService(InventoryService.class);
		
		int total = inventoryService.countListItem(categoryId,searchName);
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
		
		List<InventoryItem> items = inventoryService.listItem(categoryId,searchName, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		List<InventoryItemSubCategory> categories = inventoryService.listItemSubCategory("", 0, 0);
		model.put("items", items );
		model.put("categories", categories );
		model.put("categoryId", categoryId );
		model.put("searchName", searchName);
		model.put("pagingUtil", pagingUtil);
		
		return "/module/inventory/item/itemList";
	}
}
