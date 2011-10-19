<%@ include file="/WEB-INF/template/include.jsp" %>
<select id="drugId" name="drugId" onchange="RECEIPT.onBlur(this);"  style="width: 200px;">
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
	   <c:if test ="${not empty drugs }">
	       <c:forEach items="${drugs}" var="drug">
	           <option value="${drug.id}" title="${drug.name}">${drug.name}</option>
	       </c:forEach>
       </c:if>
</select>