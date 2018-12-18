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
<script type="text/javascript">
jQuery(document).ready(function(){ 
var tap="${totalAmountPayable}";
jQuery("#printableTotalPayablee").append("<span style='margin:5px;'>" + toWords(tap) + "</span>");
jQuery("#creditheader").hide();
jQuery("#creditheaders").hide();
jQuery("#cashheader").hide();
jQuery("#cashheaders").hide();
if(jQuery("#test").val().length!=0)
	{
	jQuery("#creditheader").show();
	jQuery("#amtgiven").hide();
	jQuery("#amtreturn").hide();
	jQuery("#amtgven").hide();
	jQuery("#amtretrn").hide();
	}
	else{
	jQuery("#cashheader").show();
	}
if(jQuery("#test1").val().length!=0)
{ 
jQuery("#creditheaders").show();
jQuery("#amtgivens").hide();
jQuery("#amtreturns").hide();
jQuery("#amtgvens").hide();
jQuery("#amtretrns").hide();
}
else{
jQuery("#cashheaders").show();
}

});
</script>
<span class="boxHeader">Issue drugs detail</span>
<span class="boxHeader"><div id="creditheader" >CREDIT BILL</div></span>
<span class="boxHeader"><div id="cashheader" >CASH BILL</div></span>
<div class="box">
<div align="center">
  <tr><td style="text-align:center;">
	     <img  type="image" src="../../moduleResources/inventory/Logo_Tibet.jpg" align="middle"/>
	     </td>
	     </tr>
		 <tr><td> <h2>${hospitalName}</h2> </td></tr>
		 </div>
<c:if  test="${not empty listDrugIssue}">
			<table align='Center'>
				<tr>
					<td>Patient ID :</td>
					<td>${issueDrugPatient.identifier }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 
					<td>Name :</td>
			        <td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}</td>
				</tr>
				<tr>
					<td>Age:</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						
					<td>Gender:</td>
        	        <td>${issueDrugPatient.patient.gender}</td>  	
				</tr>
				<tr>
					<td>Date:</td>
					<td><openmrs:formatDate date="${date}" type="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>Patient Category:</td>
			        <td>${subCategoryf.name} &nbsp;&nbsp;&nbsp; ${childCategoryf.name}</td>
			       
				</tr>
				<tr>
				<td>Bill No:</td>
				<td>${billNo}</td>
				</tr>
			</table>
			<br />
</c:if>
<table style="width:100%">
			<thead>
			<h4 align="left" style="color:black">Drugs Issued by Pharmacy</h4>
			<tr>
				<th style="text-align: center;">#</th>
			
				<th style="text-align: center;"><spring:message code="inventory.drug.name" /></th>
				<th style="text-align: center;"><spring:message code="inventory.drug.formulation" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.batchNo" /></th>
                <th style="text-align: center;"><spring:message code="inventory.receiptDrug.dateExpiry" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.quantity" /></th>
				<!--
				<th style="text-align: center;">&nbsp;</th>
				-->
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.MRP" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.total" /></th>
				<!--
				<th style="text-align: center;">&nbsp;</th>
				-->
			</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${not empty listDrugIssue}">
					<c:forEach items="${listDrugIssue}" var="issue"
						varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td style="text-align: center;"><c:out value="${varStatus.count }" /></td>
							
							<td style="text-align: center;">${issue.transactionDetail.drug.name}</td>
							<td style="text-align: center;">${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
							<td style="text-align: center;">${issue.transactionDetail.batchNo}</td>
							<td style="text-align: center;"><openmrs:formatDate date="${issue.transactionDetail.dateExpiry}"
								type="textbox" /></td>
							<td  style="text-align: center;" class='<c:if test="${issue.transactionDetail.voided==1}">retired</c:if>'>${issue.quantity}</td>
							<!--
							<c:choose>
							<c:when test="${issue.transactionDetail.voided==1}"><td style="text-align: center;">${issue.transactionDetail.quantityAfterReturn}</td></c:when>
							<c:otherwise><td style="text-align: center;">&nbsp;</td></c:otherwise>
							</c:choose>
							 -->
							<td style="text-align: center;">${issue.transactionDetail.mrpPrice}</td>
						    <td  style="text-align: center;" class='<c:if test="${issue.transactionDetail.voided==1}">retired</c:if>'>${issue.transactionDetail.mrpPrice*issue.quantity}</td>
						   <!--
						    <c:choose>
						    <c:when test="${issue.transactionDetail.voided==1}"><td style="text-align: center;">${issue.transactionDetail.mrpPrice*issue.transactionDetail.quantityAfterReturn}</td></c:when>
						    <c:otherwise><td style="text-align: center;">&nbsp;</td></c:otherwise>
						    </c:choose>
						    -->
						    <input type="hidden" id="test" value="${issue.transactionDetail.amountCredit}"/>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
			<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td style="text-align: center;">Total amount</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${totalAmount}0</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td style="text-align: center;">Discount %</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${discount}0</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td style="text-align: center;">DiscountAmount</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${discountAmount}</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td style="text-align: center;">Total amount payable</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${totalAmountPayable}</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td id="amtgven" style="text-align: center;">Amount Given</td>
<td id="amtgiven" style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${amountGiven}.00</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<!--
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
 -->
<td id="amtretrn" style="text-align: center;">Amount Returned</td>
<td id="amtreturn" style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${amountReturned}.00</td>
</tr>
			</tbody>
		</table>
</div>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div id="printDiv" style="display: none;"
		style="width: 1280px; font-size: 0.8em">

		<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 50px;
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
	.printfont {
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
}
</style>


<br><br>  
<div align="center">
  <tr><td style="text-align:center;">
	     <img  type="image" src="../../moduleResources/laboratory/Logo_Tibet.jpg" align="middle"/>
	     </td>
	     </tr>
		<center><h2>${hospitalName}</h2></center>
</div> 

<div id="creditheaders" style="color:red;text-align: center;">CREDIT BILL</div>  
<div id="cashheaders" style="color:red;text-align: center;">CASH BILL</div>  
<tr><td>BILL NO.:${billNo}</td></tr>
<c:if  test="${not empty listDrugIssue}">
<br /> <br />
			<table align='Center'>
				<tr>
					<td>Patient ID :</td>
					<td>${issueDrugPatient.identifier }&nbsp;&nbsp;&nbsp;</td>
					
					<td>Name :</td>
			        <td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}</td>
				</tr>
				<tr>
					<td>Age:</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						
					<td>Gender:</td>
        	        <td>${issueDrugPatient.patient.gender}</td>  	
				</tr>
				    <tr>
     <c:if test="${not empty dohId}">
			<td id="doh">DoH Id:</td>
			<td id="dohid">${dohId}</td>
			</c:if>
      
      </tr>
				<tr>
					<td>Date:</td>
					<td><openmrs:formatDate date="${date}" type="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>Patient Category:</td>
			        <td>${subCategoryf.name} &nbsp;&nbsp;&nbsp; ${childCategoryf.name}</td>
				</tr>
			</table>
			<hr  color="black">
			<br />
