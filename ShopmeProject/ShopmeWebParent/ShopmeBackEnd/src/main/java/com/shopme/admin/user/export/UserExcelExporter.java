package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workBook ;
	private XSSFSheet sheet ;
	
	public UserExcelExporter() {
		 workBook = new XSSFWorkbook();
	}
	private void writeHeaderLine() {
	   //File excel rỗng
	   sheet = workBook.createSheet("Users");
	   //Row rỗng 
	   XSSFRow row =  sheet.createRow(0);
	   //Tạo kiểu chữ 
	   XSSFCellStyle cellStyle = workBook.createCellStyle();
	   XSSFFont font = workBook.createFont();
	   font.setBold(true);
	   font.setFontHeight(16);
	   cellStyle.setFont(font);
	   //Row 
	   createCell(row, 0, "User Id", cellStyle);
	   createCell(row, 1, "E-mail", cellStyle);
	   createCell(row, 2, "First Name", cellStyle);
	   createCell(row, 3, "Last Name", cellStyle);
	   createCell(row, 4, "Roles", cellStyle);
	   createCell(row, 5, "Enabled", cellStyle);
	}
	
	private void createCell(XSSFRow row, int columnIndex,Object value, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(columnIndex);
		if(value instanceof Integer) {
			cell.setCellValue((Integer)value);
		}else if(value instanceof Boolean) {
			cell.setCellValue((Boolean)value);
		}else {
			cell.setCellValue((String)value);
		}
		
		cell.setCellStyle(cellStyle);
	}
	public void export(List<User> listUser,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx","users_");
		//name of sheet
		writeHeaderLine();
		writeDataLines(listUser);
		
	
		
		//write to sheet
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();
	}
	private void writeDataLines(List<User> listUser) {
		// TODO Auto-generated method stub
		 int rowIndex = 1;
		 //Set style 
		 XSSFCellStyle cellStyle = workBook.createCellStyle();
		 XSSFFont font = workBook.createFont();
		 font.setFontHeight(14);
		 cellStyle.setFont(font);
		for(User x : listUser) {
			//X at index 0 có dữ liệu Nguyễn Văn A, A@gmail.com, admin
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, columnIndex++, x.getId(), cellStyle);
			//columnIndex = 1
			createCell(row, columnIndex++, x.getEmail(), cellStyle);
			//columnIndex = 2;
			createCell(row, columnIndex++, x.getFirstName(), cellStyle);
			createCell(row, columnIndex++, x.getLastName(), cellStyle);
			createCell(row, columnIndex++, x.getRoles().toString(), cellStyle);
			createCell(row, columnIndex++, x.getEnabled(), cellStyle);

		}
	}
}
