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
<%@ include file="/WEB-INF/template/header.jsp" %>
<spring:message var="pageTitle" code="inventory.indent.manager" scope="page"/>
<%@ include file="../includes/js_css.jsp" %>

<div style="width: 45%; float: left; margin-left: 4px; ">
<b class="boxHeader">Drug</b>
<div class="box">

<form method="post" id="subStoreIndentDrug">
<br/>

<table class="box">
<tr><td><b>Drug Info</b></td></tr>
<tr>

<%-- 	// Sagar Bele - 07-08-2012 New Requirement #302 [INVENTORY] Non Mandatory Drug Category filter for drug search  --%>
		<td><spring:message code="inventory.drug.category"/></td>
		<td>
			<select name="category" id="category" onchange="INDENT.onChangeCategory(this);"  style="width: 250px;">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}"  <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
	</tr>

	<tr>
		<td>Drug Name<em>*</em></td>
		<td>
			
				<input id="drugName" name="drugName" onblur="INDENT.onBlur(this);" style="width: 200px;">
					<div id="divDrug"  ></div>
				
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.drug.formulation"/><em>*</em></td>
		<td>
			<div id="divFormulation"  >
				<select id="formulation"  name="formulation">
					<option value=""><spring:message code="inventory.pleaseSelect"/></option>
				</select>
			</div>
		</td>
	</tr>
</table>
<br/>
<table class="box">
	<tr>
		<td><spring:message code="inventory.indent.quantity"/><em>*</em></td>
		<td>
			<input type="text" id="quantity" name="quantity" />
		</td>
	</tr>
</table>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.addToSlip"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('subStoreIndentDrugList.form');">
</form>
</div>
</div>
<!-- indent list -->
<div style="width: 53%; float: right; margin-right: 16px; ">
<b class="boxHeader">Indent Slip of Pharmacy</b>
<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.drug.category"/></th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listIndent}">
	<c:forEach items="${listIndent}" var="indent" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count }"/></td>
		<td>${indent.drug.category.name} </td>	
		<td><a href="#" title="Remove this" onclick="INVENTORY.removeObject('${varStatus.index}','3');">${indent.drug.name}</a></td>
		<td>${indent.formulation.name}-${indent.formulation.dozage}</td>
		<td>${indent.quantity}</td>
		</tr>
	</c:forEach>
	
	</c:when>
	</c:choose>
</table>
<br/>
	<c:if  test="${not empty listIndent}">
		<table class="box" width="100%" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<!--<input type="button" value="<spring:message code="inventory.indent.finish"/>" onclick="INDENT.processSlip('0');" />
				--><input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.saveAndSend"/>" onclick="INDENT.processSlip('2');" />
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.print"/>" onClick="INDENT.printDiv();" />
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.indent.clear"/>"  onclick="INDENT.processSlip('1');"/>
			</td>
		</tr>
		</table>
	</c:if>
</div>
</div>
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
<table border="1">
	<tr>
	<th>S.No</th>
	<th><spring:message code="inventory.drug.category"/></th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.indent.quantity"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listIndent}">
	<c:forEach items="${listIndent}" var="indent" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${ varStatus.count }"/></td>
		<td>${indent.drug.category.name} </td>	
		<td>${indent.drug.name}</td>
		<td>${indent.formulation.name}-${indent.formulation.dozage}</td>
		<td>${indent.quantity}</td>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:left;font-size: 1.5em">Signature of sub-store/ Stamp</span><span style="float:right;font-size: 1.5em">Signature of inventory clerk/ Stamp</span>
<br/><br/><br/><br/><br/><br/>
<span style="margin-left: 13em;font-size: 1.5em">Signature of Medical Superintendent/ Stamp</span>
</div>
</div>
<!-- END PRINT DIV -->   

 
<%@ include file="/WEB-INF/template/footer.jsp" %>