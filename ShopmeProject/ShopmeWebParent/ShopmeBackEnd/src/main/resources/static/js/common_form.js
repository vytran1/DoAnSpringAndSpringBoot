 $(document).ready(function(){
	   $("#buttonCancle").on("click",function(){
	       window.location = moduleURL;
	   });
	   $("#fileImage").change(function(){
		   if(!checkFileSize(this)){
			   return;
		   }
		   showImageThumbnail(this);
	   });
   });
   
   function showImageThumbnail(fileInput){
	   var file = fileInput.files[0];
	   var reader = new FileReader();
	   reader.onload = function(e){
		   $("#thumbnail").attr("src",e.target.result);
	   };
	   reader.readAsDataURL(file);
	   
	   
   }
   
  
   
    function showModalDialog(title,message){
	   $("#modalTitle").text(title);
	   $("#modalBody").text(message);
	   $("#modalDialog").modal('show');
   }
   
   function showErrorModal(message){
	   showModalDialog("Error",message);
   }
   function showWarningModal(message){
	   showModalDialog("Warning",message);
   }
   
   function checkFileSize(fileInput){
	    filesize = fileInput.files[0].size
		   //alert("File size" + filesize);
		   if(filesize >MAX_FILE_SIZE){
			   fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + "bytes")
			   fileInput.reportValidity();
			   return false
		   }else{
			   fileInput.setCustomValidity("");
			   return true;
		   }
   }