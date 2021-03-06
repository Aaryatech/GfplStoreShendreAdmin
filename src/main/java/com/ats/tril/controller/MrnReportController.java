package com.ats.tril.controller;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.tril.common.Constants;
import com.ats.tril.common.DateConvertor;
import com.ats.tril.model.Category;
import com.ats.tril.model.ExportToExcel;
import com.ats.tril.model.GetPoHeaderList;
import com.ats.tril.model.MrnInfo;
import com.ats.tril.model.RateVerificationList;
import com.ats.tril.model.Type;
import com.ats.tril.model.Vendor;
import com.ats.tril.model.VendorItemPurchaseReport;
import com.ats.tril.model.doc.DocumentBean;
import com.ats.tril.model.indent.IndentReport;
import com.ats.tril.model.mrn.GetMrnHeader;
import com.ats.tril.model.mrn.MrnReport;
import com.ats.tril.util.ItextPageEvent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class MrnReportController {

	@RequestMapping(value = "/showMrnReport", method = RequestMethod.GET)
	public ModelAndView showMrnRpoert(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		try {
			RestTemplate rest = new RestTemplate();
			model = new ModelAndView("mrn/report/mrnReport");
			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			List<Vendor> vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			model.addObject("vendorList", vendorList);
			
			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/showPendingMrnReport", method = RequestMethod.GET)
	public ModelAndView showPendingMrnReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		try {
			RestTemplate rest = new RestTemplate();
			model = new ModelAndView("mrn/report/pendingMrn");
			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			List<Vendor> vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			model.addObject("vendorList", vendorList);

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			// SimpleDateFormat display = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("date", sf.format(date));
			DocumentBean resList = rest.postForObject(Constants.url + "getDocumentDataForMrn", map, DocumentBean.class);
			System.out.println("resList" + resList);

			model.addObject("newDate", DateConvertor.convertToDMY(resList.getFromDate()));
			
			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	List<MrnReport> mrnReportList;

	@RequestMapping(value = "/getMrnReportList", method = RequestMethod.GET)
	@ResponseBody
	public List<MrnReport> getMrnReportList(HttpServletRequest request, HttpServletResponse response) {

		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			mrnReportList = new ArrayList<MrnReport>();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			// String[] grnTypeList = request.getParameterValues("grn_type_list");

			String selectedVendor = request.getParameter("vendor_list");

			String selectedGrnType = request.getParameter("grn_type_list");

			String selectedStatus = request.getParameter("status_list");

			selectedGrnType = selectedGrnType.substring(1, selectedGrnType.length() - 1);
			selectedGrnType = selectedGrnType.replaceAll("\"", "");

			List<String> grnTypeList = new ArrayList<String>();
			grnTypeList = Arrays.asList(selectedGrnType);

			if (grnTypeList.contains("-1")) {

				map.add("grnTypeList", "3" + "," + "1" + "," + "2" + "," + "4");
			} else {

				map.add("grnTypeList", selectedGrnType);
			}

			selectedVendor = selectedVendor.substring(1, selectedVendor.length() - 1);
			selectedVendor = selectedVendor.replaceAll("\"", "");
			List<String> vendorList = new ArrayList<String>();
			vendorList = Arrays.asList(selectedVendor);
			if (vendorList.contains("-1")) {

				map.add("vendorIdList", "-1");
			} else {

				map.add("vendorIdList", selectedVendor);
			}

			selectedStatus = selectedStatus.substring(1, selectedStatus.length() - 1);
			selectedStatus = selectedStatus.replaceAll("\"", "");
			List<String> statusList = new ArrayList<String>();
			statusList = Arrays.asList(selectedStatus);

			if (statusList.contains("-1")) {

				map.add("statusList", "0,5,4" + "," + "1" + "," + "2" + "," + "3");
			} else {

				map.add("statusList", selectedStatus);
			}

			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			System.out.println(map);
			MrnReport[] mrnReport = restTemplate.postForObject(Constants.url + "/getMrnHeadReport", map,
					MrnReport[].class);

			mrnReportList = new ArrayList<MrnReport>(Arrays.asList(mrnReport));

			System.err.println("Mrn  Report  " + mrnReportList.toString());

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("MRN No");
			rowData.add("Vendor Name");
			rowData.add("MRN Date");
			rowData.add("Item Code");

			rowData.add("Item Desc");
			rowData.add("Chalan Qty");
			rowData.add("Rec Qty");
			rowData.add("Landing Value");
			rowData.add("Basic Value");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;

			for (int i = 0; i < mrnReportList.size(); i++) {
				// for (MrnReport report : mrnReportList) {
				MrnReport report = mrnReportList.get(i);
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + report.getMrnNo());
				rowData.add("" + report.getVendorName());
				rowData.add("" + report.getMrnDate());

				rowData.add("" + (report.getItemCode()));
				rowData.add("" + report.getItemDesc());
				rowData.add("" + report.getPoQty());
				rowData.add("" + report.getMrnQty());
				float basicValue = report.getItemRate() * report.getMrnQty();

				float landingValue = report.getLandingRate() * report.getMrnQty();

				rowData.add("" + landingValue);
				rowData.add("" + basicValue);

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "mrnReport");

		} catch (Exception e) {

			System.err.println("Exception in Mrn Header Report List  " + e.getMessage());

			e.printStackTrace();

		}

		return mrnReportList;

	}

	@RequestMapping(value = "/getMrnReportPdf", method = RequestMethod.GET)
	public void getMrnReportPdf(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside show Prod BOM Pdf ");
		Document doc = new Document();

		Document document = new Document(PageSize.A4, 20, 40, 100, 30);

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("time in Gen Bill PDF ==" + dateFormat.format(cal.getTime()));
		String timeStamp = dateFormat.format(cal.getTime());
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);

		try {

			String header = "TRAMBAK BUTYL Tube";
			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
			String reportDate = DF.format(new Date());
			String title = "Report-For MRN";
			writer = PdfWriter.getInstance(document, out);

			ItextPageEvent event = new ItextPageEvent(header, title, reportDate);

			writer.setPageEvent(event);

		} catch (DocumentException e) {

			e.printStackTrace();
		}
		PdfPTable table = new PdfPTable(10);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 0.7f, 1.4f, 1.4f, 1.4f, 1.4f, 1.6f, 1.2f, 1.2f, 1.4f, 1.4f });
			Font headFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.WHITE);
			Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.UNDERLINE, BaseColor.BLUE);
			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Sr.No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Desc", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Chalan Qty", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Rec Qty", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Landing Value", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Basic Value", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (MrnReport report : mrnReportList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(report.getMrnNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(4);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(report.getVendorName()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(report.getMrnDate()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(report.getItemCode()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);
				//

				cell = new PdfPCell(new Phrase(String.valueOf(report.getItemDesc()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(report.getPoQty()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(report.getMrnQty()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);

				float basicValue = report.getItemRate() * report.getMrnQty();

				float landingValue = report.getLandingRate() * report.getMrnQty();

				cell = new PdfPCell(new Phrase(String.valueOf(landingValue), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(basicValue), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(4);
				table.addCell(cell);

				// FooterTable footerEvent = new FooterTable(table);
				// writer.setPageEvent(footerEvent);
			}

			document.open();

			// document.add(new Paragraph(" "));
			document.add(table);
			// int totalPages=writer.getPageNumber();
			/*
			 * com.ats.adminpanel.model.itextpdf.Header event; // = new
			 * com.ats.adminpanel.model.itextpdf.Header(); for(int i=1;i<totalPages;i++) {
			 * event = new com.ats.adminpanel.model.itextpdf.Header(); event.setHeader(new
			 * Phrase(String.format("page %s", i)));
			 * 
			 * writer.setPageEvent(event); } FooterTable footerEvent = new
			 * FooterTable(table);
			 */

			// document.add(new
			// Paragraph(""+document.setPageCount(document.getPageNumber()));

			// sSystem.out.println("Page no "+totalPages);

			// document.addHeader("Page" ,String.valueOf(totalPages));
			// writer.setPageEvent((PdfPageEvent) new Phrase());

			document.close();

			// Atul Sir code to open a Pdf File
			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				// response.setHeader("Content-Disposition", String.format("attachment;
				// filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File for mrn report");
					e.printStackTrace();
				}
			}

			/*
			 * Desktop d=Desktop.getDesktop();
			 * 
			 * if(file.exists()) { try { d.open(file); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } }
			 */

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: Prod From Orders" + ex.getMessage());

			ex.printStackTrace();

		}

		ModelAndView model = new ModelAndView("production/pdf/productionPdf");
		// model.addObject("prodFromOrderReport",updateStockDetailList);

	}

	public void getIndentListReport(HttpServletRequest request, HttpServletResponse response) {

		try {

			// export to excel

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**********************************************************************************/
	//Mahendra
	//02-01-2020
	
	List<MrnInfo> mrnList = null;
	@RequestMapping(value = "/mrnReportList", method = RequestMethod.GET)
	public ModelAndView indetReportList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/mrnDetailReport");
		RestTemplate restTemplate = new RestTemplate();
		try {

			MrnInfo[] mrnRes = restTemplate.getForObject(Constants.url + "/getMrnDetails", MrnInfo[].class);
			mrnList = new ArrayList<MrnInfo>(Arrays.asList(mrnRes));
			System.out.println("Lisrt------------"+mrnList);
			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("MRN Date");
			rowData.add("MRN No.");
			rowData.add("Venor Name");
			rowData.add("Bill No.");
			rowData.add("Item Name");
			rowData.add("Qty.");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 0;
			for (int i = 0; i < mrnList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt+1;
				rowData.add("" + (cnt));
				rowData.add("" + mrnList.get(i).getMrnDate());
				rowData.add("" + mrnList.get(i).getMrnNo());
				rowData.add("" + mrnList.get(i).getVendorName());
				rowData.add("" + mrnList.get(i).getBillNo());				
				rowData.add("" + mrnList.get(i).getItemDesc());
				rowData.add("" + mrnList.get(i).getApproveQty());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "MRNDetailReport");

			
			model.addObject("mrnList", mrnList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}
	
	
	@RequestMapping(value = "/showMrnDetailPdf", method = RequestMethod.GET)
	public void showIndentPdf(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf showMrnDetailPdf");

		// moneyOutList = prodPlanDetailList;
		Document document = new Document(PageSize.A4);
		// ByteArrayOutputStream out = new ByteArrayOutputStream();

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("time in Gen Bill PDF ==" + dateFormat.format(cal.getTime()));
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);
		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(7);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f,  3.2f, 3.2f, 3.2f, 3.2f, 3.2f, 3.2f});
			Font headFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
			headFont1.setColor(BaseColor.WHITE);
			Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell = new PdfPCell();
			hcell.setBackgroundColor(BaseColor.PINK);

			hcell.setPadding(3);
			hcell = new PdfPCell(new Phrase("Sr.No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Bill No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Qty.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (MrnInfo mrn : mrnList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(mrn.getMrnDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + mrn.getMrnNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + mrn.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + mrn.getBillNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + mrn.getItemDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				
				cell = new PdfPCell(new Phrase("" + mrn.getApproveQty(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("MRN List Report\n", f);
			company.setAlignment(Element.ALIGN_CENTER);
			document.add(company);
			document.add(new Paragraph(" "));

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");			
			document.add(new Paragraph("\n"));
			document.add(table);

			int totalPages = writer.getPageNumber();

			System.out.println("Page no " + totalPages);

			document.close();
			// Atul Sir code to open a Pdf File
			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File");
					e.printStackTrace();
				}
			}

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: BOm Prod  View Prod" + ex.getMessage());

			ex.printStackTrace();

		}

	}
	
	@RequestMapping(value = "/showVendorItemPurchaseReport", method = RequestMethod.GET)
	public ModelAndView showVendorItemPurchaseReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		try {
			RestTemplate rest = new RestTemplate();
			model = new ModelAndView("mrn/report/vendorItemPurchase");
			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			List<Vendor> vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			model.addObject("vendorList", vendorList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}
	
	
	@RequestMapping(value = "/getVendorItems", method = RequestMethod.GET)
	@ResponseBody
	public List<RateVerificationList> getVendorItems(HttpServletRequest request, HttpServletResponse response, @RequestParam int vendrId ) {
		List<RateVerificationList> itemlist = new ArrayList<RateVerificationList>();
		try {
			RestTemplate rest = new RestTemplate();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			
			//int vendrId = Integer.parseInt(request.getParameter("vendrId"));
			System.out.println("Vender Id Found--------"+ vendrId);
			
			map.add("vendrId", vendrId);
			
			RateVerificationList[] itemArr= rest.postForObject(Constants.url + "/getAllVendorItem", map, RateVerificationList[].class);
			itemlist = new ArrayList<RateVerificationList>(Arrays.asList(itemArr));
			
			System.out.println("List--------"+itemlist);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemlist;
		
	}
	List<VendorItemPurchaseReport> viplist = null;
	@RequestMapping(value = "/getVendorItemPurchaseList", method = RequestMethod.GET)
	@ResponseBody
	public List<VendorItemPurchaseReport> getVendorItems(HttpServletRequest request, HttpServletResponse response) {
		viplist = new ArrayList<VendorItemPurchaseReport>();
		try {
			RestTemplate rest = new RestTemplate();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			int vendrId = Integer.parseInt(request.getParameter("vendrId"));
			String[] rmItemList = request.getParameterValues("rmItemList[]");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < rmItemList.length; i++) {
				sb = sb.append(rmItemList[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);

			System.out.println(fromDate+ " "+toDate+" "+vendrId+" "+ items);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));
			map.add("itemsList", items);
			map.add("vendrId", vendrId);
			
			VendorItemPurchaseReport[] itemArr= rest.postForObject(Constants.url + "/getVendorItemPuchaseList", map, VendorItemPurchaseReport[].class);
			viplist = new ArrayList<VendorItemPurchaseReport>(Arrays.asList(itemArr));
			
			System.out.println("List-----------"+viplist);
			
			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("MRN No.");
			rowData.add("Vendor Name");
			rowData.add("MRN Date");
			rowData.add("Bill No");
			rowData.add("Item Name");
			rowData.add("Qty");
			rowData.add("Taxable Amt");
			rowData.add("Total Tax Amt");
			rowData.add("Grand Total Amt");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < viplist.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + viplist.get(i).getMrnNo());
				rowData.add("" + viplist.get(i).getVendorName());
				rowData.add("" + viplist.get(i).getMrnDate());				
				rowData.add("" + viplist.get(i).getBillNo());
				rowData.add("" + viplist.get(i).getItemDesc());
				rowData.add("" + viplist.get(i).getApproveQty());
				rowData.add("" + viplist.get(i).getBasicValue());
				rowData.add("" + viplist.get(i).getTaxValue());
				rowData.add("" + viplist.get(i).getLandingCost());

			
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "VendorItemPurchaseReport");

		}catch (Exception e) {
			e.printStackTrace();
		}
		return viplist;
	}
	
	@RequestMapping(value = "/getVendorItemPurchaseList/{fromDate}/{toDate}", method = RequestMethod.GET)
	public void showPOPdf(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf showDatewiseConsumptionPdf");

		// moneyOutList = prodPlanDetailList;
		Document document = new Document(PageSize.A4);
		// ByteArrayOutputStream out = new ByteArrayOutputStream();

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("time in Gen Bill PDF ==" + dateFormat.format(cal.getTime()));
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);
		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(10);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f, 4.0f, 5.2f, 4.2f, 3.2f, 5.2f, 3.2f, 3.2f, 3.2f, 3.2f});
			Font headFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
			headFont1.setColor(BaseColor.WHITE);
			Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell = new PdfPCell();
			hcell.setBackgroundColor(BaseColor.PINK);

			hcell.setPadding(3);
			hcell = new PdfPCell(new Phrase("Sr.No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("MRN Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Bill No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Qty.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Taxable Amt", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Total Tax Amt", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Grand Total", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (VendorItemPurchaseReport list : viplist) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(list.getMrnNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + list.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + list.getMrnDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + list.getBillNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + list.getItemDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + list.getApproveQty(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("" + list.getBasicValue(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("" + list.getTaxValue(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("" + list.getLandingCost(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				

				}
			document.open();
			/*Paragraph name = new Paragraph("Trimbak Rubber\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);*/
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Vendor Item Purchase Report\n", f);
			company.setAlignment(Element.ALIGN_CENTER);
			document.add(company);
			document.add(new Paragraph(" "));

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
			String reportDate = DF.format(new Date());
			Paragraph p1 = new Paragraph("From Date:" + fromDate + "  To Date:" + toDate, headFont);
			p1.setAlignment(Element.ALIGN_CENTER);
			document.add(p1);
			document.add(new Paragraph("\n"));
			document.add(table);

			int totalPages = writer.getPageNumber();

			System.out.println("Page no " + totalPages);

			document.close();
			// Atul Sir code to open a Pdf File
			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File");
					e.printStackTrace();
				}
			}

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: BOm Prod  View Prod" + ex.getMessage());

			ex.printStackTrace();

		}

	}

}
