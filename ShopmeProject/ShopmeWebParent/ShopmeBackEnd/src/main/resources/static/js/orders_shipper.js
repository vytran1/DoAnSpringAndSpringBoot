/**
 * 
 */
var confirmtext;
var confirmModalDialog;
var yesButton;
var noButton;
var iconNames={
	'PICKED':"fa-people-carry-box",
	'SHIPPING':"fa-box-open",
	"DELIVERED":"fa-shipping-fast",
	"RETURNED":"fa-undo"
};
$(document).ready(function() {
	    confirmtext = $("#confirmText");
	    confirmModalDialog = $("#confirmModal");
	    yesButton =$("#yesButton");
	    noButton = $("#noButton");
    	$(".linkUpdateStatus").on("click",function(e){
			
			e.preventDefault();
			link = $(this);
			
			showUpdateConfirmModal(link);
			
			
			
		});
    	
    	addEventHandlerForYesButton();
})
       
function addEventHandlerForYesButton(){
	yesButton.click(function(e){
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	})
}


function showUpdateConfirmModal(link){
	noButton.text("No");
	yesButton.show();
	orderId = link.attr("orderId");
	
	status = link.attr("status");
	yesButton.attr("href",link.attr("href"));
	confirmtext.text("Are you sure you want to update status of the order ID #"+orderId +" to the status " + status + "?");
	confirmModalDialog.modal();
}

function sendRequestToUpdateOrderStatus(button){
	requestURL = button.attr("href");
	
	$.ajax({
		 type:"POST",
		 url: requestURL,
		 beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 },
	 }).done(function(response){
		 showMessageModal("Order updated successfully");
		 updateIconColor(response.orderId,response.orderStatus);
	 }).fail(function(err){
		showMessageModal("Error occuring when updating status");
	 });
}

function showMessageModal(message){
	noButton.text("Close");
	yesButton.hide();
	confirmtext.text(message);
}

function updateIconColor(orderId,status){
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fa-solid  "  + iconNames[status] + " fa-2x icon-green'></i>")
};