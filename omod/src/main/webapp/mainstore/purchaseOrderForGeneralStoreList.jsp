<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit mainstore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.purchase.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="nav.jsp" %>
<h2><spring:message code="inventory.purchase.manage"/></h2>	
<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.purchase.add'/>" onclick="ACT.go('purchaseOrderForGeneralStore.form');"/>
<br /><br />

<form method="get"  id="form">
<table >
	<tr>
		<td><spring:message code="inventory.purchase.name"/></td>
		<td>
			<input type="text" name="indentName" id="indentName" value="${indentName }"/>
		</td>
		<td><spring:message code="inventory.fromDate"/></td>
		<td><input type="text" id="fromDate" class="date-pick left" readonly="readonly" name="fromDate" value="${fromDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><spring:message code="inventory.toDate"/></td>
		<td><input type="text" id="toDate" class="date-pick left" readonly="readonly" name="toDate" value="${toDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search"/></td>
	</tr>
</table>
<br />
<span class="boxHeader"><spring:message code="inventory.purchase.list"/></span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.purchase.name"/></th>
	<th><spring:message code="inventory.purchase.createdOn"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty purchases}">
	<c:forEach items="${purchases}" var="purchase" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td> <a href="#" title="Detail indent" onclick="INDENT.detailDrugIndent('${ purchase.id}');">${purchase.name}</a> </td>	
		<td><openmrs:formatDate date="${purchase.createdOn}" type="textbox"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
	
	<tr class="paging-container">
	<td colspan="12"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</div>

</form>




<%@ include file="/WEB-INF/template/footer.jsp" %>