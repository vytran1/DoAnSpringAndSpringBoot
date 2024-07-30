//Sales Report Common
var millisecond_a_day = 24 * 60 * 60 * 1000;
function setUpButtonEventHandler(reportType,callbackFunction){
	//divCustomDateRange = $("#divCustomDateRange" + reportType );
	startDateField = document.getElementById("startDate"+ reportType);
	endDateField = document.getElementById("endDate" + reportType);
	$(".button_sales_by" + reportType).on("click",function(e){
		$(".button_sales_by" + reportType).each(function(e){
			$(this).removeClass("btn-primary").addClass("btn-light");
		})
		$(this).removeClass("btn-light").addClass("btn-primary");
		period = $(this).attr("period");
		if(period){
		    callbackFunction(period);
		   $("#divCustomDateRange" + reportType ).addClass("d-none");
		}
		else{
		   $("#divCustomDateRange" + reportType ).removeClass("d-none");
		}
	})
	
	initCustomDateRange(reportType);
	
	$("#buttonViewReportByDateRange" + reportType).on("click",function(e){
		validateDateRange(reportType,callbackFunction);
	})
}

function validateDateRange(reportType,callbackFunction){
	startDateField = document.getElementById("startDate" + reportType);
	days = calculateDays(reportType);
	//Clear validation previous
	startDateField.setCustomValidity("");
	//console.log(days);
	if(days >= 7 && days <= 30){
		//console.log("valid date range");
		callbackFunction("custom");
	}else{
		startDateField.setCustomValidity("Date must be in range of 7 .. 30 days");
		startDateField.reportValidity();
	}
};
function calculateDays(reportType){
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
	
	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;
	
	differentInMillionSecond = endDate - startDate;
	return differentInMillionSecond / millisecond_a_day;
}
function initCustomDateRange(reportType){
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
	toDate = new Date();
	endDateField.valueAsDate = toDate;
	
	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	
	startDateField.valueAsDate = fromDate;
	 
};

function formatCureency(amount){
	formatterAmount = $.number(amount,decimalDigit,decimalPointType,thousandPointType);
	return prefixCurrencySymbol + formatterAmount + suffixCurrencySymbol;
}
function getChartTitle(period){
	if(period == "last_7_days"){
		return "Sales in last 7 days";
	}else if(period == "last_28_days"){
		return "Sales in last 28 days";
	}else if(period == "last_6_months"){
		return "Sales in last 6 months";
	}else if(period == "last_year"){
		return "Sales in last year";
	}else if(period == "custom"){
		return "Custom Date Range";
	}
	return "";
}

function getDenominator(period,reportType){
	if(period == "last_7_days"){
		return 7;
	}else if(period == "last_28_days"){
		return 28;
	}else if(period == "last_6_months"){
		return 6;
	}else if(period == "last_year"){
		return 12;
	}else if(period == "custom"){
		return calculateDays(reportType);
	}
	return 7;
}

function setSalesAmount(period,reportType,labelItems){
	$("#textTotalGrossSales" + reportType).text(formatCureency(totalGrossSales));
	$("#textTotalNetSales"  + reportType).text(formatCureency(totalNetSales));
	denominator = getDenominator(period,reportType);
	$("#textAvgGrossSales" + reportType).text(formatCureency(totalGrossSales / denominator));
	$("#textAvgNetSales"  + reportType).text(formatCureency(totalNetSales / denominator));
	$("#labelToTalItems"  + reportType).text(labelItems)
	$("#textTotalOrders"  + reportType).text(totalItems);
}

function formatChartData(data,columnIndex1,columnIndex2){
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol: decimalPointType,
		groupingSymbol: thousandPointType,
		fractionDigits: decimalDigit
	});
	
	formatter.format(data,columnIndex1);
	formatter.format(data,columnIndex2);
}