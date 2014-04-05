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

<openmrs:require privilege="Add/Edit Store" otherwise="/login.htm" redirect="/module/inventory/store.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<h2><spring:message code="inventory.store.manage"/></h2>

<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span>
</c:forEach>
<spring:bind path="store">
<c:if test="${not empty  status.errorMessages}">
<div class="error">
<ul>
<c:forEach items="${status.errorMessages}" var="error">
    <li>${error}</li>   
</c:forEach>
</ul>
</div>
</c:if>
</spring:bind>
<form method="post" class="box" id="inventoryStore">
<spring:bind path="store.id">
	<input type="hidden" name="${status.expression}" id="${status.expression}" value="${status.value}" />
</spring:bind>
<table>
	<tr>
		<td><spring:message code="general.name"/><em>*</em></td>
		<td>
			<spring:bind path="store.name">
				
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.store.code"/><em>*</em></td>
		<td>
			<spring:bind path="store.code">
				
				
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.store.parent"/></td>
		<td>
			<spring:bind path="store.parentStores">
			<select name="parent"  tabindex="20" multiple>
				<option value=""></option>
                <c:forEach items="${parents}" var="vparent">
                   <option value="${vparent.id}" 
                   <c:forEach items="${store.parentStores}" var="vselectedParent">
                        <c:if test="${vselectedParent.id == vparent.id}">selected</c:if>
                   </c:forEach>>${vparent.name}</option>
				  
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.store.role"/><em>*</em></td>
		<td>
			<spring:bind path="store.role">
			<select name="${status.expression}" id="${status.expression}" tabindex="20" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${roles}" var="rl">
                    <option value="${rl.role}"  <c:if test="${rl.role == store.role.role}"> selected</c:if> >${rl.role}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.store.isDrug"/><em>*</em></td>
		<td>
			<spring:bind path="store.isDrug">
			<select name="${status.expression}" id="${status.expression}" tabindex="20" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listIsDrug}" var="dr">
                    <option value="${dr.id}"  <c:if test="${dr.id == store.isDrug }"> selected</c:if> >${dr.name}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="general.retired" /></td>
		<td>
			<spring:bind path="store.retired">
				<openmrs:fieldGen type="java.lang.Boolean" formFieldName="${status.expression}" val="${status.value}" parameters="isNullable=false" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
</table>
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.cancel"/>" onclick="ACT.go('storeList.form');">
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>