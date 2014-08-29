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

<openmrs:require privilege="View itemSubCategory" otherwise="/login.htm" redirect="/module/inventory/itemSubCategoryList.form" />

<spring:message var="pageTitle" code="inventory.itemSubCategory.manage" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="nav.jsp" %>
<h2><spring:message code="inventory.itemSubCategory.manage"/></h2>	

<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.itemSubCategory.add'/>" onclick="ACT.go('itemSubCategory.form');"/>

<br /><br />
<form method="post" onsubmit="return false" id="form">
<table cellpadding="5" cellspacing="0">
	<tr>
		<td><spring:message code="general.name"/></td>
		<td><input type="text" id="searchName" name="searchName" value="${searchName}" /></td>
		<td><input type="button" value="Search" onclick="INVENTORY.search('itemSubCategoryList.form','searchName');"/></td>
	</tr>
</table>

<span class="boxHeader"><spring:message code="inventory.itemSubCategory.list"/></span>
<div class="box">
<c:choose>
<c:when test="${not empty itemSubCategorys}">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="INVENTORY.checkValue();" value="<spring:message code='inventory.deleteSelected'/>"/>
<table cellpadding="5" cellspacing="0" width="100%">
<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.itemSubCategory.name"/></th>
	<th><spring:message code="inventory.itemSubCategory.code"/></th>
	<th><spring:message code="inventory.itemSubCategory.description"/></th>
	<th><spring:message code="inventory.itemSubCategory.category"/></th>
	<th><spring:message code="inventory.itemSubCategory.createdDate"/></th>
	<th><spring:message code="inventory.itemSubCategory.createdBy"/></th>
	<th></th>
</tr>
<c:forEach items="${itemSubCategorys}" var="itemSubCategory" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>	
		<td><a href="#" onclick="ACT.go('itemSubCategory.form?itemSubCategoryId=${ itemSubCategory.id}');">${itemSubCategory.name}</a> </td>
		<td>${itemSubCategory.code}</td>
		<td>${itemSubCategory.description}</td>
		<td>${itemSubCategory.category.name}</td>
		<td><openmrs:formatDate date="${itemSubCategory.createdOn}" type="textbox"/></td>
		<td>${itemSubCategory.createdBy}</td>
		<td><input type="checkbox" name="ids" value="${itemSubCategory.id}"/></td>
	</tr>
</c:forEach>

<tr class="paging-container">
	<td colspan="8"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</c:when>
<c:otherwise>
	No itemSubCategory found.
</c:otherwise>
</c:choose>
</div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>