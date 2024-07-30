var dropDownCountries;
var dataListState;
var fieldState;
     
     
     $(document).ready(function(){
    	  dropDownCountries = $("#country");
    	  dataListState = $("#listStates");
    	  fieldState = $("#state");
    	  dropDownCountries.on("change",function(){
    	  loadStatesForCountries();
    	  fieldState.val("").focus();
    	  });
    	});
    	         
    	function loadStatesForCountries(){
    	       	  selectedCountry = $("#country option:selected");
    	       	  countryID = selectedCountry.val();
    	       	  url = contextPath + "states/list_by_country/" + countryID;
    	       	  
    	       	  $.get(url,function(responseJson){
    	       		  dataListState.empty();
    	       		  $.each(responseJson,function(index,state){
    	       			 
    	       			  $("<option>").val(state.name).text(state.name).appendTo(dataListState);
    	       		  })
    	       	  });
    	}