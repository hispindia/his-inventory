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

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.issueItem.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
<script type="text/javascript">

function getValue()
  {
	var payMod=jQuery("#paymentMode").val();
	ISSUE.processSlipItemPatient('0',payMod);
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

<div style="width: 40%; float: left; margin-left: 4px; ">

<b class="boxHeader">Item</b>
<div class="box">

<form method="post" id="formIssueItem"  onsubmit="return false" >
<c:if  test="${not empty errors}">
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message code="${error}" /></span>
</c:forEach>
</c:if>
<br/>
<table class="box">
<tr><td><b>Item Info</b></td></tr>
<tr>
		<td><spring:message code="inventory.item.subCategory"/><em>*</em></td>
		<td>
			<select name="category" id="category" onchange="ISSUE.onChangeCategoryItemPatient(this);"  style="width: 250px;">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
	</tr>
	<tr>
		<td>Item Name<em>*</em></td>
		<td>
			<div id="divItem">
				<select id="itemId" name="itemId" onchange="ISSUE.onBlurItemPatient(this);"  style="width: 250px;">
					<option value=""><spring:message code="inventory.pleaseSelect"/></option>
					   <c:if test ="${not empty items}">
					       <c:forEach items="${items}" var="item">
					           <option value="${item.id}" >${item.name}</option>
					       </c:forEach>
				       </c:if>
				</select>
			</div>
		</td>
	</tr>
	<tr id="divSpecification">
		<td></td>
		<td></td>
	</tr>
</table>
<br/>
<div id="divItemAvailable">

<!--<table class="box" width="100%">
	<tr align="center">
		<th>Quantity available</th>
		<th>Issue quantity</th>
	</tr>
	<c:if  test="${sumReceiptItem > 0}">
	<tr align="center">
		<td>${sumReceiptItem}</td>
		<td><input type="hidden" id="currentQuantity" name="currentQuantity" value="${sumReceiptItem}"  /><input type="text" id="issueItemQuantity" name="issueItemQuantity" class="required digits" size="5"/></td>
	</tr>
	
	</c:if>
	<c:if  test="${sumReceiptItem <= 0}">
	<tr align="center">
		<td colspan="2">This item is empty in your store please indent it
		<input type="hidden" id="issueItemQuantityA" name="issueItemQuantityA" class="required digits" size="5"/>
		</td>
		
	</tr>	
	</c:if>
</table>

--></div>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="ISSUE.checkQtyBeforeIssue(this);" value="<spring:message code="inventory.issueItem.addToSlip"/>">
			<c:if test="${empty issueItemPatient}">
				<input type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all"
					value="Find Patient"
					onclick="ISSUE.createPatientForItem();">
			</c:if>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('subStoreIssueItemPatientList.form');">
</form>
</div>
</div>
<!-- Purchase list -->
<div style="width: 58%; float: right; margin-right: 16px; ">
<b class="boxHeader">Issue item to Patient Slip</b>

<div class="box">
	<c:if test="${not empty issueItemPatient}">
		<table class="box" width="100%">
			<tr>
				<th>Identifier</th>
				<th>Category</th> 
				<th>Name</th>
				<th>Age</th>
			</tr>
			<tr>
				<td>${issueItemPatient.patient.patientIdentifier.identifier}</td>
				<td>${paymentSubCategory}</td>
				<td>${issueItemPatient.patient.givenName}&nbsp;${issueItemPatient.patient.familyName}&nbsp;${fn:replace(issueItemPatient.patient.middleName,","," ")} </td>
				<td><c:choose>
						<c:when test="${issueItemPatient.patient.age == 0  }">&lt 1</c:when>
						<c:otherwise>${issueItemPatient.patient.age }</c:otherwise>
					</c:choose></td>
			</tr>

		</table>
	</c:if>
	
	

</div>

<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.price" text="Price" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listItemDetail}">
	<c:set var="total" value="${0}"/>
	<c:forEach items="${listItemDetail}" var="issue" varStatus="varStatus">
		<%-- <c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" /> --%>
		<c:set var="price" value="${ issue.quantity * issue.transactionDetail.costToPatient}" />
		<c:set var="generalVar" value="GENERAL PATIENT"/>
		<c:set var="expectantVar" value="EXPACTANT MOTHER"/>
		<c:set var="tbVar" value="TB PATIENT"/>
		<c:set var="cccVar" value="CCC PATIENT"/>
		<c:set var="total" value="${total + price}"/>
		
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${issue.transactionDetail.item.subCategory.name} </td>	
		<td>${issue.transactionDetail.item.name}</td>
		<td>${issue.transactionDetail.specification.name}</td>
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
		<td><b><spring:message code="inventory.receiptItem.total" text="Total" /></b></td>
		<td>	
			<c:choose>
				<c:when test ="${paymentSubCategory == generalVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:when test ="${paymentSubCategory == expectantVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:when test ="${paymentSubCategory == tbVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:when test ="${paymentSubCategory == cccVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:otherwise>
					<strike><fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
					</strike>  0.00
				</c:otherwise>
			</c:choose>
		</td>						
	</tr>
<!-- <tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<!-- <td><b>Payment Mode</b></td>
		<td><select id="paymentMode" name="paymentMode">
			<option value="Cash">Cash</option>
			<option value="Card">Card</option>
		</select> 
		</td>	-->					
	</tr>-->
	</c:when>
	</c:choose>
</table>
<br/>
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<c:if  test="${not empty listItemDetail && not empty issueItemPatient}">
					<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  id="bttprocess" value="<spring:message code="inventory.finish"/>" onclick="getValue();" />
				</c:if>
				<c:if  test="${not empty listItemDetail || not empty issueItemPatient}">
					<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" id="bttclear" value="<spring:message code="inventory.clear"/>"  onclick="ISSUE.processSlipItemPatient('1');"/>
				</c:if>
			</td>
		</tr>
		</table>
	
</div>
</div>
<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; ">        		
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

<c:if  test="${not empty issueItemPatient}">

			<table class="spacer" style="margin-left: 60px;">
				<tr>
					<td>Date/Time</td>
					<td>:${date}</td>
				</tr>
				<tr>
					<td>Name</td>
					<td>:${issueItemPatient.patient.givenName}&nbsp;${issueItemPatient.patient.familyName}&nbsp;${fn:replace(issueItemPatient.patient.middleName,","," ")}</td>
				</tr>
				<tr>
					<td>Identifier</td>
					<td>:${issueDrugPatient.identifier }</td>
				</tr>
				<tr>
					<td>payment category</td>
					<td>:${paymentSubCategory }</td>
				</tr>  
<%-- 				<tr>
					<td>Waiver/Exempt. No.</td>
					<td>:${exemption }</td>
				</tr>  --%> 

			</table>
			<br />
</c:if>
<table 	class="printfont"
			style="margin-left: 60px; margin-top: 10px; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif; font-style: normal;"
			width="80%">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message text="Amount" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listItemDetail}">
	<c:set var="total" value="${0}"/>
	<c:forEach items="${listItemDetail}" var="issue" varStatus="varStatus">
		<%-- <c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" /> --%>
		<c:set var="price" value="${ issue.quantity * issue.transactionDetail.costToPatient}" />
		<c:set var="generalVar" value="GENERAL PATIENT"/>
		<c:set var="expectantVar" value="EXPACTANT MOTHER"/>
		<c:set var="tbVar" value="TB PATIENT"/>
		<c:set var="cccVar" value="CCC PATIENT"/>
		<c:set var="total" value="${total + price}"/>
		
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${issue.transactionDetail.item.name}</td>
		<td>${issue.transactionDetail.specification.name}</td>
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
		<td><spring:message code="inventory.receiptItem.total" text="Total" /></td>
			
			<c:choose>
				<c:when test ="${paymentSubCategory == generalVar}">
					<td><fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/></td>
				</c:when>
				<c:when test ="${paymentSubCategory == expectantVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:when test ="${paymentSubCategory == tbVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				<c:when test ="${paymentSubCategory == cccVar}">
					<fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/>
				</c:when>
				
				<c:otherwise>
					<td><fmt:formatNumber value="0.00" type="number" maxFractionDigits="2"/></td>
				</c:otherwise>
			</c:choose>
		</td>						
	</tr>
						
	</c:when>
	</c:choose>
</table>
<br/><br/>
<!-- <table  class="spacer" style="margin-left: 60px; margin-top: 60px;">
		<tr>
			<td>PAYMENT MODE </td>
			<td><b>:</b></td>
		</tr>
	</table> -->
 -->
<br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of Inventory Clerk/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   

 
<%@ include file="/WEB-INF/template/footer.jsp" %>