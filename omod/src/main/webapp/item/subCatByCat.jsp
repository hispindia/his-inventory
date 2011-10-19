<%@ include file="/WEB-INF/template/include.jsp" %>
<select name="subCategory" id="subCategory"  >
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
       <c:forEach items="${subCategories}" var="subCategory">
           <option value="${subCategory.id}"  >${subCategory.name}</option>
       </c:forEach>
</select>