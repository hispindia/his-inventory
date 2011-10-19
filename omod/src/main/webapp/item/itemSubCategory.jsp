<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit itemSubCategory" otherwise="/login.htm" redirect="/module/inventory/itemSubCategory.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<h2><spring:message code="inventory.itemSubCategory.manage"/></h2>

<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span>
</c:forEach>
<spring:bind path="itemSubCategory">
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
<form method="post" class="box" id="itemSubCategory">
<table>
	<tr>
		<spring:bind path="itemSubCategory.id">
			<input type="hidden" name="${status.expression}" id="${status.expression}" value="${status.value}" />
		</spring:bind>
		<tr>
		<td><spring:message code="inventory.itemSubCategory.category"/><em>*</em></td>
		<td>
			<spring:bind path="itemSubCategory.category">
			<select name="${status.expression}" id="${status.expression}"  tabindex="20" >
				<option value=""><spring:message code="inventory.pleaseSelect"/></option>
                <c:forEach items="${categories}" var="vCat">
                    <option value="${vCat.id}" <c:if test="${vCat.id == itemSubCategory.category.id }">selected</c:if> >${vCat.name}</option>
                </c:forEach>
   			</select>
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>

	</tr>
	<tr>
		<td><spring:message code="inventory.itemSubCategory.name"/><em>*</em></td>
		<td>
			<spring:bind path="itemSubCategory.name">
				<input type="text" id="${status.expression}" name="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.itemSubCategory.code"/><em>*</em></td>
		<td>
			<spring:bind path="itemSubCategory.code">
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="inventory.itemSubCategory.description"/></td>
		<td>
			<spring:bind path="itemSubCategory.description">
				<input type="text" name="${status.expression}" id="${status.expression}" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	
</table>
<br />
<input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>">
<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.cancel"/>" onclick="ACT.go('itemSubCategoryList.form');">
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>