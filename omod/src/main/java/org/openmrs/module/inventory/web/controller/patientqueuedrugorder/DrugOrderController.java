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

import javax.servlet.http.HttpServletRequest;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("DrugOrderController")
@RequestMapping("/module/inventory/drugorder.form")
public class DrugOrderController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(Model model, @RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId) {
		InventoryService inventoryService = Context.getService(InventoryService.class);
		/*
		List<BillableService> serviceOrderList = billingService.listOfServiceOrder(patientId,encounterId);
		model.addAttribute("serviceOrderList", serviceOrderList);
		model.addAttribute("serviceOrderSize", serviceOrderList.size());
		model.addAttribute("patientId", patientId);
		model.addAttribute("encounterId", encounterId);
		*/
		return "/module/inventory/queue/drugOrder";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Model model, Object command,
			HttpServletRequest request,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam("indCount") Integer indCount,
			@RequestParam(value = "billType", required = false) String billType) {

		/*
		BillingService billingService = Context.getService(BillingService.class);
		
		PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);

		PatientService patientService = Context.getPatientService();

		// Get the BillCalculator to calculate the rate of bill item the patient has to pay
		Patient patient = patientService.getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);

		BillCalculatorForBDService calculator = new BillCalculatorForBDService();

		PatientServiceBill bill = new PatientServiceBill();
		bill.setCreatedDate(new Date());
		bill.setPatient(patient);
		bill.setCreator(Context.getAuthenticatedUser());

		PatientServiceBillItem item;
		String servicename;
		int quantity = 0;
		String selectservice;
		BigDecimal unitPrice;
		String reschedule;
		String paybill;
		BillableService service;
		Money mUnitPrice;
		Money itemAmount;
		Money totalAmount = new Money(BigDecimal.ZERO);
		BigDecimal rate;
		String billTyp;
		BigDecimal totalActualAmount = new BigDecimal(0);
		OpdTestOrder opdTestOrder=new OpdTestOrder();
	
		for (Integer i = 1; i <= indCount; i++) {
			selectservice = request.getParameter(i.toString() + "selectservice");
			if("billed".equals(selectservice)){
			servicename = request.getParameter(i.toString() + "service");
			quantity = NumberUtils.createInteger(request.getParameter(i.toString()+ "servicequantity"));
			reschedule = request.getParameter(i.toString() + "reschedule");
			paybill = request.getParameter(i.toString() + "paybill");
			unitPrice = NumberUtils.createBigDecimal(request.getParameter(i.toString() + "unitprice"));
			//ConceptService conceptService = Context.getConceptService();
			//Concept con = conceptService.getConcept("servicename");
			service = billingService.getServiceByConceptName(servicename);

			mUnitPrice = new Money(unitPrice);
			itemAmount = mUnitPrice.times(quantity);
			totalAmount = totalAmount.plus(itemAmount);

			item = new PatientServiceBillItem();
			item.setCreatedDate(new Date());
			item.setName(servicename);
			item.setPatientServiceBill(bill);
			item.setQuantity(quantity);
			item.setService(service);
			item.setUnitPrice(unitPrice);

			item.setAmount(itemAmount.getAmount());

			// Get the ratio for each bill item
			Map<String, Object> parameters = HospitalCoreUtils.buildParameters(
					"patient", patient, "attributes", attributes, "billItem",
					item, "request", request);
			
			if("pay".equals( paybill)){
				billTyp = "paid";
			}
			else{
				billTyp = "free";
			
			}
			
			rate = calculator.getRate(parameters, billTyp);
			item.setActualAmount(item.getAmount().multiply(rate));
			totalActualAmount = totalActualAmount.add(item.getActualAmount());
			bill.addBillItem(item);
	
			opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
			opdTestOrder.setBillingStatus(1);
			patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
			
		  }
			else{
				servicename = request.getParameter(i.toString() + "service");
				service = billingService.getServiceByConceptName(servicename);
				opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
				opdTestOrder.setCancelStatus(1);
				patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
			}
		}

		bill.setAmount(totalAmount.getAmount());
		bill.setActualAmount(totalActualAmount);
		bill.setFreeBill(2);
		bill.setReceipt(billingService.createReceipt());
		bill = billingService.savePatientServiceBill(bill);

		return "redirect:/module/billing/patientServiceBillForBD.list?patientId=" + patientId + "&billId="
        + bill.getPatientServiceBillId() + "&billType=" + billType;
        */
		return null;
	}
}
