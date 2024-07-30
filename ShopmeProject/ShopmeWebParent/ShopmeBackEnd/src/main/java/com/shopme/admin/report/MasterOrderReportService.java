package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.order.Order;

@Service
public class MasterOrderReportService extends AbstractReportService {
    
	@Autowired
	private OrderRepository orderRepository;
	
	
	
	
	
	protected List<ReportItem> getReportDataByRangeInternal(Date startTime, Date endTime,ReportType type){
		List<Order> listOrder =  orderRepository.findByOrderTimeBetween(startTime, endTime);
		printRawData(listOrder);
		List<ReportItem> reportsItem  = createReportData(startTime,endTime,type);
		calculateSalesForReport(listOrder, reportsItem);
		printReportDate(reportsItem);
		return reportsItem;
	}
	
	private void calculateSalesForReport(List<Order> listOrder,List<ReportItem> reportsItem) {
		for(Order order : listOrder) {
			String orderDateString = dateFormat.format(order.getOrderTime());
			ReportItem reportItem = new ReportItem(orderDateString);
			int indexItem = reportsItem.indexOf(reportItem);
			if(indexItem >=0 ) {
				reportItem = reportsItem.get(indexItem);
				reportItem.addGrossSale(order.getTotal());
				reportItem.addNetSale(order.getSubtotal() - order.getProductCost());
				reportItem.increaseOrderCount();
			}
			
		}
	}
	
    private void printReportDate(List<ReportItem> reportsItem) {
		// TODO Auto-generated method stub
		reportsItem.forEach(item -> {
			System.out.printf("%s || %10.2f || %10.2f %d \n",item.getIdentifier(),item.getGrossSales(),item.getNetSales(),item.getOrdersCount());
		});
	}

	//Create the first collum in report that is the collum date
	private List<ReportItem> createReportData(Date startTime, Date endTime,ReportType type) {
		// TODO Auto-generated method stub
		List<ReportItem> listReportItems = new ArrayList<>();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		
		Date currentDate = startDate.getTime();
		String dateString = dateFormat.format(currentDate);
		
		listReportItems.add(new ReportItem(dateString));
		do {
			if(type.equals(ReportType.DAY)) {
				startDate.add(Calendar.DAY_OF_MONTH, 1);
			}else if(type.equals(ReportType.MONTH)) {
				startDate.add(Calendar.MONTH, 1);
			}
			currentDate = startDate.getTime();
			dateString = dateFormat.format(currentDate);
			listReportItems.add(new ReportItem(dateString));
		}while(startDate.before(endDate));
		return listReportItems;
	}

	private void printRawData(List<Order> listOrder) {
		// TODO Auto-generated method stub
		for(Order order : listOrder) {
			System.out.printf("%-3d || %s || %10.2f || %10.2f \n"
					,order.getId(),order.getOrderTime(),order.getTotal(),order.getProductCost());
		}
	}

	

	
}
