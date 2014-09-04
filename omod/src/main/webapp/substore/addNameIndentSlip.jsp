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
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<form method="post" id="formAddNameForPurchase">
<table width="100%">
	<tr>
		<td>Name<em>*</em></td>
		<td><input type="text" name="indentName" id="indentName" size="35"/>
		<input type="hidden" name="send" id="send" value="${send}"/>
		</td>
	</tr>
	<tr>
		<td>
		<spring:message code="inventory.substore.selectMainStore"/> <spring:message code="inventory.substore.indentDrug"/>
		</td>
		<td>
			<select name="mainstore">
			<c:forEach items="${store.parentStores}" var="vparent">
			  <option value="${vparent.id}">${vparent.name}</option>
			</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="2"><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>"></td>
	</tr>
</table>
</form>