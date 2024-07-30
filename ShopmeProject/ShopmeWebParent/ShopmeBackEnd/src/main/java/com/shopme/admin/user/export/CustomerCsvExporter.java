package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class CustomerCsvExporter extends AbstractExporter {
	public void export(List<Customer> listUser,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","users_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastName","enabled"};
		csvWriter.writeHeader(csvHeader);
		for(Customer x : listUser) {
			csvWriter.write(x, fieldMapping);
		}
		csvWriter.close();
	}
}
