
 $(document).ready(function(){ 
	   formatRatingNumber();
 })
 
function formatRatingNumber(){
       ratingText = $("#ratingNumber").text();
       formatRating = $.number(ratingText,2,decimalSeparator,thousandSeparator);
       $("#ratingNumber").text(formatRating);
}
 
$(".product-detail-rating-star").rating({
        	  displayOnly:true,
        	  hoverOnclear:false,
        	  showCaption:false,
        	  theme:'krajee-svg'
 })