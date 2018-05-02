package org.openmrs.module.inventory.web.controller.mainstoreItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;
import org.openmrs.module.inventory.util.DateUtils;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("itemReceiptFormController")
@RequestMapping("/module/inventory/itemReceiptsToGeneralStore.form")
public class ReceiptFormController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 if(categoryId != null && categoryId > 0){
		 List<InventoryItem> items = inventoryService.findItem(categoryId, null);
		 model.addAttribute("items",items);
			
	 }
	 model.addAttribute("date",new Date());
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 model.addAttribute("store",store);
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "itemReceipt_"+userId;
	 List<InventoryStoreItemTransactionDetail> list = (List<InventoryStoreItemTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 model.addAttribute("listReceipt", list);
	 
	 return "/module/inventory/mainstoreItem/itemReceiptsToGeneralStore";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryItemSubCategory> listCategory = inventoryService.listItemSubCategory("", 0, 0);
		 model.addAttribute("listCategory", listCategory);
		int category = NumberUtils.toInt(request.getParameter("category"),0);
		int specification = NumberUtils.toInt(request.getParameter("specification"),0);
		int itemId = NumberUtils.toInt(request.getParameter("itemId"), 0 );
		int quantity = NumberUtils.toInt(request.getParameter("quantity"),0);
		BigDecimal VAT = NumberUtils.createBigDecimal(request.getParameter("VAT"));
		BigDecimal unitPrice =  NumberUtils.createBigDecimal(request.getParameter("unitPrice"));
		String batchNo = request.getParameter("batchNo");
		String companyName = request.getParameter("companyName");
		String dateManufacture = request.getParameter("dateManufacture");
		String receiptDate = request.getParameter("receiptDate");
		//System.out.println("itemName: "+itemName);
		InventoryItem item = inventoryService.getItemById(itemId);
		
		if(item == null){
			errors.add("inventory.receiptItem.Item.required");
			model.addAttribute("category", category);
			model.addAttribute("specification", specification);
			model.addAttribute("ItemId", itemId);
			model.addAttribute("quantity", quantity);
			model.addAttribute("VAT", VAT);
			model.addAttribute("batchNo", batchNo);
			model.addAttribute("unitPrice", unitPrice);
			model.addAttribute("companyName", companyName);
			model.addAttribute("dateManufacture", dateManufacture);
			model.addAttribute("companyName", companyName);
			model.addAttribute("receiptDate", receiptDate);
			return "/module/inventory/mainstoreItem/itemReceiptsToGeneralStore";
		}else if(CollectionUtils.isNotEmpty(item.getSpecifications()) && specification == 0 )
		{
			errors.add("inventory.receiptItem.specification.required");
			return "/module/inventory/mainstoreItem/itemReceiptsToGeneralStore";
		}
		
		InventoryStoreItemTransactionDetail transactionDetail = new InventoryStoreItemTransactionDetail();
		transactionDetail.setItem(item);
		transactionDetail.setSpecification(inventoryService.getItemSpecificationById(specification));
		transactionDetail.setCompanyName(companyName);
		transactionDetail.setCurrentQuantity(quantity);
		transactionDetail.setQuantity(quantity);
		transactionDetail.setUnitPrice(unitPrice);
		transactionDetail.setVAT(VAT);
		transactionDetail.setIssueQuantity(0);
		transactionDetail.setCreatedOn(new Date());
		transactionDetail.setReceiptDate(DateUtils.getDateFromStr(receiptDate));
		transactionDetail.setDateManufacture(DateUtils.getDateFromStr(dateManufacture));
		/*Money moneyUnitPrice = new Money(unitPrice);
		Money vATUnitPrice = new Money(VAT);
		Money m = moneyUnitPrice.plus(vATUnitPrice);
		Money totl = m.times(quantity);
		transactionDetail.setTotalPrice(totl.getAmount());*/
		
		/*Money moneyUnitPrice = new Money(unitPrice);
		Money totl = moneyUnitPrice.times(quantity);
		totl = totl.plus(totl.times(VAT.divide(new BigDecimal(100) , 2)));*/
		
		
		BigDecimal moneyUnitPrice = unitPrice.multiply(new BigDecimal(quantity));
		moneyUnitPrice = moneyUnitPrice.add(moneyUnitPrice.multiply(VAT.divide(new BigDecimal(100))));
		transactionDetail.setTotalPrice(moneyUnitPrice);
		
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "itemReceipt_"+userId;
		List<InventoryStoreItemTransactionDetail> list = (List<InventoryStoreItemTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list == null){
			list = new ArrayList<InventoryStoreItemTransactionDetail>();
		}
		list.add(transactionDetail);
		//System.out.println("list receipt: "+list);
		StoreSingleton.getInstance().getHash().put(fowardParam, list);
		//model.addAttribute("listReceipt", list);
	 return "redirect:/module/inventory/itemReceiptsToGeneralStore.form";
	}
}
