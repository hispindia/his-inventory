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
<spring:message var="pageTitle" code="inventory.receiptDrug.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<script type="text/javascript">
jQuery(document).ready(function() {
	
	jQuery("#receiptDate").change(function() {
		VALIDATION.checkRecieptDate();
	});
	});

VALIDATION={
	checkRecieptDate : function() {
		var recieptDate = new Date(STRING.convertDateFormat(jQuery('#receiptDate').val()));
		var expiryDate = new Date(STRING.convertDateFormat(jQuery('#dateExpiry').val()));

		if (recieptDate > expiryDate){
			jQuery('#receiptDate').val("");
			alert("You can not receipt an expired drug");
		}
	}
}
/*
function myFunction()
{
jQuery("#sgst").attr("disabled", "disabled");
jQuery("#cgst").attr("disabled", "disabled");
jQuery("#sgstamt").attr("disabled", "disabled");
jQuery("#cgstamt").attr("disabled", "disabled");

}

function myFunction1()
{
jQuery("#VAT").attr("disabled", "disabled");
var sgstAmount=(jQuery("#rate").val())*(jQuery("#sgst").val()/100);
var cgstAmount=(jQuery("#rate").val())*(jQuery("#cgst").val()/100);
jQuery("#sgstamt").val(sgstAmount);
jQuery("#cgstamt").val(cgstAmount);
}
*/
</script>

<div style="width: 22.5%; float: left; margin-left: 4px; ">
<b class="boxHeader">Drug</b>
<div class="box">
<form method="post" id="receiptDrug">
<c:if  test="${not empty errors}">
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message code="${error}" /></span>
</c:forEach>
</c:if>
<br/>
<table width="100%">
<tr><td><b>Drug info</b></td></tr>
<%-- ghanshyam,7-oct-2013,issue #2983,The user need not enter the category of the drug while receiving or issuing drugs to --%>
    <%--
    <tr>
		<td><spring:message code="inventory.drug.category"/></td>
		<td>
			<select name="category" id="category" onchange="RECEIPT.onChangeCategory(this);"  style="width: 200px;">
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${listCategory}" var="vCat">
                    <option value="${vCat.id}" title="${vCat.name}" <c:if test="${vCat.id == categoryId }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
		</td>
	</tr>
	--%>

	<tr>
		<td>Brand Name<em>*</em></td>
		<td>
			
				<input id="drugName" name="drugName" onblur="RECEIPT.onBlur(this);" style="width: 150px;">
				<div id="divDrug"  ></div>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.drug.formulation"/><em>*</em></td>
		<td>
			<div id="divFormulation"  >
				<select id="formulation"  name="formulation" >
					<option value=""><spring:message code="inventory.pleaseSelect"/></option>
				</select>
			</div>
		</td>
	</tr>
</table>
<br/>
<table class="box">
	<tr>
		<td><spring:message code="inventory.receiptDrug.quantiry"/><em>*</em></td>
		<td>
			<input type="text" id="quantity" name="quantity" style="width: 120px;"/>
		</td>
	</tr>
		<tr>
		<td><spring:message code="inventory.receiptDrug.Rate"/><em>*</em></td>
		<td>
			<input type="text" id="rate" name="rate" style="width: 120px;"/>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptDrug.VAT"/></td>
		<td>
			<input type="text" id="VAT" name="VAT" value="0" style="width: 120px;"/>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptDrug.MRP"/><em>*</em></td>
		<td>
			<input type="text" id="mrPrice" name="mrPrice" style="width: 120px;" />
		</td>
	</tr>
		<tr>
		<td><spring:message code="inventory.receiptDrug.companyName"/><em>*</em></td>
		<td>
			<input type="text" id="companyName" name="companyName" style="width: 120px;" />
		</td>
	</tr>
		<tr>
		<td><spring:message code="inventory.receiptDrug.batchNo"/><em>*</em></td>
		<td>
			<input type="text" id="batchNo" name="batchNo" style="width: 120px;" />
		</td>
	</tr>
		<tr>

		<td><spring:message code="inventory.receiptDrug.Dis"/></td>
		<td>
			<input type="text" id="waiverPercentage" name="waiverPercentage" style="width: 120px;" value="0"/>
		</td>
	</tr>
		<tr>
		<td><spring:message code="inventory.receiptDrug.cgst"/></td>
		<td>
			<input type="text" id="cgst" name="cgst" value="0"  style="width: 120px;"/>
		</td>
	</tr>
	
				<tr>
		<td><spring:message code="inventory.receiptDrug.sgst"/></td>
		<td>
			<input type="text" id="sgst" name="sgst" value="0" style="width: 120px;" />
		</td>
	</tr>
		<tr>
		<td><spring:message code="inventory.receiptDrug.dateExpiry"/><em>*</em></td>
		<td>
			<input type="text" id="dateExpiry" name="dateExpiry" class="date-pick left" readonly="readonly"  ondblclick="this.value='';" style="width: 120px;"/>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptDrug.dateManufacture"/><em>*</em></td>
		<td>
			<input type="text" id="dateManufacture" name="dateManufacture" class="date-pick left" readonly="readonly"  ondblclick="this.value='';" style="width: 120px;"/>
		</td>
	</tr>
	<tr>
		<td><spring:message code="inventory.receiptDrug.receiptDate"/><em>*</em></td>
		<td>
			<input type="text" id="receiptDate" name="receiptDate" class="date-pick left" readonly="readonly"  ondblclick="this.value='';" style="width: 120px;"/>
		</td>
	</tr>
