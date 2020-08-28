package com.ats.tril.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ats.tril.model.ExportToExcel;

@Controller
public class ExportExcelController {

	/*
	 * <dependency> <groupId>org.apache.poi</groupId>
	 * <artifactId>poi-ooxml</artifactId> <version>3.13</version> </dependency>
	 */

	List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();
	List<ExportToExcel> exportToExcelList1 = new ArrayList<ExportToExcel>();
	List<ExportToExcel> exportToExcelList2 = new ArrayList<ExportToExcel>();
	List<ExportToExcel> exportToExcelList3 = new ArrayList<ExportToExcel>();
	List<ExportToExcel> exportToExcelList4 = new ArrayList<ExportToExcel>();

	@RequestMapping(value = "/exportToExcel", method = RequestMethod.GET)

	public void downloadSpreadsheet(HttpServletResponse response, HttpServletRequest request) throws Exception {
		XSSFWorkbook wb = null;
		HttpSession session = request.getSession();
		try {

			exportToExcelList = (List) session.getAttribute("exportExcelList");
			System.out.println("Excel List :" + exportToExcelList.toString());

			String excelName = (String) session.getAttribute("excelName");
			wb = createWorkbook();

			response.setContentType("application/vnd.ms-excel");
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + excelName + "-" + date + ".xlsx");
			wb.write(response.getOutputStream());

		} catch (IOException ioe) {
			throw new RuntimeException("Error writing spreadsheet to output stream");
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		session.removeAttribute("exportExcelList");
		System.out.println("Session List" + session.getAttribute("exportExcelList"));
	}

	private XSSFWorkbook createWorkbook() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");

		/*
		 * writeHeaders(wb, sheet); writeHeaders(wb, sheet); writeHeaders(wb, sheet);
		 */

		for (int rowIndex = 0; rowIndex < exportToExcelList.size(); rowIndex++) {
			XSSFRow row = sheet.createRow(rowIndex);
			for (int j = 0; j < exportToExcelList.get(rowIndex).getRowData().size(); j++) {

				XSSFCell cell = row.createCell(j);

				cell.setCellValue(exportToExcelList.get(rowIndex).getRowData().get(j));

			}
			if (rowIndex == 0)
				row.setRowStyle(createHeaderStyle(wb));
		}
		return wb;
	}

	@RequestMapping(value = "/exportToExcel1", method = RequestMethod.GET)

	public void exportToExcel1(HttpServletResponse response, HttpServletRequest request) throws Exception {
		XSSFWorkbook wb = null;
		HttpSession session = request.getSession();
		try {

			exportToExcelList1 = (List) session.getAttribute("exportExcelList1");
			System.out.println("Excel List :" + exportToExcelList1.toString());

			String excelName1 = (String) session.getAttribute("excelName");
			wb = createWorkbook1();

			response.setContentType("application/vnd.ms-excel");
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + excelName1 + "-" + date + ".xlsx");
			wb.write(response.getOutputStream());

		} catch (IOException ioe) {
			throw new RuntimeException("Error writing spreadsheet to output stream");
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		session.removeAttribute("exportExcelList1");
		System.out.println("Session List" + session.getAttribute("exportExcelList1"));
	}

	private XSSFWorkbook createWorkbook1() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");

		/*
		 * writeHeaders(wb, sheet); writeHeaders(wb, sheet); writeHeaders(wb, sheet);
		 */

		for (int rowIndex = 0; rowIndex < exportToExcelList1.size(); rowIndex++) {
			XSSFRow row = sheet.createRow(rowIndex);
			for (int j = 0; j < exportToExcelList1.get(rowIndex).getRowData().size(); j++) {

				XSSFCell cell = row.createCell(j);

				cell.setCellValue(exportToExcelList1.get(rowIndex).getRowData().get(j));

			}
			if (rowIndex == 0)
				row.setRowStyle(createHeaderStyle(wb));
		}
		return wb;
	}

	@RequestMapping(value = "/exportToExcel2", method = RequestMethod.GET)

	public void exportToExcel2(HttpServletResponse response, HttpServletRequest request) throws Exception {
		XSSFWorkbook wb = null;
		HttpSession session = request.getSession();
		try {

			exportToExcelList2 = (List) session.getAttribute("exportExcelList2");
			System.out.println("Excel List2 :" + exportToExcelList2.toString());

			String excelName2 = (String) session.getAttribute("excelName2");
			wb = createWorkbook2();

			response.setContentType("application/vnd.ms-excel");
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + excelName2 + "-" + date + ".xlsx");
			wb.write(response.getOutputStream());

		} catch (IOException ioe) {
			throw new RuntimeException("Error writing spreadsheet to output stream");
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		session.removeAttribute("exportExcelList2");
		System.out.println("Session List" + session.getAttribute("exportExcelList2"));
	}

	private XSSFWorkbook createWorkbook2() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");

		for (int rowIndex = 0; rowIndex < exportToExcelList2.size(); rowIndex++) {
			XSSFRow row = sheet.createRow(rowIndex);
			for (int j = 0; j < exportToExcelList2.get(rowIndex).getRowData().size(); j++) {

				XSSFCell cell = row.createCell(j);

				cell.setCellValue(exportToExcelList2.get(rowIndex).getRowData().get(j));

			}
			if (rowIndex == 0)
				row.setRowStyle(createHeaderStyle(wb));
		}
		return wb;
	}

	private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(53, 119, 192)));

		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setBold(true);
		// font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		return style;
	}

}
