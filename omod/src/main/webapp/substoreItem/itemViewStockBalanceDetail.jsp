<%@ include file="/WEB-INF/template/include.jsp" %>

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
	<th><spring:message code="inventory.viewStockBalance.subCategory"/></th>
	<th><spring:message code="inventory.viewStockBalance.specification"/></th>
	<th><spring:message code="inventory.viewStockBalance.transaction"/></th>
	<th ><spring:message code="inventory.viewStockBalance.openingBalance"/></th>
	<th ><spring:message code="inventory.viewStockBalance.receiptQty"/></th>
	<th><spring:message code="inventory.viewStockBalance.STTSS"/></th>
	<th ><spring:message code="inventory.receiptItem.closingBalance"/></th>
	<th><spring:message code="inventory.viewStockBalance.receiptIssueDate"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listViewStockBalance}">
	<c:forEach items="${listViewStockBalance}" var="balance" varStatus="varStatus">
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${balance.item.name}</td>
		<td>${balance.item.subCategory.name} </td>	
		<td>${balance.specification.name}</td>
		<td>${balance.transaction.typeTransactionName}</td>
		<td>${balance.openingBalance}</td>
		<td>${balance.quantity }</td>
		<td>${balance.issueQuantity}</td>
		<td>${balance.closingBalance}</td>
		<td><openmrs:formatDate date="${balance.createdOn}" type="textbox"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
</div>
<%@ include file="/WEB-INF/template/footer.jsp" %>