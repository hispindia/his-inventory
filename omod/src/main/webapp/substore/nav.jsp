<%@ include file="../includes/js_css.jsp" %>
<br/>
<c:if test ="${not empty store && not empty store.parent && store.isDrug != 1  }">
<b>Drug&nbsp;| <a href="#" onclick="ACT.go('itemViewStockBalanceSubStore.form');">Item </a></b>
<br/><br/>
</c:if>
<b><a href="#" onclick="ACT.go('viewStockBalanceSubStore.form');"><spring:message code="inventory.viewStockBalance"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('viewStockBalanceExpiry.form');"><spring:message code="inventory.viewStockBalanceExpiry"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIndentDrugList.form');"><spring:message code="inventory.substore.indentDrug"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIssueDrugList.form');"><spring:message code="inventory.substore.issueDrugPatient"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('subStoreIssueDrugAccountList.form');"><spring:message code="inventory.substore.issueDrugAccount"/></a></b>
<br/><br/>


