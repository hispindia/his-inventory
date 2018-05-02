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
<select id="drugId" name="drugId" onchange="RECEIPT.onBlurDrug(this);"  style="width: 200px;">
	<option value=""><spring:message code="inventory.pleaseSelect"/></option>
	   <c:if test ="${not empty drugs }">
	       <c:forEach items="${drugs}" var="drug">
	           <option value="${drug.id}" title="${drug.name}">${drug.name}</option>
	       </c:forEach>
       </c:if>
</select>