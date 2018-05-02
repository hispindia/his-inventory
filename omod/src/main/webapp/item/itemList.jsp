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

<openmrs:require privilege="View Item" otherwise="/login.htm" redirect="/module/inventory/itemList.form" />

<spring:message var="pageTitle" code="inventory.item.manage" scope="page"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="nav.jsp" %>
<h2><spring:message code="inventory.item.manage"/></h2>	

<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.item.add'/>" onclick="ACT.go('item.form');"/>

<br /><br />

<form method="post" onsubmit="return false"  id="form">
<table cellpadding="5" cellspacing="0">
	<tr>
	    <td><spring:message code="inventory.item.subCategory"/></td>
		<td>
			<select name="categoryId" id="categoryId"   style="width: 250px;">
				<option value=""></option>
                <c:forEach items="${categories}" var="vCat">
                    <option value="${vCat.id}"  <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>

		</td>
		<td><spring:message code="general.name"/></td>
		<td><input type="text" id="searchName" name="searchName" value="${searchName}" /></td>
		<td><input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search" onclick="ITEM.searchItem(this);"/>
		
		</td>
	</tr>
</table>
<span class="boxHeader"><spring:message code="inventory.item.list"/></span>
<div class="box">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="INVENTORY.checkValue();" value="<spring:message code='inventory.deleteSelected'/>"/>
<c:choose>
<c:when test="${not empty items}">
<table cellpadding="5" cellspacing="0" width="100%">
<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.unit"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.attribute"/></th>
	<th><spring:message code="inventory.item.reorderQty"/></th>
	<th><spring:message code="inventory.item.createdDate"/></th>
	<th><spring:message code="inventory.item.createdBy"/></th>
	<th></th>
</tr>
<c:forEach items="${items}" var="item" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>	
		<td title="${item.name}"><a href="#" onclick="ACT.go('item.form?itemId=${ item.id}');">${item.nameShort}</a> </td>
		<td>${item.unit.name}</td>
		<td>
			<c:forEach items="${item.specifications}" var="specification" varStatus="status">
				${specification.name}<br/>
			</c:forEach>
		</td>
		<td>${item.category.name}</td>
		<td>${item.subCategory.name}</td>
		<td>${item.attributeName}</td>
		<td>${item.reorderQty > 0 ? item.reorderQty : ''}</td>
		<td><openmrs:formatDate date="${item.createdOn}" type="textbox"/></td>
		<td>${item.createdBy}</td>
		<td><input type="checkbox" name="ids" value="${item.id}"/></td>
	</tr>
</c:forEach>
<tr class="paging-container">
	<td colspan="11"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</c:when>
<c:otherwise>
	No item found.
</c:otherwise>
</c:choose>
</div>
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>