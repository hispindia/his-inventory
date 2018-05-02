 <%--
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author: ghanshyam
 *  date: 15-june-2013
 *  issue no: #1636
--%> 
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="Drug order queue" otherwise="/login.htm" redirect="/module/inventory/main.form" />
<body onLoad="reset()">
<spring:message var="pageTitle" code="inventory.substore.patientQueueForDrugOrders.manage" scope="page"/>
<%@ include file="../substore/nav.jsp" %>

<h2><spring:message code="inventory.substore.patientQueueForDrugOrders.manage"/></h2>	
<br />

<script type="text/javascript">
// get context path in order to build controller url
	function getContextPath(){		
		pn = location.pathname;
		len = pn.indexOf("/", 1);				
		cp = pn.substring(0, len);
		return cp;
	}
</script>

<script type="text/javascript">

	currentPage = 1;
    jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true,showOn: "button",
                buttonImage: "${pageContext.request.contextPath}/moduleResources/inventory/calendar.gif",
                buttonImageOnly: true});
    });
	
	// get queue
	function getQueue(currentPage){
		jQuery("#selection").show(0);
		this.currentPage = currentPage;
		var date = jQuery("#date").val();
		var searchKey = jQuery("#searchKey").val();
		var pgSize = jQuery("#sizeSelector").val();
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/inventory/patientsearchdruggqueue.form",
			data : ({
				date			: date,
				searchKey		: searchKey,
				currentPage		: currentPage,
				pgSize			: pgSize
			}),
			success : function(data) {
				jQuery("#queue").html(data);
				jQuery("#queue").show(0);	
			},
			
		});
	}

	
	/**
	 * RESET SEARCH FORM
	 *    Set date text box to current date
	 *    Empty the patient name/identifier textbox
	 */
	function reset(){
		jQuery("#date").val("${currentDate}");
		jQuery("#searchKey").val("");
		jQuery("#sizeSelector").val(100);
		jQuery("#selection").hide(0);
		jQuery("#queue").hide(0);
	}
</script> 

<div class="boxHeader">
	<strong>Patient Queue for Drug Orders</strong>
</div>
<div class="box">
	Date:
	<input id="date" value="${currentDate}" style="text-align:right;"/>
	Patient ID/Name:
	<input id="searchKey"/>
	<br/>
	<input type="button" value="Get patients" onClick="getQueue(1);"/>
<!-- 	<input type="button" value="Reset" onClick="reset();"/> -->
</div>

<div id="queue">
</div>
<div id="selection">
Show 
 <select name="sizeSelector" id="sizeSelector" onChange="getQueue(1)">
    	<option value="50" id="1">50</option>
      	<option value="100" id="2" selected>100</option>
      	<option value="150" id="3">150</option>
      	<option value="200" id="4">200</option>
	</select>
    entries 
</div>
<%@ include file="/WEB-INF/template/footer.jsp" %>