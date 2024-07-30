$(document).ready(function(){
	$("#buttonAddToCart").on("click",function(){
		addToCart();
	})
})

function addToCart(){
	var quantity = $("#quantity" + productID).val();
	url = contextPath + "cart/add/" + productID + "/" + quantity;
	
	$.ajax({
		type:"POST",
		url: url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(response){
		showModalDialog("Shopping Cart",response);
	}).fail(function(jqXHR){
		showErrorModal(jqXHR.responseText);
	});
}