
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
 *  date: 28-june-2013
 *  issue no: #1636
--%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<form method="post" id="admissionForm" class="box">
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
				<c:forEach items="${listReceiptDrug}" var="avaiable"
					varStatus="varStatus">
					<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
						<td><c:out value="${varStatus.count }" />
						</td>
						<td><openmrs:formatDate date="${avaiable.dateExpiry}"
								type="textbox" /></td>
						<td><openmrs:formatDate date="${avaiable.dateManufacture}"
								type="textbox" /></td>
						<td title="${avaiable.companyName }">${avaiable.companyNameShort}</td>
						<td>${avaiable.batchNo }</td>
						<td>${avaiable.currentQuantity}</td>
						<!-- ghanshyam,4-july-2013, issue no # 1984, User can issue drugs only from the first indent -->
						<td><em>*</em><input type="text" id="${avaiable.id }_quantity"
							${!varStatus.first ? 'value=0' : ''} onchange="INVENTORY.checkValueExt(this, '${avaiable.currentQuantity}');"
							name="${avaiable.id }_quantity" class="required digits" size="5" />
						</td>
						<td><input id="${avaiable.id }_drugName"
							name="${avaiable.id }_drugName" type='hidden'
							value="${avaiable.drug.name}" />
						</td>
						<td><input id="${avaiable.id }_formulation"
							name="${avaiable.id }_formulation" type='hidden'
							value="${avaiable.formulation.name}-${avaiable.formulation.dozage}" />
						</td>
						<td><input id="${avaiable.id }_formulationId"
							name="${avaiable.id }_formulationId" type='hidden'
							value="${avaiable.formulation.id }" />
						</td>
					</tr>
				</c:forEach>

			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="6">This drug is empty in your store please indent
						it <input type="hidden" id="${avaiable.id }"
						name="${avaiable.id }" class="required digits" size="5" />
					</td>

				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	<!-- ghanshyam,4-july-2013, issue no # 1984, User can issue drugs only from the first indent -->
	<br /> <input type="button"
		class="ui-button ui-widget ui-state-default ui-corner-all"
		value="Issue" onClick="issueDrugOrder('${listOfDrugQuantity}');"> <input
		type="button"
		class="ui-button ui-widget ui-state-default ui-corner-all"
		value="Cancel" onclick="cancel();">
</form>