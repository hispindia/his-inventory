<%@ include file="/WEB-INF/template/include.jsp" %>
<select id="itemId" name="itemId" onchange="RECEIPT.onBlurItem(this);"  style="width: 200px;">
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
	   <c:if test ="${not empty items }">
	       <c:forEach items="${items}" var="item">
	           <option value="${item.id}" title="${item.name}">${item.name}</option>
	       </c:forEach>
       </c:if>
</select>