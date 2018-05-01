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
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>
<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<br />

<span class="boxHeader">return the drug</span>
<form method="post" class="box" onsubmit="javascript:return validate();">
<table width="100%" cellpadding="5" cellspacing="0">
<tr>
					<td>Patient ID :</td>
					<td>${issueDrugPatient.identifier }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 
					<td>Name :</td>
			        <td>${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}</td>
				</tr>
				<tr>
					<td>Age:</td>
					<td><c:choose>
							<c:when test="${issueDrugPatient.patient.age == 0  }">&lt 1</c:when>
							<c:otherwise>${issueDrugPatient.patient.age }</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						
					<td>Gender:</td>
        	        <td>${issueDrugPatient.patient.gender}</td>  	
				</tr>
				<tr>
					<td>Date:</td>
					<td><openmrs:formatDate date="${date}" type="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>Patient Category:</td>
			        <td>${patientCategory} &nbsp;&nbsp;&nbsp; ${patientSubCategory}</td>
			       
				</tr>
				<tr>
				<td>Bill No:</td>
				<td>${billNo}</td>
				</tr>
<br />
</table>

<table width="100%" cellpadding="5" cellspacing="0">
<tr>
	<th>#</th>
	<th>Drug Name</th>
	<th>Formulation</th>
	<th>DOE</th>
	<th>Issued Quantity</th>
	<!-- <th>Action</th> -->
	</tr>
<c:choose>
	<c:when test="${not empty storeDrugTransactionDetailList}">
	<c:forEach items="${storeDrugTransactionDetailList}" var="tranDetail" varStatus="varStatus">
	<tr id="tranRow${tranDetail.id}" name="tranRow${tranDetail.id}" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
	     <td> ${tranDetail.drug.name}</td>
	     <td>${tranDetail.formulation.name}-${tranDetail.formulation.dozage}</td>
	     <td><openmrs:formatDate date="${tranDetail.dateExpiry}"
								type="textbox" /></td>
	     <td>${tranDetail.issueQuantity}</td>
	     <!--
	     <td><a style="color:red" onclick="removeDrug(${tranDetail.id},${tranDetail.mrpPrice},${tranDetail.issueQuantity});">[X]</a></td>
	     -->
	     <td><input type="hidden" id="quantity${tranDetail.id}" name="quantity${tranDetail.id}" value="${tranDetail.issueQuantity}"></td>
	     <td><input type="hidden" id="tranDetail" name="tranDetail" value="${tranDetail.id}"></td>
</tr>
</c:forEach>
</c:when>
</c:choose>

<tr>
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>voided reason<label style="color:red">*</label></td>	
<td><input type="text" id="voidedReason" name="voidedReason"></td>	
</tr>

<tr>
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>Discount %</td>	
<td><input type="text" id="waiverPercentage" name="waiverPercentage" readonly="readonly"></td>	
</tr>

<tr>
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>&nbsp;</td>	
<td>Cash Returned</td>	
<td><input type="text" id="cashReturned" name="cashReturned" readonly="readonly" value="0"></td>	
</tr>

<tr>
<td><input type="submit" id="receipt" name="receipt" value="Void Bill"></td>	
</tr>

</table>
</form>

<script type="text/javascript">
jQuery(document).ready(function(){
<c:forEach var="entry" items="${storeDrugTransactionDetailList}">
jQuery("#waiverPercentage").val(${entry.waiverPercentage});
var credit="${entry.amountCredit}";
if(credit==""){
jQuery("#cashReturned").val(${entry.amountPayable});
}
else{
jQuery("#cashReturned").val(0);
}
</c:forEach>
});

function validate(){
var tranDetailArray = new Array();
<c:forEach var="entry" items="${transactionDetailId}">
tranDetailArray.push(${entry});
</c:forEach>

var _quantityMap = new Array();
<c:forEach var="entry" items="${quantityMap}">
_quantityMap[${entry.key}] = "${entry.value}";
</c:forEach>

if (${expired}==1)
{
alert("The List Contains Expired Drugs");
return false;
}
	
for (var i = 0; i < tranDetailArray.length; i++){
var tranDetail=tranDetailArray[i];
var quantity=jQuery("#quantity"+tranDetail).val();

if (quantity==null || quantity=="")
{
alert("Please enter quantity");
return false;
}
if (quantity!=null || quantity!=""){
 if(isNaN(quantity)){
  alert("Please enter quantity in correct format");
  return false;
  }
  var quantityInInteger=parseInt(quantity);
  var issuedQuantity=parseInt(_quantityMap[tranDetail]);
 if(quantityInInteger>issuedQuantity){
 alert("return quantity can not be greater than issue quantity");
 return false;
 }
} 
  
}
var voidedReason=jQuery("#voidedReason").val();
if (voidedReason==null || voidedReason=="")
{
alert("Please enter voided reason");
return false;
}
if(confirm("Are you sure?")){
jQuery("#receipt").attr("disabled", "disabled");
return true;
}
else{
return false;
}
}

function removeDrug(tranDetailId,mrpPrice,issueQuantity){
var tranDetail=tranDetailId.toString();
jQuery("#tranRow"+tranDetail).hide();
jQuery("#quantity"+tranDetail).val(0);
//var cashReturned=mrpPrice*issueQuantity;
//jQuery("#cashReturned").val(cashReturned);
}
</script>