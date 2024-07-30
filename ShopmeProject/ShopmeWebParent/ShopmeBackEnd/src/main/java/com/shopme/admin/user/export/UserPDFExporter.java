package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPDFExporter extends AbstractExporter {

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		super.setResponseHeader(response,"application/pdf", ".pdf","users_");
		Document document = new Document(PageSize.A4);
		//write data to document
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		//write header for file 
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.ORANGE);
		Paragraph paragraph = new Paragraph("List Of Useds",font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		//set collumn
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,3.0f,1.7f});
		writeTableHeader(table);
		writeTableData(table,listUser);
		document.add(table);
		document.close();
	}

	private void writeTableData(PdfPTable table, List<User> listUser) {
		// TODO Auto-generated method stub
		for(User x : listUser) {
			table.addCell(String.valueOf(x.getId()));
			table.addCell((x.getEmail()));
			table.addCell((x.getFirstName()));
			table.addCell((x.getLastName()));
			table.addCell((x.getRoles().toString()));
			table.addCell(String.valueOf(x.getEnabled()));
		}
	}

	private void writeTableHeader(PdfPTable table) {
		// TODO Auto-generated method stub
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(18);
		font.setColor(Color.BLACK);
		cell.setPhrase(new Phrase("User Id",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("E-mail",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("First Name",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Roles",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
	}
   
}
