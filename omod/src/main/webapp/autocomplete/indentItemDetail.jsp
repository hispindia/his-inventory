<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<div class="box">
<span class="boxHeader"><spring:message code="inventory.indentItem.detail"/></span>
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>#</th>
	<th><spring:message code="inventory.indentItem.category"/></th>
	<th><spring:message code="inventory.indentItem.name"/></th>
	<th><spring:message code="inventory.indentItem.specification"/></th>
	<th><spring:message code="inventory.indentItem.quantity"/></th>
	<th><spring:message code="inventory.indentItem.transferQuantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listIndentDetail}">
	<c:forEach items="${listIndentDetail}" var="indent" varStatus="varStatus">
	<tr align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count}"/></td>
		<td>${indent.item.category.name} </td>	
		<td>${indent.item.name}</td>
		<td>${indent.specification.name}</td>
		<td>${indent.quantity}</td>
		<td>${indent.mainStoreTransfer}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>	
</table>
</div>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indentItem.print"/>" onClick="INDENT.printDiv();" />
<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; ">
<div style="margin: 10px auto; width: 981px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">        		
<br />
<br />      		
<center style="float:center;font-size: 2.2em">Indent From ${store.name}</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
<table border="1" width="100%">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.indentItem.quantity"/></th>
	<th><spring:message code="inventory.indentItem.transferQuantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listIndentDetail}">
	<c:forEach items="${listIndentDetail}" var="indent" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${indent.item.category.name} </td>	
		<td>${indent.item.name}</td>
		<td>${indent.specification.name}</td>
		<td>${indent.quantity}</td>
		<td>${indent.mainStoreTransfer}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
</center>
<br/><br/><br/><br/><br/><br/>
<span style="float:left;font-size: 1.5em">Signature of sub-store/ Stamp</span><span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
<br/><br/><br/><br/><br/><br/>
<span style="margin-left: 13em;font-size: 1.5em">Signature of Medical Superintendent/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   