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

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />

<spring:message var="pageTitle" code="inventory.indentItem.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<%@ include file="nav.jsp" %>
<h2><spring:message code="inventory.indentItem.manage"/></h2>	

<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>

<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code='inventory.indentItem.add'/>" onclick="ACT.go('subStoreIndentItem.form');"/>

<br /><br />
<form method="get"  id="formSubStoreIndentList">
<table >
	<tr>
		<td><spring:message code="inventory.indentItem.status"/></td>
		<td>
			<select name="statusId" >
      		<option value=""><spring:message code="inventory.pleaseSelect"/></option>
			<c:forEach items="${listSubStoreStatus}" var="status">
				<option value="${status.id}" 
				<c:if test="${status.id == statusId }">selected</c:if>
				>${status.name}</option>
			</c:forEach>
	   </select>
	   </td>
		<td><spring:message code="inventory.indentItem.name"/></td>
		<td><input type="text" id="indentName" name="indentName" value="${indentName}" /></td>
		<td><spring:message code="inventory.fromDate"/></td>
		<td><input type="text" id="fromDate" class="date-pick left" readonly="readonly" name="fromDate" value="${fromDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><spring:message code="inventory.toDate"/></td>
		<td><input type="text" id="toDate" class="date-pick left" readonly="readonly" name="toDate" value="${toDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search" /></td>
	</tr>
</table>

<span class="boxHeader"><spring:message code="inventory.indentItem.list"/></span>
<div class="box">
<c:choose>
<c:when test="${not empty listIndent}">
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<th >#</th>
	<th align="center" ><spring:message code="inventory.indentItem.name"/></th>
	<th align="center" ><spring:message code="inventory.indentItem.createdOn"/></th>
	<th align="center" ><spring:message code="inventory.indentItem.status"/></th>
	<th></th>
	
</tr>
<c:forEach items="${listIndent}" var="indent" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td align="center"><a href="#" title="Detail indent" onclick="INDENT.detailItemIndent('${ indent.id}');">${indent.name}</a> </td>
		<td align="center"><openmrs:formatDate date="${indent.createdOn}" type="textbox"/> </td>
		<td align="center">${indent.subStoreStatusName} </td>
		<td>
		<c:if test="${indent.subStoreStatus == 1 }">
			<a href="#" onclick="INDENT.sendToMainStoreItem('${ indent.id}');"><spring:message code="inventory.indentItem.send"/></a>
		</c:if>
		<c:if test="${indent.subStoreStatus == 3 }">
			<a href="#" onclick="ACT.go('subStoreItemProcessIndent.form?indentId=${ indent.id}');"><spring:message code="inventory.indentItem.process"/></a>
		</c:if>
	
		</td>
	</tr>
</c:forEach>






<tr class="paging-container">
	<td colspan="5"><%@ include file="../paging.jsp" %></td>
</tr>
</table>

<br>
</c:when>
<c:otherwise>
	No indent found.
</c:otherwise>
</c:choose>
</div>
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>