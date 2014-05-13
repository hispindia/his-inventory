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
<spring:message var="pageTitle" code="inventory.issueItem.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<div style="width: 40%; float: left; margin-left: 4px; ">

<b class="boxHeader">Item</b>
<div class="box">

<form method="post" id="formIssueItem"  onsubmit="return false" >
<c:if  test="${not empty errors}">
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message code="${error}" /></span>
</c:forEach>
</c:if>
<br/>
<table class="box">
<tr><td><b>Item info</b></td></tr>
<tr>
		<td><spring:message code="inventory.item.subCategory"/><em>*</em></td>
		<td>
			<select name="category" id="category" onchange="ISSUE.onChangeCategoryItem(this);"  style="width: 250px;">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
	</tr>
	<tr>
		<td>Item<em>*</em></td>
		<td>
			<div id="divItem">
				<select id="itemId" name="itemId" onchange="ISSUE.onBlurItem(this);"  style="width: 250px;">
					<option value=""><spring:message code="inventory.pleaseSelect"/></option>
					   <c:if test ="${not empty items}">
					       <c:forEach items="${items}" var="item">
					           <option value="${item.id}" >${item.name}</option>
					       </c:forEach>
				       </c:if>
				</select>
			</div>
		</td>
	</tr>
	<tr id="divSpecification">
		<td></td>
		<td></td>
	</tr>
</table>
<br/>
<div id="divItemAvailable">

<!--<table class="box" width="100%">
	<tr align="center">
		<th>Quantity available</th>
		<th>Issue quantity</th>
	</tr>
	<c:if  test="${sumReceiptItem > 0}">
	<tr align="center">
		<td>${sumReceiptItem}</td>
		<td><input type="hidden" id="currentQuantity" name="currentQuantity" value="${sumReceiptItem}"  /><input type="text" id="issueItemQuantity" name="issueItemQuantity" class="required digits" size="5"/></td>
	</tr>
	
	</c:if>
	<c:if  test="${sumReceiptItem <= 0}">
	<tr align="center">
		<td colspan="2">This item is empty in your store please indent it
		<input type="hidden" id="issueItemQuantityA" name="issueItemQuantityA" class="required digits" size="5"/>
		</td>
		
	</tr>	
	</c:if>
</table>

--></div>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="ISSUE.checkQtyBeforeIssue(this);" value="<spring:message code="inventory.issueItem.addToSlip"/>">
			<c:if test="${empty issueItemPatient}">
				<input type="button"
					class="ui-button ui-widget ui-state-default ui-corner-all"
					value="Find Patient"
					onclick="ISSUE.createPatientForItem();">
			</c:if>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('subStoreIssueItemList.form');">
</form>
</div>
</div>
<!-- Purchase list -->
<div style="width: 58%; float: right; margin-right: 16px; ">
<b class="boxHeader">Issue item to patient slip</b>

<div class="box">
	<c:if test="${not empty issueItemPatient}">
		<table class="box" width="100%">
			<tr>
				<th>Identifier</th>
				<th>Category</th> 
				<th>Name</th>
				<th>Age</th>
			</tr>
			<tr>
				<td>${issueItemPatient.patient.patientIdentifier.identifier}</td>
				<td>${patientCategory}</td>
				<td>${issueItemPatient.patient.givenName}&nbsp;${issueItemPatient.patient.middleName}&nbsp;${issueItemPatient.patient.familyName}</td>
				<td><c:choose>
						<c:when test="${issueItemPatient.patient.age == 0  }">&lt 1</c:when>
						<c:otherwise>${issueItemPatient.patient.age }</c:otherwise>
					</c:choose></td>
			</tr>

		</table>
	</c:if>
	
	

</div>

<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listPatientDetail}">
	<c:forEach items="${listPatientDetail}" var="issue" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${issue.transactionDetail.item.subCategory.name} </td>	
		<td><a href="#" title="Remove this" onclick="INVENTORY.removeObject('${varStatus.index}','1');">${issue.transactionDetail.item.name}</a></td>
		<td>${issue.transactionDetail.specification.name}</td>
		<td>${issue.quantity}</td>
		</tr>
	</c:forEach>
	
	</c:when>
	</c:choose>
</table>
<br/>
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<c:if  test="${not empty listPatientDetail && not empty issueItemAccount}">
					<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  id="bttprocess" value="<spring:message code="inventory.finish"/>" onclick="ISSUE.processSlipItemPatient('0');" />
					<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" id="bttprint" value="<spring:message code="inventory.print"/>" onClick="PURCHASE.printDiv();" />
				</c:if>
				<c:if  test="${not empty listPatientDetail || not empty issueItemPatient}">
					<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" id="bttclear" value="<spring:message code="inventory.clear"/>"  onclick="ISSUE.processSlipItemPatient('1');"/>
				</c:if>
			</td>
		</tr>
		</table>
	
</div>
</div>
<!-- PRINT DIV -->
<div  id="printDiv" style="display: none; ">        		
<div style="margin: 10px auto; width: 981px; font-size: 1.0em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">
<c:if  test="${not empty issueItemAccount}">
<br />
<br />      		
<center style="float:center;font-size: 2.2em">Issue Item To Account ${issueItemAccount.name }</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
</c:if>
<table border="1">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.item.subCategory"/></th>
	<th><spring:message code="inventory.item.name"/></th>
	<th><spring:message code="inventory.item.specification"/></th>
	<th><spring:message code="inventory.receiptItem.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listPatientDetail}">
	<c:forEach items="${listPatientDetail}" var="issue" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${issue.transactionDetail.item.subCategory.name} </td>	
		<td>${issue.transactionDetail.item.name}</td>
		<td>${issue.transactionDetail.specification.name}</td>
		<td>${issue.quantity}</td>
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