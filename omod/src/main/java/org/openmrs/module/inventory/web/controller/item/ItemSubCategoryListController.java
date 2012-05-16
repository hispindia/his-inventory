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
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.util.PagingUtil;
import org.openmrs.module.inventory.util.RequestUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("ItemSubCategoryListController")
@RequestMapping("/module/inventory/itemSubCategoryList.form")
public class ItemSubCategoryListController {
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
						InventoryItemSubCategory itemSubCategory = inventoryService.getItemSubCategoryById( NumberUtils.toInt(sId));
						int countItem = inventoryService.countItem(null,  null, itemSubCategory.getId(), null);
						if( itemSubCategory!= null && countItem == 0 )
						{
							inventoryService.deleteItemSubCategory(itemSubCategory);
						}else{
							//temp += "We can't delete subCategory="+itemSubCategory.getName()+" because that subCategory is using please check <br/>";
							temp = "This sub-category/sub-categories cannot be deleted as it is in use";
						}
					}
				}
			}catch (Exception e) {
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
				"Can not delete itemSubCategory ");
				log.error(e);
			}
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,StringUtils.isBlank(temp) ?  "itemSubCategory.deleted" : temp
			);
	    	
	    	return "redirect:/module/inventory/itemSubCategoryList.form";
	    }
		
		@RequestMapping(method=RequestMethod.GET)
		public String list( @RequestParam(value="searchName",required=false)  String searchName, 
								 @RequestParam(value="pageSize",required=false)  Integer pageSize, 
		                         @RequestParam(value="currentPage",required=false)  Integer currentPage,
		                         Map<String, Object> model, HttpServletRequest request){
			
			InventoryService inventoryService = Context.getService(InventoryService.class);
			
			int total = inventoryService.countListItemSubCategory(searchName);
			String temp = "";
			if(!StringUtils.isBlank(searchName)){	
					temp = "?searchName="+searchName;
			}
			PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
			
			List<InventoryItemSubCategory> itemSubCategorys = inventoryService.listItemSubCategory(searchName, pagingUtil.getStartPos(), pagingUtil.getPageSize());
			
			model.put("itemSubCategorys", itemSubCategorys );
			model.put("searchName", searchName);
			model.put("pagingUtil", pagingUtil);
			
			return "/module/inventory/item/itemSubCategoryList";
		}
}
