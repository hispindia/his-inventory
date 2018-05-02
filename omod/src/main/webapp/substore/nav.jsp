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
<%@ include file="../includes/js_css.jsp" %>
<br/>
<c:if test ="${not empty store && not empty store.parent && store.isDrug != 1  }">
<b>Drug&nbsp;| <a href="#" onclick="ACT.go('itemViewStockBalanceSubStore.form');">Item </a></b>
<br/><br/>
</c:if>
<b><a href="#" onclick="ACT.go('patientQueueDrugOrder.form');"><spring:message code="inventory.substore.patientQueueForDrugOrders"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIssueDrugList.form');"><spring:message code="inventory.substore.issueDrugPatient"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIssueDrugAccountList.form');"><spring:message code="inventory.substore.issueDrugAccount"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIndentDrugList.form');"><spring:message code="inventory.substore.indentDrug"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('viewStockBalanceSubStore.form');"><spring:message code="inventory.viewStockBalance"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('viewStockBalanceExpiry.form');"><spring:message code="inventory.viewStockBalanceExpiry"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIssueDrugListToVoid.form');"><spring:message code="inventory.voidBill"/></a></b>&nbsp;
<br/><br/>


