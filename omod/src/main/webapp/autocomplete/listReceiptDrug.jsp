<%@ include file="/WEB-INF/template/include.jsp" %>

<table class="box">
	<tr>
		<th>#</th>
		<th>Date of expiry</th>
		<th title="Date of manufacturing">DM</th>
		<th>Company name</th>
		<th>Batch no</th>
		<th title="Quantity available">Qty available</th>
		<th title="Issue quantity">Issue qty</th>
	</tr>
	<c:choose>
	<c:when test="${not empty listReceiptDrug}">
	<c:forEach items="${listReceiptDrug}" var="avaiable" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${varStatus.count }"/></td>
		<td><openmrs:formatDate date="${avaiable.dateExpiry}" type="textbox"/> </td>
		<td><openmrs:formatDate date="${avaiable.dateManufacture}" type="textbox"/> </td>
		<td title="${avaiable.companyName }">${avaiable.companyNameShort}</td>
		<td>${avaiable.batchNo }</td>
		<td>${avaiable.currentQuantity}</td>
		<td><em>*</em><input type="text" id="${avaiable.id }" ${!varStatus.first ? 'value=0' : ''} onchange="INVENTORY.checkValueExt(this, '${avaiable.currentQuantity}');" name="${avaiable.id }" class="required digits" size="5"/></td>
		</tr>
	</c:forEach>
	
	</c:when>
	<c:otherwise>
	<tr >
		<td colspan="6">This drug is empty in your store please indent it
		<input type="hidden" id="${avaiable.id }" name="${avaiable.id }" class="required digits" size="5"/></td>
		
	</tr>	
	</c:otherwise>
	</c:choose>
</table>