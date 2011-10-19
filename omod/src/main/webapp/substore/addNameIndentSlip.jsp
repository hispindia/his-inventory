<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<form method="post" id="formAddNameForPurchase">
<table width="100%">
	<tr>
		<td><spring:message code="inventory.indent.name"/><em>*</em></td>
		<td><input type="text" name="indentName" id="indentName" size="35"/>
		<input type="hidden" name="send" id="send" value="${send}"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="2"><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>"></td>
	</tr>
</table>
</form>