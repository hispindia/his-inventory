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

<span class="boxHeader">Issue items detail</span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.category"/></th>
	<th><spring:message code="inventory.viewStockBalance.item"/></th>
	<th><spring:message code="inventory.viewStockBalance.specification"/></th>
	<th><spring:message code="inventory.issueDrug.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.price" text="Price" /></th>
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
<div style="margin: 10px auto; width: 981px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">
<c:if  test="${not empty issueItemPatient}">
<br />
<br />      		
<center style="float:center;font-size: 2.2em">Issue Item To Patient ${issueItemPatient.patient.givenName}&nbsp;${issueItemPatient.patient.middleName}&nbsp;${issueItemPatient.patient.familyName}</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
</c:if>
<table border="1">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.price" text="Price" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listItemPatientIssue}">
	<c:set var="total" value="${0}"/>
	<c:forEach items="${listItemPatientIssue}" var="issue" varStatus="varStatus">
	
		<c:set var="price" value="${ issue.quantity* (issue.transactionDetail.unitPrice + 0.01*issue.transactionDetail.VAT*issue.transactionDetail.unitPrice) }" />
		<c:set var="generalVar" value="General"/>
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
	
	</c:when>
	</c:choose>
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   
