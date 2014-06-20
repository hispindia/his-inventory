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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
<script type="text/javascript">
String cat="General";
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
<span class="boxHeader">Issue items detail</span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0" >
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.category"/></th>
	<th><spring:message code="inventory.viewStockBalance.item"/></th>
	<th><spring:message code="inventory.viewStockBalance.specification"/></th>
	<th><spring:message code="inventory.issueDrug.quantity"/></th>
	<th><spring:message  text="Amount" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listItemPatientIssue}">
	<c:set var="total" value="${0}"/>
	<c:forEach items="${listItemPatientIssue}" var="detail" varStatus="varStatus">
		<c:set var="price" value="${ detail.quantity* (detail.transactionDetail.unitPrice + 0.01*detail.transactionDetail.VAT*detail.transactionDetail.unitPrice) }" />
		<c:set var="generalVar" value="General"/>
		<c:set var="total" value="${total + price}"/>	
	
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${detail.transactionDetail.item.category.name} </td>	
		<td>${detail.transactionDetail.item.name} </td>	
		<td>${detail.transactionDetail.specification.name}</td>
		<td>${detail.quantity }</td>
		<td><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
</div>

<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; ">        		
<div style="width: 1280px; font-size: 0.8em">
<center><img width="100" height="100" align="center" title="OpenMRS" alt="OpenMRS" src="/kenya_openmrs/images/kenya_logo.bmp"><center>
  <table  class="spacer" style="margin-left: 60px;"> 		
<tr><h3><center><b><u>${userLocation}</u> </b></center></h3></tr>
<tr><h5><b><center>CASH RECEIPT</center></b></h5></tr>
</table>
<c:if  test="${not empty issueItemPatient}">
<table class="spacer" style="margin-left: 60px; margin-top: 40px;">
	<tr><td>Date/Time: </td><td>:${date}</td></tr>
	<tr><td>Name</td><td>:${issueItemPatient.patient.givenName}&nbsp;${issueItemPatient.patient.middleName}&nbsp;${issueItemPatient.patient.familyName}</td></tr>
	<tr><td>Identifier</td><td>:${issueItemPatient.identifier }</td></tr>
	<tr><td>Patient category</td><td>:${category}</td></tr>
	<tr><td>Waiver/Exempt. No.</td><td>:${exemption}</td></tr>
	
</table>
</c:if>
<table class="spacer" style="margin-left: 60px; margin-top: 40px;">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message text="Amount" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listItemPatientIssue}">
	<c:set var="total" value="${0}"/>
	<c:forEach items="${listItemPatientIssue}" var="issue" varStatus="varStatus">
	
		<c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" />
		<c:set var="generalVar" value="General"/>
		<c:set var="total" value="${total + price}"/>	
			
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><center><c:out value="${varStatus.count }"/></center></td>
		<td><center>${issue.transactionDetail.item.name}</center></td>
		<td><center>${issue.transactionDetail.specification.name}</center></td>
		<td><center>${issue.quantity}</center></td>
		<td><center><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></center></td>
		</tr>
	</c:forEach>
	<tr><td>&nbsp;</td></tr>
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td></td>
		<td></td>
		<td></td>
		<td><b><spring:message text="Total" /></b></td>
		<c:if  test="${category!='General'}">
			<td><fmt:formatNumber value="0.00" type="number" maxFractionDigits="2"/></td>
		</c:if>
		<c:if  test="${category=='General'}">
			<td><fmt:formatNumber value="${total}" type="number" maxFractionDigits="2"/></td>
		</c:if>

	</tr>	
	
	
	</c:when>
	</c:choose>
</table>
	<table  class="spacer" style="margin-left: 60px; margin-top: 40px;">
		<tr>
			<td>PAYMENT MODE </td>
			<td><b>:</b></td>
		</tr>
		<tr>
			<td>CASHIER </td>
			<td><b>:${cashier}</b></td>
		</tr>
	</table>

<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   
