<%--
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author: ghanshyam
 *  date: 15-june-2013
 *  issue no: #1636
--%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/ui.core.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/ui.tabs.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/inventory/scripts/common.js"></script>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/css/start/ui.tabs.css" />
<script type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/css/start/jquery-ui-1.8.2.custom.css"></script>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/inventory/styles/drug.process.css" />

<script type="text/javascript">
function disable(incon){

}
</script>

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">List of drug</b>
</div>
<br />

<form id="orderBillingForm"
	action="drugorder.form?patientId=${patientId}&encounterId=${encounterId}&indCount=${serviceOrderSize}&billType=mixed"
	method="POST">
	<table id="myTable" class="tablesorter" class="thickbox">
		<thead>
			<tr>
				<th style="text-align: center;">S.No</th>
				<th style="text-align: center;">Drug Name</th>
				<th style="text-align: center;">Formulation</th>
				<th style="text-align: center;">Frequency</th>
				<th style="text-align: center;">Days</th>
				<th style="text-align: center;">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="dol" items="${drugOrderList}" varStatus="index">
				<c:choose>
					<c:when test="${index.count mod 2 == 0}">
						<c:set var="klass" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="klass" value="even" />
					</c:otherwise>
				</c:choose>
				<tr class="${klass}" id="">
					<td align="center">${index.count}</td>
					<td align="center">${dol.inventoryDrug.name}</td>
					<td align="center">${dol.inventoryDrugFormulation.name}-${dol.inventoryDrugFormulation.dozage}</td>
					<td align="center">${dol.frequency.name}</td>
					<td align="center">${dol.noOfDays}</td>
					<td align="center"><input type="button" onclick=""
						value="Process">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>

<!-- Right side div for drug process -->
<div id="billDiv">
	<form method="POST" id="billForm" onsubmit="return false">
		<div>
			<input type="button" onclick="submitBillForm()" id="subm" name="subm"
				value="<spring:message code='billing.bill.save'/>" /> <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='patientServiceBill.list?patientId=${patientId}'" />
			<!-- 
		    <select name="enctype"  tabindex="20" >
                <c:forEach items="${encounterTypes}" var="enct">
                    <option value="${enct.encounterTypeId}">${enct.name}</option>
                </c:forEach>
            </select>
		 -->
			<input type="button" id="toogleBillBtn" value="-"
				onclick="toogleBill(this);" class="min" style="float: right" />
		</div>
		<div id="total"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='25' value='Total:' />&nbsp; <input
				type='text' size='3' value='' readonly="readonly" />&nbsp; <input
				type='text' id='totalprice' name='totalprice' size='5' value='0'
				readonly="readonly" />&nbsp; <b>
		</div>

		<div id="extra" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='25' value='Service Name' readonly='readonly' />&nbsp;
			<input type='text' size="3" value='Qty' readonly="readonly" />&nbsp;
			<input type='text' size='5' value='Price' readonly="readonly" />&nbsp;</b>
			<hr />
		</div>


	</form>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>