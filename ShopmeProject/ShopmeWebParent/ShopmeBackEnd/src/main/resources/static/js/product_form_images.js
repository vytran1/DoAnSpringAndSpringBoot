/**
 * 
 */
   var extraImageCount =0;
   
   
       $(document).ready(function(){
    	  
    	   
    	   $("input[name='extraImage']").each(function(index){
			   extraImageCount++;
			   $(this).change(function(){
				if(!checkFileSize(this)){
			         return;
		        } 
				showExtraImageThumbnail(this,index);
			   });
		   });
    	   
    	   $("a[name='linkRemoveExtraImage']").each(function(index){
			   $(this).click(function(){
				   removeExtraImage(index);
			   });
		   });
       });
       
      
       
       function showExtraImageThumbnail(fileInput,index){
		    var file = fileInput.files[0];
		    
		    fileName = file.name;
		    imageNameHiddenField = $("#imageName" + index);
		    if(imageNameHiddenField.length){
				imageNameHiddenField.val(fileName);
			}
		    
	        var reader = new FileReader();
	        reader.onload = function(e){
		       $("#extraThumbnail" + index).attr("src",e.target.result);
	        };
	        reader.readAsDataURL(file);
	        if(index >= extraImageCount - 1){
			   addExtraImageSection(index + 1);	
			}
	        
	   } 
       
        function  addExtraImageSection(index){
	   htmlExtraImage = `
	           <div class="col border m-3 p-2" id="divExtraImage${index}">
                        <div id="extraImageHeader${index}">
                            <label>Extra Image ${index + 1}</label>
                            
                        </div>
                        <div>
                            <img alt="Extra image ${index + 1} review" src="${defaultImageThumbnailSrc}" class="img-fluid" id="extraThumbnail${index}">
                        </div>
                        <div>
                             <input type="file"  name ="extraImage"  accept="image/png, image/jpeg" onchange="showExtraImageThumbnail(this,${index})">
                        </div>
                  </div>
	   `;
	   htmlRemove = `<a class="btn fa-solid fa-times-circle fa-2x float-right" href="javascript:removeExtraImage(${index-1})"> </a> `;
        
	   $("#divProductImages").append(htmlExtraImage);
	   $("#extraImageHeader" + (index -1)).append(htmlRemove);
	   extraImageCount++;
   }
   
         
       
       
       function removeExtraImage(index){
		   $("#divExtraImage" + index).remove();
	   }