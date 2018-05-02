/**
 * <p> File: org.openmrs.module.inventory.web.controller.interceptor.MyInterceptor.java </p>
 * <p> Project: inventory-omod </p>
 * <p> Copyright (c) 2011 CHT Technologies. </p>
 * <p> All rights reserved. </p>
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Jan 5, 2011 12:57:29 PM </p>
 * <p> Update date: Jan 5, 2011 12:57:29 PM </p>
 **/

package org.openmrs.module.inventory.web.controller.interceptor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.inventory.InventoryService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p> Class: MyInterceptor </p>
 * <p> Package: org.openmrs.module.inventory.web.controller.interceptor </p> 
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Jan 5, 2011 12:57:29 PM </p>
 * <p> Update date: Jan 5, 2011 12:57:29 PM </p>
 **/
//@Controller("myInterceptor")

public class MyInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		 InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
		 if(store != null && store.getParent() == null){
			 response.sendRedirect("/module/inventory/mainstore/mainPage");
		 }else{
			 response.sendRedirect( "/module/inventory/substore/mainPage");
		 }
		return false;
	}

}
