<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<div id="searchbox"></div>
<div id="patientSearchResult"></div>

<script type="text/javascript">

	jQuery(document).ready(function(){
		jQuery("#searchbox").showPatientSearchBox({					
			resultView: "/module/inventory/patientsearch/issuePatientDrug",
			rowPerPage: 15
		});		
	});
</script>