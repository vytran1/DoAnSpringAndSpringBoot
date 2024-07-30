$(document).ready(function(){
	$(".linkMinus").on("click",function(evt){
		evt.preventDefault();
		productID = $(this).attr("pid");
		quantityInput = $("#quantity" + productID);
		newQuantityValue = parseInt(quantityInput.val()) -1;
		
		if(newQuantityValue > 0 ){
			quantityInput.val(newQuantityValue);
		}else{
			showWarningModal("Quantity minimum is one")
		}
		
	});
	$(".linkPlus").on("click",function(evt){
		evt.preventDefault();
		productID = $(this).attr("pid");
		quantityInput = $("#quantity" + productID);
		newQuantityValue = parseInt(quantityInput.val()) +1;
		
		if(newQuantityValue <= 5 ){
			quantityInput.val(newQuantityValue);
		}else{
			showWarningModal("Maximum quantity is five")
		}
	});
});