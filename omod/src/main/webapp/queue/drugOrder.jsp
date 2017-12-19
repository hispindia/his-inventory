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
<openmrs:require privilege="Drug order queue" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<%@ include file="../includes/js_css.jsp"%>
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/inventory/scripts/jquery/jquery.PrintArea.js"></script>
<script type="text/javascript">
// get context path in order to build controller url
	function getContextPath(){		
		pn = location.pathname;
		len = pn.indexOf("/", 1);				
		cp = pn.substring(0, len);
		return cp;
	}
</script>


<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 40px;
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
	.printfont {
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
}
</style>



<script type="text/javascript">
jQuery(document).ready(function(){ jQuery("#creditheader").hide();
//jQuery("#headerValue").hide();
});
var count=0;
function process(drugId,formulationId,frequencyName,days,comments){

	
jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/inventory/processDrugOrder.form",
			data : ({
				drugId			: drugId,
				formulationId		: formulationId,
				frequencyName		: frequencyName,
				days		: days,
				comments		: comments
			}),
			success : function(data) {
				jQuery("#processDrugOrder").html(data);	
				jQuery("#processDrugOrder").show();
			},
			
		});
		
		jQuery("#process"+drugId).attr("disabled", "disabled");
		//jQuery("#headerValue").show();
}

</script>


