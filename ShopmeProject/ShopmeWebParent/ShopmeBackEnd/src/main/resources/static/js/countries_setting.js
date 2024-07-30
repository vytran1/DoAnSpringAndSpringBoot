var buttonLoad;
var dropDownCountries;
var buttonAddCountries;
var buttonDeleteCountries;
var buttonUpdateCountries;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;
$(document).ready(function(){
	 buttonLoad = $("#buttonLoadCountries");
	 dropDownCountries = $("#dropDownCountries");
	 buttonAddCountries = $("#buttonAddCountries");
	 buttonUpdateCountries = $("#buttonUpdateCountries");
	 buttonDeleteCountries = $("#buttonDeleteCountries");
	 labelCountryName = $("#labelCountryName");
	 fieldCountryName = $("#fieldCountryName");
	 fieldCountryCode = $("#fieldCountryCode");
	 
	 
	 buttonLoad.click(function(){
		loadCountries(); 
	 });
	 
	 dropDownCountries.on("change",function(){
		changeFormCountryToSelectedCountry(); 
	 });
	 
	 buttonAddCountries.on("click",function(){
		 if(buttonAddCountries.val() == "Add"){
			 addCountry();
		 }
		 else{			 
		     changeFormCountryToNew();
		 }
	 });
	 buttonUpdateCountries.on("click",function(){
		 updateCountry(); 
	 });
	 buttonDeleteCountries.on("click",function(){
		 deleteCountry();
	 })
});


function loadCountries(){
	url = contextPath +  "countries/list";
	$.get(url,function(responseJSON){
		dropDownCountries.empty();
		$.each(responseJSON,function(index,country){
			optionValue = country.id + "-" + country.code;
			//alert(optionValue);
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
		}); 
	}).done(function(){
		buttonLoad.val("Refresh Countries List")
		//alert("All countries have been loaded");
		showToastMessage("All countries have been loaded");
	}).fail(function(){
		showToastMessage("Error Could not connect to server or server encounter a error");
	});
}

function showToastMessage(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}
function changeFormCountryToSelectedCountry(){
	buttonAddCountries.prop("value","New");
	buttonUpdateCountries.prop("disabled",false);
	buttonDeleteCountries.prop("disabled",false);
	
	labelCountryName.text("Selected Country");
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	
	countryCode = dropDownCountries.val().split("-")[1];
	fieldCountryCode.val(countryCode);
	
}
function changeFormCountryToNew(){
	buttonAddCountries.val("Add");
	labelCountryName.text("Country Name:");
	
	buttonUpdateCountries.prop("disabled",true);
	buttonDeleteCountries.prop("disabled",true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("").focus();
}
function addCountry(){
	//formCountry = document.getElementById("formCountry");
	//if(!formCountry.checkValidity()){
		//formCountry.reportValidity();
		//return;
	//}
	if(!validityFormCountry()){
		return;
	}
	
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type:'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json',
		
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId,countryCode,countryName);
		showToastMessage("The new country has been add to database with ID " + countryId);
	}).fail(function(){
		showToastMessage("Error Could not connect to server or server encounter a error");
	});
}
function selectNewlyAddedCountry(countryId,countryCode,countryName){
	optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);
    $("#dropDownCountries option[value= '" + optionValue + "']").prop("selected",true);
    fieldCountryName.val("");
    fieldCountryCode.val("").focus(); 	
}
function  updateCountry(){
	
	if(!validityFormCountry()){
		return;
	}
	
	url = contextPath + "countries/save";
	
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryID = dropDownCountries.val().split("-")[0];
	
	jsonData = {id:countryID,name: countryName, code: countryCode};
	
	$.ajax({
		type:'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json',
		
	}).done(function(countryId){
		$("#dropDownCountries option:selected").text(countryName);
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		showToastMessage("The country has been updated to database with ID " + countryId);
		changeFormStateToNew();
	}).fail(function(){
		showToastMessage("Error Could not connect to server or server encounter a error");
	});
}; 
function deleteCountry(){
	optionValue = dropDownCountries.val();
	countryID = optionValue.split("-")[0];
	
	url = contextPath + "countries/delete/" + countryID;
	
	 $.ajax({
		 type:"DELETE",
		 url:url,
		  beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 }
	 }).done(function(){
		 $("#dropDownCountries option[value='" + countryID +"']").remove();
		 changeFormCountryToNew();
		 showToastMessage("The country have been deleted");
	 }).fail(function(){
		 showToastMessage("Error Could not connect to server or server encounter a error");
	 });
}
function validityFormCountry(){
    formCountry = document.getElementById("formCountry");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
	}
	return true;
}