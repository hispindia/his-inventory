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
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp"%>

<script type="text/javascript">
jQuery(document).ready(function(){
$('.serquncalc').keyup(function() {
    var result = 0;
    $('#total').attr('value', function() {
        $('.serpricalc').each(function() {
            if ($(this).val() !== '') {
                result += parseInt($(this).val());
            }
        });
        return result;
    });
});
var sos=${serviceOrderSize};
if(sos==0){
jQuery("#savebill").hide(); 
  }

});
</script>

<script type="text/javascript">
function updatePrice(incon){
var con=incon.toString();
var serqunid=con.concat("servicequantity"); 
var serpriid=con.concat("serviceprice");
var unipriid=con.concat("unitprice");  
//alert(document.getElementById(serqunid).value);
serqun=jQuery("#"+serqunid).val();
unpri=jQuery("#"+unipriid).val();
jQuery("#"+serpriid).val(serqun*unpri);
}
</script>

<script type="text/javascript">
function disable(incon){
var icon=incon.toString();
if(jQuery("#"+icon+"selectservice").attr('checked')) {
  jQuery("#"+icon+"servicequantity").removeAttr("disabled");
  jQuery("#"+icon+"paybill").removeAttr("disabled");
} 
else{
 jQuery("#"+icon+"servicequantity").attr("disabled", "disabled"); 
 jQuery("#"+icon+"paybill").attr("disabled", "disabled"); 
}
}
</script>

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">List of drug</b>
</div>
<br />

<form id="orderBillingForm"
	action="drugorder.form?patientId=${patientId}&encounterId=${encounterId}&indCount=${serviceOrderSize}&billType=mixed"
	method="POST">
	<table id="myTable" class="tablesorter" class="thickbox">
		<thead>
			<tr>
				<th style="text-align: center;">S.No</th>
				<th style="text-align: center;">Drug Name</th>
				<th style="text-align: center;">Formulation</th>
				<th style="text-align: center;">Frequency</th>
				<th style="text-align: center;">Days</th>
				<th style="text-align: center;">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="dol" items="${drugOrderList}" varStatus="index">
				<c:choose>
					<c:when test="${index.count mod 2 == 0}">
						<c:set var="klass" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="klass" value="even" />
					</c:otherwise>
				</c:choose>
				<tr class="${klass}" id="">
					<td align="center">${index.count}</td>
					<td align="center">${dol.inventoryDrug.name}</td>
					<td align="center">${dol.inventoryDrugFormulation.name}-${dol.inventoryDrugFormulation.dozage}</td>
					<td align="center">${dol.frequency.name}</td>
					<td align="center">${dol.noOfDays}</td>
					<td align="center"><input type="button" onclick=""
						value="Process">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>