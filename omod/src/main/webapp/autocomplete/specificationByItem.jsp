<%@ include file="/WEB-INF/template/include.jsp" %>
<c:if test="${not empty  specifications}">
<td><spring:message code="inventory.item.specification"/><em>*</em></td>
<td>		
<select name="specification" id="specification"   style="width: 200px;">
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
       <c:forEach items="${specifications}" var="specification">
           <option value="${specification.id}" <c:if test="${specification.id == specificationId }">selected</c:if> >${specification.name}</option>
       </c:forEach>
</select>
</td>
</c:if>
