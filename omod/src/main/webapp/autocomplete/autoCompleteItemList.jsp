<%@ include file="/WEB-INF/template/include.jsp" %>
<c:choose>
<c:when test="${not empty items}">
[<c:forEach items="${items}" var="item" varStatus="loop">"${item.name}"${!loop.last ? ',' : ''}</c:forEach>]
</c:when>
</c:choose>