</table>
<br/>
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.addToSlip"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.back"/>" onclick="ACT.go('receiptsToGeneralStoreList.form');">
</form>
</div>
</div>
<!-- Receipt list -->

<div style="width: 76.5%; float: right; margin-right: 4px; ">
<b class="boxHeader">Receipt Slip</b>  <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --Change case of word Slip-->
<div class="box">
<table class="box" width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.quantity"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
	<th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
	<th><spring:message code="inventory.receiptDrug.MRP"/></th>
	<th><spring:message code="inventory.receiptDrug.batchNo"/></th>
	<th><spring:message code="inventory.receiptDrug.Dis"/></th>
    <th ><spring:message code="inventory.receiptDrug.cgst"/></th>
   <th><spring:message code="inventory.receiptDrug.sgst"/></th>
   <th><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
   <th><spring:message code="inventory.receiptDrug.totalPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.totalAmountAfterGst"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceipt}">
	<c:forEach items="${listReceipt}" var="receipt" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td><a href="#" title="Remove this" onclick="INVENTORY.removeObject('${varStatus.index}','7');">${receipt.drug.name}</a></td>
		<td>${receipt.formulation.name}-${receipt.formulation.dozage}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.rate}</td>
		<td>${receipt.unitPrice}</td>
		<c:choose>
		<c:when test="${not empty receipt.VAT}" >
		<td>${receipt.VAT}</td>
		</c:when>
		<c:otherwise>
		<td>NA</td>
		</c:otherwise>
		</c:choose>
        <td>${receipt.mrpPrice}</td>
        <td>${receipt.batchNo}</td>
        <td>${receipt.waiverPercentage}</td>
        <c:choose>
        <c:when test="${not empty receipt.cgst}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
		<c:when test="${not empty receipt.sgst}" >
        <td>${receipt.sgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <td><openmrs:formatDate date="${receipt.dateExpiry}" type="textbox"/></td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.totalAmountAfterGst}</td>
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
				<input type="button" id="finish" name="finish" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.finish"/>" onclick="disableFunction();RECEIPT.receiptSlip('0','${totAmountafterGst}');" />
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.clear"/>"  onclick="RECEIPT.receiptSlip('1');"/>
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptDrug.print"/>" onClick="RECEIPT.printDiv();" />
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
<center style="float:center;font-size: 2.2em">${store.name} - Receipt - Drugs</center>
<br/>
<br/>
<span style="float:right;font-size: 1.7em">Date: <openmrs:formatDate date="${date}" type="textbox"/></span>
<br />
<br />
<table border="1">
	<tr>
	<th>#</th>
	<th><spring:message code="inventory.drug.name"/></th>
	<th><spring:message code="inventory.drug.formulation"/></th>
	<th><spring:message code="inventory.receiptDrug.quantity"/></th>
	<th><spring:message code="inventory.receiptDrug.Rate"/></th>
	<th><spring:message code="inventory.receiptDrug.unitPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.VAT"/></th>
	<th><spring:message code="inventory.receiptDrug.MRP"/></th>
	<th><spring:message code="inventory.receiptDrug.batchNo"/></th>
	<th><spring:message code="inventory.receiptDrug.Dis"/></th>
	<th><spring:message code="inventory.receiptDrug.DiscountAmount"/></th>
   <th><spring:message code="inventory.receiptDrug.cgst"/></th>
   <th><spring:message code="inventory.receiptDrug.cgstamt"/></th>
   <th><spring:message code="inventory.receiptDrug.sgst"/></th>
   <th><spring:message code="inventory.receiptDrug.sgstamt"/></th>
   <th><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
   <th><spring:message code="inventory.receiptDrug.totalPrice"/></th>
	<th><spring:message code="inventory.receiptDrug.totalAmountAfterGst"/></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceipt}">
	<c:forEach items="${listReceipt}" var="receipt" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
		<td>${receipt.drug.name}</td>
		<td>${receipt.formulation.name}-${receipt.formulation.dozage}</td>
		<td>${receipt.quantity}</td>
		<td>${receipt.rate}</td>
		<td>${receipt.unitPrice}</td>
		<c:choose>
		<c:when test="${not empty receipt.VAT}" >
		<td>${receipt.VAT}</td>
		</c:when>
		<c:otherwise>
		<td>NA</td>
		</c:otherwise>
		</c:choose>
        <td>${receipt.mrpPrice}</td>
        <td>${receipt.batchNo}</td>
        <td>${receipt.waiverPercentage}</td>
        <td>${receipt.waiverAmount}</td>
        <c:choose>
        <c:when test="${not empty receipt.cgst}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
         <c:choose>
        <c:when test="${not empty receipt.cgstAmount}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
		<c:when test="${not empty receipt.sgst}" >
        <td>${receipt.sgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${not empty receipt.sgstAmount}" >
         <td>${receipt.cgst}</td>
        </c:when>
              <c:otherwise>
        <td>NA</td>
        </c:otherwise>
        </c:choose>
        <td><openmrs:formatDate date="${receipt.dateExpiry}" type="textbox"/></td>
		<td>${receipt.totalPrice}</td>
		<td>${receipt.totalAmountAfterGst}</td>
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

<script type="text/javascript">
function disableFunction(){
jQuery("#finish").attr("disabled", "disabled");
}
</script>
 
<%@ include file="/WEB-INF/template/footer.jsp" %>