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


<table class="box">
		<tr>
			<th>Identifier</th>
			<th>Name</th>
			<th>Age</th>
		</tr>
		<c:if test ="${not empty patients }">
			<c:forEach items="${patients }" var="patient" varStatus="varStatus">
				<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '  >
					<td ><a href="#" onclick="ISSUE.addPatient('createPatientIssueDrug.form?patientId=${patient.patientId}');">${patient.patientIdentifier.identifier}</a></td>
					<td>${patient.givenName}&nbsp;${patient.familyName}&nbsp;${fn:replace(patient.middleName,","," ")}</td>
	                <td>
	                	<c:choose>
	                		<c:when test="${patient.age == 0  }">&lt 1</c:when>
	                		<c:otherwise >${patient.age }</c:otherwise>
	                	</c:choose>
	                </td>
				</tr>
			</c:forEach>
		</c:if>
	</table>