<script type="text/javascript">
//ghanshyam,4-july-2013, issue no # 1984, User can issue drugs only from the first indent
function issueDrugOrder(listOfDrugQuantity) {
   var availableIdArr=listOfDrugQuantity.split("."); 
   	var totalValue = 0;var totalDisValue=0;
	var preTotal = document.getElementById('totalValue');
   for (var i = 0; i < availableIdArr.length-1; i++) {
	
   var quantity=document.getElementById(availableIdArr[i].toString()+'_quantity').value;
	
   //ghanshyam,5-july-2013, issue no # 1990, User is able to 'finish' without issuing a drug to patient
   if (quantity==null || quantity==""){
       alert("Please enter quantity");
       return false;
     }
   if (quantity!=null || quantity!=""){
	   if(isNaN(quantity)){
	   alert("Please enter quantity in correct format");
	   return false;
	  }
	 }
	 
   if(quantity!="0"){
  
   var drugName=document.getElementById(availableIdArr[i].toString()+'_drugName').value;
   var formulation=document.getElementById(availableIdArr[i].toString()+'_formulation').value;
   var formulationId=document.getElementById(availableIdArr[i].toString()+'_formulationId').value;
   var quant=document.getElementById(availableIdArr[i].toString()+'_quantity').value;
   var frequencyName=document.getElementById(availableIdArr[i].toString()+'_frequencyName').value;
   var noOfDays=document.getElementById(availableIdArr[i].toString()+'_noOfDays').value;
   var comments=document.getElementById(availableIdArr[i].toString()+'_comments').value;
   var price=document.getElementById(availableIdArr[i].toString()+'_price').value;
   var batch=document.getElementById(availableIdArr[i].toString()+'_batchNo').value;
   var expire=document.getElementById(availableIdArr[i].toString()+'_dateexpiry').value;
   //jQuery("#qty"+drugId).append("<span style='margin:5px;'>" + totalValue + "</span>");
   //jQuery("#mrp"+drugId).append("<span style='margin:5px;'>" + waiverPercentage + "</span>");
  //jQuery("#total"+drugId).append("<span style='margin:5px;'>" + totalAmountPayable + "</span>");
  
  	totalValue = (totalValue + price*quant);
 
   if (preTotal != null){
		totalValue = +totalValue + +preTotal.value;
		preTotal.value = totalValue;
		}

   var avaiableId=availableIdArr[i];
   var deleteString = 'deleteInput(\"'+avaiableId+'\")';
   var deleteString = 'deleteInput(\"'+avaiableId+'\")';
   var htmlText =  "<div id='com_"+avaiableId+"_div'>"
	       	 +"<input id='"+avaiableId+"_fName'  name='"+avaiableId+"_fName' type='text' size='20' value='"+drugName+"'  readonly='readonly'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fFormulationName'  name='"+avaiableId+"_fFormulationName' type='text' size='11' value='"+formulation+"'  readonly='readonly'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fbatchNo'  name='"+avaiableId+"_fbatchNo'  type='text' size='11' value='"+batch+"' readonly='readonly'/>&nbsp;"
	       	+"<input id='"+avaiableId+"_fdateexpiry'  name='"+avaiableId+"_fdateexpiry'  type='text' size='11' value='"+expire+"' readonly='readonly'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fQuantity'  name='"+avaiableId+"_fQuantity' type='text' size='3' value='"+quant+"'  readonly='readonly'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fPrice'  name='"+avaiableId+"_fPrice' type='text' size='3' type='hidden' value='"+price+"'  readonly='readonly'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fFormulationId'  name='"+avaiableId+"_fFormulationId' type='hidden' value='"+formulationId+"'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fAvaiableId'  name='avaiableId' type='hidden' value='"+avaiableId+"'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fFrequencyName'  name='"+avaiableId+"_fFrequencyName' type='hidden' value='"+frequencyName+"'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fnoOfDays'  name='"+avaiableId+"_fnoOfDays' type='hidden' value='"+noOfDays+"'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fcomments'  name='"+avaiableId+"_fcomments' type='hidden' value='"+comments+"'/>&nbsp;"
	       	 +"<input id='drugProcessName'  name='drugProcessName' type='hidden' value='"+drugName+"'/>&nbsp;"
	       	
	       	 //+"<a style='color:red' href='#' onclick='"+deleteString+"' >[X]</a>"	
	       	 +"</div>";
	
   var newElement = document.createElement('div');
   
   newElement.setAttribute("id", avaiableId);   
   newElement.innerHTML = htmlText;
   var fieldsArea = document.getElementById('headerValue');
   fieldsArea.appendChild(newElement);
   
  jQuery("#"+drugName).hide();
   jQuery("#processDrugOrder").hide();
    }
  }
  	if (preTotal == null){
		var totalText =  "<div id='com_"+avaiableId+"_div'>"
		  +"<tr>"
				 +"<td id='"+avaiableId+"_fTotal'  name='"+avaiableId+"_fTotal'><b>Total Price:</b>"
				 +"<input id='totalValue'  name='totalValue' type='text' size='6' value='"+Math.round(totalValue)+"'  readonly='readonly'/>&nbsp;"
				 +"</td>"
				 +"</tr>"
				 +"</div>";  	
	   var totalElement = document.createElement('div');
	   totalElement.innerHTML = totalText;
		var totalDiv = document.getElementById('totalDiv');
		var totalDisDiv = document.getElementById('totalDisValue');
	   totalDiv.appendChild(totalElement);
	   jQuery("#totalDiv").show();	
	   jQuery("#totalDisValue").show();	
	} 
	
	jQuery("#estTotal").val(jQuery("#totalValue").val());
	
	var total=jQuery("#totalValue").val();
    var waiverPercentage=jQuery("#waiverPercentage").val();
    var totalAmountPay=total-(total*waiverPercentage)/100;
    var tap=Math.round(totalAmountPay);
    jQuery("#totalAmountPayable").val(tap);
    
    var totalPrice=parseInt(quant)*parseInt(price);
    count++;
    var drugIssuedText = "<td>"
				 +count
				 +"</td>"
				 +"<td>"
				 +drugName
				 +"</td>"
				 +"<td>"
				 +formulation
				 +"</td>"
				 +"<td>"
				 +batch
				 +"</td>"
				 +"<td>"
				 +expire
				 +"</td>"
				 +"<td>"
				 +quant
				 +"</td>"
				 +"<td>"
				 +price
				 +"</td>"
				 +"<td>"
				 +totalPrice
				 +"</td>";
   
   var newElementt = document.createElement('tr');
   newElementt.setAttribute("align", "center");   
   newElementt.innerHTML = drugIssuedText;
   var fieldsAreaa = document.getElementById('drugIssuedheaderValue');
   fieldsAreaa.appendChild(newElementt);

}

function deleteInput(avaiableId) {
	
   var parentDiv = 'headerValue';
   var child = document.getElementById(avaiableId);
   var parent = document.getElementById(parentDiv);
   
   var price = document.getElementById(avaiableId.toString()+'_fPrice').value;
   var quantity=document.getElementById(avaiableId.toString()+'_fQuantity').value;
   var removedTotal = +price* +quantity;

   var preTotal = document.getElementById('totalValue');
   var currentTotal = preTotal.value;
   if( +currentTotal <= +removedTotal){
		 var totalDiv = document.getElementById('totalDiv');
		 while (totalDiv.firstChild) {
			totalDiv.removeChild(totalDiv.firstChild);
			}
		jQuery("#totalDiv").hide();	
		}
	else{
		preTotal.value = (+currentTotal - + removedTotal).toFixed(2)
	}	
   parent.removeChild(child); 
}

</script>

