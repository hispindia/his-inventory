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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.viewStockBalance.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="nav.jsp" %>


<span class="boxHeader"><spring:message code="inventory.viewStockBalance.detail"/></span>

<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.name"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<th><spring:message code="inventory.viewStockBalance.transaction"/></th>
	<th ><spring:message code="inventory.viewStockBalance.openingBalance"/></th>
	<th ><spring:message code="inventory.viewStockBalance.receiptQty"/></th>
	<th><spring:message code="inventory.viewStockBalance.STTSS"/></th>
	<th ><spring:message code="inventory.receiptDrug.closingBalance"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
	<th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
	<th><spring:message code="inventory.receiptDrug.MRP"/></th>
	<th><spring:message code="inventory.receiptDrug.batchNo"/></th>
	<th><spring:message code="inventory.receiptDrug.Dis"/></th>
	<th><spring:message code="inventory.receiptDrug.DiscountAmount"/></th>
	<th ><spring:message code="inventory.receiptDrug.cgst"/></th>
	<th><spring:message code="inventory.receiptDrug.cgstamt"/></th>
    <th><spring:message code="inventory.receiptDrug.sgst"/></th>
    <th><spring:message code="inventory.receiptDrug.sgstamt"/></th>
	<th ><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
	<th><spring:message code="inventory.viewStockBalance.receiptIssueDate"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listViewStockBalance}">
	<c:forEach items="${listViewStockBalance}" var="balance" varStatus="varStatus">
	<tr  align="center" class='${balance.expiryLessThan3Month == 1 ?" lessThan3Month " : ""}${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${balance.drug.name}</td>
		<td>${balance.formulation.name}-${balance.formulation.dozage}</td>
		<c:choose>
		<c:when test="${ balance.transaction.typeTransaction==2}">
		<td>${balance.transaction.description}</td>
		</c:when>
		<c:otherwise>
		<td> ${balance.transaction.typeTransactionName}</td>
		</c:otherwise>
		</c:choose>
		<td>${balance.openingBalance}</td>
		<td>${balance.quantity }</td>
		<td>${balance.issueQuantity}</td>
		<td>${balance.closingBalance}</td>
		<td>${rate}</td>
		<td>${unitprice}</td>	
		<td>${vat}</td>
		<td>${balance.mrpPrice}</td>
		<td>${balance.batchNo}</td>
		<td>${discount}</td>
		<td><fmt:formatNumber value="${(rate*balance.closingBalance)*((0.01)*discount)}" maxFractionDigits="2" /></td>
		<td>${cgst}</td>
		<td><fmt:formatNumber value="${((0.01)*cgst*balance.closingBalance)*(unitprice)}" maxFractionDigits="2" /></td>
		<td>${sgst}</td>
		<td><fmt:formatNumber value="${((0.01)*sgst*balance.closingBalance)*(unitprice)}" maxFractionDigits="2" /></td>
		<td><openmrs:formatDate date="${balance.dateExpiry}" type="textbox"/></td>
		<td><openmrs:formatDate date="${balance.createdOn}" type="textbox"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
</div>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div  id="printDiv" style="display: none;">
<div style="margin: 10px auto; width: 1700px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">        		
<br />
<table >
	<tr>
	<td style="width:100%;">
	<table>
	<tr align="left">
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.name"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<th><spring:message code="inventory.viewStockBalance.transaction"/></th>
	<th ><spring:message code="inventory.viewStockBalance.openingBalance"/></th>
	<th ><spring:message code="inventory.viewStockBalance.receiptQty"/></th>
	<th><spring:message code="inventory.viewStockBalance.STTSS"/></th>
	<th ><spring:message code="inventory.receiptDrug.closingBalance"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
	<th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
	<th><spring:message code="inventory.receiptDrug.MRP"/></th>
	<th><spring:message code="inventory.receiptDrug.batchNo"/></th>
	<th><spring:message code="inventory.receiptDrug.Dis"/></th>
	<th><spring:message code="inventory.receiptDrug.DiscountAmount"/></th>
	<th ><spring:message code="inventory.receiptDrug.cgst"/></th>
	<th><spring:message code="inventory.receiptDrug.cgstamt"/></th>
    <th><spring:message code="inventory.receiptDrug.sgst"/></th>
    <th><spring:message code="inventory.receiptDrug.sgstamt"/></th>
	<th ><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
	<th><spring:message code="inventory.viewStockBalance.receiptIssueDate"/></th>
	</tr>
<c:choose>
	<c:when test="${not empty listViewStockBalance}">
	<c:forEach items="${listViewStockBalance}" var="balance" varStatus="varStatus">
	<tr  align="center" class='${balance.expiryLessThan3Month == 1 ?" lessThan3Month " : ""}${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${balance.drug.name}</td>
		<td>${balance.formulation.name}-${balance.formulation.dozage}</td>
		<c:choose>
		<c:when test="${ balance.transaction.typeTransaction==2}">
		<td>${balance.transaction.description}</td>
		</c:when>
		<c:otherwise>
		<td> ${balance.transaction.typeTransactionName}</td>
		</c:otherwise>
		</c:choose>
		<td>${balance.openingBalance}</td>
		<td>${balance.quantity }</td>
		<td>${balance.issueQuantity}</td>
		<td>${balance.closingBalance}</td>
		<td>${rate}</td>
		<td>${unitprice}</td>	
		<td>${vat}</td>
		<td>${balance.mrpPrice}</td>
		<td>${balance.batchNo}</td>
		<td>${discount}</td>
		<td><fmt:formatNumber value="${(rate*balance.closingBalance)*((0.01)*discount)}" maxFractionDigits="2" /></td>
		<td>${cgst}</td>
		<td><fmt:formatNumber value="${((0.01)*cgst*balance.closingBalance)*(unitprice)}" maxFractionDigits="2" /></td>
		<td>${sgst}</td>
		<td><fmt:formatNumber value="${((0.01)*sgst*balance.closingBalance)*(unitprice)}" maxFractionDigits="2" /></td>
		<td><openmrs:formatDate date="${balance.dateExpiry}" type="textbox"/></td>
		<td><openmrs:formatDate date="${balance.createdOn}" type="textbox"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
		</table>
	</td>
	
	</tr>
	</br>
</table> 
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp" %>