<%@ include file="/WEB-INF/template/include.jsp" %>

<table class="box" width="100%">
	<tr>
		<th>Quantity available</th>
		<th>Issue quantity</th>
	</tr>
	<c:if  test="${sumReceiptItem > 0}">
	<tr >
		<td>${sumReceiptItem}</td>
		<td><em>*</em><input type="hidden" id="currentQuantity" name="currentQuantity" value="${sumReceiptItem}"  /><input type="text" id="issueItemQuantity" onchange="INVENTORY.checkValueExt(this, '${sumReceiptItem}');" name="issueItemQuantity" class="required digits" size="5"/></td>
	</tr>
	
	</c:if>
	<c:if  test="${sumReceiptItem <= 0}">
	<tr >
		<td colspan="2">This item is empty in your store please indent it
		<input type="hidden" id="issueItemQuantityA" name="issueItemQuantityA" class="required number" size="5"/>
		</td>
		
	</tr>	
	</c:if>
</table>