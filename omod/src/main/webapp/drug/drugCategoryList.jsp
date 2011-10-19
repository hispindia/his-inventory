<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View drugCategory" otherwise="/login.htm" redirect="/module/inventory/drugCategoryList.form" />

<spring:message var="pageTitle" code="inventory.drugCategory.manage" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="nav.jsp" %>
<h2><spring:message code="inventory.drugCategory.manage"/></h2>	

<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.drugCategory.add'/>" onclick="ACT.go('drugCategory.form');"/>

<br /><br />

<form method="post" onsubmit="return false" id="form">
<table cellpadding="5" cellspacing="0"  >
	<tr>
		<td><spring:message code="general.name"/></td>
		<td><input type="text" id="searchName" name="searchName" value="${searchName}" /></td>
		<td><input type="button" value="Search" onclick="INVENTORY.search('drugCategoryList.form','searchName');"/></td>
	</tr>
</table>

<span class="boxHeader"><spring:message code="inventory.drugCategory.list"/></span>
<div class="box">
<c:choose>
<c:when test="${not empty drugCategories}">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="INVENTORY.checkValue();" value="<spring:message code='inventory.deleteSelected'/>"/>
<table cellpadding="5" cellspacing="0" width="100%">
<tr>
	<th>#</th>
	<th><spring:message code="inventory.drugCategory.name"/></th>
	<th><spring:message code="inventory.drugCategory.description"/></th>
	<th><spring:message code="inventory.drugCategory.createdDate"/></th>
	<th><spring:message code="inventory.drugCategory.createdBy"/></th>
	<th></th>
</tr>
<c:forEach items="${drugCategories}" var="drugCategory" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>	
		<td><a href="#" onclick="ACT.go('drugCategory.form?drugCategoryId=${ drugCategory.id}');">${drugCategory.name}</a> </td>
		<td>${drugCategory.description}</td>
		<td><openmrs:formatDate date="${drugCategory.createdOn}" type="textbox"/></td>
		<td>${drugCategory.createdBy}</td>
		<td><input type="checkbox" name="ids" value="${drugCategory.id}"/></td>
	</tr>
</c:forEach>

<tr class="paging-container">
	<td colspan="6"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</c:when>
<c:otherwise>
	No drugCategory found.
</c:otherwise>


</c:choose>
</div>
</form>


<%@ include file="/WEB-INF/template/footer.jsp" %>