</c:if>
<table style="width:100%">
			<thead>
			<h4 align="left" style="color:black">Drugs Issued by Pharmacy</h4>
			<tr>
				<th style="text-align: center;">#</th>
				<th style="text-align: center;"><spring:message code="inventory.drug.name" /></th>
				<th style="text-align: center;"><spring:message code="inventory.drug.formulation" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.batchNo" /></th>
                <th style="text-align: center;"><spring:message code="inventory.receiptDrug.dateExpiry" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.quantity" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.MRP" /></th>
				<th style="text-align: center;"><spring:message code="inventory.receiptDrug.total" /></th>
			</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${not empty listDrugIssue}">
					<c:forEach items="${listDrugIssue}" var="issue"
						varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td style="text-align: center;"><c:out value="${varStatus.count }" /></td>
					
							<td style="text-align: center;">${issue.transactionDetail.drug.name}</td>
							<td style="text-align: center;">${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
						    <td style="text-align: center;">${issue.transactionDetail.batchNo}</td>
							<td style="text-align: center;"><openmrs:formatDate date="${issue.transactionDetail.dateExpiry}"
								type="textbox" /></td>
							<td style="text-align: center;" class='<c:if test="${issue.transactionDetail.voided==1}">retired</c:if>'>${issue.quantity}</td>
							<td style="text-align: center;">${issue.transactionDetail.mrpPrice}</td>
							<td style="text-align: center;" class='<c:if test="${issue.transactionDetail.voided==1}">retired</c:if>'>${issue.transactionDetail.mrpPrice*issue.quantity}</td>
						    <input type="hidden" id="test1" value="${issue.transactionDetail.amountCredit}"/>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
			<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Total amount</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${totalAmount}0</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Discount %</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${discount}0</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">DiscountAmount</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${discountAmount}</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Total amount payable</td>
<td  style="text-align: center;" class='<c:if test="${voided==1}">retired</c:if>'>${totalAmountPayable}</td>
</tr>
<tr>
<td colspan="6"><b>Total Amount  Payable Rupees:</b><span id="printableTotalPayablee" > </span> only</td>
</tr>
<!--  <tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td id="amtgvens" style="text-align: center;">Amount Given</td>
<td id="amtgivens" style="text-align: center;">${amountGiven}.00</td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td id="amtretrns" style="text-align: center;">Amount Returned</td>
<td id="amtreturns" style="text-align: center;">${amountReturned}.00</td>
</tr>-->
</tbody>
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of pharmacist/ Stamp</span>
</div>
<!-- END PRINT DIV -->   