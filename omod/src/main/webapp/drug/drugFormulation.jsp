<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit drugFormulation" otherwise="/login.htm" redirect="/module/inventory/drugFormulation.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<h2><spring:message code="inventory.drugFormulation.manage"/></h2>

<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span>
</c:forEach>
<spring:bind path="drugFormulation">
<c:if test="${not empty  status.errorMessages}">
<div class="error">
<ul>
<c:forEach items="${status.errorMessages}" var="error">
    <li>${error}</li>   
</c:forEach>
</ul>
</div>
</c:if>
</spring:bind>
<form method="post" class="box" id="drugFormulation">
<table>
	<tr>
		<spring:bind path="drugFormulation.id">
			<input type="hidden" name="${status.expression}" id="${status.expression}" value="${status.value}" />
		</spring:bind>
		<td><spring:message code="inventory.drugFormulation.name"/><em>*</em></td>
		<td>
			<spring:bind path="drugFormulation.name">
				<input type="text" id="${status.expression}" name="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.drugFormulation.dozage"/><em>*</em></td>
		<td>
			<spring:bind path="drugFormulation.dozage">
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.drugFormulation.description"/></td>
		<td>
			<spring:bind path="drugFormulation.description">
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	
</table>
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.cancel"/>" onclick="ACT.go('drugFormulationList.form');">
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>