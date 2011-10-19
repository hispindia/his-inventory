<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span>
</c:forEach>
<spring:bind path="issue">
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
<span class="boxHeader">Create account</span>
<form method="post" class="box" id="formCreateAccount">

<table  width="90%" cellpadding="5" cellspacing="5">
<tr>
	<td>Name<em>*</em></td>
	<td>
		<spring:bind path="issue.name">
		<input type="text" id="${status.expression}" name="${status.expression}"  size="35" />
		<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
		</spring:bind>
	</td>

</tr>
</table>
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>">

</form>