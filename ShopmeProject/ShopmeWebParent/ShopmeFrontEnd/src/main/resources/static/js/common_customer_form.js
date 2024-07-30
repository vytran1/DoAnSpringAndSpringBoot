
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
    	       	  url = contextPath + "settings/list_states_by_country/" + countryID;
    	       	  
    	       	  $.get(url,function(responseJson){
    	       		  dataListState.empty();
    	       		  $.each(responseJson,function(index,state){
    	       			 
    	       			  $("<option>").val(state.name).text(state.name).appendTo(dataListState);
    	       		  })
    	       	  });
    	}
    function checkPasswordMatch(confirmPassword){
	   if(confirmPassword.value != $("#password").val()){
		   confirmPassword.setCustomValidity("Password do not match")
	   }else{
		   confirmPassword.setCustomValidity("")
	   }
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