package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCSVExporter extends AbstractExporter {
     
	
	public void export(List<Category> categories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Category ID","Category Name"};
		String[] fieldMapping = {"id","name"};
		csvWriter.writeHeader(csvHeader);
		for(Category x : categories) {
			x.setName(x.getName().replace("--", " "));
			csvWriter.write(x, fieldMapping);
		}
		csvWriter.close();
	}
	
}
