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
<center><b>Drug&nbsp;| <a href="#" onclick="ACT.go('itemViewStockBalance.form');">Item</a></b></center>
<br/><br/>
<b><a href="#" onclick="ACT.go('viewStockBalance.form');"><spring:message code="inventory.viewStockBalance"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('viewStockBalanceExpiry.form');"><spring:message code="inventory.viewStockBalanceExpiry"/></a></b>&nbsp;|
<!-- 
<b><a href="#" onclick="ACT.go('purchaseOrderForGeneralStoreList.form');"><spring:message code="inventory.mainStore.purchaseOrderForGeneralStore"/></a></b>&nbsp;|
 --> 
<b><a href="#" onclick="ACT.go('receiptsToGeneralStoreList.form');"><spring:message code="inventory.mainStore.receiptsToGeneralStore"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('transferDrugFromGeneralStore.form');"><spring:message code="inventory.mainStore.transferFromGeneralStore"/></a></b>
<br/><br/>

