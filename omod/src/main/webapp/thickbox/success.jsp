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
<script type="text/javascript">

	function runout(urlS,value){
		setTimeout(function(){
			self.parent.tb_remove();
			self.parent.ACT.go(urlS);
			},value);
		//setTimeout("self.parent.location.href=self.parent.location.href;self.parent.tb_remove()",3000);
	}
</script>
<body onload="runout('${urlS}',3000);">
<center>
		<div style="height:40px; float: center; vertical-align:middle"><img src="${pageContext.request.contextPath}/moduleResources/inventory/ajax-loader.gif"/></div>
		<span class="text center" style="color:#000000">
         ${message}
		<a href="#"  onclick="runout('${urlS}',0);">click here</a>
		</span>
</center>
</body>
</html>
