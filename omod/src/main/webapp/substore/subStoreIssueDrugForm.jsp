
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
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm"
	redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.issueDrug.manage"
	scope="page" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){ 
var tot=parseFloat(${total});
jQuery("#totalValue").val("");
jQuery("#totalValue").val(tot);

var waiverPercentage=parseFloat(${waiverPercentage});
jQuery("#waiverPercentage").val("");
jQuery("#waiverPercentage").val(waiverPercentage);

var totalAmountPay=parseFloat(${totalAmountPayable});
jQuery("#totalAmountPayable").val("");
jQuery("#totalAmountPayable").val(totalAmountPay);
});
</script>

<script type="text/javascript">
function totalAmountToPay(){
var total=jQuery("#totalValue").val();
var waiverPercentage=jQuery("#waiverPercentage").val();
var totalAmountPay=total-(total*waiverPercentage)/100;
var tap=Math.round(totalAmountPay);
jQuery("#totalAmountPayable").val(tap);
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-tap;
jQuery("#amountReturned").val(amountReturned);
}

function amountReturnedToPatient(){
var totalAmountToPay=jQuery("#totalAmountPayable").val();
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-totalAmountToPay;
jQuery("#amountReturned").val(amountReturned);
}
</script>

<script type="text/javascript">
function finishDrugOrder() {

if(jQuery("#waiverPercentage").val() ==""){
alert("Please enter Discount Percentage");
return false;
}

if(jQuery("#waiverPercentage").val() < 0 ){
alert("Please enter correct Discount Percentage");
return false;
}

                
/*if(jQuery("#waiverPercentage").val()>0 && jQuery("#waiverComment").val() ==""){
alert("Please enter comment");
return false;
}
*/

if(jQuery("#amountGiven").val() ==""){
alert("Please enter Amount Given");
return false;
}

if(jQuery("#amountGiven").val() < 0 || !StringUtils.isDigit(jQuery("#amountGiven").val())){
alert("Please enter correct Amount Given");
return false;
}

var amgiv=jQuery("#amountGiven").val();
var tamp=jQuery("#totalAmountPayable").val();

if(amgiv-tamp < 0 ){
alert("Amount Given must be greater than Total Amount Payable");
return false;
}

if(jQuery("#amountReturned").val() ==""){
alert("Please enter Amount Returned");
return false;
}

if(jQuery("#amountReturned").val() < 0 || !StringUtils.isDigit(jQuery("#amountReturned").val())){
alert("Please enter correct Amount Returned");
return false;
}
                
ISSUE.processSlip('0');
}
</script>


<form method="post" id="formIssueDrug">
<div style="width: 40%; float: left; margin-left: 4px;">
	<b class="boxHeader">Drug</b>
	<div class="box">

			<c:if test="${not empty errors}">
				<c:forEach items="${errors}" var="error">
					<span class="error"><spring:message code="${error}" /></span>
				</c:forEach>
			</c:if>
			<br />
			<table class="box">
				<tr>
					<td><b>Drug info</b></td>
				</tr>
				<!-- @support feature#174
					 @author Thai Chuong
	 				 @date <dd/mm/yyyy> 08/05/2012
	 				 -->
<%-- ghanshyam,7-oct-2013,issue #2983,The user need not enter the category of the drug while receiving or issuing drugs to --%>
                <%--
				<tr>
					<td><spring:message code="inventory.drug.category" /></td>
					<td><select name="category" id="category"
						onchange="ISSUE.onChangeCategory(this);" style="width: 250px;">
							<option value="">
								<spring:message code="inventory.pleaseSelect" />
							</option>
							<c:forEach items="${listCategory}" var="vCat">
								<option value="${vCat.id}"
									<c:if test="${vCat.id == categoryId }">selected</c:if>>${vCat.name}</option>
							</c:forEach>
					</select></td>
				</tr>
				--%>

				<tr>
					<td>Drug<em>*</em></td>
					<td><input id="drugName" name="drugName"
						onblur="ISSUE.onBlur(this);" style="width: 200px;">
						<div id="divDrug"></div></td>
				</tr>
				<tr>
					<td><spring:message code="inventory.drug.formulation" /><em>*</em></td>
					<td>
						<div id="divFormulation">
							<select id="formulation" name="formulation">
								<option value="">
									<spring:message code="inventory.pleaseSelect" />
								</option>
							</select>
						</div>
					</td>
				</tr>
			</table>
			<br />
			<div id="divDrugAvailable">
				<!--<c:if  test="${not empty listReceiptDrug}">
