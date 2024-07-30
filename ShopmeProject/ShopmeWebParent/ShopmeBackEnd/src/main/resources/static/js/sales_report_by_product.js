//Sales Report By Product
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
	setUpButtonEventHandler("_product",loadSalesReportByDateForProduct);
})


function loadSalesReportByDateForProduct(period){
	if(period == "custom"){
		startDate = $("#startDate_product").val();
		endDate = $("#endDate_product").val();
		requestURL = contextPath + "reports/product/" + startDate + "/" + endDate;
	}else{
	    requestURL = contextPath + "reports/product/" + period;
	}
	$.get(requestURL,function(responseJSON){
		prepareChartDataForSalesReportByDateForProduct(responseJSON);
		customizeChartForSalesReportByDateForProduct();
		formatChartData(data,2,3);
		drawChartForSalesReportByDateForProduct(period);
		setSalesAmount(period,"_product","Total Products")
		
	})
}

function prepareChartDataForSalesReportByDateForProduct(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string','Product');
	data.addColumn('number','Quantity');
	data.addColumn('number','Gross Sales');
	data.addColumn('number','Net Sales');
	
	 totalGrossSales = 0.0;
     totalNetSales = 0.0;
     totalItems = 0.0;
	
	
	$.each(responseJSON,function(index,reportItem){
		data.addRows([[reportItem.identifier,reportItem.productsCount,reportItem.grossSales,reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales   += parseFloat(reportItem.netSales);
		totalItems     += parseInt(reportItem.productsCount);
	})
}

function customizeChartForSalesReportByDateForProduct(){
	chartOption = {
		height:360,
		width:"80%",
		showRowNumber: true,
		page:"enable",
		sortColumn: 2,
		sortAscending:false
	};
	
}

function drawChartForSalesReportByDateForProduct(){
	var salesChart = new google.visualization.Table(document.getElementById("chart_sales_by_product"));
	salesChart.draw(data,chartOption);
	
}

