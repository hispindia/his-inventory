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
					<td>${patient.givenName}&nbsp;${patient.middleName}&nbsp;${patient.familyName}</td>
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