<table class="box">
	<tr>
		<th>#</th>
		<th>Date of expiry</th>
		<th>Date of manufacturing</th>
		<th>Company name</th>
		<th>Patch no</th>
		<th>Quantity available</th>
		<th>Issue quantity</th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceiptDrug}">
	<c:forEach items="${listReceiptDrug}" var="avaiable" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td><openmrs:formatDate date="${avaiable.dateExpiry}" type="textbox"/> </td>
		<td><openmrs:formatDate date="${avaiable.dateManufacture}" type="textbox"/> </td>
		<td>${avaiable.companyName }</td>
		<td>${avaiable.batchNo }</td>
		<td>${avaiable.currentQuantity}</td>
		<td><input type="text" id="${avaiable.id }" name="${avaiable.id }" class="required digits" size="5"/></td>
	</tr>
	</c:forEach>
	
	</c:when>
	<c:otherwise>
	<tr >
		<td colspan="6">This drug is empty in your store please indent it
		<input type="hidden" id="${avaiable.id }" name="${avaiable.id }" class="required digits" size="5"/></td>
		
	</tr>	
	</c:otherwise>
	</c:choose>
</table>
</c:if>

-->
			</div>
			<br /> <input type="submit"
				class="ui-button ui-widget ui-state-default ui-corner-all"
				value="<spring:message code="inventory.issueDrug.addToSlip"/>">
			<c:if test="${empty issueDrugPatient}">
				<input type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all"
					value="<spring:message code="inventory.issueDrug.createPatient"/>"
					onclick="ISSUE.createPatient();">
			</c:if>
			<input type="button"
				class="ui-button ui-widget ui-state-default ui-corner-all"
				value="<spring:message code="inventory.back"/>"
				onclick="ACT.go('subStoreIssueDrugList.form');">
	</div>
