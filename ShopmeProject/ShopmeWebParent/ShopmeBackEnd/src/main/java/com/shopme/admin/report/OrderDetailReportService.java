package com.shopme.admin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderDetailRepository;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;

@Service
public class OrderDetailReportService extends AbstractReportService {
	
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Override
	protected List<ReportItem> getReportDataByRangeInternal(Date startDate, Date endDate, ReportType type) {
		// TODO Auto-generated method stub
		List<OrderDetail> listOrderDetail = null;
		if(type.equals(ReportType.CATEGORY)) {
			listOrderDetail = orderDetailRepository.findWithCategoryAndTimeBetween(startDate, endDate);
		}else if(type.equals(ReportType.PRODUCT)) {
			listOrderDetail = orderDetailRepository.findWithProductAndTimeBetween(startDate, endDate);
		}
		//printRawData(listOrderDetail);
		List<ReportItem> listReportItem = new ArrayList<>();
		for(OrderDetail detail : listOrderDetail) {
			String identifiers = "";
			if(type.equals(ReportType.CATEGORY)) {
				identifiers = detail.getProduct().getCategory().getName();
			}
			else if(type.equals(ReportType.PRODUCT)) {
				identifiers = detail.getProduct().getShortName();
			}
			ReportItem item = new ReportItem(identifiers);
			float grossSales = detail.getSubtotal() + detail.getShippingCost();
			float netSales = detail.getSubtotal() - detail.getProductCost();
			int itemIndex = listReportItem.indexOf(item);
			if(itemIndex >=0) {
				item = listReportItem.get(itemIndex);
				item.addGrossSale(grossSales);
				item.addNetSale(netSales);
				item.increaseProductCount(detail.getQuantity());
			}else {
				listReportItem.add(new ReportItem(identifiers,grossSales,netSales,detail.getQuantity()));
			}
		}
		//printListReportItem(listReportItem);
		return listReportItem;
	}

	private void printListReportItem(List<ReportItem> listReportItem) {
		// TODO Auto-generated method stub
		for(ReportItem item : listReportItem) {
			System.out.printf("%-20s || %10.2f || %10.2f || %d \n",
					item.getIdentifier(),
					item.getGrossSales(),
					item.getNetSales(),
					item.getProductsCount()
					);
		}
	}

	private void printRawData(List<OrderDetail> listOrderDetail) {
		// TODO Auto-generated method stub
		for(OrderDetail detail : listOrderDetail) {
			System.out.printf("%d || %-20s || %10.2f || %10.2f || %10.2f \n\n",detail.getQuantity()
					,detail.getProduct().getShortName(),
					detail.getSubtotal(), detail.getProductCost(),detail.getShippingCost()
					);
		}
	}
    
}
