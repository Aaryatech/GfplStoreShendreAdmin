package com.ats.tril.controller;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.tril.common.Constants;
import com.ats.tril.common.DateConvertor;
import com.ats.tril.model.Category;
import com.ats.tril.model.Dept;
import com.ats.tril.model.ERPHeader;
import com.ats.tril.model.ExportToExcel;
import com.ats.tril.model.GatepassReport;
import com.ats.tril.model.GetItem;
import com.ats.tril.model.GetPoHeaderList;
import com.ats.tril.model.GetSubDept;
import com.ats.tril.model.GetpassDetail;
import com.ats.tril.model.GetpassReturnVendor;
import com.ats.tril.model.MrnExcelPuch;
import com.ats.tril.model.Type;
import com.ats.tril.model.Vendor;
import com.ats.tril.model.doc.DocumentBean;
import com.ats.tril.model.indent.IndentReport;
import com.ats.tril.model.item.ItemList;
import com.ats.tril.model.rejection.RejectionReport;
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
@Scope("session")
public class ReportController {

	RestTemplate rest = new RestTemplate();
	List<GetItem> getItemList = null;
	List<ERPHeader> getErpHeaderList = null;
	List<Vendor> vendorList = null;
	List<Dept> deparmentList = null;
	List<GetSubDept> getSubDeptList = null;
	List<IndentReport> getlist1 = null;
	List<GetPoHeaderList> getPoList = null;
	List<RejectionReport> getRejList = null;

	List<GatepassReport> getnonList = null;

	@RequestMapping(value = "/getItemListExportToExcel", method = RequestMethod.GET)
	public @ResponseBody List<GetItem> getItemListExportToExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			int catId = Integer.parseInt(request.getParameter("catId"));
			if (catId == 0) {
				GetItem[] item = rest.getForObject(Constants.url + "/getAllItems", GetItem[].class);
				getItemList = new ArrayList<GetItem>(Arrays.asList(item));

			} else {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", catId);
				GetItem[] GetItem = rest.postForObject(Constants.url + "itemListByCatId", map, GetItem[].class);
				getItemList = new ArrayList<>(Arrays.asList(GetItem));

			}

