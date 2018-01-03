package org.openmrs.module.inventory.web.controller.mainstore;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.DateUtils;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ReceiptFormController")
@RequestMapping("/module/inventory/receiptsToGeneralStore.form")
public class ReceiptFormController {
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(
			@RequestParam(value="categoryId",required=false)  Integer categoryId,
			Model model) {
	 InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
	 model.addAttribute("listCategory", listCategory);
	 model.addAttribute("categoryId", categoryId);
	 if(categoryId != null && categoryId > 0){
		 List<InventoryDrug> drugs = inventoryService.findDrug(categoryId, null);
		 model.addAttribute("drugs",drugs);
		 
	 }
	 model.addAttribute("date",new Date());
	 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	 model.addAttribute("store",store);
 	 int userId = Context.getAuthenticatedUser().getId();
	 String fowardParam = "reipt_"+userId;
	 List<InventoryStoreDrugTransactionDetail> list = (List<InventoryStoreDrugTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
	 model.addAttribute("listReceipt", list);
	 if(list!=null)
	 {
	 float totAmtgst[]=new float[list.size()]; double totAmountafterGst=0.0;
	 for(int i=0;i<list.size();i++)
	 {
		 if( totAmtgst[i]==0.0)
		
		 {totAmtgst[i]=totAmtgst[i]+list.get(i).getTotalAmountAfterGst().floatValue();
		
		 }
		 totAmountafterGst=totAmountafterGst+totAmtgst[i];
	 }
	model.addAttribute("totAmountafterGst", totAmountafterGst);
	 }
	
	 
	 return "/module/inventory/mainstore/receiptsToGeneralStore";
	 
	}
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request, Model model) {
		List<String> errors = new ArrayList<String>();
		int drugId=0;
		String drugN="",drugIdStr="";
		InventoryDrug drug=null;
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 List<InventoryDrugCategory> listCategory = inventoryService.findDrugCategory("");
		 model.addAttribute("listCategory", listCategory);
		//int category = NumberUtils.toInt(request.getParameter("category"),0);
		int formulation = NumberUtils.toInt(request.getParameter("formulation"),0);
		
		if (request.getParameter("drugName")!=null)
		drugN=request.getParameter("drugName");
		if (request.getParameter("drugId")!=null)
	 drugIdStr=request.getParameter("drugId");
		
		if (!drugN.equalsIgnoreCase("")){
			
			 drug=inventoryService.getDrugByName(drugN);
		}else if (!drugIdStr.equalsIgnoreCase("")){
			drugId=Integer.parseInt(drugIdStr);
			 drug=inventoryService.getDrugById(drugId);
		}
		
		if(drug == null){
			errors.add("inventory.receiptDrug.drug.required");
		}else{
		 drugId = drug.getId();
		}
		
		int quantity = NumberUtils.toInt(request.getParameter("quantity"),0);
		BigDecimal VAT = NumberUtils.createBigDecimal(request.getParameter("VAT"));
		BigDecimal Rate=  NumberUtils.createBigDecimal(request.getParameter("rate"));
		BigDecimal sgst=  NumberUtils.createBigDecimal(request.getParameter("sgst"));
	
		BigDecimal cgst=  NumberUtils.createBigDecimal(request.getParameter("cgst"));
	
		BigDecimal mrPrice =  NumberUtils.createBigDecimal(request.getParameter("mrPrice"));
		float waiverPercentage =  Float.parseFloat(request.getParameter("waiverPercentage"));
		String batchNo = request.getParameter("batchNo");
		String companyName = request.getParameter("companyName");
		String dateManufacture = request.getParameter("dateManufacture");
		String dateExpiry = request.getParameter("dateExpiry");
		String receiptDate = request.getParameter("receiptDate");
		BigDecimal costToPatient = NumberUtils.createBigDecimal(request.getParameter("costToPatient"));
		if(!StringUtils.isBlank(dateManufacture)){
			Date dateManufac = DateUtils.getDateFromStr(dateManufacture);
			Date dateExpi = DateUtils.getDateFromStr(dateExpiry);
			if(dateManufac.after(dateExpi)  ){
				errors.add("inventory.receiptDrug.manufacNeedLessThanExpiry");
			}
		}
		
		InventoryDrugFormulation formulationO = inventoryService.getDrugFormulationById(formulation);
		if(formulationO == null)
		{
			errors.add("inventory.receiptDrug.formulation.required");
		}
		//InventoryDrug drug = inventoryService.getDrugById(drugId);
	
		if(formulationO != null && drug != null && !drug.getFormulations().contains(formulationO))
		{
			errors.add("inventory.receiptDrug.formulation.notCorrect");
		}
		if(!CollectionUtils.isEmpty(errors)){
			model.addAttribute("errors", errors);
		//	model.addAttribute("category", category);
			model.addAttribute("formulation", formulation);
			model.addAttribute("drugId", drugId);
			model.addAttribute("quantity", quantity);
			//model.addAttribute("VAT", VAT);
			model.addAttribute("batchNo", batchNo);
			model.addAttribute("mrpPrice", mrPrice);
			model.addAttribute("Discount", waiverPercentage);
			
			model.addAttribute("companyName", companyName);
			model.addAttribute("dateManufacture", dateManufacture);
			model.addAttribute("companyName", companyName);
			model.addAttribute("dateExpiry", dateExpiry);
			
			return "/module/inventory/mainstore/receiptsToGeneralStore";
		}
		
		InventoryStoreDrugTransactionDetail transactionDetail = new InventoryStoreDrugTransactionDetail();
		transactionDetail.setDrug(drug);
		transactionDetail.setFormulation(inventoryService.getDrugFormulationById(formulation));
		transactionDetail.setBatchNo(batchNo);
		transactionDetail.setCompanyName(companyName);
		transactionDetail.setCurrentQuantity(quantity);
		transactionDetail.setQuantity(quantity);
		transactionDetail.setMrpPrice(mrPrice);
		transactionDetail.setWaiverPercentage(waiverPercentage);
		BigDecimal waiverAmount=Rate.multiply(new BigDecimal(quantity)).multiply(new BigDecimal(waiverPercentage).multiply(new BigDecimal(1).divide(new BigDecimal(100))));
		transactionDetail.setWaiverAmount(waiverAmount.floatValue());
		BigDecimal unitPrice = Rate;
		if(waiverPercentage!=0.0)
		{
			 unitPrice = Rate.subtract(new BigDecimal(waiverPercentage).multiply(new BigDecimal(.01)).multiply(Rate));
			
		}
		transactionDetail.setUnitPrice(unitPrice.setScale(0, BigDecimal.ROUND_HALF_UP));
		transactionDetail.setVAT(VAT);
		transactionDetail.setRate(Rate);
		transactionDetail.setCgst(cgst);
		transactionDetail.setSgst(sgst);
		BigDecimal cgstAmount=new BigDecimal(0.0);
		BigDecimal sgstAmount=new BigDecimal(0.0);
		
		if(cgst!=null)
		{
	     cgstAmount= (cgst.multiply(new BigDecimal(quantity).multiply(unitPrice))).divide(new BigDecimal(100));
		transactionDetail.setCgstAmount(cgstAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(sgst!=null)
		{
		sgstAmount= (sgst.multiply(new BigDecimal(quantity).multiply(unitPrice))).divide(new BigDecimal(100));
		transactionDetail.setSgstAmount(sgstAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		transactionDetail.setIssueQuantity(0);
		transactionDetail.setCreatedOn(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			transactionDetail.setDateExpiry(formatter.parse(dateExpiry+" 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		transactionDetail.setDateManufacture(DateUtils.getDateFromStr(dateManufacture));
		transactionDetail.setReceiptDate(DateUtils.getDateFromStr(receiptDate));
		BigDecimal totprice=new BigDecimal(0.0);
		totprice=new BigDecimal(quantity).multiply(Rate);
		BigDecimal totalAmountAfterGst =new BigDecimal(0.0);
		if(cgst!=null || sgst!=null)
		{
		totalAmountAfterGst=totprice.subtract(waiverAmount).add(cgstAmount).add(sgstAmount);
		}
		else
		{  VAT=VAT.multiply(unitPrice).multiply(new BigDecimal(quantity)).multiply(new BigDecimal(.01));
			totalAmountAfterGst=totprice.subtract(waiverAmount).add(VAT);
		}
		transactionDetail.setTotalAmountAfterGst(totalAmountAfterGst.setScale(0, BigDecimal.ROUND_HALF_UP));
		//BigDecimal moneyUnitPrice = Rate.add(cgstAmount).add(sgstAmount);
		transactionDetail.setTotalPrice(totprice.setScale(0, BigDecimal.ROUND_HALF_UP));
		
		int userId = Context.getAuthenticatedUser().getId();
		String fowardParam = "reipt_"+userId;
		List<InventoryStoreDrugTransactionDetail> list = (List<InventoryStoreDrugTransactionDetail> )StoreSingleton.getInstance().getHash().get(fowardParam);
		if(list == null){
			list = new ArrayList<InventoryStoreDrugTransactionDetail>();
		}
		list.add(transactionDetail);
		StoreSingleton.getInstance().getHash().put(fowardParam, list);
		//model.addAttribute("listReceipt", list);
	 return "redirect:/module/inventory/receiptsToGeneralStore.form";
	}
}
