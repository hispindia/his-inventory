package org.openmrs.module.inventory.web.controller.substoreItem;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.web.controller.global.StoreSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller("CreateAccountIssueItemController")
@RequestMapping("/module/inventory/createAccountIssueItem.form")
public class CreateAccountIssueItemController {

	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("issue") InventoryStoreItemAccount issue ,Model model) {
		return "/module/inventory/substoreItem/createAccountIssueItem";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("issue") InventoryStoreItemAccount issue, BindingResult bindingResult, HttpServletRequest request, SessionStatus status, Model model) {
		new IssueItemAccountValidator().validate(issue, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/inventory/substoreItem/createAccountIssueItem";
			
		}else{
			
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			int userId = Context.getAuthenticatedUser().getId();
			InventoryStore subStore =  inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
			issue.setCreatedBy(Context.getAuthenticatedUser().getGivenName());
			issue.setCreatedOn(new Date());
			issue.setStore(subStore);
			status.setComplete();
			
			String fowardParam = "issueItem_"+userId;
			StoreSingleton.getInstance().getHash().put(fowardParam,issue);
			
			model.addAttribute("message", "Succesfully");
			model.addAttribute("urlS", "subStoreIssueItemForm.form");
			
			return "/module/inventory/thickbox/success";
		}
	}
}
