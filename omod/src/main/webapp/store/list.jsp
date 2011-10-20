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

<openmrs:require privilege="View Store" otherwise="/login.htm" redirect="/module/inventory/storeList.form" />

<spring:message var="pageTitle" code="inventory.store.manage" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="../includes/nav.jsp" %>

<h2><spring:message code="inventory.store.manage"/></h2>	

<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.store.add'/>" onclick="ACT.go('store.form');"/>

<br /><br />
<form method="post" onsubmit="return false" id="form">
<span class="boxHeader"><spring:message code="inventory.store.list"/></span>
<div class="box">
<c:choose>
<c:when test="${not empty stores}">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="INVENTORY.checkValue();" value="<spring:message code='inventory.deleteSelected'/>"/>
<table cellpadding="5" cellspacing="0" width="100%">
<tr>
	<th>#</th>
	<th><spring:message code="general.name"/></th>
	<th><spring:message code="inventory.store.code"/></th>
	<th><spring:message code="inventory.store.parent"/></th>
	<th><spring:message code="inventory.store.role"/></th>
	<th><spring:message code="inventory.store.isDrug"/></th>
	<th><spring:message code="inventory.store.retired"/></th>
	<th><spring:message code="inventory.store.createdDate"/></th>
	<th><spring:message code="inventory.store.createdBy"/></th>
	<th></th>
</tr>
<c:forEach items="${stores}" var="store" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>	
		<td><a href="#" onclick="STORE.edit('${ store.id}');">${store.name}</a> </td>
		<td>${store.code}</td>
		<td>${store.parent.name}</td>
		<td>${store.role.role}</td>
		<td>${store.isDrugName}</td>
		<td>${store.retired}</td>
		<td><openmrs:formatDate date="${store.createdOn}" type="textbox"/></td>
		<td>${store.createdBy}</td>
		<td><input type="checkbox" name="ids" value="${store.id}"/></td>
	</tr>
</c:forEach>

<tr class="paging-container">
	<td colspan="9"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</c:when>
<c:otherwise>
	No Store found.
</c:otherwise>
</c:choose>
</div>
</form>



<%@ include file="/WEB-INF/template/footer.jsp" %>
