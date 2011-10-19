<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit mainstore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.purchaseItem.manager" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<div style="width: 45%; float: left; margin-left: 4px; ">
<b class="boxHeader">Item</b>
<div class="box">

<form method="post" id="purchaseOrderItem">
<br/>

<table class="box">
<tr><td><b>Item info</b></td></tr>
<tr>
		<td><spring:message code="inventory.item.subCategory"/><em>*</em></td>
		<td>
			<select name="category" id="category" onchange="PURCHASE.onChangeCategoryItem(this);" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.item.name"/><em>*</em></td>
		<td>
			<input type="text" id="itemName" name="itemName" onblur="PURCHASE.onBlurItem(this);" style="width:350px;"/>
		</td>
	</tr>
	<tr id="divSpecification">
		<td></td>
		<td></td>
	</tr>
</table>
<br/>
<table class="box">
	<tr>
		<td><spring:message code="inventory.purchaseItem.quantity"/><em>*</em></td>
		<td>
			<input type="text" id="quantity" name="quantity" />
		</td>
	</tr>
</table>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.purchaseItem.addToSlip"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('itemPurchaseOrderForGeneralStoreList.form');">
</form>
</div>
</div>
<!-- Purchase list -->
<div style="width: 53%; float: right; margin-right: 16px; ">
<b class="boxHeader">Purchase order item for general store</b>
<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listPurchase}">
	<c:forEach items="${listPurchase}" var="purchase" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${purchase.item.category.name} </td>	
		<td>${purchase.item.name}</td>
		<td>${purchase.specification.name}</td>
		<td>${purchase.quantity}</td>
		</tr>
	</c:forEach>
	
	</c:when>
	</c:choose>
</table>
<br/>
	<c:if  test="${not empty listPurchase}">
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
			<td> 
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.purchaseItem.finish"/>" onclick="PURCHASE.processSlipItem('0');" />
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.purchaseItem.clear"/>"  onclick="PURCHASE.processSlipItem('1');"/>
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.purchaseItem.print"/>" onClick="PURCHASE.printDiv();" />
			</td>
		</tr>
		</table>
	</c:if>
</div>
</div>
<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; margin: 10px auto; width: 981px; font-size: 1.5em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">        		

<center>
Purchase Order Item For General Store
</center>
<table border="1">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.purchaseItem.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listPurchase}">
	<c:forEach items="${listPurchase}" var="purchase" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td>${purchase.item.category.name} </td>	
		<td>${purchase.item.name}</td>
		<td>${purchase.specification.name}</td>
		<td>${purchase.quantity}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
<!-- END PRINT DIV -->   

 
<%@ include file="/WEB-INF/template/footer.jsp" %>