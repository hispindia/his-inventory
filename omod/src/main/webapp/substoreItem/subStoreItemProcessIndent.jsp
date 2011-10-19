<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<h2><spring:message code="inventory.indentItem.process"/></h2>
<form method="post" class="box" id="formSubStoreItemProcessIndent">
<input type="hidden" name="indentId" id="indentId"  value="${indent.id}">
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message code="${error}" /></span><br/>
</c:forEach>
<table>
<tr>
	<td><spring:message code="inventory.indentItem.name"/></td>
	<td><input type="text" disabled="disabled"  value="${indent.name}" size="50"></td>

</tr>
<tr>
	<td><spring:message code="inventory.indentItem.createdOn"/></td>
	<td><input type="text" disabled="disabled"  value="<openmrs:formatDate date="${indent.createdOn}" type="textbox"/>"> </td>

</tr>
</table>
<table class="box" width="100%" id="tableIndent">
	<tr align="center">
		<th >#</th>
		<th ><spring:message code="inventory.indentItem.item"/></th>
		<th  ><spring:message code="inventory.indentItem.specification"/></th>
		<th  ><spring:message code="inventory.indentItem.quantityIndent"/></th>
		<th  ><spring:message code="inventory.indentItem.transferQuantity"/></th>
	</tr>
	
	<c:forEach items="${listItemNeedProcess}" var="itemIndent" varStatus="varStatus">
	<tr align="center" class='${varStatus.index % 2 == 0 ? "oddRow " : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td >${itemIndent.item.name} </td>
		<td >${itemIndent.specification.name}</td>

		<td >
		${itemIndent.quantity}
		</td>
		<td >
			${itemIndent.mainStoreTransfer} 
		</td>
	</tr>
	</c:forEach>
</table>
		
		
<br />		
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indentItem.receipt"/>">
<input type="hidden" id="refuse" name="refuse" value="">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indentItem.refuse"/>" onclick="INDENT.refuseIndentFromSubStoreItem(this);">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.returnList"/>" onclick="ACT.go('subStoreIndentItemList.form');">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
