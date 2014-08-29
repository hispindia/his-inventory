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
<spring:message var="pageTitle" code="inventory.receiptItem.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<div style="width: 25%; float: left; margin-left: 4px; ">
<b class="boxHeader">Item</b>
<div class="box">

<form method="post" id="receiptItem">
<br/>

<table class="box">
<tr><td><b>Item Info</b></td></tr>
<tr>
		<td><spring:message code="inventory.item.subCategory"/><em>*</em></td>
		<td>
			<select name="category" id="category" onchange="RECEIPT.onChangeCategoryItem(this);"   style="width: 200px;">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
		

	</tr>
	<tr>
		<td>Item Name<em>*</em></td>
		<td>
			<div id="divItem">
				<select id="itemId" name="itemId" onchange="RECEIPT.onBlurItem(this);"  style="width: 200px;">
					<option value=""><spring:message code="inventory.pleaseSelect"/></option>
					   <c:if test ="${not empty items }">
					       <c:forEach items="${items}" var="item">
					           <option value="${item.id}" title="${item.name}">${item.name}</option>
					       </c:forEach>
				       </c:if>
				</select>
			</div>
		</td>
	</tr>
	<tr id="divSpecification" >
		<td></td>
		<td></td>
	</tr>
</table>
<br/>
<table class="box">
	<tr>
		<td><spring:message code="inventory.receiptItem.quantity"/><em>*</em></td>
		<td>
			<input type="text" id="quantity" name="quantity" />
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptItem.unitPrice"/><em>*</em></td>
		<td>
			<input type="text" id="unitPrice" name="unitPrice" />
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptItem.VAT"/><em>*</em></td>
		<td>
			<input type="text" id="VAT" name="VAT" />
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptItem.companyName"/></td>
		<td>
			<input type="text" id="companyName" name="companyName" />
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptItem.dateManufacture"/></td>
		<td>
			<input type="text" id="dateManufacture" name="dateManufacture" class="date-pick left" readonly="readonly"  ondblclick="this.value='';"/>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptItem.receiptDate"/><em>*</em></td>
		<td>
			<input type="text" id="receiptDate" name="receiptDate" class="date-pick left" readonly="readonly"  ondblclick="this.value='';"/>
		</td>
	</tr>
</table>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.addToSlip"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('itemReceiptsToGeneralStoreList.form');">
</form>
</div>
</div>
<!-- Receipt list -->  <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --Change case of word Slip-->
<div style="width: 73%; float: right; margin-right: 16px; ">
<b class="boxHeader">Receipt Slip</b>
<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.unitPrice"/></th>
	<th><spring:message code="inventory.receiptItem.VAT"/></th>
	<th><spring:message code="inventory.receiptItem.totalPrice"/></th>
	<th title="<spring:message code="inventory.receiptItem.companyName"/>">CN</th>
	<th title="<spring:message code="inventory.receiptItem.dateManufacture"/>">DM</th>
	<th title="<spring:message code="inventory.receiptItem.receiptDate"/>">RD</th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceipt}">
	<c:forEach items="${listReceipt}" var="receipt" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${receipt.item.category.name} </td>	
		<td><a href="#" title="Remove this" onclick="INVENTORY.removeObject('${varStatus.index}','6');">${receipt.item.name}</a></td>
		<td>${receipt.specification.name}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.unitPrice}</td>
		<td>${receipt.VAT}</td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.companyName}</td>
		<td><openmrs:formatDate date="${receipt.dateManufacture}" type="textbox"/></td>
		<td><openmrs:formatDate date="${receipt.receiptDate}" type="textbox"/></td>
		</tr>
	</c:forEach>
	
	</c:when>
	</c:choose>
</table>
<br/>
	<c:if  test="${not empty listReceipt}">
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.finish"/>" onclick="RECEIPT.receiptSlipItem('0');" />
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.clear"/>"  onclick="RECEIPT.receiptSlipItem('1');"/>
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.print"/>" onClick="RECEIPT.printDiv();" />
			</td>
		</tr>
		</table>
	</c:if>
</div>
</div>
<!-- PRINT DIV -->
<div  id="printDiv" style="display: none;">
<div style="margin: 10px auto; width: 981px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">        		
<br />
<br />      		
<center style="float:center;font-size: 2.2em">${store.name} - Receipt - Items</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
<table border="1">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.item.category"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	<th><spring:message code="inventory.receiptItem.unitPrice"/></th>
	<th><spring:message code="inventory.receiptItem.VAT"/></th>
	<th><spring:message code="inventory.receiptItem.totalPrice"/></th>
	<th><spring:message code="inventory.receiptItem.companyName"/></th>
	<th><spring:message code="inventory.receiptItem.dateManufacture"/></th>
	<th><spring:message code="inventory.receiptItem.receiptDate"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceipt}">
	<c:forEach items="${listReceipt}" var="receipt" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count }"/></td>
		<td>${receipt.item.category.name} </td>	
		<td>${receipt.item.name}</td>
		<td>${receipt.specification.name}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.unitPrice}</td>
		<td>${receipt.VAT}</td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.companyName}</td>
		<td><openmrs:formatDate date="${receipt.dateManufacture}" type="textbox"/></td>
		<td><openmrs:formatDate date="${receipt.receiptDate}" type="textbox"/></td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   

 
<%@ include file="/WEB-INF/template/footer.jsp" %>