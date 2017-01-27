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
<spring:message var="pageTitle" code="inventory.viewStockBalanceExpiry.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="nav.jsp" %>

<script type="text/javascript">
var transactionIdList = new Array();

function selectAll(){
if(jQuery("#select").attr('checked')){
transactionIdList = new Array();
<c:forEach var="stockBalance" items="${stockBalances}">
var drugCheck="#drugCheck"+"${stockBalance.id}";
jQuery(drugCheck).attr('checked','checked');
transactionIdList.push(${stockBalance.id}); 
</c:forEach>  
}
else{
transactionIdList = new Array();
<c:forEach var="stockBalance" items="${stockBalances}">
var drugCheck="#drugCheck"+"${stockBalance.id}";
jQuery(drugCheck).attr('checked',false);
</c:forEach>  
}
}


function selectDrug(addId){
var drugCheck="#drugCheck"+addId.toString();
if(jQuery(drugCheck).attr('checked')){
transactionIdList.push(addId);
}
else{
transactionIdList = jQuery.grep(transactionIdList, function(value) {
  return value != addId;
});
}
}

function disableDeleteButton(){
jQuery("#delete").attr('disabled',true);
}
</script>

<h2><spring:message code="inventory.viewStockBalanceExpiry.manage"/></h2>	
<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<br /><br />
<form method="get"  id="form">
<table >
	<tr >
		<td><spring:message code="inventory.receiptDrug.category"/></td>
		<td>
			<select name="categoryId" id="categoryId"  style="width: 250px;">
      			<option value=""></option>
				<c:forEach items="${listCategory}" var="category">
					<option value="${category.id}" title="${category.name}"
					<c:if test="${category.id == categoryId }">selected</c:if>
					>${category.name}</option>
				</c:forEach>
	   		</select>
		</td>
		<td><spring:message code="inventory.receiptDrug.drugName"/></td>
		<td>
			<input type="text" name="drugName" id="drugName" value="${drugName }"/>
		</td>
		<!--<td><spring:message code="inventory.fromDate"/></td>
		<td><input type="text" id="fromDate" class="date-pick left" readonly="readonly" name="fromDate" value="${fromDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><spring:message code="inventory.toDate"/></td>
		<td><input type="text" id="toDate" class="date-pick left" readonly="readonly" name="toDate" value="${toDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		-->
		<td><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search"/></td>
	</tr>
</table>
<span class="boxHeader"><spring:message code="inventory.viewStockBalance.list"/></span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr  align="center" >
	<th>#</th>
	<th><spring:message code="inventory.viewStockBalance.name"/></th>
	<th><spring:message code="inventory.viewStockBalance.category"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<!--
		<th ><spring:message code="inventory.viewStockBalance.receiptQty"/></th>
		<th><spring:message code="inventory.viewStockBalance.STTSS"/></th>
	-->
	<th ><spring:message code="inventory.receiptDrug.currentQuantity"/></th>
	<th ><spring:message code="inventory.viewStockBalance.reorderPoint"/></th>
	<th><input type="checkbox" id="select" value="select" onClick="selectAll();" />Select All</th>
	<th><input type="button" style="float: right" id="delete" name="delete" value="Delete" onclick="disableDeleteButton();javascript:window.location.href='expireDrug.form?transactionIdList='+transactionIdList"  /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty stockBalances}">
	<c:forEach items="${stockBalances}" var="balance" varStatus="varStatus">
	<tr  align="center"  class='${balance.currentQuantity < balance.drug.reorderQty ?" reorder " : ""}${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }' >
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td><a href="#" onclick="STOCKBALLANCE.detailExpiry('${balance.drug.id}', '${balance.formulation.id}');" title="Detail all transactions of this drug">${balance.drug.name}</td>
		<td>${balance.drug.category.name} </td>	
		<td>${balance.formulation.name}-${balance.formulation.dozage}</td>
		<!--
		<td>${balance.quantity}</td>
		<td>${balance.issueQuantity}</td>
		-->
		<td>${balance.currentQuantity}</td>
		<td>${balance.drug.reorderQty}</td>
		<td><input type="checkbox"  id="drugCheck${balance.id}" name="drugCheck${balance.id}" onclick="selectDrug(${balance.id});"/>
		</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
	
	<tr class="paging-container">
	<td colspan="6"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</div>

</form>




<%@ include file="/WEB-INF/template/footer.jsp" %>