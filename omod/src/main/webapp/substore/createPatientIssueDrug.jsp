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
<div id="searchbox"></div>
<div id="patientSearchResult"></div>

<script type="text/javascript">
var totalVal=0;
var waiverPercentge=0;
var totalAmountPy=0;
	jQuery(document).ready(function(){
	 var ca = document.cookie.split(';'); 
	 var resetTov=0;
	 var resetWp=0;
	 var resetTap=0;
     totalVal=ca[0];
     waiverPercentge=ca[1];
     totalAmountPy=ca[2];
		jQuery("#searchbox").showPatientSearchBox({					
			resultView: "/module/inventory/patientsearch/issuePatientDrug",
			rowPerPage: 15
		});		
	});
</script>