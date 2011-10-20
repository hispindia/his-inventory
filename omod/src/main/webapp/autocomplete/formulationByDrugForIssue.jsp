 <%--
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Inventory module.
 *
 *  Inventory module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Inventory module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Inventory module.  If not, see <http://www.gnu.org/licenses/>.
 *
--%> 
<%@ include file="/WEB-INF/template/include.jsp" %>
<select name="formulation" id="formulation"  onchange="ISSUE.formulationOnChange(this);"  style="width: 200px;">
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
       <c:forEach items="${formulations}" var="formulation">
           <option value="${formulation.id}" <c:if test="${formulation.id == formulationId }">selected</c:if> >${formulation.name}-${formulation.dozage}</option>
       </c:forEach>
</select>