			System.out.println("getItemList" + getItemList.toString());

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Item Code");
			rowData.add("Item Date");
			rowData.add("Item Weight");
			rowData.add("Item UOM");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getItemList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + getItemList.get(i).getItemDesc());
				rowData.add("" + getItemList.get(i).getItemDate());
				rowData.add("" + getItemList.get(i).getItemWt());
				rowData.add("" + getItemList.get(i).getItemUom());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "GetItem");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return getItemList;

	}
	
	
	@RequestMapping(value = "/ERPlistMRN", method = RequestMethod.GET)
	public ModelAndView ERPExportToExcel(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("report/indentReport");
				try {

			String from_date = request.getParameter("from_date");
			String to_date = request.getParameter("to_date");
			from_date="2019-04-01";
			from_date="2019-04-10";
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("from_date", from_date);
				map.add("to_date", to_date);
				ERPHeader[] objErpHeader = rest.postForObject(Constants.url + "ERPlistMRN", map, ERPHeader[].class);
				getErpHeaderList = new ArrayList<>(Arrays.asList(objErpHeader));
				System.out.println("getErpHeaderList" + getErpHeaderList.toString());

			} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return model;

	}

	@RequestMapping(value = "/getDeptListExportToExcel", method = RequestMethod.GET)
	public @ResponseBody List<Dept> getDeptListExportToExcel(HttpServletRequest request, HttpServletResponse response) {
		try {

			Dept[] Dept = rest.getForObject(Constants.url + "/getAllDeptByIsUsed", Dept[].class);
			deparmentList = new ArrayList<Dept>(Arrays.asList(Dept));

			System.out.println("deparmentList" + deparmentList.toString());

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Department Code");
			rowData.add("Department Desc");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < deparmentList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + deparmentList.get(i).getDeptCode());
				rowData.add("" + deparmentList.get(i).getDeptDesc());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "Dept");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return deparmentList;

	}

	@RequestMapping(value = "/getSubDeptListExportToExcel", method = RequestMethod.GET)
	public @ResponseBody List<GetSubDept> getSubDeptListExportToExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			GetSubDept[] getSubDept = rest.getForObject(Constants.url + "/getAllSubDept", GetSubDept[].class);
			getSubDeptList = new ArrayList<GetSubDept>(Arrays.asList(getSubDept));

			System.out.println("getSubDeptList" + getSubDeptList.toString());

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Sub Department Desc");
			rowData.add("Sub Department Code");
			rowData.add("Department Code");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getSubDeptList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));

				rowData.add("" + getSubDeptList.get(i).getSubDeptDesc());
				rowData.add("" + getSubDeptList.get(i).getSubDeptCode());
				rowData.add("" + getSubDeptList.get(i).getDeptCode());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "GetSubDept");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return getSubDeptList;

	}

	@RequestMapping(value = "/getVendorListExportToExcel", method = RequestMethod.GET)
	public @ResponseBody List<Vendor> getVendorListExportToExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			System.out.println("vendorList" + vendorList.toString());

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			
			rowData.add("Name");
			rowData.add("City");
			rowData.add("State");
			rowData.add("Email");
			rowData.add("Gst No");
			rowData.add("Item");
			rowData.add("Date");
			rowData.add("Mobile No");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < vendorList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				
				rowData.add("" + vendorList.get(i).getVendorName());
				rowData.add("" + vendorList.get(i).getVendorCity());
				rowData.add("" + vendorList.get(i).getVendorState());
				rowData.add("" + vendorList.get(i).getVendorEmail());
				rowData.add("" + vendorList.get(i).getVendorGstNo());
				rowData.add("" + vendorList.get(i).getVendorItem());
				rowData.add("" + vendorList.get(i).getVendorDate());
				rowData.add("" + vendorList.get(i).getVendorMobile());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "Vendor");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return vendorList;

	}

	@RequestMapping(value = "/itemListPdf/{catId}", method = RequestMethod.GET)
	public void itemListPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable int catId)
			throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf showBillwisePurchasePdf");

		if (catId == 0) {
			GetItem[] item = rest.getForObject(Constants.url + "/getAllItems", GetItem[].class);
			getItemList = new ArrayList<GetItem>(Arrays.asList(item));

		} else {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("catId", catId);
			GetItem[] GetItem = rest.postForObject(Constants.url + "itemListByCatId", map, GetItem[].class);
			getItemList = new ArrayList<>(Arrays.asList(GetItem));

		}

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

		PdfPTable table = new PdfPTable(5);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1.4f, 5.7f, 2.0f, 2.0f, 2.0f });
			Font headFont = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
			headFont1.setColor(BaseColor.BLACK);
			Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell = new PdfPCell();
			hcell.setBackgroundColor(BaseColor.PINK);

			hcell.setPadding(3);
			hcell = new PdfPCell(new Phrase("Sr.No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Weight", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Uom", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (GetItem item : getItemList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(item.getItemDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(item.getItemDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + item.getItemWt(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + item.getItemUom(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Item List\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Item List Report\n", f);
			company.setAlignment(Element.ALIGN_CENTER);
			document.add(company);
			document.add(new Paragraph(" "));

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");

			document.add(new Paragraph("\n"));
			table.setHeaderRows(1);
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

	@RequestMapping(value = "/vendorListPdf", method = RequestMethod.GET)
	public void vendorListPdf(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf vendorListPdf");

		List<Vendor> getVendorList = vendorList;

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
			table.setWidths(new float[] { 1.4f, 3.7f, 3.7f, 3.7f, 3.7f, 3.7f, 3.2f, 3.2f, 4f, 3.7f });
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

			hcell = new PdfPCell(new Phrase("Item Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("City", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("State", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Email", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Gst No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Mobile No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (Vendor vendor : vendorList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(vendor.getVendorCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(vendor.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(vendor.getVendorCity(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorState(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorEmail(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorGstNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorItem(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorMobile(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + vendor.getVendorDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Item List\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Item List Report\n", f);
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

	@RequestMapping(value = "/deptListPdf", method = RequestMethod.GET)
	public void deptListPdf(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf showBillwisePurchasePdf");

		List<Dept> deptList = deparmentList;

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

		PdfPTable table = new PdfPTable(3);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1.4f, 3.7f, 3.7f });
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

			hcell = new PdfPCell(new Phrase("Department Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Department Desc", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (Dept dept : deptList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(dept.getDeptCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(dept.getDeptDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Department List\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Department List Report\n", f);
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

	@RequestMapping(value = "/subDeptListPdf", method = RequestMethod.GET)
	public void subDeptListPdf(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		BufferedOutputStream outStream = null;
		System.out.println("Inside Pdf showBillwisePurchasePdf");

		List<GetSubDept> subDeptList = getSubDeptList;

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

		PdfPTable table = new PdfPTable(4);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1.4f, 3.7f, 3.7f, 3.7f });
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

			hcell = new PdfPCell(new Phrase("Sub Department Desc", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Sub Department Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Department Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (GetSubDept subDept : subDeptList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(subDept.getSubDeptDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(subDept.getSubDeptCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(subDept.getDeptCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Sub Department List\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Sub Department List Report\n", f);
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

	@RequestMapping(value = "/indetReportList", method = RequestMethod.GET)
	public ModelAndView indetReportList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/indentReport");
		try {

			Category[] categoryRes = rest.getForObject(Constants.url + "/getAllCategoryByIsUsed", Category[].class);
			List<Category> catList = new ArrayList<Category>(Arrays.asList(categoryRes));

			model.addObject("catList", catList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/showIndentPdf/{fromDate}/{toDate}", method = RequestMethod.GET)
	public void showIndentPdf(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
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

		PdfPTable table = new PdfPTable(8);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f, 5.0f, 5.2f, 5.2f, 5.2f, 3.2f, 3.2f, 3.2f });
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

			hcell = new PdfPCell(new Phrase("Indent No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Indent Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Category Indent", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Monthly Indent", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Indent Is Dev", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Indent Status", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("No Of Days", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (IndentReport bill : getlist1) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(bill.getIndMNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getIndMDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getIndMDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getIndMDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getCatDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				if (bill.getIndIsmonthly() == 0) {
					cell = new PdfPCell(new Phrase("No", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getIndIsmonthly() == 1) {

					cell = new PdfPCell(new Phrase("Yes", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				}

				if (bill.getIndIsdev() == 0) {
					cell = new PdfPCell(new Phrase("No", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getIndIsdev() == 1) {

					cell = new PdfPCell(new Phrase("Yes", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				}

				if (bill.getIndMStatus() == 0) {
					cell = new PdfPCell(new Phrase("Pending", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getIndMStatus() == 1) {
					cell = new PdfPCell(new Phrase("Partial Pending", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getIndMStatus() == 2) {
					cell = new PdfPCell(new Phrase("Return", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				}

				cell = new PdfPCell(new Phrase("" + bill.getNoOfDays(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Trimbak Rubber\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Indent List Report\n", f);
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

	@RequestMapping(value = "/getIndentListReport", method = RequestMethod.GET)
	@ResponseBody
	public List<IndentReport> getIndentListReport(HttpServletRequest request, HttpServletResponse response) {

		try {
			getlist1 = new ArrayList<>();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			int isDev = Integer.parseInt(request.getParameter("isDev"));
			int isMonthly = Integer.parseInt(request.getParameter("isMonthly"));
			String[] catIdList = request.getParameterValues("catIdList[]");

			System.out.println("fromDate" + fromDate);
			System.out.println("toDate" + toDate);

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < catIdList.length; i++) {
				sb = sb.append(catIdList[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			System.out.println("fromDate" + DateConvertor.convertToYMD(fromDate));
			System.out.println("toDate" + DateConvertor.convertToYMD(toDate));
			map.add("catIdList", items);
			map.add("indIsmonthly", isMonthly);
			map.add("indIsdev", isDev);

			IndentReport[] getlist = rest.postForObject(Constants.url + "/getIndentListReport", map,
					IndentReport[].class);
			System.out.println("getList" + getlist);
			getlist1 = new ArrayList<IndentReport>(Arrays.asList(getlist));

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Indent No");
			rowData.add("Indent Date");
			rowData.add("Indent Category");
			rowData.add("Monthly Indent");
			rowData.add("Indent IsDev");
			rowData.add("Indent Status");
			rowData.add("No Of Days");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getlist1.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + getlist1.get(i).getIndMNo());
				rowData.add("" + getlist1.get(i).getIndMDate());
				rowData.add("" + getlist1.get(i).getCatDesc());

				if (getlist1.get(i).getIndIsmonthly() == 0) {
					rowData.add("No");
				} else if (getlist1.get(i).getIndIsmonthly() == 1) {
					rowData.add("Yes");
				}

				if (getlist1.get(i).getIndIsdev() == 0) {
					rowData.add("No");
				} else if (getlist1.get(i).getIndIsdev() == 1) {
					rowData.add("Yes");
				}

				if (getlist1.get(i).getIndMStatus() == 0) {
					rowData.add("Pending");

				} else if (getlist1.get(i).getIndMStatus() == 1) {
					rowData.add("Partial Pending");
				} else if (getlist1.get(i).getIndMStatus() == 2) {
					rowData.add("Closed");
				}

				rowData.add("" + getlist1.get(i).getNoOfDays());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "IndentReport");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getlist1;
	}

	@RequestMapping(value = "/poReportList", method = RequestMethod.GET)
	public ModelAndView poReportList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/poHeaderReport");
		try {
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

	@RequestMapping(value = "/pendingPoReportList", method = RequestMethod.GET)
	public ModelAndView pendingPoReportList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/pendingPo");
		try {
			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			List<Vendor> vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			model.addObject("vendorList", vendorList);

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			// SimpleDateFormat display = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("date", sf.format(date));
			DocumentBean resList = rest.postForObject(Constants.url + "getDocumentDataForPO", map, DocumentBean.class);
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

	@RequestMapping(value = "/getTypeListForPendingPo", method = RequestMethod.GET)
	@ResponseBody
	public List<Type> getTypeListForPendingPo(HttpServletRequest request, HttpServletResponse response) {

		Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
		List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));

		return typeList;
	}

	@RequestMapping(value = "/getPoListReport", method = RequestMethod.GET)
	@ResponseBody
	public List<GetPoHeaderList> getPoListReport(HttpServletRequest request, HttpServletResponse response) {

		try {
			getPoList = new ArrayList<>();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String[] vendorIdList = request.getParameterValues("vendorIdList[]");
			String[] poTypeList = request.getParameterValues("poTypeList[]");
			String[] poStatus = request.getParameterValues("poStatus[]");

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < vendorIdList.length; i++) {
				sb = sb.append(vendorIdList[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);
			sb = new StringBuilder();
			for (int j = 0; j < poTypeList.length; j++) {
				sb = sb.append(poTypeList[j] + ",");

			}
			String items1 = sb.toString();
			items1 = items1.substring(0, items1.length() - 1);
			sb = new StringBuilder();
			for (int k = 0; k < poStatus.length; k++) {
				sb = sb.append(poStatus[k] + ",");

			}
			String items2 = sb.toString();
			items2 = items2.substring(0, items2.length() - 1);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));
			map.add("vendorIdList", items);
			map.add("poTypeList", items1);
			map.add("poStatus", items2);

			GetPoHeaderList[] getlist = rest.postForObject(Constants.url + "/getPoHeaderListReport", map,
					GetPoHeaderList[].class);
			System.out.println("getList" + getlist);
			getPoList = new ArrayList<GetPoHeaderList>(Arrays.asList(getlist));

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("PO Date");
			rowData.add("Vendor Name");
			rowData.add("Po No");
			rowData.add("Po Basic Value");
			rowData.add("Po Total Value");
			rowData.add("Tax Appliacble");

			rowData.add("Po Status");
			rowData.add("Po Type");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getPoList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + getPoList.get(i).getPoDate());
				rowData.add("" + getPoList.get(i).getVendorName());
				rowData.add("" + getPoList.get(i).getPoNo());
				rowData.add("" + getPoList.get(i).getPoBasicValue());

				rowData.add("" + getPoList.get(i).getTotalValue());
				rowData.add("" + getPoList.get(i).getPoTaxValue());

				if (getPoList.get(i).getPoStatus() == 0) {
					rowData.add("Pending");
				} else if (getPoList.get(i).getPoStatus() == 1) {
					rowData.add("Partial Pending");
				} else if (getPoList.get(i).getPoStatus() == 2) {
					rowData.add("Return");
				}
				if (getPoList.get(i).getPoType() == 1) {
					rowData.add("Regular");
				} else if (getPoList.get(i).getPoType() == 2) {
					rowData.add("Job Work");
				} else if (getPoList.get(i).getPoType() == 3) {
					rowData.add("General");
				} else if (getPoList.get(i).getPoType() == 4) {
					rowData.add("Other");
				}
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "GetPoHeaderList");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPoList;
	}

	@RequestMapping(value = "/showPOPdf/{fromDate}/{toDate}", method = RequestMethod.GET)
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

		PdfPTable table = new PdfPTable(9);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f, 5.0f, 5.2f, 5.2f, 5.2f, 5.2f, 3.2f, 3.2f, 3.2f });
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

			hcell = new PdfPCell(new Phrase("PO Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PO No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PO Basic Value", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PO Total Value", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Tax Appliacble", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PO Status", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PO Type", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (GetPoHeaderList bill : getPoList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(bill.getPoDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getPoNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getPoBasicValue(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getTotalValue(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getPoTaxValue(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				if (bill.getPoStatus() == 0) {
					cell = new PdfPCell(new Phrase("Pending", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getPoStatus() == 1) {
					cell = new PdfPCell(new Phrase("Partial Pending", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getPoStatus() == 2) {
					cell = new PdfPCell(new Phrase("Return", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				}

				if (bill.getPoType() == 1) {
					cell = new PdfPCell(new Phrase("Regular", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);
				} else if (bill.getPoType() == 2) {
					cell = new PdfPCell(new Phrase("Job Work", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);

				} else if (bill.getPoType() == 3) {
					cell = new PdfPCell(new Phrase("General", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);

				} else if (bill.getPoType() == 4) {
					cell = new PdfPCell(new Phrase("Other", headFont));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPaddingRight(2);
					cell.setPadding(3);
					table.addCell(cell);

				}

			}
			document.open();
			Paragraph name = new Paragraph("Trimbak Rubber\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Indent List Report\n", f);
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

	@RequestMapping(value = "/rejectionReportVendorwise", method = RequestMethod.GET)
	public ModelAndView rejectionReportVendorwise(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/rejectionVendorwise");
		try {
			Vendor[] vendorRes = rest.getForObject(Constants.url + "/getAllVendorByIsUsed", Vendor[].class);
			List<Vendor> vendorList = new ArrayList<Vendor>(Arrays.asList(vendorRes));

			model.addObject("vendorList", vendorList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/getRejectionVendorwise", method = RequestMethod.GET)
	@ResponseBody
	public List<RejectionReport> getRejectionVendorwise(HttpServletRequest request, HttpServletResponse response) {

		try {
			getRejList = new ArrayList<>();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String[] vendorIdList = request.getParameterValues("vendorIdList[]");

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < vendorIdList.length; i++) {
				sb = sb.append(vendorIdList[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);
			sb = new StringBuilder();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));
			map.add("vendorIdList", items);

			RejectionReport[] getlist = rest.postForObject(Constants.url + "/getRejectionListReport", map,
					RejectionReport[].class);
			System.out.println("getList" + getlist);
			getRejList = new ArrayList<RejectionReport>(Arrays.asList(getlist));

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Vendor Code");
			rowData.add("Vendor Name");
			rowData.add("Rejection No");
			rowData.add("Rejection Date");
			rowData.add("Item Name");
			rowData.add("Memo Quantity");
			rowData.add("Mrn No");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getRejList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + getRejList.get(i).getVendorCode());
				rowData.add("" + getRejList.get(i).getVendorName());
				rowData.add("" + getRejList.get(i).getRejectionNo());
				rowData.add("" + getRejList.get(i).getRejectionDate());
				rowData.add("" + getRejList.get(i).getItemDesc());
				rowData.add("" + getRejList.get(i).getMemoQty());
				rowData.add("" + getRejList.get(i).getMrnNo());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "RejectionReport");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getRejList;
	}

	@RequestMapping(value = "/showRejectionVendorwisePdf/{fromDate}/{toDate}", method = RequestMethod.GET)
	public void showRejectionVendorwisePdf(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {
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

		PdfPTable table = new PdfPTable(8);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f, 3.2f, 3.2f, 3.2f, 3.2f, 3.2f, 3.3f, 3.2f });
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

			hcell = new PdfPCell(new Phrase("Vendor Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Rejection No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Rejection Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Memo Quantity", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Mrn No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (RejectionReport bill : getRejList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(bill.getVendorCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getRejectionNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getRejectionDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getItemDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getMemoQty(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getMrnNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("Trimbak Rubber\n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Rejection Vendorwise Report\n", f);
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

	@RequestMapping(value = "/rejectionReportItemwise", method = RequestMethod.GET)
	public ModelAndView rejectionReportItemwise(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("report/rejectionItemwise");
		try {

			Category[] categoryRes = rest.getForObject(Constants.url + "/getAllCategoryByIsUsed", Category[].class);
			List<Category> catList = new ArrayList<Category>(Arrays.asList(categoryRes));

			model.addObject("catList", catList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/getItemIdByCatId", method = RequestMethod.GET)
	@ResponseBody
	public List<GetItem> getItemIdByCatId(HttpServletRequest request, HttpServletResponse response) {

		List<GetItem> itemList = new ArrayList<GetItem>();
		try {
			int catId = Integer.parseInt(request.getParameter("catId"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("catId", catId);
			GetItem[] resList = rest.postForObject(Constants.url + "itemListByCatId", map, GetItem[].class);

			itemList = new ArrayList<>(Arrays.asList(resList));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}

	@RequestMapping(value = "/getRejectionItemwise", method = RequestMethod.GET)
	@ResponseBody
	public List<RejectionReport> getRejectionItemwise(HttpServletRequest request, HttpServletResponse response) {

		try {

			getRejList = new ArrayList<>();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String[] itemIdList = request.getParameterValues("itemIdList[]");

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < itemIdList.length; i++) {
				sb = sb.append(itemIdList[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);
			sb = new StringBuilder();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));
			map.add("itemIdList", items);

			RejectionReport[] getlist = rest.postForObject(Constants.url + "/getRejectionListItemwise", map,
					RejectionReport[].class);
			System.out.println("getList" + getlist);
			getRejList = new ArrayList<RejectionReport>(Arrays.asList(getlist));

			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No");
			rowData.add("Item Code");
			rowData.add("Item Name");
			rowData.add("Vendor Code");
			rowData.add("Vendor Name");
			rowData.add("Rejection No");
			rowData.add("Rejection Date");

			rowData.add("Memo Quantity");
			rowData.add("Mrn No");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int i = 0; i < getRejList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				cnt = cnt + i;
				rowData.add("" + (cnt));
				rowData.add("" + getRejList.get(i).getItemCode());
				rowData.add("" + getRejList.get(i).getItemDesc());
				rowData.add("" + getRejList.get(i).getVendorCode());
				rowData.add("" + getRejList.get(i).getVendorName());
				rowData.add("" + getRejList.get(i).getRejectionNo());
				rowData.add("" + getRejList.get(i).getRejectionDate());

				rowData.add("" + getRejList.get(i).getMemoQty());
				rowData.add("" + getRejList.get(i).getMrnNo());

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelList", exportToExcelList);
			session.setAttribute("excelName", "RejectionReport");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getRejList;
	}

	@RequestMapping(value = "/showRejectionItemwisePdf/{fromDate}/{toDate}", method = RequestMethod.GET)
	public void showRejectionItemwisePdf(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {
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

		PdfPTable table = new PdfPTable(9);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2.4f, 3.2f, 3.2f, 3.2f, 3.2f, 3.2f, 4.1f, 3.3f, 3.5f });
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

			hcell = new PdfPCell(new Phrase("Item Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Item Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Vendor Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Rejection No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Rejection Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Memo Quantity", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Mrn No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);

			table.addCell(hcell);

			int index = 0;
			for (RejectionReport bill : getRejList) {
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(3);
				cell.setPaddingRight(2);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(bill.getItemCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getItemDesc(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(bill.getVendorCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getVendorName(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getRejectionNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getRejectionDate(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getMemoQty(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + bill.getMrnNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(2);
				cell.setPadding(3);
				table.addCell(cell);

			}
			document.open();
			Paragraph name = new Paragraph("GFPL \n", f);
			name.setAlignment(Element.ALIGN_CENTER);
			document.add(name);
			document.add(new Paragraph(" "));
			Paragraph company = new Paragraph("Rejection Itemwise Report\n", f);
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
	// getDocumentDataForPO

	@RequestMapping(value = "/getDocumentDataForPO", method = RequestMethod.GET)
	@ResponseBody
	public List<DocumentBean> getDocumentDataForPO(HttpServletRequest request, HttpServletResponse response) {

		List<DocumentBean> docList = new ArrayList<DocumentBean>();
		try {
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat display = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("date", sf.format(date));
			DocumentBean resList = rest.postForObject(Constants.url + "getDocumentDataForPO", map, DocumentBean.class);
			System.out.println("resList" + resList);

			String dateNew = resList.getFromDate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return docList;
	}
	

	public static float roundUp(float d) {
		return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	@RequestMapping(value = "/excelForMrnExcel", method = RequestMethod.GET)
	@ResponseBody
	public List<MrnExcelPuch> excelForMrnExcel(HttpServletRequest request, HttpServletResponse response) {

		List<MrnExcelPuch> mrnExcelListRes = null;
		MrnExcelPuch[] mrnExcelList = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String checkboxes = request.getParameter("checkboxes");
			System.out.println("checkboxes " + checkboxes);

			checkboxes = checkboxes.substring(0, checkboxes.length() - 1);
			System.out.println("string " + checkboxes);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mrnIdList", checkboxes);
			System.out.println("map " + map);

			mrnExcelList = restTemplate.postForObject(Constants.url + "/getMrnHsnwiseExcelReport", map,
					MrnExcelPuch[].class);
			mrnExcelListRes = new ArrayList<MrnExcelPuch>(Arrays.asList(mrnExcelList));
			System.out.println("mrnExcelList " + mrnExcelListRes.toString());
			List<Integer> mrnIds = Stream.of(checkboxes.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			try {
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();

				rowData.add("MRN Id");
				rowData.add("MRN DTL ID");  
				rowData.add("MRN_TYPE_ID");
				rowData.add("MRN_TYPE");
				rowData.add("MRN NO");
				rowData.add("Date");
				rowData.add("Bill No");
				rowData.add("Bill Date");
				rowData.add("PO NO");
				rowData.add("PO Date");
				rowData.add("Type");
				rowData.add("Supp_id");
				rowData.add("SUPP Code");
				rowData.add("Supp  ERP Code");
				rowData.add("Supplier Name");
				rowData.add("Gst No");
				rowData.add("State");
				rowData.add("Cat Id");
				rowData.add("Item Id");
				rowData.add("Vendor ERP Code");
				rowData.add("Item Name");
				rowData.add("Hsn Code");
				rowData.add("Qty");
				rowData.add("Uom");
				rowData.add("Rate");
				rowData.add("Amount");
				rowData.add("Sgst Per");
				rowData.add("Sgst Rs");
				rowData.add("Cgst Per");
				rowData.add("Cgst Rs");
				rowData.add("Igst Per");
				rowData.add("Igst Rs");
				rowData.add("Cess Per");
				rowData.add("Cess Rs");
				rowData.add("Item Disc %");
				rowData.add("Total Discount");
			
				rowData.add("Total Amt");
				rowData.add("Total Taxable Amt");
				rowData.add("Cgst sum");
				rowData.add("Sgst sum");
				rowData.add("Igst sum");
				rowData.add("Tax Amt");
				rowData.add("P&F");
				rowData.add("Freight");
				rowData.add("Insurance Amt");
				rowData.add("Other Charges After");
				rowData.add("Bill Total");
				rowData.add("Bill Total R.Off");
				rowData.add("Rount Off");
				rowData.add("Remark");
				rowData.add("Erp Link");
				rowData.add("Bill No_C");
				
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				
				for(int l=0;l<mrnIds.size();l++)
				{
					float roundOff=0.0f;
					float totalAmount=0.0f;
					float totalTaxableAmt=0.0f;
					float cgstSum=0.0f;float sgstSum=0.0f;float igstSum=0.0f;
					float taxAmt=0.0f;float pAndf=0.0f;float freight=0.0f;float otherCharges=0.0f;
					float otherChargesAfter=0.0f; float billTotal=0.0f;
					
					for(int j=0;j<mrnExcelListRes.size();j++)
					{
						
					
						if(mrnIds.get(l)==mrnExcelListRes.get(j).getMrnId())
						{
							totalTaxableAmt=totalTaxableAmt+(mrnExcelListRes.get(j).getAmount()+mrnExcelListRes.get(j).getPackValue()+mrnExcelListRes.get(j).getFreightValue()+mrnExcelListRes.get(j).getOtherChargesBefor()+mrnExcelListRes.get(j).getInsu());
							cgstSum=cgstSum+mrnExcelListRes.get(j).getCgstRs();
							sgstSum=sgstSum+mrnExcelListRes.get(j).getSgstRs();
							igstSum=igstSum+mrnExcelListRes.get(j).getIgstRs();
							pAndf=pAndf+mrnExcelListRes.get(j).getPackValue();
							freight=freight+mrnExcelListRes.get(j).getFreightValue();
							otherCharges=otherCharges+(mrnExcelListRes.get(j).getOtherChargesBefor()+mrnExcelListRes.get(j).getInsu());
							otherChargesAfter=otherChargesAfter+mrnExcelListRes.get(j).getOtherChargesAfter();
						
						}
					}
					billTotal=billTotal+(totalTaxableAmt+cgstSum+sgstSum+otherChargesAfter);
				for(int i=0;i<mrnExcelListRes.size();i++)
				{
					if(mrnIds.get(l)==mrnExcelListRes.get(i).getMrnId())
					{
						
						int billNo=0;
						try{
					       billNo=Integer.valueOf(mrnExcelListRes.get(i).getBillNo());
					    } catch (NumberFormatException e) {
					    	billNo=0;
					    }
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("" + mrnExcelListRes.get(i).getMrnId());
				rowData.add("" + mrnExcelListRes.get(i).getMrnDetailId());
				rowData.add("" + mrnExcelListRes.get(i).getMrnTypeId());
				rowData.add("" + mrnExcelListRes.get(i).getMrnType());
				rowData.add("" + mrnExcelListRes.get(i).getMrnNo());
				rowData.add("" + mrnExcelListRes.get(i).getMrnDate());
				if(billNo==0)
				rowData.add("");
				else
				rowData.add("" + mrnExcelListRes.get(i).getBillNo());
				rowData.add("" + mrnExcelListRes.get(i).getBillDate());
				rowData.add("" + mrnExcelListRes.get(i).getPoNo());
				rowData.add("" + mrnExcelListRes.get(i).getPoDate());
				rowData.add("" + mrnExcelListRes.get(i).getType());
				rowData.add("" + mrnExcelListRes.get(i).getSupplierId());
				rowData.add("" + mrnExcelListRes.get(i).getSupplierCode());
				rowData.add("" + mrnExcelListRes.get(i).getSuppErpCode());
				rowData.add("" + mrnExcelListRes.get(i).getSupplierName());
				rowData.add("" + mrnExcelListRes.get(i).getGstNo());
				rowData.add("" + mrnExcelListRes.get(i).getState());
				rowData.add("" + mrnExcelListRes.get(i).getCatId());
				rowData.add("" + mrnExcelListRes.get(i).getItemId());
				rowData.add("" + mrnExcelListRes.get(i).getVendorErpCode());
				rowData.add("" + mrnExcelListRes.get(i).getItemName());
				rowData.add("" + mrnExcelListRes.get(i).getHsnCode());
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getQty()));
				rowData.add("" + mrnExcelListRes.get(i).getUom());
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getRate()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getAmount()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getSgstPer()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getSgstRs()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getCgstPer()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getCgstRs()));
				rowData.add("" +roundUp(mrnExcelListRes.get(i).getIgstPer()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getIgstRs()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getCessPer()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getCessRs()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getItemDiscount()));
				rowData.add("" + roundUp(mrnExcelListRes.get(i).getTotalDiscount()));
				
				
				rowData.add(roundUp(mrnExcelListRes.get(i).getAmount()+ mrnExcelListRes.get(i).getCgstRs()+mrnExcelListRes.get(i).getSgstRs())+"");//calc//calc
				rowData.add("" + roundUp(totalTaxableAmt));//calc
				rowData.add("" + roundUp(cgstSum));//calc
				rowData.add("" + roundUp(sgstSum));//calc
				rowData.add("" + roundUp(igstSum));//calc
				rowData.add("" + roundUp(cgstSum+sgstSum));//calc
				
				rowData.add(""+roundUp(pAndf));//p&f
				rowData.add(""+roundUp(freight));//Freight
				rowData.add(""+roundUp(otherCharges));//Other Charges
				rowData.add(""+roundUp(otherChargesAfter));//oth chrges after
				rowData.add(""+roundUp(billTotal));//Bill Total
				rowData.add(""+Math.round(billTotal));//Bill Total
				rowData.add(roundUp(Math.round(billTotal)-roundUp(billTotal))+"");//calc
				rowData.add(""+mrnExcelListRes.get(i).getRemark());//remark==blnk
				rowData.add("");//Erp Link==blnk
				if(billNo==0)
					rowData.add(""+mrnExcelListRes.get(i).getBillNo());
				else
					rowData.add("");
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
					}
				}
				
				/*for(int l=0;l<crnIds.size();l++)
				{
					float docAmt=0;
					
					for(int j=0;j<crnExcelListRes.size();j++)
					{
						if(crnIds.get(l)==crnExcelListRes.get(j).getCrnId())
						{
							docAmt=docAmt+roundUp(crnExcelListRes.get(j).getTaxableAmt() + crnExcelListRes.get(j).getCgstRs()
									+ crnExcelListRes.get(j).getSgstRs());
						}
					}
					for(int i=0;i<crnExcelListRes.size();i++)
					{
						if(crnIds.get(l)==crnExcelListRes.get(i).getCrnId())
						{
					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add("" + (i + 1));
					rowData.add("" + crnExcelListRes.get(i).getSupplierInvoiceNo());
					rowData.add("" + crnExcelListRes.get(i).getSupplierInvoiceDate());
					rowData.add("" + crnExcelListRes.get(i).getInvoiceNo());
					rowData.add("" + crnExcelListRes.get(i).getInvoiceDate());
					rowData.add("" + crnExcelListRes.get(i).getFrName());
					rowData.add("" + crnExcelListRes.get(i).getItemHsncd());
					rowData.add("" + roundUp(crnExcelListRes.get(i).getQty()));
					rowData.add("" + roundUp(crnExcelListRes.get(i).getTaxableAmt()));
					rowData.add("" + roundUp(crnExcelListRes.get(i).getCgstRs()));
					rowData.add("" + roundUp(crnExcelListRes.get(i).getSgstRs()));
					rowData.add("" + roundUp(crnExcelListRes.get(i).getIgstRs()));
					rowData.add("" + roundUp(crnExcelListRes.get(i).getTaxRate()));
					rowData.add(roundUp(crnExcelListRes.get(i).getTaxableAmt() + crnExcelListRes.get(i).getCgstRs()
							+ crnExcelListRes.get(i).getSgstRs()) + "");
					rowData.add("" + roundUp(docAmt));
					rowData.add("" + crnExcelListRes.get(i).getFrGstNo());
					rowData.add("" + crnExcelListRes.get(i).getCountry());
					rowData.add("" + crnExcelListRes.get(i).getState());

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);
						}
					}
				}*/
				
				}
		

				HttpSession session = request.getSession();
				session.setAttribute("exportExcelList", exportToExcelList);
				session.setAttribute("excelName", "mrnExcel");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception to genrate excel ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mrnExcelListRes;

	}
}
