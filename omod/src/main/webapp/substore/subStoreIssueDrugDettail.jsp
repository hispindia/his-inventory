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
<%@ include file="../includes/js_css.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
<script type="text/javascript">
String cat="General";
</script>
		<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 100px;
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

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">Detail Issue</b>
</div>

<div id="patientDetails">	
	<table>
		<tr>
			<td>Patient ID :</td>
            <td>&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;${identifier}</td>
		</tr>
		<tr>
			<td>Name :</td><td>&nbsp;</td>
			<td>&nbsp;${givenName}&nbsp;
				${familyName}&nbsp;&nbsp;${fn:replace(middleName,","," ")}</td>
		</tr>
        <tr>
        	<td>Age:</td><td>&nbsp;</td>
        	<td>${age}</td>
      </tr>
        <tr>
        	<td>Gender:</td><td>&nbsp;</td>
        	<td>&nbsp;${gender}</td>
        </tr>
		<tr>
			<td>Date :</td><td>&nbsp;</td>
			<td>${date}</td>
		</tr>
	</table>
</div>

<span class="boxHeader">Issue Drugs Detail</span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr align="center">
	<th>S.No</th>
	<th><spring:message code="inventory.viewStockBalance.category"/></th>
	<th><spring:message code="inventory.viewStockBalance.drug"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<th ><spring:message code="inventory.receiptDrug.dateExpiry"/></th>
	<th><spring:message code="inventory.issueDrug.quantity"/></th>
	<th><spring:message code="inventory.receiptDrug.price" text="Price" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listDrugIssue}">
	<c:set var="total" value="${0}"/>  
	<c:forEach items="${listDrugIssue}" var="detail" varStatus="varStatus">
	<c:set var="price" value="${ detail.quantity* (detail.transactionDetail.unitPrice + 0.01*detail.transactionDetail.VAT*detail.transactionDetail.unitPrice) }" />
	<c:set var="total" value="${total + price}"/>
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${detail.transactionDetail.drug.category.name} </td>	
		<td>${detail.transactionDetail.drug.name} </td>	
		<td>${detail.transactionDetail.formulation.name}-${detail.transactionDetail.formulation.dozage}</td>
		<td><openmrs:formatDate date="${detail.transactionDetail.dateExpiry}" type="textbox"/></td>
		<td>${detail.quantity }</td>
		<td><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></td>
		</tr>
	</c:forEach>
	<tr><td>&nbsp;</td></tr>
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td><b><spring:message code="inventory.receiptDrug.total" text="Estimated Price" /></b></td>
		<td><fmt:formatNumber value="${total}" type="number" pattern="#"/></td>						
	</tr>	
	<tr>
			<td>Treating Doctor </td>
			<td><b>:${paymentMode}</b></td>
		</tr>
		<tr>
			<td>Attending Pharmacist</td>
			<td><b>:${cashier}</b></td>
		</tr>
	</c:when>
	</c:choose>
</table>
</div>
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="inventory.receiptItem.print"/>" onClick="INDENT.printDiv();" />

<!-- PRINT DIV -->
<div  id="printDiv" style="display: none;  width: 1280px; font-size: 0.8em">
<br/>
<br/>     

<center><img width="100" height="100" align="center" title="OpenMRS" alt="OpenMRS" src="${pageContext.request.contextPath}/moduleResources/inventory/kenya_logo.bmp"><center>
  <table  class="spacer" style="margin-left: 60px;"> 		
<tr><h3><center><b><u>${userLocation}</u> </b></center></h3></tr>
<tr><h5><b><center>CASH RECEIPT</center></b></h5></tr>
</table>
<br/>
<br/>
<c:if  test="${not empty listDrugIssue}">
<table class="spacer" style="margin-left: 60px;">
	<tr><td>Date/Time: </td><td>:${date}</td></tr>
	<tr><td>Name</td><td>:${issueDrugPatient.patient.givenName}&nbsp;${issueDrugPatient.patient.familyName}&nbsp;${fn:replace(issueDrugPatient.patient.middleName,","," ")} </td></tr>
	<tr><td>Patient ID</td><td>:${issueDrugPatient.identifier }</td></tr>
	<tr><td>Age</td><td>:${age}</td></tr>
	<tr><td>Gender</td><td>:${gender}</td></tr>
	<tr><td>Payment Category</td><td>:${category}</td></tr>
	<!-- <tr><td>Waiver/Exempt. No.</td><td>:${exemption}</td></tr> -->
	
</table>
</c:if>
<table class="printfont"
			style="margin-left: 60px; margin-top: 10px; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif; font-style: normal;"
			width="80%">
		<tr align="center">
	<th>S.No</th>
	<th><spring:message code="inventory.viewStockBalance.drug"/></th>
	<th><spring:message code="inventory.viewStockBalance.formulation"/></th>
	<th><spring:message code="inventory.issueDrug.quantity"/></th>
	<th><spring:message text="Amount" /></th>
	</tr>
	<c:choose>
	<c:when test="${not empty listDrugIssue}">
	<c:set var="total" value="${0}"/>  
	<c:forEach items="${listDrugIssue}" var="detail" varStatus="varStatus">
	<c:set var="price" value="${ detail.quantity* (detail.transactionDetail.unitPrice + 0.01*detail.transactionDetail.VAT*detail.transactionDetail.unitPrice) }" />
	<c:set var="total" value="${total + price}"/>
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td>${detail.transactionDetail.drug.name} </td>	
		<td>${detail.transactionDetail.formulation.name}-${detail.transactionDetail.formulation.dozage}</td>
		<td>${detail.quantity }</td>
		<td><fmt:formatNumber value="${price}" type="number" maxFractionDigits="2"/></td>
		</tr>
	</c:forEach>
	<tr><td>&nbsp;</td></tr>
	<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td></td>
		<td></td>
		<td></td>
		<td><b><spring:message code="inventory.receiptDrug.total" text="Estimated Price" /></b></td>
		<c:if  test="${category!='General'}">
			<td><fmt:formatNumber value="0.00" type="number" pattern="#"/></td>
		</c:if>
		<c:if  test="${category=='General'}">
			<td><fmt:formatNumber value="${total}" type="number" pattern="#"/></td>
		</c:if>

	</tr>	
	<br />
	</c:when>
	</c:choose>
	</table>
	<table  class="spacer" style="margin-left: 60px; margin-top: 60px;">
		<tr>
			<td>Treating Doctor </td>
			<td><b>:${paymentMode}</b></td>
		</tr>
		<tr>
			<td>Attending Pharmacist</td>
			<td><b>:${cashier}</b></td>
		</tr>
	</table>
<br/><br/><br/><br/><br/><br/>
<span style="float:right;font-size: 1.5em">Signature of Inventory Clerk/ Stamp</span>
</div>
<!-- END PRINT DIV -->   