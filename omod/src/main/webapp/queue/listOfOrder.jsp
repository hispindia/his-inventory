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

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">List of Order</b>
</div>
<input type="hidden" id="patientType" value="${patientType}">
<div>	
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
</div>

<br />

	<table id="myTable" class="tablesorter" class="thickbox">
	<thead>
		<tr align="center">
			<th>S.No</th>
			<th>Order Id</th>
			<th>Date</th>  
			<th>Sent From</th>         
            
		</tr>
	</thead>
	<tbody>
		<c:forEach var="listOfOrder" items="${listOfOrders}" varStatus="index">
			<c:choose>
				<c:when test="${index.count mod 2 == 0}">
					<c:set var="klass" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="klass" value="even" />
				</c:otherwise>
			</c:choose>
			<tr class="${klass}">
				<td>${index.count}</td>
				<td><a href="drugorder.form?patientId=${patientId}&encounterId=${listOfOrder.encounter.encounterId}
				&date=${date}&patientType=${patientType}">${listOfOrder.encounter.encounterId}</a>
				</td>
				<td><openmrs:formatDate date="${listOfOrder.createdOn}" /></td>
				<td>${listOfOrder.referralWardName}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="/WEB-INF/template/footer.jsp"%>