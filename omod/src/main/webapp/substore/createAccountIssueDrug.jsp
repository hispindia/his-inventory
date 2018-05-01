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
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<form class="box" method="post" id="createAccountIssueDrug" onsubmit="javascript:return validate();">
<table width="100%">
	<tr>
		<td><spring:message code="inventory.issueDrug.account"/><em>*</em></td>
		<td><input type="text" name="accountName" id="accountName" size="35"/>
		</td>
	</tr>
	<tr></tr><tr></tr><tr></tr><tr></tr>
	<tr>
		<td><spring:message code="inventory.issueDrug.accountType"/><em>*</em></td>
		<td><select id="accountType" name="accountType" style="width:280px">
        <option value=""> </option>
        <option value="ISSUE DRUG TO ADJUSTMENT">ISSUE DRUG TO ADJUSTMENT</option>
        <option value="ISSUE DRUG TO RETUN">ISSUE DRUG TO RETUN</option>
        </select>
        </td>
	</tr>
	<tr>
		<td></td>
		<td colspan="2"><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="<spring:message code="general.save"/>"></td>
	</tr>
</table>
</form>

<script type="text/javascript">
function validate(){
var accountType=jQuery("#accountType").val();
if (accountType==null || accountType=="")
{
alert("Please Select Account Type");
return false;
}
}
</script>