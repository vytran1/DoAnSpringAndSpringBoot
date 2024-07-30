/**
 * 
 */

 var buttonLoadCounrtyForStates;
 var dropDownCountryForStates;
 var dropDownState;
 var buttonAddState;
 var buttonUpdateState;
 var buttonDeleteState;
 var labelStateName;
 var fieldStateName;
 
 
 $(document).ready(function(){
	 buttonLoadCounrtyForStates = $("#buttonLoadCountriesForStates");
	 dropDownCountryForStates = $("#dropDownCountriesForStates");
	 
	 dropDownState = $("#dropDownStates");
	 buttonAddState = $("#buttonAddState");
	 buttonUpdateState = $("#buttonUpdateState");
	 buttonDeleteState = $("#buttonDeleteState");
	 
	 labelStateName = $("#labelStateName");
	 fieldStateName = $("#fieldStateName");
	 
	 buttonLoadCounrtyForStates.on("click",function(){
		 loadCountriesForState();
	 });
	 
	 dropDownCountryForStates.on("change",function(){
		 loadStatesForCountries();
	 })
	 
	 dropDownState.on("change",function(){
		 changeFormToSelectedState();
	 })
	 
	 buttonAddState.on("click",function(){
		 if(buttonAddState.val() == "Add"){
			 addState();
		 }
		 else{
			 changeFormStateToNew();
		 }
	 })
	 
	 buttonUpdateState.on("click",function(){
		 updateState();
	 })
	 
	 buttonDeleteState.on("click",function(){
		 deleteState();
	 })
 });
 
 
 function loadCountriesForState(){
	 url = contextPath + "countries/list";
	 $.get(url,function(responseJSON){
		 dropDownCountryForStates.empty();
		 $.each(responseJSON,function(index,country){
			 $("<option>").val(country.id).text(country.name).appendTo(dropDownCountryForStates);
		 }); 
	 }).done(function(){
		 buttonLoadCounrtyForStates.val("Refesh Country List");
		 showToastMessage("All Countries have been loaded");
	 }).fail(function(){
		 showToastMessage("ERROR: Could not connect to server");
	 });
 };
 
 function loadStatesForCountries(){
	 selectedCountry = $("#dropDownCountriesForStates option:selected");
	 countryID = selectedCountry.val();
	 url = contextPath + "states/list_by_country/" + countryID;
	 
	 $.get(url,function(responseJson){
		 dropDownState.empty();
		 
		 $.each(responseJson,function(index,state){
			 $("<option>").val(state.id).text(state.name).appendTo(dropDownState);
		 });
	 }).done(function(){
		 changeFormStateToNew();
		 showToastMessage("All state have been loaded with countryID " + selectedCountry.text());
	 }).fail(function(){
		 showToastMessage("ERROR: Could not connect to server");
	 });
	 
 };
 
 function changeFormStateToNew(){
	 buttonAddState.val("Add");
	 labelStateName.text("text/provinces name:");
	 
	 buttonUpdateState.prop("disabled",true);
	 buttonDeleteState.prop("disabled",true);
	 
	 fieldStateName.val("").focus();
 };
 
 function changeFormToSelectedState(){
	 buttonAddState.prop("value","New");
	 buttonUpdateState.prop("disabled",false);
	 buttonDeleteState.prop("disabled",false);
	 
	 labelStateName.text("Selected State/Provinces");
	 
	 selectedStateName = $("#dropDownStates option:selected").text();
	 fieldStateName.val(selectedStateName);
 };
 
 function addState(){
	 if(!validityFormState()){
		 return;
	 }
	 url = contextPath + "states/save";
	 stateName = fieldStateName.val();
	 
	 selectedCountry = $("#dropDownCountriesForStates option:selected");
	 countryID =selectedCountry.val();
	 countryName = selectedCountry.text();
	 
	 jsonData = {name:stateName,country:{id:countryID,name:countryName}};
	 
	 $.ajax({
		 type:"POST",
		 url: url,
		 beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 },
		 data:JSON.stringify(jsonData),
		 contentType:"application/json"
	 }).done(function(stateID){
		 selectedNewlyAdded(stateID,stateName);
		 showToastMessage("The new state have been added");
	 }).fail(function(){
		 showToastMessage("Error Could Not connect to server || Please Choose One Country");
	 });
 };
 
 function selectedNewlyAdded(stateID,stateName){
	 $("<option>").val(stateID).text(stateName).appendTo(dropDownState);
	 
	 $("#dropDownStates option[value='" + stateID + "']").prop("selected",true);
	 
	 fieldStateName.val("").focus();
 };
 
 
 function updateState(){
	 if(!validityFormState()){
		 return;
	 }
	 url = contextPath + "states/save";
	 stateID = dropDownState.val();
	 stateName = fieldStateName.val();
	 
	 selectedCountry = $("#dropDownCountriesForStates option:selected");
	 countryID = selectedCountry.val();
	 countryName = selectedCountry.text();
	 
	 jsonData = {id: stateID, name: stateName, country: {id:countryID,name:countryName}};
	 
	 $.ajax({
		 type:"POST",
		 url: url,
		 beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 },
		 data: JSON.stringify(jsonData),
		 contentType:"application/json"
	 }).done(function(){
		 $("#dropDownStates option:selected").text(stateName);
		  showToastMessage("The state have been updated");
	 }).fail(function(){
		 showToastMessage("Error Could Not connect to server || Please Choose One Country");
	 });
	 
 };
 
 function deleteState(){
	 stateID = dropDownState.val();
	 url = contextPath + "states/delete/" + stateID;
	 
	 $.ajax({
		 type:"DELETE",
		 url:url,
		  beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 }
	 }).done(function(){
		 $("#dropDownStates option[value='" +stateID + "']").remove();
		 changeFormStateToNew();
		 showToastMessage("The state have been deleted");
	 }).fail(function(){
		 showToastMessage("ERROR: Could not connect to server or server encountered an error");
	 });
 }
 function validityFormState(){
	 formState = document.getElementById("formState");
	 if(!formState.checkValidity()){
		 formState.reportValidity();
		 return false;
	 }
	 return true;
 }