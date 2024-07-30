decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';

$(document).ready(function(){
	$(".linkMinus").on("click",function(evt){
		evt.preventDefault();
		decrease_quantity($(this));
		
	});
	$(".linkPlus").on("click",function(evt){
		evt.preventDefault();
		increase_quantity($(this));
	});
	$(".linkRemove").on("click",function(evt){
		evt.preventDefault();
		
		removeProductCartItem($(this));
	});
});

function decrease_quantity(link){
	    productID = link.attr("pid");
	    inputQuantityAvailbel = $("#maxAdd" + productID);
	    quantityAvailbel =  parseInt(inputQuantityAvailbel.val());
		quantityInput = $("#quantity" + productID);
		newQuantityValue = parseInt(quantityInput.val()) -1;
		newMaxValue = quantityAvailbel + 1;
			
		
		
		if(newQuantityValue > 0 ){
			quantityInput.val(newQuantityValue);
			inputQuantityAvailbel.val(newMaxValue);
			updateQuantity(productID,newQuantityValue,"DECREASE");
		}else{
			showWarningModal("Quantity minimum is one")
		}
}

function increase_quantity(link){
	    productID = link.attr("pid");
	    inputQuantityAvailbel = $("#maxAdd" + productID);
	    quantityAvailbel =  parseInt(inputQuantityAvailbel.val());
		quantityInput = $("#quantity" + productID);
		newQuantityValue = parseInt(quantityInput.val()) + 1;
		newMaxValue = quantityAvailbel - 1;
		
		
		if(quantityAvailbel < 1){
		     showWarningModal("We do not have enought product for your need");
		     return;
		}
		
		if(newQuantityValue <= 5 ){
			quantityInput.val(newQuantityValue);
			console.log(productID);
			console.log(inputQuantityAvailbel);
			console.log(quantityAvailbel);
			inputQuantityAvailbel.val(newMaxValue);
			updateQuantity(productID,newQuantityValue,"INCREASE");
		}else{
			showWarningModal("Quantity maximum is five")
		}
}

function updateQuantity(productID,quantity,type){
	
	url = contextPath + "cart/update/" + productID + "/" + quantity +"/" +type;
	
	$.ajax({
		type:"POST",
		url: url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(updatedSubTotal){
		updateSubToTal(updatedSubTotal,productID);
		updateEstimatedToTal();
	}).fail(function(jqXHR){
		showErrorModal(jqXHR.responseText);
	});
}

function updateSubToTal(updatedSubToTal,productID){
	$("#subToTal" + productID).text(formatCurrency(updatedSubToTal));
}

function updateEstimatedToTal(){
	total = 0.0;
	productCount = 0;
	$(".subTotal").each(function(index,element){
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
		productCount++;
	});
	if(productCount < 1){
		showEmptyShoppingCart();
	}
	else{
	   $("#total").text(formatCurrency(total));
	}
}
function formatCurrency(amount) {
	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator);
}
function clearCurrencyFormat(numberString) {
	result = numberString.replaceAll(thousandsSeparator, "");
	return result.replaceAll(decimalSeparator, ".");
}

function removeProductCartItem(link){
	url = link.attr("href");
	$.ajax({
		type:"DELETE",
		url: url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(response){
		rowNumber = link.attr("rowNumber");
		deleteProductHTML(rowNumber);
		updateEstimatedToTal();
		updateCountNumber();
		showModalDialog("Shopping Cart",response);
	}).fail(function(){
		showErrorModal("Error while deleting quantityto shopping cart");
	});
}
function deleteProductHTML(rowNumber){
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}
function updateCountNumber(){
	$(".divCount").each(function(index,element){
		element.innerHTML = " " + (index+1);
	});
};
function showEmptyShoppingCart(){
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
}