<script type="text/javascript">
function cancel() {
jQuery("#processDrugOrder").hide();
}
</script>

<script type="text/javascript">
function finishDrugOrder() {

var drugProcessName=document.getElementById("drugProcessName");
if (drugProcessName==null){
alert("Please select at least one drug");
return false;
}

if(jQuery("#waiverPercentage").val() ==""){
alert("Please enter Discount Percentage");
return false;
}

if(jQuery("#waiverPercentage").val() < 0 ){
alert("Please enter correct Discount Percentage");
return false;
}

                
/*if(jQuery("#waiverPercentage").val()>0 && jQuery("#waiverComment").val() ==""){
alert("Please enter comment");
return false;
}
*/

if(document.getElementById("amountGiven").disabled != true)
	{
if(jQuery("#amountGiven").val() ==""){
alert("Please enter Amount Given");
return false;
}


if(jQuery("#amountGiven").val() < 0 || !StringUtils.isDigit(jQuery("#amountGiven").val())){
alert("Please enter correct Amount Given");
return false;
}
var amgiv=jQuery("#amountGiven").val();
var tamp=jQuery("#totalAmountPayable").val();

if(amgiv-tamp < 0 ){
alert("Amount Given must be greater than Total Amount Payable");
return false;
}

if(jQuery("#amountReturned").val() ==""){
alert("Please enter Amount Returned");
return false;
}

if(jQuery("#amountReturned").val() < 0 || !StringUtils.isDigit(jQuery("#amountReturned").val())){
alert("Please enter correct Amount Returned");
return false;
}
	}
if(confirm("Are you sure?")){
jQuery("#subm").attr("disabled", "disabled");
printDiv2();
return true;
}
if(confirm("Are you sure?")){
	jQuery("#sub").attr("disabled", "disabled");
	printDiv2();
	return true;
	}
return false;
}
</script>

<script type="text/javascript">
function totalAmountToPay(){
var total=jQuery("#totalValue").val();
var waiverPercentage=jQuery("#waiverPercentage").val();
var totalAmountPay=total-(total*waiverPercentage)/100;
var tap=Math.round(totalAmountPay);
jQuery("#totalAmountPayable").val(tap);
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-tap;
jQuery("#amountReturned").val(amountReturned);
}

function amountReturnedToPatient(){
var totalAmountToPay=jQuery("#totalAmountPayable").val();
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-totalAmountToPay;
jQuery("#amountReturned").val(amountReturned);
}
</script>

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">List of drug</b>
</div>
<br />
<input type="hidden" id="patientType" value="${patientType}">
<div id="patientDetails">
	<!--
<div id="patientDetails" style="margin: 10px auto; width: 981px;">
-->
	<table>
		<tr>
			<td>Patient ID :</td>
            <td>&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;${patientSearch.identifier}</td>
		</tr>
		<tr>
			<td>Name :</td><td>&nbsp;</td>
			<td>&nbsp;${patientSearch.givenName}&nbsp;
				${patientSearch.familyName}&nbsp;&nbsp;${fn:replace(patientSearch.middleName,","," ")}</td>
		</tr>
        <tr>
        	<td>Age:</td><td>&nbsp;</td>
        	<td><c:choose>
							<c:when test="${patientSearch.age == 0}">&lt 1</c:when>
							<c:otherwise>${patientSearch.age}</c:otherwise>
						</c:choose></td>
      </tr>
        <tr>
        	<td>Gender:</td><td>&nbsp;</td>
        	<td>&nbsp;${patientSearch.gender}</td>
        </tr>
		<tr>
			<td>Date :</td><td>&nbsp;</td>
			<td>${date}</td>
		</tr>
	</table>
</div>

<form id="drugOrderForm"
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
				<th style="text-align: center;">Comments</th>
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
				<tr class="${klass}" id="${dol.inventoryDrug.name}">
					<td align="center">${index.count}</td>
					<td align="center">${dol.inventoryDrug.name}</td>
					<td align="center">${dol.inventoryDrugFormulation.name}-${dol.inventoryDrugFormulation.dozage}</td>
					<td align="center">${dol.frequency.name}</td>
					<td align="center">${dol.noOfDays}</td>
					<td align="center">${dol.comments}</td>
					<td align="center"><input type="button" id="process${dol.inventoryDrug.id}" name="process${dol.inventoryDrug.id}"
						onclick="process(${dol.inventoryDrug.id},${dol.inventoryDrugFormulation.id},'${dol.frequency.name}',${dol.noOfDays},'${dol.comments}');"
						value="Process">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>