</div>
<!-- Purchase list -->
<div style="width: 58%; float: right; margin-right: 16px;">
	<b class="boxHeader">Issue drug to patient slip</b>
	<div class="box">
		<c:if test="${not empty issueDrugPatient}">
			<table class="box" width="100%">
				<tr>
					<th>Identifier</th>
					<th>Category</th>
					<th>Name</th>
					<th>Age</th>
				</tr>
				<tr>
					<td>${issueDrugPatient.patient.patientIdentifier.identifier}</td>
					<td>${patientCategory}</td>
					<td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.middleName}&nbsp;${issueDrugPatient.patient.familyName}</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose></td>
				</tr>

			</table>
		</c:if>
	</div>
	<div class="box">
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
			<tr>
				<th>#</th>
				<th><spring:message code="inventory.drug.category" /></th>
				<th><spring:message code="inventory.drug.name" /></th>
				<th><spring:message code="inventory.drug.formulation" /></th>
				<th><spring:message code="inventory.receiptDrug.quantity" /></th>
			</tr>
			<c:choose>
				<c:when test="${not empty listPatientDetail}">
					<c:forEach items="${listPatientDetail}" var="issue"
						varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td><c:out value="${varStatus.count }" /></td>
							<td>${issue.transactionDetail.drug.category.name}</td>
							<td><a href="#" title="Remove this"
								onclick="INVENTORY.removeObject('${varStatus.index}','5');">${issue.transactionDetail.drug.name}</a></td>
							<td>${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
							<td>${issue.quantity}</td>
						</tr>
					</c:forEach>

				</c:when>
			</c:choose>
		</table>
		<br />
		
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
		<td>Total</td>
		<td><input type="text" id="totalValue" name="totalValue"
				size="11" value="0"/></td>
		</tr>
		<tr>
		<td>Discount %</td>
		<td><input type="text" id="waiverPercentage" name="waiverPercentage"
				size="11" value="0" onkeyup="totalAmountToPay();"/></td>
		</tr>
		<tr>
		<td>Total amount payable</td>
		<td><input type="text" id="totalAmountPayable" name="totalAmountPayable"
				size="11" value="0" readOnly="true"/></td>
		</tr>
		<tr>
		<td>Comment</td>
		<td><input type="text" id="waiverComment" name="waiverComment" size="11"/></td>
		</tr>
		<tr>
		<td>Amount Given</td>
		<td><input type="text" id="amountGiven" name="amountGiven" size="11" onkeyup="amountReturnedToPatient();"></td>
		</tr>
		<tr>
		<td>Amount Returned to Patient</td>
		<td><input type="text" id="amountReturned" name="amountReturned" size="11" readOnly="true"/></td>
		</tr>
		<tr>
		<td> </td>
		<td> </td>
		</tr>
		</table>

		<table class="box" width="100%" cellpadding="5" cellspacing="0">
			<tr>
				<td><c:if
						test="${not empty listPatientDetail && not empty issueDrugPatient}">
						<input type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all"
							id="bttprocess" name="bttprocess" value="<spring:message code="inventory.finish"/>"
							onclick="finishDrugOrder();" />
						<input type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all"
							id="bttprint" value="<spring:message code="inventory.print"/>"
							onClick="PURCHASE.printDiv();" />
					</c:if> <c:if
						test="${not empty listPatientDetail || not empty issueDrugPatient}">
						<input type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all"
							id="bttclear" value="<spring:message code="inventory.clear"/>"
							onclick="ISSUE.processSlip('1');" />
					</c:if></td>
			</tr>
		</table>

	</div>
</div>
</form>
<!-- PRINT DIV -->
<div id="printDiv" style="display: none;">
	<div
		style="margin: 10px auto; width: 981px; font-size: 1.0em; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif;">
		<c:if test="${not empty issueDrugPatient}">
			<br />
			<br />
			<center style="float: center; font-size: 2.2em">Issue Drug To Patient</center>
			<br />
			<br />
			<table border="1">
				<tr>
					<td>Patient identifier</td>
					<td>${issueDrugPatient.identifier }</td>
				</tr>
				<tr>
					<td>Patient category</td>
					<td>${patientCategory}</td>
				</tr>
				<tr>
					<td>Name</td>
					<td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.middleName}&nbsp;${issueDrugPatient.patient.familyName}</td>
				</tr>
				<tr>
					<td>Age</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<td>Date</td>
					<td><openmrs:formatDate date="${date}" type="textbox" /></td>
				</tr>
			</table>
			<br />
		</c:if>
		<table border="1">
			<tr>
				<th>#</th>
				<th><spring:message code="inventory.drug.category" /></th>
				<th><spring:message code="inventory.drug.name" /></th>
				<th><spring:message code="inventory.drug.formulation" /></th>
				<th><spring:message code="inventory.receiptDrug.quantity" /></th>
			</tr>
			<c:choose>
				<c:when test="${not empty listPatientDetail}">
					<c:forEach items="${listPatientDetail}" var="issue"
						varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td><c:out value="${varStatus.count }" /></td>
							<td>${issue.transactionDetail.drug.category.name}</td>
							<td>${issue.transactionDetail.drug.name}</td>
							<td>${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
							<td>${issue.quantity}</td>
						</tr>
					</c:forEach>

				</c:when>
			</c:choose>
		</table>
		<table border="1">
		<tr>
		<th>Total</th>
		<th>Discount</th>
		<th>Total amount payable</th>
		</tr>
		<br /> <br />
		<tr>
		<td><span id="printableTotal" /></td>
		<td><span id="printableDiscount" /></td>
		<td><span id="printableTotalAmountPayable" /></td>
		</tr>
		</table>
		<br /> <br /> <br /> <br /> <br /> <br /> 
		<!-- [Inventory] kesavulu 21/03/2013 Support #1136 In the Print out of receipt signature  Inventory clerk changed to  pharmacist -->
		<span style="float: right; font-size: 1.5em">Signature of pharmacist/ Stamp</span>
	</div>
</div>
<!-- END PRINT DIV -->


<%@ include file="/WEB-INF/template/footer.jsp"%>