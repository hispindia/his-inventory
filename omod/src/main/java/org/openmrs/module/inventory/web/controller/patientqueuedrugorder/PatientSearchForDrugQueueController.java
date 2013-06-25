/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Inventory module.
 *
 *  Inventory module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Inventory module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Inventory module.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  author: ghanshyam
 *  date: 15-june-2013
 *  issue no: #1636
 **/

package org.openmrs.module.inventory.web.controller.patientqueuedrugorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.inventory.InventoryConstants;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.util.PagingUtil;

@Controller("PatientSearchForDrugQueueController")
@RequestMapping("/module/inventory/patientsearchdruggqueue.form")
public class PatientSearchForDrugQueueController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(
			@RequestParam(value = "date", required = false) String dateStr,
			@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "currentPage", required = false) Integer currentPage,
			Model model) {
		InventoryService inventoryService = Context
				.getService(InventoryService.class);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PatientSearch> patientSearchResult = inventoryService
				.searchListOfPatient(date, searchKey, currentPage);
		currentPage = 1;
		int total = patientSearchResult.size();
		PagingUtil pagingUtil = new PagingUtil(InventoryConstants.PAGESIZE,
				currentPage, total);
		model.addAttribute("pagingUtil", pagingUtil);
		model.addAttribute("patientList", patientSearchResult);
		model.addAttribute("date", dateStr);
		return "/module/inventory/queue/searchResult";
	}
}
