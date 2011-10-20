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
<b><a href="#" onclick="ACT.go('storeList.form');"><spring:message code="inventory.store.manage"/></a></b>&nbsp; | 
<b><spring:message code="inventory.item.manage"/></b>&nbsp;| 
<b><a href="#" onclick="ACT.go('drugList.form');"><spring:message code="inventory.drug.manage"/></a></b>
<br/>
<br/>
<b><a href="#" onclick="ACT.go('itemCategoryList.form');"><spring:message code="inventory.itemCategory.manage"/></a></b>&nbsp; | 
<b><a href="#" onclick="ACT.go('itemUnitList.form');"><spring:message code="inventory.itemUnit.manage"/></a></b>&nbsp;| 
<b><a href="#" onclick="ACT.go('itemSubCategoryList.form');"><spring:message code="inventory.itemSubCategory.manage"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('itemSpecificationList.form');"><spring:message code="inventory.itemSpecification.manage"/></a></b>&nbsp;|
<b><a href="#" onclick="ACT.go('itemList.form');"><spring:message code="inventory.item.manage"/></a></b>
<br/><br/>

