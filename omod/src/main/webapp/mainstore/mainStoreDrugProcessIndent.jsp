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

<openmrs:require privilege="Add/Edit mainstore" otherwise="/login.htm" redirect="/module/inventory/main.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<h2><spring:message code="inventory.indent.process"/></h2>
<form method="post" class="box" id="formMainStoreProcessIndent">
<input type="hidden" name="indentId" id="indentId"  value="${indent.id}">
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message code="${error}" /></span><br/>
</c:forEach>
<div class="box">
<span class="boxHeader"><spring:message code="inventory.indent"/></span>
<table>
<tr>
	<td><spring:message code="inventory.indent.name"/></td>
	<td><input type="text" disabled="disabled"  value="${indent.name}" size="50"></td>

</tr>
<tr>
	<td><spring:message code="inventory.indent.fromStore"/></td>
	<td><input type="text" disabled="disabled"  value="${indent.store.name}" size="50"></td>

</tr>
<tr>
	<td><spring:message code="inventory.indent.createdOn"/></td>
	<td><input type="text" disabled="disabled"  value="<openmrs:formatDate date="${indent.createdOn}" type="textbox"/>"> </td>

</tr>
</table>
</div>
<br/>
<div class="box">
<span class="boxHeader"><spring:message code="inventory.indent.processingIndent"/></span>
<table  width="100%" id="tableIndent">
	<tr align="center">
		<th >#</th>
		<th ><spring:message code="inventory.indent.drug"/></th>
		<th  ><spring:message code="inventory.indent.formulation"/></th>
		<th  ><spring:message code="inventory.indent.quantityIndent"/></th>
		<th  ><spring:message code="inventory.indent.transferQuantity"/></th>
		<th  ><spring:message code="inventory.indent.mainStoreQuantity"/></th>
	</tr>
	
	<c:forEach items="${listDrugNeedProcess}" var="drugIndent" varStatus="varStatus">
	
	
	<tr align="center" class='${varStatus.index % 2 == 0 ? "oddRow " : "evenRow" } '>
	<c:choose>
	<c:when test="${not empty drugIndent.mainStoreTransfer && drugIndent.mainStoreTransfer > 0 }">
		<td><c:out value="${varStatus.count }"/></td>
		<td >${drugIndent.drug.name} </td>
		<td >${drugIndent.formulation.name}-${drugIndent.formulation.dozage} </td>

		<td >
		${drugIndent.quantity}
		</td>
		<td >
		<p>
		<em>*</em>
		<input type="text" id="${drugIndent.id}"    name="${drugIndent.id}" size="15" value="${quantityTransfers[varStatus.index] }"  class="required digits" onblur="INDENT.onBlurInput(this,'${drugIndent.quantity}','${drugIndent.mainStoreTransfer}');" />
		</p>
		</td>
		<td >
			${drugIndent.mainStoreTransfer} 
		</td>
	</c:when>
	<c:otherwise>
		<td><del><c:out value="${varStatus.count }"/></del></td>
		<td ><del>${drugIndent.drug.name} </del></td>
		<td ><del>${drugIndent.formulation.name}-${drugIndent.formulation.dozage} </del></td>

		<td >
		<del>${drugIndent.quantity}</del>
		</td>
		<td >
		<p>
		<em>*</em>
		<input type="text" id="${drugIndent.id}"   disabled='disabled' name="${drugIndent.id}" size="15" value="${quantityTransfers[varStatus.index] }"  class="required digits"  />
		</p>
		</td>
		<td >
			<del>${drugIndent.mainStoreTransfer} </del>
		</td>
	</c:otherwise>
	</c:choose>
	</tr>
	</c:forEach>
</table>
</div>
		
<br />		
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.accept"/>">
<input type="hidden" id="refuse" name="refuse" value="">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.refuse"/>" onclick="INDENT.refuseIndentFromMainStore(this);">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.returnList"/>" onclick="ACT.go('transferDrugFromGeneralStore.form');">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
