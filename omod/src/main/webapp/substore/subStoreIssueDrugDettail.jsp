 <%--
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
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
--%> 
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<span class="boxHeader">Issue drugs detail</span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.category"/></th>
	<th><spring:message code="inventory.viewStockBalance.drug"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
	<th><spring:message code="inventory.issueDrug.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listDrugIssue}">
	<c:forEach items="${listDrugIssue}" var="detail" varStatus="varStatus">
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${detail.transactionDetail.drug.category.name} </td>	
		<td>${detail.transactionDetail.drug.name} </td>	
		<td>${detail.transactionDetail.formulation.name}-${detail.transactionDetail.formulation.dozage}</td>
		<td><openmrs:formatDate date="${detail.transactionDetail.dateExpiry}" type="textbox"/></td>
		<td>${detail.quantity }</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
</div>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; margin: 10px auto; width: 981px; font-size: 1.5em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">
<br/>
<br/>        		
<c:if  test="${not empty listDrugIssue}">
<br /> <br />
			<table align='Center'>
				<tr>
					<td>Patient ID :</td>
					<td>${issueDrugPatient.identifier }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>Name :</td>
			        <td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}</td>
				</tr>
				<tr>
					<td>Age</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						
					<td>Gender:</td>
        	        <td>${issueDrugPatient.patient.gender}</td>  	
				</tr>
				<tr>
					<td>Date</td>
					<td><openmrs:formatDate date="${date}" type="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>Patient Category:</td>
			        <td>${patientCategory}</td>
				</tr>
			</table>
			<br />
</c:if>
<table style="width:100%">
			<thead>
			<h4 align="left" style="color:black">Drugs Issued by Pharmacy</h4>
			<tr>
				<th style="text-align: center;">#</th>
				<th style="text-align: center;"><spring:message code="inventory.drug.category" /></th>
				<th style="text-align: center;"><spring:message code="inventory.drug.name" /></th>
				<th style="text-align: center;"><spring:message code="inventory.drug.formulation" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.quantity" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.unitprice" /></th>
			</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${not empty listDrugIssue}">
					<c:forEach items="${listDrugIssue}" var="issue"
						varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td style="text-align: center;"><c:out value="${varStatus.count }" /></td>
							<td style="text-align: center;">${issue.transactionDetail.drug.category.name}</td>
							<td style="text-align: center;">${issue.transactionDetail.drug.name}</td>
							<td style="text-align: center;">${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
							<td style="text-align: center;">${issue.quantity}</td>
							<td style="text-align: center;">${issue.transactionDetail.unitPrice}</td>
						</tr>
					</c:forEach>

				</c:when>
			</c:choose>
			</tbody>
		</table>

<table style="width:100%">
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td colspan="6">Total amount</td>
<td>${totalAmount}</td>
</tr>
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td colspan="6">Discount %</td>
<td>${discount}</td>
</tr>
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td colspan="6">Total amount payable</td>
<td>${totalAmountPayable}</td>
</tr>
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td colspan="6">Amount Given</td>
<td>${amountGiven}</td>
</tr>
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td colspan="6">Amount Returned</td>
<td>${amountReturned}</td>
</tr>
<!-- 
<tr>
<td><b>Total Amount  Payable Rupees:</b>toWords(parseInt(${totalAmountPayable})) only</td>
</tr>
--> 
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of pharmacist/ Stamp</span>
</div>
<!-- END PRINT DIV -->   