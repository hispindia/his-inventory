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

<openmrs:require privilege="Add/Edit Item" otherwise="/login.htm" redirect="/module/inventory/item.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<h2><spring:message code="inventory.item.manage"/></h2>

<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span>
</c:forEach>
<spring:bind path="item">
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
<form method="post" class="box" id="item">
<table>
	<tr>
		<spring:bind path="item.id">
			<input type="hidden" name="${status.expression}" id="${status.expression}" value="${status.value}" />
		</spring:bind>
		<td><spring:message code="inventory.item.name"/><em>*</em></td>
		<td>
			<spring:bind path="item.name">
				<input type="text" id="${status.expression}" name="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.item.unit"/><em>*</em></td>
		<td>
			<spring:bind path="item.unit">
			<select name="${status.expression}" id="${status.expression}"  tabindex="20" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${units}" var="vUnit">
                    <option value="${vUnit.id}" <c:if test="${vUnit.id == item.unit.id }">selected</c:if> >${vUnit.name}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>

	</tr>
	<tr>
		<td><spring:message code="inventory.item.specification"/></td>
		<td><spring:bind path="item.specifications">
			<select ${not empty  delete? 'disabled="disabled"' : ''} name="${status.expression }" id="${status.expression }" multiple="multiple" size="10" style="width:150px">
				<c:forEach items="${specifications}" var="sSpecification">
					<option value="${sSpecification.id}"
						<c:forEach items="${status.value}" var="specification">
							<c:if test="${sSpecification.id == specification.id}"> selected</c:if>
						</c:forEach>
					>${sSpecification.name} 
					</option>
				</c:forEach>
			</select>
			${not empty  delete? 'Specification field is read only in this case' : ''}
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>

	</tr>
	<tr>
		<td><spring:message code="inventory.item.subCategory"/><em>*</em></td>
		<td>
			<div id="divSubCat">
			<spring:bind path="item.subCategory">
			<select name="${status.expression}" id="${status.expression}"  tabindex="20" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${subCategories}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == item.subCategory.id }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
			</div>
		</td>

	</tr>
	<tr>
		<td><spring:message code="inventory.item.attribute"/><em>*</em></td>
		<td>
			<spring:bind path="item.attribute">
			<select name="${status.expression}" id="${status.expression}"   onchange="ITEM.onChangeAttribute(this);">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${attributes}" var="vAttr">
                    <option value="${vAttr.id}" <c:if test="${vAttr.id == item.attribute }">selected</c:if> >${vAttr.name}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>

	</tr>
	<tr  class="depentOnAttribute" <c:if test="${item.attribute != 1 }"> style='display: none;'</c:if> >
		<td><spring:message code="inventory.item.reorderQty"/><em>*</em></td>
		<td>
			<spring:bind path="item.reorderQty">
			<input type="text" id="${status.expression}" name="${status.expression}" value="${status.value}" size="35" />
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
</table>
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>" >
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.cancel"/>" onclick="ACT.go('itemList.form');">
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>