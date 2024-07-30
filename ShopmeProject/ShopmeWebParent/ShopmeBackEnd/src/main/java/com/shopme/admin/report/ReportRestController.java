package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {
    
	@Autowired
	private MasterOrderReportService masterOrderReportService;
	
	@Autowired
	private OrderDetailReportService orderDetailReportService;
	
	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period){
		System.out.println("Report period: " + period);
		switch(period) {
		   case "last_7_days":
			   return masterOrderReportService.getReportsDataLast7Days(ReportType.DAY);
		   case "last_28_days":
			   return masterOrderReportService.getReportsDataLast28Days(ReportType.DAY);
		   case "last_6_months":
			   return masterOrderReportService.getReportsDataLast6Months(ReportType.MONTH);
		   case "last_year":
			   return masterOrderReportService.getReportsDataLastYear(ReportType.MONTH);
		   default:
			  return masterOrderReportService.getReportsDataLast7Days(ReportType.DAY);
		}
		
	}
	
	@GetMapping("/reports/sales_by_date/{startDate}/{endDate}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) throws ParseException{
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date startTime = dateFormatter.parse(startDate);
		Date endTime = dateFormatter.parse(endDate);
		return masterOrderReportService.getReportDataByDateRange(startTime, endTime,ReportType.DAY);
	}
	
	@GetMapping("/reports/{groupBy}/{startDate}/{endDate}")
	public List<ReportItem> getReportDataByCategoryOrProductRangeDate(@PathVariable("groupBy") String groupby,
			@PathVariable("startDate") String startDate, 
			@PathVariable("endDate") String endDate) throws ParseException{
		ReportType type = ReportType.valueOf(groupby.toUpperCase());
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date startTime = dateFormatter.parse(startDate);
		Date endTime = dateFormatter.parse(endDate);
		return orderDetailReportService.getReportDataByDateRange(startTime, endTime,type);
	}
	
	 @GetMapping("/reports/{groupBy}/{period}")
	 public List<ReportItem> getDataByCategoryOrProduct(@PathVariable("groupBy") String groupby, @PathVariable("period") String period){
		 ReportType reportType = ReportType.valueOf(groupby.toUpperCase());
		 switch(period) {
		   case "last_7_days":
			   return orderDetailReportService.getReportsDataLast7Days(reportType);
		   case "last_28_days":
			   return orderDetailReportService.getReportsDataLast28Days(reportType);
		   case "last_6_months":
			   return orderDetailReportService.getReportsDataLast6Months(reportType);
		   case "last_year":
			   return orderDetailReportService.getReportsDataLastYear(reportType);
		   default:
			  return orderDetailReportService.getReportsDataLast7Days(reportType);
		}
	 }
} 
