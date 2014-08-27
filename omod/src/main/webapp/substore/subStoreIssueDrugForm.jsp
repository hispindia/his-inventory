
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm"
	redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.issueDrug.manage"
	scope="page" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp"%>
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
<script type="text/javascript">
var cat="General";

function getValue()
  {
	var payMod=jQuery("#paymentMode").val();
	ISSUE.processSlip('0',payMod);
  }


</script>
		<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 100px;
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

<div style="width: 40%; float: left; margin-left: 4px;">
	<b class="boxHeader">Drug</b>
	<div class="box">

		<form method="post" id="formIssueDrug">
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
	 				
	 				18/7/2012 : harsh issue :302
	 				 -->
				<tr>

<%-- 	// Sagar Bele - 07-08-2012 New Requirement #302 [INVENTORY] Non Mandatory Drug Category filter for drug search  --%>
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
				  
				<tr>
					<td>Drug<em>*</em></td>
					<td>
					
				 <input id="drugName" name="drugName" onblur="ISSUE.onBlur(this);" style="width: 200px;">
						<div id="divDrug"  ></div>
					
					</td>
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
		</form>
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
					<td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}&nbsp;${fn:replace(issueDrugPatient.patient.middleName,","," ")} </td>
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
				<th><spring:message code="inventory.receiptDrug.price" text="Price" /></th>
			</tr>
			<c:choose>
				<c:when test="${not empty listPatientDetail}">
					<c:set var="total" value="${0}"/>   
					<c:forEach items="${listPatientDetail}" var="issue"
						varStatus="varStatus">
						<c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" />
						<c:set var="generalVar" value="General"/>
						<c:set var="total" value="${total + price}"/>	
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td><c:out value="${varStatus.count }" /></td>
							<td>${issue.transactionDetail.drug.category.name}</td>
							<td><a href="#" title="Remove this"
								onclick="INVENTORY.removeObject('${varStatus.index}','5');">${issue.transactionDetail.drug.name}</a></td>
							<td>${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</td>
							<td>${issue.quantity}</td>
							<td><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></td>
						</tr>
					</c:forEach>
						<tr><td>&nbsp;</td></tr>
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><b><spring:message code="inventory.receiptDrug.total" text="Total" /></b></td>
							<td>	
								<c:choose>
									<c:when test ="${patientCategory == generalVar}">
										<fmt:formatNumber value="${total}" type="number" pattern="#"/>
									</c:when>
									
									<c:otherwise>
										<strike><fmt:formatNumber value="${total}" type="number" pattern="#"/>
										</strike>  0.00
									</c:otherwise>
								</c:choose>
							</td>						
						</tr>
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><b>Payment Mode</b></td>
							<td><select id="paymentMode" name="paymentMode">
								<option value="Cash">Cash</option>
								<option value="Card">Card</option>
							</select>
							</td>						
						</tr>
				</c:when>
			</c:choose>
		</table>
		<br />

		<table class="box" width="100%" cellpadding="5" cellspacing="0">
			<tr>
				<td><c:if
						test="${not empty listPatientDetail && not empty issueDrugPatient}">
						<input type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all"
							id="bttprocess" value="<spring:message code="inventory.finish"/>"
							onclick="getValue();" />
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
<!-- PRINT DIV -->
<div id="printDiv" style="display: none;">
	<div style="width: 1280px; font-size: 0.8em">
		
		<br/>
<br/>     

<center><img width="100" height="100" align="center" title="OpenMRS" alt="OpenMRS" src="${pageContext.request.contextPath}/moduleResources/inventory/kenya_logo.bmp"><center>
  <table  class="spacer" style="margin-left: 60px;"> 		
<tr><h3><center><b><u>${userLocation}</u> </b></center></h3></tr>
<tr><h5><b><center>CASH RECEIPT</center></b></h5></tr>
</table>
<br/>
<br/>

		<c:if test="${not empty issueDrugPatient}">

			<table class="spacer" style="margin-left: 60px;">
				<tr>
					<td>Date/Time</td>
					<td>:${date}</td>
				</tr>
				<tr>
					<td>Name</td>
					<td>:${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}&nbsp;${fn:replace(issueDrugPatient.patient.middleName,","," ")}</td>
				</tr>
				<tr>
					<td>Identifier</td>
					<td>:${issueDrugPatient.identifier }</td>
				</tr>
				<tr>
					<td>Patient category</td>
					<td>:${patientCategory }</td>
				</tr>  
				<tr>
					<td>Waiver/Exempt. No.</td>
					<td>:${exemption }</td>
				</tr>  

			</table>
			<br />
		</c:if>
		<table class="printfont"
			style="margin-left: 60px; margin-top: 10px; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif; font-style: normal;"
			width="80%">
			<tr>
				<th>#</th>
				<th><spring:message code="inventory.drug.name" /></th>
				<th><spring:message code="inventory.drug.formulation" /></th>
				<th><spring:message code="inventory.receiptDrug.quantity" /></th>
				<th><spring:message code="inventory.receiptDrug.price" text="Price" /></th>
			</tr>
			<c:choose>
				<c:when test="${not empty listPatientDetail}">
				<c:set var="total" value="${0}"/>
					<c:forEach items="${listPatientDetail}" var="issue"
						varStatus="varStatus">
						<c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" />
						<c:set var="total" value="${total + price}"/>
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td><center><c:out value="${varStatus.count }" /></center></td>
							<td><center>${issue.transactionDetail.drug.name}</center></td>
							<td><center>${issue.transactionDetail.formulation.name}-${issue.transactionDetail.formulation.dozage}</center></td>
							<td><center>${issue.quantity}</center></td>
							<td><center><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></center></td>
						</tr>
					</c:forEach>
						<tr><td>&nbsp;</td></tr>
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><spring:message code="inventory.receiptDrug.total" text="Total" /></td>
								
								<c:choose>
									<c:when test ="${patientCategory == generalVar}">
										<td><fmt:formatNumber value="${total}" type="number" pattern="#"/></td>
									</c:when>
									
									<c:otherwise>
										<td><fmt:formatNumber value="0.00" type="number" pattern="#"/></td>
										
									</c:otherwise>
								</c:choose>
							</td>						
						</tr>
				</c:when>
			</c:choose>
		</table   class="spacer" style="margin-left: 60px; margin-top: 60px;">
			<table  class="spacer" style="margin-left: 60px; margin-top: 60px;">
		<tr>
			<td>PAYMENT MODE </td>
			<td><b>:</b></td>
		</tr>
	</table>
		<br /> <br /> <br /> <br /> <br /> <br /> <span
			style="float: right; font-size: 1.5em">Signature of Inventory
			Clerk/ Stamp</span>
	</div>
</div>
<!-- END PRINT DIV -->


<%@ include file="/WEB-INF/template/footer.jsp"%>