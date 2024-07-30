//Sales Report By Dates
var data;
var chartOption;
//var totalGrossSales;
//var totalNetSales;
//var totalOrders;
//var startDateField;
//var endDateField;


$(document).ready(function(){
	/*
	divCustomDateRange = $("#divCustomDateRange_date");
	startDateField = document.getElementById("startDate_date");
	endDateField = document.getElementById("endDate_date");
	$(".button_sales_by_date").on("click",function(e){
		$(".button_sales_by_date").each(function(e){
			$(this).removeClass("btn-primary").addClass("btn-light");
		})
		$(this).removeClass("btn-light").addClass("btn-primary");
		period = $(this).attr("period");
		if(period){
		   loadSalesReportByDate(period);
		    divCustomDateRange.addClass("d-none");
		}
		else{
		   divCustomDateRange.removeClass("d-none");
		}
	})
	
	initCustomDateRange();
	
	$("#buttonViewReportByDateRange_date").on("click",function(e){
		validateDateRange();
	})
	*/
	setUpButtonEventHandler("_category",loadSalesReportByDateForCategory);
})


function loadSalesReportByDateForCategory(period){
	if(period == "custom"){
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
	}else{
	    requestURL = contextPath + "reports/category/" + period;
	}
	$.get(requestURL,function(responseJSON){
		prepareChartDataForSalesReportByDateForCategory(responseJSON);
		customizeChartForSalesReportByDateForCategory();
		formatChartData(data,1,2);
		drawChartForSalesReportByDateForCategory(period);
		setSalesAmount(period,"_category","Total Products")
		
	})
}

function prepareChartDataForSalesReportByDateForCategory(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string','Category');
	data.addColumn('number','Gross Sales');
	data.addColumn('number','Net Sales');
	
	 totalGrossSales = 0.0;
     totalNetSales = 0.0;
     totalItems = 0.0;
	
	
	$.each(responseJSON,function(index,reportItem){
		data.addRows([[reportItem.identifier,reportItem.grossSales,reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales   += parseFloat(reportItem.netSales);
		totalItems     += parseInt(reportItem.productsCount);
	})
}

function customizeChartForSalesReportByDateForCategory(){
	chartOption = {
		height:360,
		legend: {
			position: "right"
		}
	};
	
}

function drawChartForSalesReportByDateForCategory(){
	var salesChart = new google.visualization.PieChart(document.getElementById("chart_sales_by_category"));
	salesChart.draw(data,chartOption);
	
}

