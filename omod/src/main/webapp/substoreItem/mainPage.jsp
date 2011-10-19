<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit substore" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<spring:message var="pageTitle" code="inventory.subStore.manage" scope="page"/>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="nav.jsp" %>


<%@ include file="/WEB-INF/template/footer.jsp" %>