<div id="processDrugOrder"></div>

<!-- Right side div for drug process -->
<div id="finishDrugOrderDiv">
	<form id="finishDrugOrderForm"
		action="drugorder.form?patientId=${patientId}&encounterId=${encounterId}&patientType=${patientType}"
		method="POST" onsubmit="javascript:return finishDrugOrder();">
		<div>
			<input type="submit" id="subm" name="subm"
				value="<spring:message code='inventory.drug.process.finish'/>" />
				<input type="submit" id="sub" name="sub"
				value="<spring:message code='inventory.drug.process.credit'/>"  onClick="credit();" />
				 <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='patientQueueDrugOrder.form'" />
			<input type="button" id="print" name="print"
				value="<spring:message code='inventory.drug.process.print'/>" onClick="printDiv2();"/>
			<!-- 
		    <select name="enctype"  tabindex="20" >
                <c:forEach items="${encounterTypes}" var="enct">
                    <option value="${enct.encounterTypeId}">${enct.name}</option>
                </c:forEach>
            </select>
		 -->
			<input type="button" id="toogleFinishDrugOrderBtn" value="-"
				onclick="toogleFinishDrugOrder(this);" class="min" style="float: right" />
		</div>

		<div id="totalDiv" style="padding: 0.3em; margin: 0.3em 0em; width: 50%; display:none;">
				
		</div>
		
		<div id="headerValue" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width:100%;">
		    <input type='text' size='20' value='Drug Name' readonly='readonly' />
			<input type='text' size="11" value='Formulation' readonly="readonly" />
			<input type='text' size="3" value='Qty' readonly="readonly" />
			<input type='text' size="3" value='MRP' readonly="readonly" />
			<hr />	
			</div>
			
		<div id="waiverDiv"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<div>
			Discount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" id="waiverPercentage" name="waiverPercentage"
				size="11" value="0" class="cancelDraggable" onkeyup="totalAmountToPay();"/>%
		</div>
		<div>
		Total amount payable&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="totalAmountPayable" name="totalAmountPayable"
				size="11" readOnly="true"/>
		</div>
		<div>
		Comment&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="waiverComment" name="waiverComment" size="11" class="cancelDraggable"/>
		</div>
		<div>
		Amount Given&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="amountGiven" name="amountGiven" size="11" class="cancelDraggable" onkeyup="amountReturnedToPatient();"/>
		</div>
		<div>
		Amount Returned to Patient&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="amountReturned" name="amountReturned" size="11" readOnly="true"/>
		</div>
		</div>
			
	</form>
</div>
	<div id="printDiv" style="display: none;"
		style="width: 1280px; font-size: 0.8em">

		<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 50px;
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
	.printfont {
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
}
</style>


<br><br>
		<table align='Center'>
		<tr><td ></td><td id="creditheader" style="color:red">CREDIT BILL</td><td></td></tr>
		<tr><td>BILL NO.:${isdpdt}</td></tr>
		<tr>
			<td>Patient ID :</td>
			<td>${patientSearch.identifier}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			
			<td>Name :</td>
			<td>${patientSearch.givenName}&nbsp;${patientSearch.familyName}</td>
		</tr>
        <tr>
        	<td>Age:</td>
			<td><c:choose>
							<c:when test="${patientSearch.age == 0}">&lt 1</c:when>
							<c:otherwise>${patientSearch.age}</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					
			<td>Gender:</td>
        	<td>${patientSearch.gender}</td>  	
      </tr>
		<tr>
			<td>Date :</td>
			<td>${date}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			
			<td>Patient Category:</td>
			<td>${patientCategory} &nbsp;&nbsp;&nbsp; ${patientSubCategory}</td>
		</tr>
		</table>
 <hr  color="black">
<table style="width:100%; margin-top:15px">
<thead>
<h4 align="left" style="color:black">Drugs Issued by Pharmacy</h4>
<tr>
<th style="text-align: center;">S.No</th>
<th style="text-align: center;">Drug Name</th>
<th style="text-align: center;">Formulation</th>
<th style="text-align: center;">Batch No.</th>
<th style="text-align: center;">Date Of Expiry</th>
<th style="text-align: center;">Qty</th>
<th style="text-align: center;">MRP</th>
<th style="text-align: center;">Total</th>
</tr>
</thead>
<tbody id="drugIssuedheaderValue">
</tbody>
</table>

