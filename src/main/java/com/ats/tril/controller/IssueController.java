package com.ats.tril.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
import com.ats.tril.model.AccountHead;
import com.ats.tril.model.BillOfMaterialDetailed;
import com.ats.tril.model.BillOfMaterialHeader;
import com.ats.tril.model.Category;
import com.ats.tril.model.Dept;
import com.ats.tril.model.ErrorMessage;
import com.ats.tril.model.ExportToExcel;
import com.ats.tril.model.FrItemStockConfigure;
import com.ats.tril.model.FrItemStockConfigureList;
import com.ats.tril.model.GetBillOfMaterialList;
import com.ats.tril.model.GetIssueDetail;
import com.ats.tril.model.GetIssueExcelList;
import com.ats.tril.model.GetIssueHeader;
import com.ats.tril.model.GetItemGroup;
import com.ats.tril.model.GetPoHeaderList;
import com.ats.tril.model.GetSubDept;
import com.ats.tril.model.GetpassHeader;
import com.ats.tril.model.Info;
import com.ats.tril.model.IssueDetail;
import com.ats.tril.model.IssueHeader;
import com.ats.tril.model.StockHeader;
import com.ats.tril.model.Type;
import com.ats.tril.model.doc.DocumentBean;
import com.ats.tril.model.doc.SubDocument;
import com.ats.tril.model.indent.IndentTrans;
import com.ats.tril.model.item.GetItem;
import com.ats.tril.model.item.ItemList;
import com.ats.tril.model.login.UserResponse;
import com.ats.tril.model.mrn.GetMrnHeader;
import com.ats.tril.model.mrn.MrnDetail;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

@Controller
@Scope("session")
public class IssueController {

	RestTemplate rest = new RestTemplate();
	List<IssueDetail> issueDetailList = new ArrayList<IssueDetail>();
	List<MrnDetail> batchList = new ArrayList<MrnDetail>();
	List<MrnDetail> updateMrnDetail = new ArrayList<MrnDetail>();

