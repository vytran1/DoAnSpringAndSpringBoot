//allalala
$(document).ready(function(){
	$(".linkVoteReview").on("click",function(e){
		e.preventDefault();
		voteReview($(this));
	})
})

function voteReview(link){
	requestURL = link.attr("href");
	$.ajax({
		type:"POST",
		url: requestURL,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		}
	}).done(function(voteResult){
		console.log(voteResult);
		$("#modalDialog").on("hide.bs.modal",function(e){
			updateVoteCountAndIcons(link,voteResult);
		})
		showModalDialog("Vote Review",voteResult.message);
	}).fail(function(){
		showErrorModal("Error voting review")
	});
}

function updateVoteCountAndIcons(link,voteResult){
	reviewId = link.attr("reviewId");
	voteUpLink = $("#linkVoteUp-" + reviewId);
	voteDownLink = $("#linkVoteDown-" + reviewId);
	$("#voteCount-"+reviewId).text(voteResult.voteCount);
	message = voteResult.message;
	if(message.includes("You have successfully voted  up")){
		highlightVoteUpIcon(link,voteDownLink);
	}else if(message.includes("You have successfully voted  down")){
		highlightVoteDownIcon(link,voteUpLink);
	}else if(message.includes("unvoted up")){
		unhighlightVoteUpIcon(voteUpLink);
	}else if(message.includes("unvoted down")){
		unhighlightVoteDownIcon(voteDownLink);
	}
}

function highlightVoteUpIcon(upLink,downLink){
	upLink.removeClass("fa-regular").addClass("fa-solid");
	upLink.attr("title","Undo vote up this review");
	downLink.removeClass("fa-solid").addClass("fa-regular");
}
function highlightVoteDownIcon(downLink,upLink){
	downLink.removeClass("fa-regular").addClass("fa-solid");
	downLink.attr("title","Undo vote down this review");
	upLink.removeClass("fa-solid").addClass("fa-regular");
}
function unhighlightVoteUpIcon(voteUpLink){
	voteUpLink.attr("title","Vote down this review");
	voteUpLink.removeClass("fa-solid").addClass("fa-regular");
}
function unhighlightVoteDownIcon(voteDownLink){
	voteDownLink.attr("title","Vote down this review");
	voteDownLink.removeClass("fa-solid").addClass("fa-regular");
}