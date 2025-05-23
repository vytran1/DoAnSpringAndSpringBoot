package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter {
   
	public void export(List<User> listUser,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","users_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Roles","Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
		csvWriter.writeHeader(csvHeader);
		for(User x : listUser) {
			csvWriter.write(x, fieldMapping);
		}
		csvWriter.close();
	}
}
