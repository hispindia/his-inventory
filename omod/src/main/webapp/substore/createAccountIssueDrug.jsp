<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<form class="box" method="post" id="createAccountIssueDrug">
<table width="100%">
	<tr>
		<td><spring:message code="inventory.issueDrug.account"/><em>*</em></td>
		<td><input type="text" name="accountName" id="accountName" size="35"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="2"><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>"></td>
	</tr>
</table>
</form>