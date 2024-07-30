/**
 * 
 */

   $(document).ready(function() {
    	   $("#logoutLink").on("click",function(e){
    		   e.preventDefault();
    		   document.logoutForm.submit();
    	   });
    	   customizeDropDownMenu();
    	   customizeTabs();
       })
       
    
 function customizeDropDownMenu(){
	 $(".navbar .dropdown").hover(
		 function(){
		    $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown()
	     },
	     function(){
		    $(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp()
	     }
	 );
	 
	 $(".dropdown > a").click(function(){
		 location.href= this.href
	 })
	 
 }   
 
 
 function showDeteleConfirmModal(link,entityName){
	 var url = link.attr("href");
	 entityId = link.attr("entityID");
	 $("#yesButton").attr("href",url);
	 $("#confirmText").text("Are you sure want to delete this  " + entityName + "ID" + entityId + "?");
	 $("#confirmModal").modal();
 }
 
 function customizeTabs(){
	 var url = document.location.toString();
	 if(url.match('#')){
		 $('.nav-tabs a[href="#' + url.split("#")[1] + '"]').tab("show");
		 
	 }
	 //change hash for page load
	 $('.nav-tabs a').on("shown.bs.tab",function(e){
		 window.location.hash= e.target.hash;
	 })
 }