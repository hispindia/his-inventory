<%@ include file="/WEB-INF/template/include.jsp" %>
<select name="formulation" id="formulation"   style="width: 200px;">
	   <option value=""><spring:message code="inventory.pleaseSelect"/></option>
       <c:forEach items="${formulations}" var="formulation">
           <option value="${formulation.id}" <c:if test="${formulation.id == formulationId }">selected</c:if> >${formulation.name}-${formulation.dozage}</option>
       </c:forEach>
</select>
