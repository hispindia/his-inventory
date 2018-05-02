/**
 * <p> File: org.openmrs.module.inventory.schedule.ExampleTask.java </p>
 * <p> Project: inventory-api </p>
 * <p> Copyright (c) 2011 HISP Technologies. </p>
 * <p> All rights reserved. </p>
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Feb 24, 2011 4:31:57 PM </p>
 * <p> Update date: Feb 24, 2011 4:31:57 PM </p>
 **/

package org.openmrs.module.inventory.schedule;

import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * <p> Class: ExampleTask </p>
 * <p> Package: org.openmrs.module.inventory.schedule </p> 
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Feb 24, 2011 4:31:57 PM </p>
 * <p> Update date: Feb 24, 2011 4:31:57 PM </p>
 **/
public class ExampleTask  extends AbstractTask {
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try {
			Context.openSession();
			// do something
			InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
			System.out.println("store 1: "+inventoryService.getStoreById(1));
			System.out.println("tao la chuyen day ok? ");
			Context.closeSession();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void authenticate() {
		// TODO Auto-generated method stub
		super.authenticate();
	}

	@Override
	public void shutdown() {
		System.out.println("tao can shutdown ok?");
		super.shutdown();
	}

}