<table style="width:100%">
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Total amount</td>
<td style="text-align: center;"><span id="printableTotal" /></td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Discount %</td>
<td style="text-align: center;"><span id="printableDiscount" /></td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">Total amount payable</td>
<td style="text-align: center;"><span id="printableTotalAmountPayable" /></td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td id=amtgiven style="text-align: center;">Amount Given</td>
<td style="text-align: center;"><span id="printableGiven" /></td>
</tr>
<tr>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td style="text-align: center;">&nbsp;</td>
<td id="amtreturned" style="text-align: center;">Amount Returned</td>
<td style="text-align: center;"><span id="printableAmountReturned" /></td>
</tr>
<tr>
<td><b>Total Amount  Payable Rupees:</b><span id="printableTotalPayable" > </span> only</td>
</tr>
</table>

<c:choose>
<c:when test="${not empty drugOrderListNotAvailable}">
<hr  color="black">
<table id="drugNotAvailable" class="tablesorter" class="thickbox" style="width:100%; margin-top:15px">
		<thead>
			<h4 align="left" style="color:black">Drugs Not Issued by Pharmacy</h4>
			<tr>
				<th style="text-align: center;">S.No</th>
				<th style="text-align: center;">Drug Name</th>
				<th style="text-align: center;">Formulation</th>
				<th style="text-align: center;">Days</th>
				<th style="text-align: center;">Frequency</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="dol" items="${drugOrderListNotAvailable}" varStatus="index">
				<c:choose>
					<c:when test="${index.count mod 2 == 0}">
						<c:set var="klass" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="klass" value="even" />
					</c:otherwise>
				</c:choose>
				<tr class="${klass}" id="${dol.inventoryDrug.name}">
					<td align="center">${index.count}</td>
					<td align="center">${dol.inventoryDrug.name}</td>
					<td align="center">${dol.inventoryDrugFormulation.name}-${dol.inventoryDrugFormulation.dozage}</td>
					<td align="center">${dol.noOfDays}</td>
					<td align="center">${dol.frequency.name}</td>
				</tr>
			</c:forEach>
		</tbody>
</table>
</c:when>
</c:choose>
<br><br><br><br><br><br><br>
<table  class="spacer" style="margin-left: 60px;width:100%;">
				<tr>
					<td align="right"><b>Treating Doctor</b></td><td>:${doctor}</td>
				</tr>
				<tr>
					<td align="right"><b>Treating Paharmacist</b></td><td>:${pharmacist}</td>
				</tr>
</table>


</div>



<script type="text/javascript">
	function printDivNoJQuery() {
		var divToPrint = document.getElementById('printDiv');
		var newWin = window
				.open('', '',
						'letf=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
		newWin.document.write(divToPrint.innerHTML);
		newWin.print();
		newWin.close();
		alert("Printing ...");
		//setTimeout(function(){window.location.href = $("#contextPath").val()+"/getBill.list"}, 1000);	
	}
	function credit()
	{  
	jQuery("#waiverPercentage").attr("disabled", "disabled");
	jQuery("#amountGiven").attr("disabled", "disabled");
	jQuery("#amountReturned").attr("disabled", "disabled");
	jQuery("#amtgiven").hide();
	jQuery("#amtreturned").hide();
	jQuery("#creditheader").show();
		
	}
	function printDiv2() {

		var totalValue=jQuery("#totalValue").val();
		var waiverPercentage=jQuery("#waiverPercentage").val();
		var totalAmountPayable=jQuery("#totalAmountPayable").val();
		var waiverComment=jQuery("#waiverComment").val();
		var amountGiven=jQuery("#amountGiven").val();
		var amountReturned=jQuery("#amountReturned").val();
		jQuery("#printableTotal").append("<span style='margin:5px;'>" + totalValue + "</span>");
		jQuery("#printableDiscount").append("<span style='margin:5px;'>" + waiverPercentage + "</span>");
		jQuery("#printableTotalAmountPayable").append("<span style='margin:5px;'>" + totalAmountPayable + "</span>");
		jQuery("#printableTotalPayable").append("<span style='margin:5px;'>" + toWords(totalAmountPayable) + "</span>");
		jQuery("#printableGiven").append("<span style='margin:5px;'>" + amountGiven + "</span>");
		jQuery("#printableAmountReturned").append("<span style='margin:5px;'>" + amountReturned + "</span>");
		
		var printer = window.open('', '', 'width=300,height=300');
		printer.document.open("text/html");
		printer.document.write(document.getElementById('printDiv').innerHTML);
		printer.print();
		printer.document.close();
		printer.window.close();
		//alert("Printing ...");
	}

</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>
