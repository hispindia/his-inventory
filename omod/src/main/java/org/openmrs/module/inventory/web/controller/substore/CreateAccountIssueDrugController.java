package org.openmrs.module.inventory.web.controller.substore;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.util.DateUtils;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("CreateAccountIssueDrugController")
@RequestMapping("/module/inventory/createAccountIssueDrug.form")
public class CreateAccountIssueDrugController {

	@RequestMapping(method = RequestMethod.GET)
	public String firstView(Model model) {
		return "/module/inventory/substore/createAccountIssueDrug";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(HttpServletRequest request,Model model) {
		String account = request.getParameter("accountName");
		if(!StringUtils.isBlank(account)){
			int userId = Context.getAuthenticatedUser().getId();
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
			InventoryStoreDrugAccount issueAccount = new InventoryStoreDrugAccount();
			InventoryStoreDrugTransaction transaction = new InventoryStoreDrugTransaction();
			issueAccount.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			issueAccount.setCreatedOn(new Date());
			issueAccount.setName(account);
			issueAccount.setStore(store);
			
			transaction.setDescription(request.getParameter("accountType")+" "+DateUtils.getDDMMYYYY());
			
			StoreSingleton.getInstance().getHash().put("issueDrugAccount_"+userId , issueAccount);
			StoreSingleton.getInstance().getHash().put("transaction_"+userId , transaction);
			model.addAttribute("message", "Succesfully");
			model.addAttribute("urlS", "subStoreIssueDrugAccountForm.form");
			return "/module/inventory/thickbox/success";
		}
		return "/module/inventory/substore/createAccountIssueDrug";
	}
	
}