	@RequestMapping(value = "/addIssueItem", method = RequestMethod.GET)
	public ModelAndView addIssueItem(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/addIssueItem");
		try {
			updateMrnDetail = new ArrayList<MrnDetail>();
			issueDetailList = new ArrayList<IssueDetail>();
			Dept[] Dept = rest.getForObject(Constants.url + "/getAllDeptByIsUsed", Dept[].class);
			List<Dept> deparmentList = new ArrayList<Dept>(Arrays.asList(Dept));
			model.addObject("deparmentList", deparmentList);

			/*
			 * GetItemGroup[] itemGroupList = rest.getForObject(Constants.url +
			 * "/getAllItemGroupByIsUsed", GetItemGroup[].class);
			 * model.addObject("itemGroupList", itemGroupList);
			 */

			Category[] category = rest.getForObject(Constants.url + "/getAllCategoryByIsUsed", Category[].class);
			List<Category> categoryList = new ArrayList<Category>(Arrays.asList(category));
			model.addObject("categoryList", categoryList);

			AccountHead[] accountHead = rest.getForObject(Constants.url + "/getAllAccountHeadByIsUsed",
					AccountHead[].class);
			List<AccountHead> accountHeadList = new ArrayList<AccountHead>(Arrays.asList(accountHead));

			model.addObject("accountHeadList", accountHeadList);

			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);

			StockHeader stockHeader = rest.getForObject(Constants.url + "/getCurrentRunningMonthAndYear",
					StockHeader.class);

			System.out.println(" stock Date: " + stockHeader.getDate());

			model.addObject("stockDateDDMMYYYY", DateConvertor.convertToDMY(stockHeader.getDate()));

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			model.addObject("date", sf.format(date));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/getSubDeptList", method = RequestMethod.GET)
	@ResponseBody
	public List<GetSubDept> getSubDeptListByDeptId(HttpServletRequest request, HttpServletResponse response) {

		List<GetSubDept> subDeptList = new ArrayList<GetSubDept>();
		try {
			int deptId = Integer.parseInt(request.getParameter("deptId"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("deptId", deptId);
			GetSubDept[] GetSubDept = rest.postForObject(Constants.url + "getSubDeptListByDeptId", map,
					GetSubDept[].class);

			subDeptList = new ArrayList<>(Arrays.asList(GetSubDept));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return subDeptList;
	}

	List<GetItem> itemListbyCatId = new ArrayList<GetItem>();

	@RequestMapping(value = "/getItemIdByCatIdInIssue", method = RequestMethod.GET)
	@ResponseBody
	public List<GetItem> getItemIdByCatIdInIssue(HttpServletRequest request, HttpServletResponse response) {

		itemListbyCatId = new ArrayList<GetItem>();

		try {
			int grpId = Integer.parseInt(request.getParameter("grpId"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("catId", grpId);
			GetItem[] GetItem = rest.postForObject(Constants.url + "itemListByCatId", map, GetItem[].class);
			itemListbyCatId = new ArrayList<>(Arrays.asList(GetItem));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemListbyCatId;
	}

	@RequestMapping(value = "/getBatchByItemId", method = RequestMethod.GET)
	@ResponseBody
	public List<MrnDetail> getBatchByItemId(HttpServletRequest request, HttpServletResponse response) {

		try {
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int type = Integer.parseInt(request.getParameter("type"));
			String date = request.getParameter("date");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("itemId", itemId);
			map.add("date", date);
			map.add("type", type);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "getBatchByItemId", map, MrnDetail[].class);

			batchList = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

			for (int i = 0; i < issueDetailList.size(); i++) {
				for (int j = 0; j < batchList.size(); j++) {
					if (batchList.get(j).getMrnDetailId() == issueDetailList.get(i).getMrnDetailId()) {
						batchList.get(j).setRemainingQty(
								batchList.get(j).getRemainingQty() - issueDetailList.get(i).getItemIssueQty());
						batchList.get(j)
								.setIssueQty(batchList.get(j).getIssueQty() + issueDetailList.get(i).getItemIssueQty());
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return batchList;
	}

	@RequestMapping(value = "/qtyValidationFromBatch", method = RequestMethod.GET)
	@ResponseBody
	public MrnDetail qtyValidationFromBatch(HttpServletRequest request, HttpServletResponse response) {

		MrnDetail mrnDetail = new MrnDetail();
		try {
			int mrnDetailId = Integer.parseInt(request.getParameter("batchNo"));

			for (int i = 0; i < batchList.size(); i++) {
				if (batchList.get(i).getMrnDetailId() == mrnDetailId) {
					mrnDetail = batchList.get(i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mrnDetail;
	}

	@RequestMapping(value = "/addItmeInIssueList", method = RequestMethod.GET)
	@ResponseBody
	public List<IssueDetail> addItmeInIssueList(HttpServletRequest request, HttpServletResponse response) {

		try {
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int mrnDetailId = Integer.parseInt(request.getParameter("batchNo"));
			float qty = Float.parseFloat(request.getParameter("qty"));
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			int acc = Integer.parseInt(request.getParameter("acc"));
			String editIndex = request.getParameter("editIndex");
			String itemName = request.getParameter("itemName");
			String groupName = request.getParameter("groupName");
			String deptName = request.getParameter("deptName");
			String subDeptName = request.getParameter("subDeptName");
			String accName = request.getParameter("accName");

			if (editIndex.equalsIgnoreCase("") || editIndex.equalsIgnoreCase(null)) {
				IssueDetail issueDetail = new IssueDetail();
				issueDetail.setItemId(itemId);
				issueDetail.setItemIssueQty(qty);

				issueDetail.setDeptId(deptId);
				issueDetail.setSubDeptId(subDeptId);
				issueDetail.setAccHead(acc);
				issueDetail.setItemName(itemName);
				issueDetail.setGroupName(groupName);
				issueDetail.setDeptName(deptName);
				issueDetail.setSubDeptName(subDeptName);
				issueDetail.setAccName(accName);
				issueDetail.setDelStatus(1);

				for (int i = 0; i < itemListbyCatId.size(); i++) {
					if (itemListbyCatId.get(i).getItemId() == itemId) {

						issueDetail.setItemGroupId(itemListbyCatId.get(i).getGrpId());
					}
				}
				for (int i = 0; i < batchList.size(); i++) {
					if (batchList.get(i).getMrnDetailId() == mrnDetailId) {
						issueDetail.setBatchNo(batchList.get(i).getBatchNo());
						issueDetail.setMrnDetailId(mrnDetailId);
					}
				}
				issueDetailList.add(issueDetail);
			} else {
				int index = Integer.parseInt(editIndex);
				issueDetailList.get(index).setItemId(itemId);
				issueDetailList.get(index).setItemGroupId(groupId);
				issueDetailList.get(index).setDeptId(deptId);
				issueDetailList.get(index).setSubDeptId(subDeptId);
				issueDetailList.get(index).setAccHead(acc);
				issueDetailList.get(index).setItemName(itemName);
				issueDetailList.get(index).setGroupName(groupName);
				issueDetailList.get(index).setDeptName(deptName);
				issueDetailList.get(index).setSubDeptName(subDeptName);
				issueDetailList.get(index).setAccName(accName);

				for (int i = 0; i < batchList.size(); i++) {
					if (batchList.get(i).getMrnDetailId() == mrnDetailId) {
						issueDetailList.get(index).setBatchNo(batchList.get(i).getBatchNo());
						issueDetailList.get(index).setMrnDetailId(mrnDetailId);
					}
				}

				issueDetailList.get(index).setItemIssueQty(qty);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetailList;
	}

	@RequestMapping(value = "/editItemInIssueList", method = RequestMethod.GET)
	@ResponseBody
	public IssueDetail editItemInIssueList(HttpServletRequest request, HttpServletResponse response) {

		IssueDetail issueDetail = new IssueDetail();

		try {
			int index = Integer.parseInt(request.getParameter("index"));

			issueDetail = issueDetailList.get(index);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetail;
	}

	@RequestMapping(value = "/deleteItemFromIssueList", method = RequestMethod.GET)
	@ResponseBody
	public List<IssueDetail> deleteItemFromIssueList(HttpServletRequest request, HttpServletResponse response) {

		try {
			int index = Integer.parseInt(request.getParameter("index"));
			issueDetailList.remove(index);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetailList;
	}

	@RequestMapping(value = "/submitIssueReciept", method = RequestMethod.POST)
	public String submitIssueReciept(HttpServletRequest request, HttpServletResponse response) {

		try {

			String issueNo = request.getParameter("issueNo");
			String issueDate = request.getParameter("issueDate");
			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			int acc = Integer.parseInt(request.getParameter("acc"));
			int type = Integer.parseInt(request.getParameter("type"));
			String issueSlipNo = request.getParameter("issueSlipNo");

			System.out.println("issueDate " + issueDate);
			IssueHeader issueHeader = new IssueHeader();

			issueHeader.setIssueDate(issueDate);
			// DocumentBean docBean=null;
			try {

				/*
				 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
				 * Object>(); map.add("docId",6); map.add("catId", 1); map.add("date",
				 * issueDate); map.add("typeId", type); RestTemplate restTemplate = new
				 * RestTemplate();
				 * 
				 * docBean = restTemplate.postForObject(Constants.url + "getDocumentData", map,
				 * DocumentBean.class); String
				 * indMNo=docBean.getSubDocument().getCategoryPrefix()+""; int
				 * counter=docBean.getSubDocument().getCounter(); int counterLenth =
				 * String.valueOf(counter).length(); counterLenth =4 - counterLenth;
				 * StringBuilder code = new StringBuilder(indMNo+"");
				 * 
				 * for (int i = 0; i < counterLenth; i++) { String j = "0"; code.append(j); }
				 * code.append(String.valueOf(counter));
				 */

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("docType", 2);
				map.add("date", issueDate);

				RestTemplate restTemplate = new RestTemplate();

				ErrorMessage errorMessage = restTemplate.postForObject(Constants.url + "generateIssueNoAndMrnNo", map,
						ErrorMessage.class);

				issueHeader.setIssueNo("" + errorMessage.getMessage());

				// docBean.getSubDocument().setCounter(docBean.getSubDocument().getCounter()+1);
			} catch (Exception e) {
				e.printStackTrace();
				issueHeader.setIssueNo("1");
			}
			// issueHeader.setIssueNo(issueNo);
			issueHeader.setDeleteStatus(1);
			issueHeader.setIssueDetailList(issueDetailList);
			issueHeader.setDeptId(deptId);
			issueHeader.setSubDeptId(subDeptId);
			issueHeader.setAccHead(acc);
			issueHeader.setItemCategory(type);
			issueHeader.setIssueSlipNo(issueSlipNo);
			issueHeader.setStatus(2);

			String mrnDetailList = new String();

			for (int i = 0; i < issueDetailList.size(); i++) {
				mrnDetailList = mrnDetailList + "," + issueDetailList.get(i).getMrnDetailId();
				issueDetailList.get(i).setDeptId(deptId);
				issueDetailList.get(i).setSubDeptId(subDeptId);
				issueDetailList.get(i).setAccHead(acc);
				issueDetailList.get(i).setStatus(2);
			}

			mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("mrnDetailList", mrnDetailList);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
					MrnDetail[].class);

			updateMrnDetail = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

			for (int j = 0; j < issueDetailList.size(); j++) {
				for (int i = 0; i < updateMrnDetail.size(); i++) {
					if (updateMrnDetail.get(i).getMrnDetailId() == issueDetailList.get(j).getMrnDetailId()) {
						updateMrnDetail.get(i).setRemainingQty(
								updateMrnDetail.get(i).getRemainingQty() - issueDetailList.get(j).getItemIssueQty());
						updateMrnDetail.get(i).setIssueQty(
								updateMrnDetail.get(i).getIssueQty() + issueDetailList.get(j).getItemIssueQty());
						if (updateMrnDetail.get(i).getIssueQty() > 0) {
							updateMrnDetail.get(i).setChalanQty(1);
						} else {
							updateMrnDetail.get(i).setChalanQty(0);
						}
					}
				}
			}

			System.out.println("issueHeader  " + issueHeader);

			IssueHeader res = rest.postForObject(Constants.url + "/saveIssueHeaderAndDetail", issueHeader,
					IssueHeader.class);
			if (res != null) {
				try {

					// SubDocument subDocRes = rest.postForObject(Constants.url + "/saveSubDoc",
					// docBean.getSubDocument(), SubDocument.class);

				} catch (Exception e) {
					e.printStackTrace();
				}

				MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
						MrnDetail[].class);
			}
			System.out.println(res);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/issueList";
	}

	@RequestMapping(value = "/issueList", method = RequestMethod.GET)
	public ModelAndView issueList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/issueList");
		try {

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat disply = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			if (request.getParameter("fromDate") == null || request.getParameter("toDate") == null) {

				map.add("fromDate", sf.format(date));
				map.add("toDate", sf.format(date));

				GetIssueHeader[] IssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderList", map,
						GetIssueHeader[].class);
				List<GetIssueHeader> issueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(IssueHeader));
				model.addObject("issueHeaderList", issueHeaderList);

				model.addObject("fromDate", disply.format(date));
				model.addObject("toDate", disply.format(date));

			} else {

				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				map.add("fromDate", DateConvertor.convertToYMD(fromDate));
				map.add("toDate", DateConvertor.convertToYMD(toDate));

				GetIssueHeader[] IssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderList", map,
						GetIssueHeader[].class);
				List<GetIssueHeader> issueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(IssueHeader));
				model.addObject("issueHeaderList", issueHeaderList);

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				map.add("fromDate", DateConvertor.convertToYMD(fromDate));
				map.add("toDate", DateConvertor.convertToYMD(toDate));
				GetIssueExcelList[] issueArr = rest.postForObject(Constants.url + "/getIssueExcelList", map,
						GetIssueExcelList[].class);
				List<GetIssueExcelList> issueList  = new ArrayList<GetIssueExcelList>(Arrays.asList(issueArr));
				
				// export to excel

							List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

							ExportToExcel expoExcel = new ExportToExcel();
							List<String> rowData = new ArrayList<String>();

							rowData.add("Sr. No");
							rowData.add("iss_m_id");
							rowData.add("iss_m_date");
							rowData.add("iss_slip_no");
							rowData.add("iss_d_id");
							rowData.add("Item_id");
							rowData.add("item_description");
							rowData.add("item_qty");
							rowData.add("item_uom");
							rowData.add("Item_mrn_ref");
							rowData.add("item_rate");

							expoExcel.setRowData(rowData);
							exportToExcelList.add(expoExcel);
							int cnt = 1;
							for (int i = 0; i < issueList.size(); i++) {
								expoExcel = new ExportToExcel();
								rowData = new ArrayList<String>();
								cnt = cnt + i;
								rowData.add("" + (cnt));
								rowData.add("" + issueList.get(i).getIssueId());
								rowData.add("" + issueList.get(i).getIssueDate());
								rowData.add("" + issueList.get(i).getIssueSlipNo());
								rowData.add("" + issueList.get(i).getIssueDetailId());
								rowData.add("" + issueList.get(i).getItemId());

								rowData.add("" + issueList.get(i).getItemDesc());
								rowData.add("" + issueList.get(i).getItemIssueQty());
								rowData.add("" + issueList.get(i).getItemUom());
								rowData.add("" + issueList.get(i).getBatchNo());
								rowData.add("" + issueList.get(i).getItemRate());
								
								expoExcel.setRowData(rowData);
								exportToExcelList.add(expoExcel);

							}

							HttpSession session = request.getSession();
							session.setAttribute("exportExcelList", exportToExcelList);
							session.setAttribute("excelName", "GetPoHeaderList");

			}

			StockHeader stockHeader = rest.getForObject(Constants.url + "/getCurrentRunningMonthAndYear",
					StockHeader.class);
			String stockDate = stockHeader.getYear() + "-" + stockHeader.getMonth() + "-" + "01";
			model.addObject("stockDate", DateConvertor.convertToDMY(stockDate));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/getBomListFromAdminPanel", method = RequestMethod.GET)
	public ModelAndView getBomListFromAdminPanel(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/bomList");
		try {

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat disply = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			if (request.getParameter("fromDate") == null || request.getParameter("toDate") == null) {

				map.add("status", "0,1,2,3,4");
				GetBillOfMaterialList getBillOfMaterialList = rest.postForObject(
						Constants.adminUrl + "/getBOMListForStoreSWHeaderBmsAndStore", map,
						GetBillOfMaterialList.class);

				model.addObject("bomList", getBillOfMaterialList.getBillOfMaterialHeader());

				model.addObject("fromDate", disply.format(date));
				model.addObject("toDate", disply.format(date));

			} else {

				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				map.add("fromDate", DateConvertor.convertToYMD(fromDate));
				map.add("toDate", DateConvertor.convertToYMD(toDate));

				GetIssueHeader[] IssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderList", map,
						GetIssueHeader[].class);
				List<GetIssueHeader> issueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(IssueHeader));
				model.addObject("issueHeaderList", issueHeaderList);

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

			}

			StockHeader stockHeader = rest.getForObject(Constants.url + "/getCurrentRunningMonthAndYear",
					StockHeader.class);
			String stockDate = stockHeader.getYear() + "-" + stockHeader.getMonth() + "-" + "01";
			model.addObject("stockDate", DateConvertor.convertToDMY(stockDate));

			model.addObject("date", sf.format(date));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader();

	@RequestMapping(value = "/bomHeaderWithDetail/{reqId}", method = RequestMethod.GET)
	public ModelAndView bomHeaderWithDetail(@PathVariable int reqId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/bomHeaderWithDetail");

		try {

			issueDetailList = new ArrayList<IssueDetail>();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("reqId", reqId);
			billOfMaterialHeader = rest.postForObject(Constants.adminUrl + "/getBomdetailedListByIdAndRmType", map,
					BillOfMaterialHeader.class);

			String itemIds = new String();
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {

				itemIds = itemIds + "," + billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmId();

			}

			map = new LinkedMultiValueMap<String, Object>();
			map.add("itemIds", itemIds.substring(1, itemIds.length()));
			map.add("date", sf.format(date));
			MrnDetail[] mrnDetail = rest.postForObject(Constants.url + "getBatchByMultipleItemIds", map,
					MrnDetail[].class);
			batchList = new ArrayList<>(Arrays.asList(mrnDetail));
			model.addObject("billOfMaterialHeader", billOfMaterialHeader);
			model.addObject("batchList", batchList);

			StockHeader stockHeader = rest.getForObject(Constants.url + "/getCurrentRunningMonthAndYear",
					StockHeader.class);
			String stockDate = stockHeader.getYear() + "-" + stockHeader.getMonth() + "-" + "01";
			model.addObject("stockDateDDMMYYYY", DateConvertor.convertToDMY(stockDate));

			Dept[] Dept = rest.getForObject(Constants.url + "/getAllDeptByIsUsed", Dept[].class);
			List<Dept> deparmentList = new ArrayList<Dept>(Arrays.asList(Dept));
			model.addObject("deparmentList", deparmentList);

			model.addObject("date", sf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "/submitIssueByBomRequest", method = RequestMethod.POST)
	public String submitIssueByBomRequest(HttpServletRequest request, HttpServletResponse response) {

		try {

			String issueNo = request.getParameter("issueNo");
			String issueDate = request.getParameter("issueDate");
			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			// int acc = Integer.parseInt(request.getParameter("acc"));
			// int type = Integer.parseInt(request.getParameter("type"));
			String issueSlipNo = request.getParameter("issueSlipNo");

			System.out.println("issueDate " + issueDate);
			IssueHeader issueHeader = new IssueHeader();

			issueHeader.setIssueDate(issueDate);
			// DocumentBean docBean=null;
			try {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("docType", 2);
				map.add("date", issueDate);

				RestTemplate restTemplate = new RestTemplate();

				ErrorMessage errorMessage = restTemplate.postForObject(Constants.url + "generateIssueNoAndMrnNo", map,
						ErrorMessage.class);

				issueHeader.setIssueNo("" + errorMessage.getMessage());

				// docBean.getSubDocument().setCounter(docBean.getSubDocument().getCounter()+1);
			} catch (Exception e) {
				e.printStackTrace();
				issueHeader.setIssueNo("1");
			}

			for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {

				if (Float.parseFloat(request.getParameter(
						"issueQty" + billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getReqDetailId())) > 0) {

					float issueQty = Float.parseFloat(request.getParameter(
							"issueQty" + billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getReqDetailId()));
					int mrnDetailId = Integer.parseInt(request.getParameter(
							"batchId" + billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getReqDetailId()));
					IssueDetail issueDetail = new IssueDetail();
					issueDetail.setItemId(billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmId());
					issueDetail.setItemIssueQty(issueQty);
					issueDetail.setDeptId(deptId);
					issueDetail.setSubDeptId(subDeptId);
					issueDetail.setAccHead(1);
					issueDetail.setDelStatus(1);
					issueDetail.setMrnDetailId(mrnDetailId);
					issueDetail.setStatus(2);
					billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setRmIssueQty(
							billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmIssueQty() + issueQty);

					if (billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmIssueQty() == billOfMaterialHeader
							.getBillOfMaterialDetailed().get(i).getRmReqQty()) {
						billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setExInt1(2);
					} else if (billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmIssueQty() == 0) {
						billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setExInt1(0);
					} else {
						billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setExInt1(1);
					}

					try {
						if (billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getExVarchar1() == null) {

							billOfMaterialHeader.getBillOfMaterialDetailed().get(i)
									.setExVarchar1(String.valueOf(mrnDetailId));

						} else {

							billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setExVarchar1(
									billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getExVarchar1() + ","
											+ mrnDetailId);
						}

					} catch (Exception e) {

						billOfMaterialHeader.getBillOfMaterialDetailed().get(i)
								.setExVarchar1(String.valueOf(mrnDetailId));
					}
					issueDetailList.add(issueDetail);

				}
			}

			issueHeader.setDeleteStatus(1);

			issueHeader.setDeptId(deptId);
			issueHeader.setSubDeptId(subDeptId);
			issueHeader.setAccHead(1);
			issueHeader.setItemCategory(2);
			issueHeader.setIssueSlipNo(issueSlipNo);
			issueHeader.setStatus(2);

			String mrnDetailList = new String();
			String itemIds = new String();

			for (int i = 0; i < issueDetailList.size(); i++) {
				mrnDetailList = mrnDetailList + "," + issueDetailList.get(i).getMrnDetailId();
				itemIds = itemIds + "," + issueDetailList.get(i).getItemId();

			}

			mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());
			itemIds = itemIds.substring(1, itemIds.length());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("mrnDetailList", mrnDetailList);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
					MrnDetail[].class);

			map = new LinkedMultiValueMap<String, Object>();
			map.add("itemIds", itemIds);
			GetItem[] item = rest.postForObject(Constants.url + "/getItemByItemIds", map, GetItem[].class);
			List<GetItem> itemList = new ArrayList<GetItem>(Arrays.asList(item));

			updateMrnDetail = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

			for (int j = 0; j < issueDetailList.size(); j++) {
				for (int i = 0; i < updateMrnDetail.size(); i++) {
					if (updateMrnDetail.get(i).getMrnDetailId() == issueDetailList.get(j).getMrnDetailId()) {
						updateMrnDetail.get(i).setRemainingQty(
								updateMrnDetail.get(i).getRemainingQty() - issueDetailList.get(j).getItemIssueQty());
						updateMrnDetail.get(i).setIssueQty(
								updateMrnDetail.get(i).getIssueQty() + issueDetailList.get(j).getItemIssueQty());
						issueDetailList.get(j).setBatchNo(updateMrnDetail.get(i).getBatchNo());
						if (updateMrnDetail.get(i).getIssueQty() > 0) {
							updateMrnDetail.get(i).setChalanQty(1);
						} else {
							updateMrnDetail.get(i).setChalanQty(0);
						}
					}
				}
			}

			for (int j = 0; j < issueDetailList.size(); j++) {
				for (int i = 0; i < itemList.size(); i++) {
					if (itemList.get(i).getItemId() == issueDetailList.get(j).getItemId()) {
						issueDetailList.get(j).setItemGroupId(itemList.get(i).getGrpId());
					}
				}
			}
			issueHeader.setIssueDetailList(issueDetailList);

			System.out.println("issueHeader  " + issueHeader);

			IssueHeader res = rest.postForObject(Constants.url + "/saveIssueHeaderAndDetail", issueHeader,
					IssueHeader.class);
			if (res != null) {
				try {

					// SubDocument subDocRes = rest.postForObject(Constants.url + "/saveSubDoc",
					// docBean.getSubDocument(), SubDocument.class);
					MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
							MrnDetail[].class);

					System.out.println("billOfMaterialHeader " + billOfMaterialHeader);
					ErrorMessage updateBom = rest.postForObject(Constants.adminUrl + "/updateBomWhileIssueFromStore",
							billOfMaterialHeader, ErrorMessage.class);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			System.out.println(res);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/getBomListFromAdminPanel";
	}

	@RequestMapping(value = "/getIssueDateBetweenDate", method = RequestMethod.GET)
	@ResponseBody
	public List<GetIssueHeader> getIssueDateBetweenDate(HttpServletRequest request, HttpServletResponse response) {

		List<GetIssueHeader> issueHeaderList = new ArrayList<>();

		try {
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			GetIssueHeader[] IssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderList", map,
					GetIssueHeader[].class);
			issueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(IssueHeader));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueHeaderList;
	}

	@RequestMapping(value = "/deleteIssueHeader/{issueId}", method = RequestMethod.GET)
	public String deleteIssueHeader(@PathVariable int issueId, HttpServletRequest request,
			HttpServletResponse response) {

		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("issueId", issueId);
			GetIssueHeader getIssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderAndDetailById", map,
					GetIssueHeader.class);
			String mrnDetailList = new String();

			for (int i = 0; i < getIssueHeader.getIssueDetailList().size(); i++) {
				mrnDetailList = mrnDetailList + "," + getIssueHeader.getIssueDetailList().get(i).getMrnDetailId();
			}
			try {
				mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());
			} catch (Exception e) {
				mrnDetailList = "0";
			}
			map = new LinkedMultiValueMap<>();
			map.add("mrnDetailList", mrnDetailList);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
					MrnDetail[].class);
			List<MrnDetail> updateMrnDetail = new ArrayList<>(Arrays.asList(MrnDetail));

			for (int i = 0; i < getIssueHeader.getIssueDetailList().size(); i++) {
				for (int j = 0; j < updateMrnDetail.size(); j++) {
					if (updateMrnDetail.get(j).getMrnDetailId() == getIssueHeader.getIssueDetailList().get(i)
							.getMrnDetailId()) {
						updateMrnDetail.get(j).setRemainingQty(updateMrnDetail.get(j).getRemainingQty()
								+ getIssueHeader.getIssueDetailList().get(i).getItemIssueQty());
						updateMrnDetail.get(j).setIssueQty(updateMrnDetail.get(j).getIssueQty()
								- getIssueHeader.getIssueDetailList().get(i).getItemIssueQty());
						if (updateMrnDetail.get(i).getIssueQty() > 0) {
							updateMrnDetail.get(i).setChalanQty(1);
						} else {
							updateMrnDetail.get(i).setChalanQty(0);
						}
					}
				}
			}

			map = new LinkedMultiValueMap<String, Object>();
			map.add("issueId", issueId);
			ErrorMessage errorMessage = rest.postForObject(Constants.url + "/deleteHeaderAndDetail", map,
					ErrorMessage.class);
			System.out.println(errorMessage);

			if (errorMessage.isError() == false) {
				MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
						MrnDetail[].class);
				System.out.println(update);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/issueList";
	}

	List<GetIssueDetail> issueDetailEditList = new ArrayList<>();
	List<GetIssueDetail> originalIssueDetail = new ArrayList<>();
	GetIssueHeader getIssueHeader = new GetIssueHeader();
	List<MrnDetail> batchListInEditIssue = new ArrayList<MrnDetail>();

	@RequestMapping(value = "/editIssueHeader/{issueId}", method = RequestMethod.GET)
	public ModelAndView editIssueHeader(@PathVariable int issueId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/editIssueItem");

		try {
			originalIssueDetail = new ArrayList<>();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("issueId", issueId);
			getIssueHeader = rest.postForObject(Constants.url + "/getIssueHeaderAndDetailById", map,
					GetIssueHeader.class);
			model.addObject("getIssueHeader", getIssueHeader);

			issueDetailEditList = getIssueHeader.getIssueDetailList();

			/*
			 * for(int i = 0 ; i< issueDetailEditList.size() ; i++) {
			 * originalIssueDetail.add(issueDetailEditList.get(i)); }
			 */

			Dept[] Dept = rest.getForObject(Constants.url + "/getAllDeptByIsUsed", Dept[].class);
			List<Dept> deparmentList = new ArrayList<Dept>(Arrays.asList(Dept));
			model.addObject("deparmentList", deparmentList);

			/*
			 * GetItemGroup[] itemGroupList = rest.getForObject(Constants.url +
			 * "/getAllItemGroupByIsUsed", GetItemGroup[].class);
			 * model.addObject("itemGroupList", itemGroupList);
			 */

			Category[] category = rest.getForObject(Constants.url + "/getAllCategoryByIsUsed", Category[].class);
			List<Category> categoryList = new ArrayList<Category>(Arrays.asList(category));
			model.addObject("categoryList", categoryList);

			AccountHead[] accountHead = rest.getForObject(Constants.url + "/getAllAccountHeadByIsUsed",
					AccountHead[].class);
			List<AccountHead> accountHeadList = new ArrayList<AccountHead>(Arrays.asList(accountHead));

			model.addObject("accountHeadList", accountHeadList);

			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);

			StockHeader stockHeader = rest.getForObject(Constants.url + "/getCurrentRunningMonthAndYear",
					StockHeader.class);

			System.out.println(" stock Date: " + stockHeader.getDate());

			model.addObject("stockDateDDMMYYYY", DateConvertor.convertToDMY(stockHeader.getDate()));
			model.addObject("date", DateConvertor.convertToYMD(getIssueHeader.getIssueDate()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "/qtyValidationFromBatchInEdit", method = RequestMethod.GET)
	@ResponseBody
	public MrnDetail qtyValidationFromBatchInEdit(HttpServletRequest request, HttpServletResponse response) {

		MrnDetail mrnDetail = new MrnDetail();
		try {
			int mrnDetailId = Integer.parseInt(request.getParameter("batchNo"));

			for (int i = 0; i < batchListInEditIssue.size(); i++) {
				if (batchListInEditIssue.get(i).getMrnDetailId() == mrnDetailId) {
					mrnDetail = batchListInEditIssue.get(i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mrnDetail;
	}

	@RequestMapping(value = "/getBatchByItemIdInIssueEdit", method = RequestMethod.GET)
	@ResponseBody
	public List<MrnDetail> getBatchByItemIdInIssueEdit(HttpServletRequest request, HttpServletResponse response) {

		try {
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int type = Integer.parseInt(request.getParameter("type"));
			String date = request.getParameter("date");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("itemId", itemId);
			map.add("date", date);
			map.add("type", type);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "getBatchByItemId", map, MrnDetail[].class);

			batchListInEditIssue = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

			for (int i = 0; i < issueDetailEditList.size(); i++) {
				for (int j = 0; j < batchListInEditIssue.size(); j++) {

					if (batchListInEditIssue.get(j).getMrnDetailId() == issueDetailEditList.get(i).getMrnDetailId()
							&& issueDetailEditList.get(i).getIssueDetailId() == 0) {
						System.out.println("In if ");
						batchListInEditIssue.get(j).setRemainingQty(batchListInEditIssue.get(j).getRemainingQty()
								- issueDetailEditList.get(i).getItemIssueQty());
						batchListInEditIssue.get(j).setIssueQty(batchListInEditIssue.get(j).getIssueQty()
								+ issueDetailEditList.get(i).getItemIssueQty());
						break;
					} else if (batchListInEditIssue.get(j).getMrnDetailId() == issueDetailEditList.get(i)
							.getMrnDetailId() && issueDetailEditList.get(i).getDelStatus() == 0) {
						System.out.println("In if else");
						batchListInEditIssue.get(j).setRemainingQty(batchListInEditIssue.get(j).getRemainingQty()
								+ issueDetailEditList.get(i).getItemIssueQty());
						batchListInEditIssue.get(j).setIssueQty(batchListInEditIssue.get(j).getIssueQty()
								- issueDetailEditList.get(i).getItemIssueQty());
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return batchListInEditIssue;
	}

	@RequestMapping(value = "/addItmeInEditIssueList", method = RequestMethod.GET)
	@ResponseBody
	public List<GetIssueDetail> addItmeInEditIssueList(HttpServletRequest request, HttpServletResponse response) {

		try {
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int mrnDetailId = Integer.parseInt(request.getParameter("batchNo"));
			float qty = Float.parseFloat(request.getParameter("qty"));
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			int acc = Integer.parseInt(request.getParameter("acc"));
			String editIndex = request.getParameter("editIndex");
			String itemName = request.getParameter("itemName");
			String groupName = request.getParameter("groupName");
			String deptName = request.getParameter("deptName");
			String subDeptName = request.getParameter("subDeptName");
			String accName = request.getParameter("accName");

			if (editIndex.equalsIgnoreCase("") || editIndex.equalsIgnoreCase(null)) {
				GetIssueDetail issueDetail = new GetIssueDetail();
				issueDetail.setItemId(itemId);
				issueDetail.setItemIssueQty(qty);
				issueDetail.setDeptId(deptId);
				issueDetail.setSubDeptId(subDeptId);
				issueDetail.setAccHead(acc);
				issueDetail.setItemCode(itemName);
				issueDetail.setGrpCode(groupName);
				issueDetail.setDeptCode(deptName);
				issueDetail.setSubDeptCode(subDeptName);
				issueDetail.setAccHeadDesc(accName);
				issueDetail.setDelStatus(1);
				issueDetail.setStatus(2);

				for (int i = 0; i < itemListbyCatId.size(); i++) {
					if (itemListbyCatId.get(i).getItemId() == itemId) {

						issueDetail.setItemGroupId(itemListbyCatId.get(i).getGrpId());
					}
				}

				for (int i = 0; i < batchListInEditIssue.size(); i++) {
					if (batchListInEditIssue.get(i).getMrnDetailId() == mrnDetailId) {
						issueDetail.setBatchNo(batchListInEditIssue.get(i).getBatchNo());
						issueDetail.setMrnDetailId(mrnDetailId);
					}
				}
				issueDetailEditList.add(issueDetail);
			} else {
				int index = Integer.parseInt(editIndex);
				issueDetailEditList.get(index).setItemId(itemId);
				issueDetailEditList.get(index).setItemIssueQty(qty);
				issueDetailEditList.get(index).setItemGroupId(groupId);
				issueDetailEditList.get(index).setDeptId(deptId);
				issueDetailEditList.get(index).setSubDeptId(subDeptId);
				issueDetailEditList.get(index).setAccHead(acc);
				issueDetailEditList.get(index).setItemCode(itemName);
				issueDetailEditList.get(index).setGrpCode(groupName);
				issueDetailEditList.get(index).setDeptCode(deptName);
				issueDetailEditList.get(index).setSubDeptCode(subDeptName);
				issueDetailEditList.get(index).setAccHeadDesc(accName);
				for (int i = 0; i < batchListInEditIssue.size(); i++) {
					if (batchListInEditIssue.get(i).getMrnDetailId() == mrnDetailId) {
						issueDetailEditList.get(index).setBatchNo(batchListInEditIssue.get(i).getBatchNo());
						issueDetailEditList.get(index).setMrnDetailId(mrnDetailId);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetailEditList;
	}

	@RequestMapping(value = "/editItemInEditIssueList", method = RequestMethod.GET)
	@ResponseBody
	public GetIssueDetail editItemInEditIssueList(HttpServletRequest request, HttpServletResponse response) {

		GetIssueDetail issueDetail = new GetIssueDetail();

		try {
			int index = Integer.parseInt(request.getParameter("index"));

			issueDetail = issueDetailEditList.get(index);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetail;
	}

	@RequestMapping(value = "/deleteItemFromEditIssueList", method = RequestMethod.GET)
	@ResponseBody
	public List<GetIssueDetail> deleteItemFromEditIssueList(HttpServletRequest request, HttpServletResponse response) {

		try {
			int index = Integer.parseInt(request.getParameter("index"));

			if (issueDetailEditList.get(index).getIssueDetailId() == 0) {
				issueDetailEditList.remove(index);
			} else {
				issueDetailEditList.get(index).setDelStatus(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return issueDetailEditList;
	}

	@RequestMapping(value = "/submitEditIssueReciept", method = RequestMethod.POST)
	public String submitEditIssueReciept(HttpServletRequest request, HttpServletResponse response) {

		try {

			String issueNo = request.getParameter("issueNo");
			String issueDate = request.getParameter("issueDate");

			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			int acc = Integer.parseInt(request.getParameter("acc"));
			String issueSlipNo = request.getParameter("issueSlipNo");

			getIssueHeader.setIssueDate(issueDate);
			// getIssueHeader.setIssueNo(issueNo);
			getIssueHeader.setDeleteStatus(1);
			getIssueHeader.setDeptId(deptId);
			getIssueHeader.setSubDeptId(subDeptId);
			getIssueHeader.setAccHead(acc);
			getIssueHeader.setIssueSlipNo(issueSlipNo);
			getIssueHeader.setIssueDetailList(issueDetailEditList);

			String mrnDetailList = new String();

			for (int i = 0; i < issueDetailEditList.size(); i++) {
				mrnDetailList = mrnDetailList + "," + issueDetailEditList.get(i).getMrnDetailId();
				issueDetailEditList.get(i).setDeptId(deptId);
				issueDetailEditList.get(i).setSubDeptId(subDeptId);
				issueDetailEditList.get(i).setAccHead(acc);
			}

			mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());
			System.out.println("getIssueHeader      " + getIssueHeader);
			IssueHeader res = rest.postForObject(Constants.url + "/saveIssueHeaderAndDetail", getIssueHeader,
					IssueHeader.class);
			System.out.println(res);

			if (res != null) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
				map.add("mrnDetailList", mrnDetailList);
				MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
						MrnDetail[].class);

				List<MrnDetail> updateMrnDetail = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

				for (int j = 0; j < issueDetailEditList.size(); j++) {
					for (int i = 0; i < updateMrnDetail.size(); i++) {
						if (updateMrnDetail.get(i).getMrnDetailId() == issueDetailEditList.get(j).getMrnDetailId()
								&& issueDetailEditList.get(j).getDelStatus() == 0) {
							System.out.println("in if ");
							updateMrnDetail.get(i).setRemainingQty(updateMrnDetail.get(i).getRemainingQty()
									+ issueDetailEditList.get(j).getItemIssueQty());
							updateMrnDetail.get(i).setIssueQty(updateMrnDetail.get(i).getIssueQty()
									- issueDetailEditList.get(j).getItemIssueQty());
							if (updateMrnDetail.get(i).getIssueQty() > 0) {
								updateMrnDetail.get(i).setChalanQty(1);
							} else {
								updateMrnDetail.get(i).setChalanQty(0);
							}
							break;
						} else if (updateMrnDetail.get(i).getMrnDetailId() == issueDetailEditList.get(j)
								.getMrnDetailId() && issueDetailEditList.get(j).getIssueDetailId() == 0) {
							System.out.println("in else if ");
							updateMrnDetail.get(i).setRemainingQty(updateMrnDetail.get(i).getRemainingQty()
									- issueDetailEditList.get(j).getItemIssueQty());
							updateMrnDetail.get(i).setIssueQty(updateMrnDetail.get(i).getIssueQty()
									+ issueDetailEditList.get(j).getItemIssueQty());
							if (updateMrnDetail.get(i).getIssueQty() > 0) {
								updateMrnDetail.get(i).setChalanQty(1);
							} else {
								updateMrnDetail.get(i).setChalanQty(0);
							}
							break;
						}
					}
				}

				System.out.println("updateMrnDetail      " + updateMrnDetail);
				MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
						MrnDetail[].class);
				System.out.println(update);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/issueList";
	}

	@RequestMapping(value = "/firstApproveIssue", method = RequestMethod.GET)
	public ModelAndView firstApprovePurchaseOrder(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/approveIssue");
		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("status", "0,1");
			GetIssueHeader[] getIssueHeader = rest.postForObject(Constants.url + "getIssueHeaderByStatus", map,
					GetIssueHeader[].class);
			List<GetIssueHeader> getIssueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(getIssueHeader));
			model.addObject("approve", 1);
			model.addObject("getIssueHeaderList", getIssueHeaderList);

			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/secondApproveIssue", method = RequestMethod.GET)
	public ModelAndView secondApprovePurchaseOrder(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/approveIssue");
		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("status", "1");
			GetIssueHeader[] getIssueHeader = rest.postForObject(Constants.url + "getIssueHeaderByStatus", map,
					GetIssueHeader[].class);
			List<GetIssueHeader> getIssueHeaderList = new ArrayList<GetIssueHeader>(Arrays.asList(getIssueHeader));
			model.addObject("approve", 2);
			model.addObject("getIssueHeaderList", getIssueHeaderList);

			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	GetIssueHeader issueForApprove = new GetIssueHeader();

	@RequestMapping(value = "/approveIssueDetail/{issueId}/{approve}", method = RequestMethod.GET)
	public ModelAndView approvePoDetail(@PathVariable int issueId, @PathVariable int approve,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/approveIssueDetail");
		try {

			issueForApprove = new GetIssueHeader();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("issueId", issueId);
			issueForApprove = rest.postForObject(Constants.url + "/getIssueHeaderAndDetailById", map,
					GetIssueHeader.class);

			model.addObject("issueForApprove", issueForApprove);
			model.addObject("approve", approve);

			Type[] type = rest.getForObject(Constants.url + "/getAlltype", Type[].class);
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(type));
			model.addObject("typeList", typeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/submitIssueApprove", method = RequestMethod.POST)
	public String submitMrnApprove(HttpServletRequest request, HttpServletResponse response) {

		String ret = null;
		int approve = Integer.parseInt(request.getParameter("approve"));
		try {

			String issueDetalId = new String();
			int issueId = 0;
			int status = 1;

			if (approve == 1) {

				issueForApprove.setStatus(1);
				issueId = issueForApprove.getIssueId();
				String[] checkbox = request.getParameterValues("select_to_approve");
				status = 1;
				for (int i = 0; i < checkbox.length; i++) {

					for (int j = 0; j < issueForApprove.getIssueDetailList().size(); j++) {

						if (Integer.parseInt(checkbox[i]) == issueForApprove.getIssueDetailList().get(j)
								.getIssueDetailId()) {
							issueForApprove.getIssueDetailList().get(j).setStatus(1);
							issueDetalId = issueDetalId + ","
									+ issueForApprove.getIssueDetailList().get(j).getIssueDetailId();
							break;
						}
					}
				}

			} else if (approve == 2) {

				issueForApprove.setStatus(2);
				issueId = issueForApprove.getIssueId();
				String[] checkbox = request.getParameterValues("select_to_approve");
				status = 2;
				for (int i = 0; i < checkbox.length; i++) {

					for (int j = 0; j < issueForApprove.getIssueDetailList().size(); j++) {

						if (Integer.parseInt(checkbox[i]) == issueForApprove.getIssueDetailList().get(j)
								.getIssueDetailId()) {
							issueForApprove.getIssueDetailList().get(j).setStatus(2);
							issueDetalId = issueDetalId + ","
									+ issueForApprove.getIssueDetailList().get(j).getIssueDetailId();
							break;
						}
					}
				}

			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("issueId", issueId);
			map.add("issueDetalId", issueDetalId.substring(1, issueDetalId.length()));
			map.add("status", status);
			System.out.println("map " + map);
			ErrorMessage approved = rest.postForObject(Constants.url + "/updateStatusWhileIssueApprov", map,
					ErrorMessage.class);

			if (approve == 2 && approved.isError() == false) {

				issueForApprove = new GetIssueHeader();
				map = new LinkedMultiValueMap<String, Object>();
				map.add("issueId", issueId);
				issueForApprove = rest.postForObject(Constants.url + "/getIssueHeaderAndDetailById", map,
						GetIssueHeader.class);

				String mrnDetailList = new String();

				for (int i = 0; i < issueForApprove.getIssueDetailList().size(); i++) {
					mrnDetailList = mrnDetailList + "," + issueForApprove.getIssueDetailList().get(i).getMrnDetailId();
				}

				mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());

				map = new LinkedMultiValueMap<>();
				map.add("mrnDetailList", mrnDetailList);
				MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
						MrnDetail[].class);
				List<MrnDetail> updateMrnDetail = new ArrayList<>(Arrays.asList(MrnDetail));

				for (int i = 0; i < issueForApprove.getIssueDetailList().size(); i++) {
					for (int j = 0; j < updateMrnDetail.size(); j++) {
						if (updateMrnDetail.get(j).getMrnDetailId() == issueForApprove.getIssueDetailList().get(i)
								.getMrnDetailId() && (issueForApprove.getIssueDetailList().get(i).getStatus() != 2)) {
							updateMrnDetail.get(j).setRemainingQty(updateMrnDetail.get(j).getRemainingQty()
									+ issueForApprove.getIssueDetailList().get(i).getItemIssueQty());
							updateMrnDetail.get(j).setIssueQty(updateMrnDetail.get(j).getIssueQty()
									- issueForApprove.getIssueDetailList().get(i).getItemIssueQty());
						}
					}
				}

				MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
						MrnDetail[].class);
				System.out.println(update);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (approve == 1) {
			ret = "redirect:/firstApproveIssue";
		} else {
			ret = "redirect:/secondApproveIssue";
		}

		return ret;
	}

	@RequestMapping(value = "/genrateNo", method = RequestMethod.GET)
	@ResponseBody
	public ErrorMessage getInvoiceNo(HttpServletRequest request, HttpServletResponse response) {

		String invNo = "";
		ErrorMessage errorMessage = new ErrorMessage();
		try {
			int catId = Integer.parseInt(request.getParameter("catId"));
			int docId = Integer.parseInt(request.getParameter("docId"));
			String date = request.getParameter("date");
			int typeId = Integer.parseInt(request.getParameter("typeId"));

			if (date == "") {
				Date currDate = new Date();
				date = new SimpleDateFormat("yyyy-MM-dd").format(currDate);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("docType", docId);
			map.add("date", DateConvertor.convertToYMD(date));

			RestTemplate restTemplate = new RestTemplate();

			errorMessage = restTemplate.postForObject(Constants.url + "generateIssueNoAndMrnNo", map,
					ErrorMessage.class);

			System.out.println(errorMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return errorMessage;
	}

	public List<BillOfMaterialHeader> getbomList = new ArrayList<BillOfMaterialHeader>();
	private List<BillOfMaterialHeader> getBOMListall;
	List<BillOfMaterialDetailed> bomwithdetaild = new ArrayList<BillOfMaterialDetailed>();

	@RequestMapping(value = "/getBomListBmsToStores", method = RequestMethod.GET)
	public ModelAndView getBomLists(HttpServletRequest request, HttpServletResponse response) {
		Constants.mainAct = 8;
		Constants.subAct = 47;

		ModelAndView model = new ModelAndView("issue/bmsToStoreBomHeader");//
		getbomList = new ArrayList<BillOfMaterialHeader>();
		HttpSession session = request.getSession();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		RestTemplate rest = new RestTemplate();
		int fromDept = 0;
		try {
			String settingKey = new String();
			settingKey = "BMS" + "," + "STORE";
			map.add("settingKeyList", settingKey);

			// web Service to get Dept Name And Dept Id for bom toDept and toDeptId

			FrItemStockConfigureList settingList = rest.postForObject(Constants.adminUrl + "getDeptSettingValue", map,
					FrItemStockConfigureList.class);

			int toDeptId = 0;

			List<FrItemStockConfigure> settingKeyList = settingList.getFrItemStockConfigure();

			for (int i = 0; i < settingKeyList.size(); i++) {

				if (settingKeyList.get(i).getSettingKey().equalsIgnoreCase("BMS")) {

					fromDept = settingKeyList.get(i).getSettingValue();

				}
				if (settingKeyList.get(i).getSettingKey().equalsIgnoreCase("STORE")) {

					toDeptId = settingKeyList.get(i).getSettingValue();

				}

			}

			map = new LinkedMultiValueMap<String, Object>();
			map.add("fromDept", fromDept);
			map.add("toDept", toDeptId);
			map.add("status", "0");
			System.out.println("map " + map);
			GetBillOfMaterialList getBillOfMaterialList = rest
					.postForObject(Constants.adminUrl + "/getBOMHeaderBmsAndStore", map, GetBillOfMaterialList.class);

			getbomList = getBillOfMaterialList.getBillOfMaterialHeader();

		} catch (Exception e) {
			System.out.println("error in controller " + e.getMessage());
		}
		model.addObject("settingvalue", fromDept);
		model.addObject("deptId", 17);
		model.addObject("getbomList", getbomList);
		model.addObject("flag", 1);
		return model;

	}

	@RequestMapping(value = "/getBomListBmsToStoreWithDate", method = RequestMethod.GET)
	@ResponseBody
	public List<BillOfMaterialHeader> getBomAllListWithDate(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * Constants.mainAct = 17; Constants.subAct=184;
		 */
		System.out.println("in controller");
		String frmdate = request.getParameter("from_date");
		String todate = request.getParameter("to_date");

		try {

			System.out.println("in getMixingListWithDate   " + frmdate + todate);
			String frdate = DateConvertor.convertToYMD(frmdate);
			String tdate = DateConvertor.convertToYMD(todate);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			String settingKey = new String();
			settingKey = "STORE";
			map.add("settingKeyList", settingKey);

			RestTemplate rest = new RestTemplate();
			FrItemStockConfigureList settingList = rest.postForObject(Constants.adminUrl + "getDeptSettingValue", map,
					FrItemStockConfigureList.class);

			map = new LinkedMultiValueMap<String, Object>();
			String settingKey2 = new String();
			settingKey2 = "BMS";
			map.add("settingKeyList", settingKey2);

			FrItemStockConfigureList settingList2 = rest.postForObject(Constants.adminUrl + "getDeptSettingValue", map,
					FrItemStockConfigureList.class);

			map = new LinkedMultiValueMap<String, Object>();
			map.add("fromDept", settingList2.getFrItemStockConfigure().get(0).getSettingValue());
			map.add("toDept", settingList.getFrItemStockConfigure().get(0).getSettingValue());
			map.add("frmdate", frdate);
			map.add("todate", tdate);

			// System.out.println("in getBOMListWithDate "+frdate+tdate);

			GetBillOfMaterialList getBillOfMaterialList = rest.postForObject(
					Constants.adminUrl + "/getBOMHeaderListBmsAndStore", map, GetBillOfMaterialList.class);
			getBOMListall = getBillOfMaterialList.getBillOfMaterialHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getBOMListall;

	}

	@RequestMapping(value = "/viewDetailBOMBmsToStoreRequest", method = RequestMethod.GET)
	public ModelAndView viewDetailBOMRequest(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * Constants.mainAct = 17; Constants.subAct=184;
		 */
		ModelAndView model = new ModelAndView("issue/bmsToStoreBomDetail");

		HttpSession session = request.getSession();

		try {

			int reqId = Integer.parseInt(request.getParameter("reqId"));
			System.out.println(reqId);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("reqId", reqId);
			RestTemplate rest = new RestTemplate();
			billOfMaterialHeader = rest.postForObject(Constants.adminUrl + "/getDetailedwithreqId", map,
					BillOfMaterialHeader.class);
			bomwithdetaild = billOfMaterialHeader.getBillOfMaterialDetailed();

			String ids = "-1";

			for (int i = 0; i < bomwithdetaild.size(); i++) {

				ids = ids + "," + bomwithdetaild.get(i).getRmId();
			}

			if (billOfMaterialHeader.getStatus() == 0) {

				map = new LinkedMultiValueMap<String, Object>();
				map.add("itemIds", ids);
				map.add("date", DateConvertor.convertToYMD(billOfMaterialHeader.getReqDate()));
				MrnDetail[] mrnDetail = rest.postForObject(Constants.url + "/getBatchByItemIds", map,
						MrnDetail[].class);
				batchList = new ArrayList<>(Arrays.asList(mrnDetail));
				model.addObject("batchList", batchList);

				for (int i = 0; i < bomwithdetaild.size(); i++) {

					float availableQty = 0;

					for (int j = 0; j < batchList.size(); j++) {

						if (bomwithdetaild.get(i).getRmId() == batchList.get(j).getRejectRemark()
								&& batchList.get(j).getRemainingQty() > 0) {

							availableQty = availableQty + batchList.get(j).getRemainingQty();

						}
					}

					bomwithdetaild.get(i).setTotalAvaiQty(availableQty);

				}

				Dept[] Dept = rest.getForObject(Constants.url + "/getAllDeptByIsUsed", Dept[].class);
				List<Dept> deparmentList = new ArrayList<Dept>(Arrays.asList(Dept));
				model.addObject("deparmentList", deparmentList);

				AccountHead[] accountHead = rest.getForObject(Constants.url + "/getAllAccountHeadByIsUsed",
						AccountHead[].class);
				List<AccountHead> accountHeadList = new ArrayList<AccountHead>(Arrays.asList(accountHead));

				model.addObject("accountHeadList", accountHeadList);
			}

			model.addObject("billOfMaterialHeader", billOfMaterialHeader);
			model.addObject("bomwithdetaild", bomwithdetaild);
			model.addObject("flag", 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/approvedBomFromStore", method = RequestMethod.POST)
	public String approvedBom(HttpServletRequest request, HttpServletResponse response) {
		Constants.mainAct = 17;
		Constants.subAct = 184;

		try {
			Date date = new Date();
			SimpleDateFormat ymdSDF = new SimpleDateFormat("dd-MM-yyyy");

			HttpSession session = request.getSession();
			UserResponse userResponse = (UserResponse) session.getAttribute("UserDetail");

			for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {

				String issue_qty = request.getParameter(
						"issue_qty" + billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getReqDetailId());

				if (issue_qty != null) {
					// System.out.println("issue_qty Qty :"+issue_qty);
					float issueqty = Float.parseFloat(issue_qty);
					billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setRmIssueQty(issueqty);
					// System.out.println("productionQty :"+issueqty);
				} else {
					billOfMaterialHeader.getBillOfMaterialDetailed().get(i).setRmIssueQty(0);
				}

			}
			billOfMaterialHeader.setStatus(1);
			billOfMaterialHeader.setApprovedUserId(userResponse.getUser().getId());
			billOfMaterialHeader.setApprovedDate(ymdSDF.format(date));

			System.out.println(billOfMaterialHeader.toString());

			RestTemplate rest = new RestTemplate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		try {

			String issueNo = request.getParameter("issueNo");
			String issueDate = request.getParameter("issueDate");
			int deptId = Integer.parseInt(request.getParameter("deptId"));
			int subDeptId = Integer.parseInt(request.getParameter("subDeptId"));
			int acc = Integer.parseInt(request.getParameter("acc"));
			int type = Integer.parseInt(request.getParameter("type"));
			String issueSlipNo = request.getParameter("issueSlipNo");

			System.out.println("issueDate " + issueDate);
			IssueHeader issueHeader = new IssueHeader();

			issueHeader.setIssueDate(issueDate);
			// DocumentBean docBean=null;
			try {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("docType", 2);
				map.add("date", issueDate);

				RestTemplate restTemplate = new RestTemplate();

				ErrorMessage errorMessage = restTemplate.postForObject(Constants.url + "generateIssueNoAndMrnNo", map,
						ErrorMessage.class);

				issueHeader.setIssueNo("" + errorMessage.getMessage());

				// docBean.getSubDocument().setCounter(docBean.getSubDocument().getCounter()+1);
			} catch (Exception e) {
				e.printStackTrace();
				issueHeader.setIssueNo("1");
			}
			// issueHeader.setIssueNo(issueNo);
			issueHeader.setDeleteStatus(1);
			issueHeader.setDeptId(deptId);
			issueHeader.setSubDeptId(subDeptId);
			issueHeader.setAccHead(acc);
			issueHeader.setItemCategory(type);
			issueHeader.setIssueSlipNo(issueSlipNo);
			issueHeader.setStatus(2);

			String mrnDetailList = new String();

			for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {

				if (billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmIssueQty() > 0) {

					float issueQty = billOfMaterialHeader.getBillOfMaterialDetailed().get(i).getRmIssueQty();

					for (int j = 0; j < batchList.size(); j++) {

						if (batchList.get(j).getRejectRemark() == billOfMaterialHeader.getBillOfMaterialDetailed()
								.get(i).getRmId() && batchList.get(j).getRemainingQty() > 0) {

							int flag = 0;

							IssueDetail issueDetail = new IssueDetail();
							issueDetail.setItemId(batchList.get(j).getItemId());
							issueDetail.setDeptId(deptId);
							issueDetail.setSubDeptId(subDeptId);
							issueDetail.setAccHead(acc);
							issueDetail.setDelStatus(1);
							issueDetail.setBatchNo(batchList.get(j).getBatchNo());
							issueDetail.setMrnDetailId(batchList.get(j).getMrnDetailId());
							issueDetail.setItemGroupId((int) batchList.get(j).getRejectQty());
							if (issueQty > batchList.get(j).getRemainingQty()) {
								issueDetail.setItemIssueQty(batchList.get(j).getRemainingQty());
								issueQty = issueQty - batchList.get(j).getRemainingQty();
								
							} else {
								issueDetail.setItemIssueQty(issueQty);
								flag = 1;
							}
							issueDetailList.add(issueDetail);

							if (flag == 1) {
								break;
							}

						}
					}

				}

			}
			issueHeader.setIssueDetailList(issueDetailList);

			System.out.println("issueDetailList  " + issueDetailList);

			for (int i = 0; i < issueDetailList.size(); i++) {
				mrnDetailList = mrnDetailList + "," + issueDetailList.get(i).getMrnDetailId();
				issueDetailList.get(i).setDeptId(deptId);
				issueDetailList.get(i).setSubDeptId(subDeptId);
				issueDetailList.get(i).setAccHead(acc);
				issueDetailList.get(i).setStatus(2);
			}

			mrnDetailList = mrnDetailList.substring(1, mrnDetailList.length());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("mrnDetailList", mrnDetailList);
			System.out.println("map  " + map);
			MrnDetail[] MrnDetail = rest.postForObject(Constants.url + "/getMrnDetailListByMrnDetailId", map,
					MrnDetail[].class);

			updateMrnDetail = new ArrayList<MrnDetail>(Arrays.asList(MrnDetail));

			for (int j = 0; j < issueDetailList.size(); j++) {
				for (int i = 0; i < updateMrnDetail.size(); i++) {
					if (updateMrnDetail.get(i).getMrnDetailId() == issueDetailList.get(j).getMrnDetailId()) {
						updateMrnDetail.get(i).setRemainingQty(
								updateMrnDetail.get(i).getRemainingQty() - issueDetailList.get(j).getItemIssueQty());
						updateMrnDetail.get(i).setIssueQty(
								updateMrnDetail.get(i).getIssueQty() + issueDetailList.get(j).getItemIssueQty());
						if (updateMrnDetail.get(i).getIssueQty() > 0) {
							updateMrnDetail.get(i).setChalanQty(1);
						} else {
							updateMrnDetail.get(i).setChalanQty(0);
						}
					}
				}
			}

			Info info = rest.postForObject(Constants.adminUrl + "saveBom", billOfMaterialHeader, Info.class);
			IssueHeader res = rest.postForObject(Constants.url + "/saveIssueHeaderAndDetail", issueHeader,
					IssueHeader.class);
			if (res != null) {
				try {

					// SubDocument subDocRes = rest.postForObject(Constants.url + "/saveSubDoc",
					// docBean.getSubDocument(), SubDocument.class);

				} catch (Exception e) {
					e.printStackTrace();
				}

				MrnDetail[] update = rest.postForObject(Constants.url + "/updateMrnDetailList", updateMrnDetail,
						MrnDetail[].class);
			}
			System.out.println(res);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(info);

		return "redirect:/getBomListBmsToStores";
	}

	/*********************************************************************************/
	//02-03-2020 Mahendra
	@RequestMapping(value = "/getSelIssueList", method = RequestMethod.GET)
	public ModelAndView getSelIssueList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("issue/issueExcelList");
		try {

			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat disply = new SimpleDateFormat("dd-MM-yyyy");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			if (request.getParameter("fromDate") == null || request.getParameter("toDate") == null) {

				map.add("fromDate", sf.format(date));
				map.add("toDate", sf.format(date));

				GetIssueExcelList[] IssueHeader = rest.postForObject(Constants.url + "/getIssueExcelList", map,
						GetIssueExcelList[].class);
				List<GetIssueExcelList> issueHeaderList = new ArrayList<GetIssueExcelList>(Arrays.asList(IssueHeader));
				model.addObject("issueHeaderList", issueHeaderList);

				model.addObject("fromDate", disply.format(date));
				model.addObject("toDate", disply.format(date));

			} else {

				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				map.add("fromDate", DateConvertor.convertToYMD(fromDate));
				map.add("toDate", DateConvertor.convertToYMD(toDate));

				GetIssueExcelList[] IssueHeader = rest.postForObject(Constants.url + "/getIssueExcelList", map,
						GetIssueExcelList[].class);
				List<GetIssueExcelList> issueHeaderList = new ArrayList<GetIssueExcelList>(Arrays.asList(IssueHeader));
				model.addObject("issueHeaderList", issueHeaderList);

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}
	
	@RequestMapping(value = "/getIssueDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<GetIssueExcelList> getPoListReport(HttpServletRequest request, HttpServletResponse response) {
		List<GetIssueExcelList> issueList = null;
		try {
			List<GetIssueExcelList> getPoList = new ArrayList<>();

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String list = request.getParameter("list");
			list = list.substring(1, list.length() - 1);
			list = list.replaceAll("\"", "");

			List<Integer> headIdList = Stream.of(list.split(",")).map(Integer::parseInt).collect(Collectors.toList());

			System.out.println("Val---------" + fromDate + " " + toDate + " " + headIdList);
			StringBuilder sb = new StringBuilder();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			GetIssueExcelList[] issueArr = rest.postForObject(Constants.url + "/getIssueExcelList", map,
					GetIssueExcelList[].class);
			issueList = new ArrayList<GetIssueExcelList>(Arrays.asList(issueArr));
			System.out.println(issueList);
			// export to excel

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			//rowData.add("Sr. No");
			rowData.add("iss_m_id");
			rowData.add("iss_m_date");
			rowData.add("iss_slip_no");
			rowData.add("iss_d_id");
			rowData.add("Item_id");
			rowData.add("item_description");
			rowData.add("item_qty");
			rowData.add("item_uom");
			rowData.add("Item_mrn_ref");
			rowData.add("item_rate");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			int cnt = 1;
			for (int j = 0; j < issueList.size(); j++) {
				System.err.println(issueList.get(j).getIssueId());

				for (int i = 0; i < headIdList.size(); i++) {

					if (issueList.get(j).getIssueId() == headIdList.get(i)) {

						expoExcel = new ExportToExcel();
						rowData = new ArrayList<String>();
						
						//rowData.add("" + (cnt));
						//cnt = cnt + 1;
						rowData.add("" + issueList.get(j).getIssueId());
						rowData.add("" + issueList.get(j).getIssueDate());
						rowData.add("" + issueList.get(j).getIssueSlipNo());
						rowData.add("" + issueList.get(j).getIssueDetailId());
						rowData.add("" + issueList.get(j).getItemId());
	
						rowData.add("" + issueList.get(j).getItemDesc());
						rowData.add("" + issueList.get(j).getItemIssueQty());
						rowData.add("" + issueList.get(j).getItemUom());
						rowData.add("" + issueList.get(j).getBatchNo());
						rowData.add("" + issueList.get(j).getItemRate());
	
						expoExcel.setRowData(rowData);
						exportToExcelList.add(expoExcel);
					}
					HttpSession session = request.getSession();
					session.setAttribute("exportExcelList", exportToExcelList);
					session.setAttribute("excelName", "IssueDetailList");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return issueList;
	}
}
