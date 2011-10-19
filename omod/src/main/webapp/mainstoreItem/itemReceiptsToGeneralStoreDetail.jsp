<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<span class="boxHeader"><spring:message code="inventory.receiptItem.receiptItemList"/></span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.unitPrice"/></th>
	<th><spring:message code="inventory.receiptItem.VAT"/></th>
	<th><spring:message code="inventory.receiptItem.totalPrice"/></th>
	<th title="<spring:message code="inventory.receiptItem.companyName"/>">CN</th>
	<th title="<spring:message code="inventory.receiptItem.dateManufacture"/>">DM</th>
	<th title="<spring:message code="inventory.receiptItem.receiptDate"/>">RD</th>
	</tr>
	<c:choose>
	<c:when test="${not empty transactionDetails}">
	<c:forEach items="${transactionDetails}" var="receipt" varStatus="varStatus">
	<tr align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count }"/></td>
		<td>${receipt.item.subCategory.name} </td>	
		<td>${receipt.item.name}</td>
		<td>${receipt.specification.name}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.unitPrice}</td>
		<td>${receipt.VAT}</td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.companyName}</td>
		<td><openmrs:formatDate date="${receipt.dateManufacture}" type="textbox"/></td>
		<td><openmrs:formatDate date="${receipt.receiptDate}" type="textbox"/></td>
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
<br />
<br />      		
<center style="float:center;font-size: 2.2em">${store.name} - Receipt - Items</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
<table border="1">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.unitPrice"/></th>
	<th><spring:message code="inventory.receiptItem.VAT"/></th>
	<th><spring:message code="inventory.receiptItem.totalPrice"/></th>
	<th>CN</th>
	<th>DM</th>
	<th>DR</th>
	</tr>
	<c:choose>
	<c:when test="${not empty transactionDetails}">
	<c:forEach items="${transactionDetails}" var="receipt" varStatus="varStatus">
	<tr align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count }"/></td>
		<td>${receipt.item.subCategory.name} </td>	
		<td>${receipt.item.name}</td>
		<td>${receipt.specification.name}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.unitPrice}</td>
		<td>${receipt.VAT}</td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.companyName}</td>
		<td><openmrs:formatDate date="${receipt.dateManufacture}" type="textbox"/></td>
		<td><openmrs:formatDate date="${receipt.receiptDate}" type="textbox"/></td>
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
