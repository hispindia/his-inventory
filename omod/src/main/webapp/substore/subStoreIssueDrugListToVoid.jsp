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

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.issueDrug.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="nav.jsp" %>
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span><
</c:forEach>
<br />

<form method="get"  id="form">
<table >
	<tr>
		<td>Identifier/Name</td>
		<td>
			<input type="text" name="issueName" id="issueName" value="${issueName }"/>
		</td>
		<td><spring:message code="inventory.fromDate"/></td>
		<td><input type="text" id="fromDate" class="date-pick left" readonly="readonly" name="fromDate" value="${fromDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td><spring:message code="inventory.toDate"/></td>
		<td><input type="text" id="toDate" class="date-pick left" readonly="readonly" name="toDate" value="${toDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
		<td>Bill No.</td>
		<td><input type="text" id="billNo" name="billNo" value="${billNo }" ></td>
		<td><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search"/></td>
	</tr>
</table>
<br />
<span class="boxHeader"><spring:message code="inventory.issueDrug.list"/></span>
<div class="box">
<table width="100%" cellpadding="5" cellspacing="0">
	<tr>
	<th>#</th>
	<th>Bill No.</th>
	<th><spring:message code="inventory.issueDrug.identifier"/></th>
	<th>Name</th>
	<th>Age</th>
	<th><spring:message code="inventory.issueDrug.createdOn"/></th>
	<th>Action</th>
	<th>Description</th>
	<th>Voided By</th>
	</tr>
	<c:choose>
	<c:when test="${not empty listIssue}">
	<c:forEach items="${listIssue}" var="issue" varStatus="varStatus">
	<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
		<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>
	     <td> ${issue.id}</td>
		<td> <a href="#" title="Detail issue drug to this patient" onclick="ISSUE.detailIssueDrug('${issue.id}');">${issue.identifier}</a> </td>
		<td>${issue.patient.givenName}&nbsp;${issue.patient.familyName}</td>
		<td>
              	<c:choose>
              		<c:when test="${issue.patient.age == 0  }">&lt 1</c:when>
              		<c:otherwise >${issue.patient.age }</c:otherwise>
              	</c:choose>
        </td>	
		<td><openmrs:formatDate date="${issue.createdOn}" type="textbox"/></td>
		<td class='<c:if test="${issue.voided==1}">retired </c:if>'>
		<input type="button" value="Void Bill"
					onclick="voidTheBill(${issue.id},${issue.voided});" />
		</td>
		<c:if test="${issue.voided==1}">
		<td>${issue.voidedReason}</td>
		<td>${issue.voidedBy}</td>
		</c:if>
		</tr>
	</c:forEach>
	</c:when>
	</c:choose>
	
	<tr class="paging-container">
	<td colspan="5"><%@ include file="../paging.jsp" %></td>
</tr>
</table>
</div>

</form>

<script type="text/javascript">
function voidTheBill(billNo,voided){
if(voided==1){
alert("bill already voided");
return false;
}
else{
url = "voidTheBill.form?billNo="+billNo+"&keepThis=false&TB_iframe=true&height=400&width=700";
tb_show(" ",url,false);
return true;
}
}
</script>


<%@ include file="/WEB-INF/template/footer.jsp" %>