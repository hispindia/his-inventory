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
function process(drugId,formulationId){

jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/inventory/processDrugOrder.form",
			data : ({
				drugId			: drugId,
				formulationId		: formulationId
			}),
			success : function(data) {
				jQuery("#processDrugOrder").html(data);	
				jQuery("#processDrugOrder").show();
			},
			
		});
}

</script>


<script type="text/javascript">
//ghanshyam,4-july-2013, issue no # 1984, User can issue drugs only from the first indent
function issueDrugOrder(listOfDrugQuantity) {
   var availableIdArr=listOfDrugQuantity.split("."); 
   	var totalValue = 0;
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
   var price=document.getElementById(availableIdArr[i].toString()+'_price').value;
  
  totalValue = (totalValue + price*quantity);
  	
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
	       	 +"<input id='"+avaiableId+"_fQuantity'  name='"+avaiableId+"_fQuantity' type='text' size='3' value='"+quantity+"'  readonly='readonly'/>&nbsp;"
	       	 //+"<input id='"+avaiableId+"_fPrice'  name='"+avaiableId+"_fPrice' type='text' size='3' type='hidden' value='"+price+"'  readonly='readonly'/>&nbsp;"
			 +"<input id='"+avaiableId+"_fFormulationId'  name='"+avaiableId+"_fFormulationId' type='hidden' value='"+formulationId+"'/>&nbsp;"
	       	 +"<input id='"+avaiableId+"_fAavaiableId'  name='avaiableId' type='hidden' value='"+avaiableId+"'/>&nbsp;"
	       	 +"<input id='drugProcessName'  name='drugProcessName' type='hidden' value='"+drugName+"'/>&nbsp;"
	       	 +"<a style='color:red' href='#' onclick='"+deleteString+"' >[X]</a>"	
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
				 +"<td id='"+avaiableId+"_fTotal'  name='"+avaiableId+"_fTotal'><b>Total Price:</b></td>&nbsp;"
				 +"<input id='totalValue'  name='totalValue' type='text' size='11' value='"+Math.round(totalValue)+"'  readonly='readonly'/>&nbsp;"
				 +"</div>";  	
	   var totalElement = document.createElement('div');
	   totalElement.innerHTML = totalText;
		var totalDiv = document.getElementById('totalDiv');
	   totalDiv.appendChild(totalElement);
	   jQuery("#totalDiv").show();	

	} 
	
	jQuery("#estTotal").val(jQuery("#totalValue").val());

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
alert("Please select atleast one drug");
return false;
}

if(confirm("Are you sure?")){
printDiv2();
return true;
}

return false;
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
					<td align="center"><input type="button"
						onclick="process(${dol.inventoryDrug.id},${dol.inventoryDrugFormulation.id});"
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
				value="<spring:message code='inventory.drug.process.finish'/>" /> <input
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

		<div id="headerValue" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<div id="totalDiv" style="padding: 0.3em; margin: 0.3em 0em; width: 100%; display: none;">
				
			</div>
			<div id="WaiverAmountField" class="cancelDraggable"
				style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
				
			</div>
			
			<input type='text' size='20' value='Drug Name' readonly='readonly' />
			<input type='text' size="11" value='Formulation' readonly="readonly" />
			<input type='text' size="3" value='Qty' readonly="readonly" />
			
			<hr />
		</div>


	</form>
</div>


	<div id="printDiv" class="hidden"
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
<center><img width="100" height="100" align="center" title="OpenMRS" alt="OpenMRS" src="${pageContext.request.contextPath}/moduleResources/inventory/kenya_logo.bmp"><center>
<h5><center>${userLocation}</center>
<br><br>
		<table align='Center'>
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
						</c:choose>
					</td>
        	
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


<table id="myTablee" class="tablesorter" class="thickbox" style="width:100%; margin-top:30px">
		<thead>
			<tr>
				<th style="text-align: center;">S.No</th>
				<th style="text-align: center;">Drug Name</th>
				<th style="text-align: center;">Formulation</th>
				<th style="text-align: center;">Days</th>
				<th style="text-align: center;">Frequency</th>
				<th style="text-align: center;">Comments</th>
				<!-- <th style="text-align: center;">Quantity</th> -->
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
					<td align="center">${dol.noOfDays}</td>
					<td align="center">${dol.frequency.name}</td>
					<td align="center">${dol.comments}</td>
				</tr>
			</c:forEach>
			

		</tbody>
</table>
<br><br><br><br><br><br><br>
<table  class="spacer" style="margin-left: 60px;width:100%;">
				<tr>
					<td width="20%"><b>Treating Doctor</b></td><td>:${doctor}</td>
				</tr>
				<tr>
					<td width="20%"><b>Treating Paharmacist</b></td><td>:${pharmacist}</td>
				</tr>
</table>


</div>

<script>
	function printDivNoJQuery() {
		var divToPrint = document.getElementById('printDiv');
		var newWin = window
				.open('', '',
						'letf=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
		newWin.document.write(divToPrint.innerHTML);
		newWin.print();
		newWin.close();
		//setTimeout(function(){window.location.href = $("#contextPath").val()+"/getBill.list"}, 1000);	
	}
	function printDiv2() {

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