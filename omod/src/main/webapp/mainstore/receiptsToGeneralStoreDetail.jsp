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
<span class="boxHeader"><spring:message code="inventory.receiptDrug.receiptDrugList"/></span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<td style="width:100%;">
	<table>
	<tr align="left">
	<th>#</th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.receiptQuantity"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
    <th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
	<th><spring:message code="inventory.receiptDrug.MRP"/></th>
	<th><spring:message code="inventory.receiptDrug.batchNo"/></th>
    <th>CD(%)</th>
    <th>CD Amount</th>
    <th>CGST(%)</th>
    <th>CGST Amount</th>
    <th>SGST(%)</th>
    <th>SGST Amount</th>
	<th>DE</th>
	<th>Total Amount</th>
	<th>Amount After GST</th>
	</tr>
	
	<c:choose>
	<c:when test="${not empty transactionDetails}">
	<c:forEach items="${transactionDetails}" var="receipt" varStatus="varStatus">
	<tr align="left" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${receipt.drug.name}</td>
		<td>${receipt.formulation.name}-${receipt.formulation.dozage}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.rate}</td>
       <td>${receipt.unitPrice}</td>
		<c:choose>
		<c:when test="${not empty receipt.VAT}" >
		<td>${receipt.VAT}</td>
		</c:when>
		<c:otherwise>
		<td>NA</td>
		</c:otherwise>
		</c:choose>
		<td>${receipt.mrpPrice}</td>
		<td>${receipt.batchNo}</td>
		<td>${receipt.waiverPercentage}</td>
		<td>${receipt.waiverAmount}</td>
        <c:choose>
        <c:when test="${not empty receipt.cgst}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${not empty receipt.cgstAmount}" >
         <td>${receipt.cgstAmount}</td>
        </c:when>
        <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
	        <c:choose>
		<c:when test="${not empty receipt.sgst}" >
        <td>${receipt.sgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${not empty receipt.sgstAmount}" >
         <td>${receipt.sgstAmount}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
		<td><openmrs:formatDate date="${receipt.dateExpiry}" type="textbox"/></td>

		<td>${receipt.totalPrice}</td>
		<td>${receipt.totalAmountAfterGst}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
	</table>
	</td>
	
	</tr>
<tr>
<td>
</br>
<table style="width:20%;float:right;">
<tr>
<td style="text-align: center;">Total Cash Discount</td>
<td style="text-align: center;">${totCDamount }</td>
</tr>
<tr>
<td style="text-align: center;">Total CGST</td>
<td style="text-align: center;">${totcgstAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total SGST</td>
<td style="text-align: center;">${totsgstAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total Amount</td>
<td style="text-align: center;">${totAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total Amount After GST</td>
<td style="text-align: center;">${totAmountafterGst }</td>
</tr>
</table>
</td>
</tr>

</table>
</div>

<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div  id="printDiv" style="display: none;">
<div style="margin: 10px auto; width: 1700px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">        		
<br />
<br />      		
<center style="float:center;font-size: 2.2em">Name Of The Vendor : ${vendorName}</center>
<br/>
<br/>
<span style="float:left;font-size: 1.7em">Receipt No: ${receiptNo}</span>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
<table >
	<tr>
	<td style="width:100%;">
	<table>
	<tr align="left">
	<th>#</th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.quantity"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
	<th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
    <th><spring:message code="inventory.receiptDrug.MRP"/></th>
    <th><spring:message code="inventory.receiptDrug.batchNo"/></th>
    <th>CD(%)</th>
    <th>CD Amount</th>
    <th>CGST(%)</th>
    <th>CGST Amount</th>
    <th>SGST(%)</th>
    <th>SGST Amount</th>
	<th>DE</th>
	<th>Total Amount</th>
	<th>Amount After GST</th>
	</tr>
	<c:choose>
	<c:when test="${not empty transactionDetails}">
	<c:forEach items="${transactionDetails}" var="receipt" varStatus="varStatus">
	<tr align="left" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td>${receipt.drug.name}</td>
		<td>${receipt.formulation.name}-${receipt.formulation.dozage}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.rate}</td>
		<td>${receipt.unitPrice}</td>
		<c:choose>
		<c:when test="${not empty receipt.VAT}" >
		<td>${receipt.VAT}</td>
		</c:when>
		<c:otherwise>
		<td>NA</td>
		</c:otherwise>
		</c:choose>
		<td>${receipt.mrpPrice}</td>
		<td>${receipt.batchNo}</td>
		<td>${receipt.waiverPercentage}</td>
		<td>${receipt.waiverAmount}</td>
	        <c:choose>
        <c:when test="${not empty receipt.cgst}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${not empty receipt.cgstAmount}" >
         <td>${receipt.cgstAmount}</td>
        </c:when>
        <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
		         <c:choose>
        <c:when test="${not empty receipt.sgst}" >
         <td>${receipt.sgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
		 <c:choose>
        <c:when test="${not empty receipt.sgstAmount}" >
         <td>${receipt.sgstAmount}</td>
        </c:when>
        <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
		<td><openmrs:formatDate date="${receipt.dateExpiry}" type="textbox"/></td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.totalAmountAfterGst}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
		</table>
	</td>
	
	</tr>
	</br>
<tr>
<td>

<table style="width:20%;float:right;">
<tr>
<td style="text-align: center;">Total Cash Discount</td>
<td style="text-align: center;">${totCDamount }</td>
</tr>
<tr>
<td style="text-align: center;">Total CGST</td>
<td style="text-align: center;">${totcgstAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total SGST</td>
<td style="text-align: center;">${totsgstAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total Amount</td>
<td style="text-align: center;">${totAmount }</td>
</tr>
<tr>
<td style="text-align: center;">Total Amount After GST</td>
<td style="text-align: center;">${totAmountafterGst }</td>
</tr>
</table>
	</td>
	</tr>
</table> 
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   
