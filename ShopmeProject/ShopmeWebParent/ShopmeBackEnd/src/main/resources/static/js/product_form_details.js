/**
 * 
 */

       $(document).ready(function(){
    	  
    	   
    	 
    	   
    	   $("a[name='linkRemoveDetail']").each(function(index){
			   $(this).click(function(){
				   removeDetailSectionByIndex(index);
			   });
		   });
       });
 function addNextDetailSection(){
	 
	 allDivDetails = $("[id^='divDetail']");
	 
	 divDetailsCount = allDivDetails.length;
	 
	 
	 htmlDetailSection = `
	  <div class="form-inline" id="divDetail${divDetailsCount}">
	             <input type="hidden" name="detailIDs" value="0">
                 <label class="m-3">Name:</label>
                 <input type="text" class="form-control w-25" name="detailNames" maxlength="255" >
                  <label class="m-3">Value:</label>
                 <input type="text" class="form-control " name="detailValues" maxlength="255" >
            </div>
	 
	 `;
	 
	 $("#divProductDetails").append(htmlDetailSection);
	 previousDivDetailSection = allDivDetails.last();
	 previousDivDetailId = previousDivDetailSection.attr("id");
	 htmlRemove = `<a class="btn fa-solid fa-times-circle fa-2x" href="javascript:removeDetailSectionById('${previousDivDetailId}')"> </a> `;
	 
	 previousDivDetailSection.append(htmlRemove);
	 $("input[name='detailNames']").last().focus();
 }
 
 function removeDetailSectionById(id){
	 
	 $("#" + id).remove();
 }
 function removeDetailSectionByIndex(index){
	 $("#divDetail" + index).remove();
 }