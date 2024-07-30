package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
    protected DateFormat dateFormat;
	
	
	public List<ReportItem> getReportsDataLast7Days(ReportType type){
		System.out.println("getReportsDataLast7Days");
		return getReportDataLastXDays(7,type);
	}
	
	public List<ReportItem> getReportsDataLast28Days(ReportType type){
		System.out.println("getReportsDataLast7Days");
		return getReportDataLastXDays(28,type);
	}
	
	private List<ReportItem> getReportDataLastXDays(int days,ReportType type){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		//15-5-2024 
		//9-5-2024;
		calendar.add(Calendar.DAY_OF_MONTH, -(days -1));
		//startTime
		Date startTime = calendar.getTime();
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByRangeInternal(startTime, endTime,type);
	}
	
	public List<ReportItem> getReportsDataLast6Months(ReportType type) {
		// TODO Auto-generated method stub
		return getReportDataLastXMonths(6,type);
	}
	
	protected List<ReportItem> getReportDataLastXMonths(int months,ReportType type){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -(months -1));
		Date startTime = calendar.getTime();
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		dateFormat = new SimpleDateFormat("yyyy-MM");
		return getReportDataByRangeInternal(startTime, endTime,type);
	}
	
	public List<ReportItem> getReportsDataLastYear(ReportType type) {
		// TODO Auto-generated method stub
		return getReportDataLastXMonths(12,type);
	}
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime,ReportType type){
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByRangeInternal(startTime, endTime,type);
	}
	protected abstract List<ReportItem> getReportDataByRangeInternal(Date startDate, Date endDate, ReportType type);
}
