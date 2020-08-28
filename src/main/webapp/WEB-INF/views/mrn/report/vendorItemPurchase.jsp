<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<c:url var="getVendorItems" value="/getVendorItems"></c:url>
	<c:url var="getVendorItemPurchaseList" value="/getVendorItemPurchaseList"></c:url>


	<div class="container" id="main-container">

		<!-- BEGIN Sidebar -->
		<div id="sidebar" class="navbar-collapse collapse">

			<jsp:include page="/WEB-INF/views/include/navigation.jsp"></jsp:include>

			<div id="sidebar-collapse" class="visible-lg">
				<i class="fa fa-angle-double-left"></i>
			</div>
			<!-- END Sidebar Collapse Button -->
		</div>
		<!-- END Sidebar -->

		<!-- BEGIN Content -->
		<div id="main-content">
			<!-- BEGIN Page Title -->
			<div class="page-title">
				<div>
					<h1>

						<i class="fa fa-file-o"></i>MRN Report
					</h1>
				</div>
			</div>
			<!-- END Page Title -->

			<div class="row">
				<div class="col-md-12">

					<div class="box" id="todayslist">
						<div class="box-title">
							<h3>
								<i class="fa fa-table"></i>MRN Report List
							</h3>
							<div class="box-tool">
								<a href="${pageContext.request.contextPath}/"> </a> <a
									data-action="collapse" href="#"><i class="fa fa-chevron-up"></i></a>
							</div>

						</div>

						<div class="box-content">

							<div class="box-content">

								<div class="col-md-2">From Date*</div>
								<div class="col-md-3">
									<input id="fromDate" class="form-control date-picker"
										placeholder="From Date" value="${fromDate}" name="fromDate"
										type="text" required>


								</div>
								<div class="col-md-1"></div>
								<div class="col-md-2">To Date*</div>
								<div class="col-md-3">
									<input id="toDate" class="form-control date-picker"
										placeholder="To Date" value="${toDate}" name="toDate"
										type="text" required>


								</div>


							</div>
							<br> <br>

							<div class="box-content">

								<div class="col-md-2">Select Vendor</div>
								<div class="col-md-3">

									<select name="vendId" id="vendId" class="form-control chosen"
										tabindex="6"  placeholder="Select Vendor">
										<option value="-1">Select Vendors</option>
										<c:forEach items="${vendorList}" var="vendorList">

											<option value="${vendorList.vendorId}">${vendorList.vendorName}</option>
										</c:forEach>
									</select>

								</div>
							<!-- 	<div class="col-md-1"></div>
								<div class="col-md-2">Select Items</div>
								<div class="col-md-3">

									<select name="rm_item_list1" id="rm_item_list1"
										class="form-control chosen" tabindex="6" multiple="multiple">
										<option value="-1">All</option>
									</select>

								</div> -->
							</div>
							
							<br>
							<div class="box-content" style="margin-top:1%; margin-bottom: 2%;">
								<div class="col-lg-2">Select Items</div>
								<div class="col-lg-10">

									<select name="rm_item_list" id="rm_item_list"
										class="form-control chosen" tabindex="6" multiple="multiple">
										<option value="-1">All</option>
									</select>

								</div>
							</div>
							
							<br>
							
							<div class="form-group">
								<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-5">
									<input type="button" class="btn btn-primary" value="Search "
										onclick="search()">
										
											<button class="btn btn-primary" value="PDF" id="PDFButton"
									disabled="disabled" onclick="genPdf()">PDF</button>
									
									<input type="button" id="expExcel" class="btn btn-primary"
											disabled="disabled" value="EXPORT TO Excel"
											onclick="exportToExcel();">
								</div>
							</div>
							<br>

							<div align="center" id="loader" style="display: none">

								<span>
									<h4>
										<font color="#343690">Loading</font>
									</h4>
								</span> <span class="l-1"></span> <span class="l-2"></span> <span
									class="l-3"></span> <span class="l-4"></span> <span class="l-5"></span>
								<span class="l-6"></span>
							</div>

							<br /> <br />
							<div class="clearfix"></div>
							<div class="table-responsive" style="border: 0">
								<table class="table table-advance" id="table1">
									<thead>
										<tr class="bgpink">
											<th class="col-sm-1">Sr No.</th>
											<th class="col-md-1">MRN No.</th>
											<th class="col-md-1">Vendor Name</th>
											<th class="col-md-1">Date</th>
											<th class="col-md-1">Bill No.</th>
											<th class="col-md-2">Item Desc</th>
											<th class="col-md-1">Qty.</th>
											<th class="col-md-1">Taxable Amt</th>
											<th class="col-md-1">Total Tax Amt</th>
											<th class="col-md-1">Grand Total</th>
										</tr>
									</thead>
									<tbody>

										<%-- 	<c:forEach items="${list}" var="list" varStatus="count">
											<tr>
												<td class="col-md-1"><c:out value="${count.index+1}" /></td>


												<td class="col-md-1"><c:out
														value="${list.gpReturnDate}" /></td>

												<td class="col-md-1"><c:out value="${list.vendorName}" /></td>

												<td class="col-md-1"><c:out value="${list.gpNo}" /></td>

												<td class="col-md-1"><c:out value="${list.returnNo}" /></td>



												<td><a
													href="${pageContext.request.contextPath}/editReturnList/${list.returnId}"><abbr
														title="Edit"><i class="fa fa-edit"></i></abbr></a> <a
													href="${pageContext.request.contextPath}/deleteGetpassHeaderReturn/${list.returnId}"
													onClick="return confirm('Are you sure want to delete this record');"><span
														class="glyphicon glyphicon-remove"></span></a></td>

											</tr>
										</c:forEach>
 --%>

										
									</tbody>
									<thead>
										<tr class="bgpink">
											<th class="col-sm-1"></th>
											<th class="col-md-1"></th>
											<th class="col-md-1"></th>
											<th class="col-md-1"></th>
											<th class="col-md-1"></th>
											<th class="col-md-2"></th>
											<th class="col-md-1">Total</th>
											<th class="col-md-1" ><input type="text" id="ttl_taxable" disabled="disabled"></th>
											<th class="col-md-1" ><input type="text" id="ttl_tax" disabled="disabled"></th>
											<th class="col-md-1" ><input type="text" id="grand_ttl" disabled="disabled"></th>
										</tr>
									</thead>

								</table>

							</div>
						</div>



					</div>

				</div>

			</div>
			<footer>
				<p>2019 © MONGINIS</p>
			</footer>
		</div>


	</div>

	<!-- END Content -->

	<!-- END Container -->

	<!--basic scripts-->
	<script
		src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="${pageContext.request.contextPath}/resources/assets/jquery/jquery-2.0.3.min.js"><\/script>')
	</script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/jquery-slimscroll/jquery.slimscroll.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/jquery-cookie/jquery.cookie.js"></script>

	<!--page specific plugin scripts-->
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.resize.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.pie.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.stack.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.crosshair.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.tooltip.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/sparkline/jquery.sparkline.min.js"></script>


	<!--page specific plugin scripts-->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/jquery.validate.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/additional-methods.min.js"></script>


	<!--flaty scripts-->
	<script src="${pageContext.request.contextPath}/resources/js/flaty.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/flaty-demo-codes.js"></script>
	<!--page specific plugin scripts-->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-fileupload/bootstrap-fileupload.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/chosen-bootstrap/chosen.jquery.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/clockface/js/clockface.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/date.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/daterangepicker.js"></script>


	<script type="text/javascript">
	$( "#vendId" ).change(function() {
		  var vendrId = $("#vendId").val();
		  //alert(vendrId);
			
		  $.getJSON('${getVendorItems}', {

			  vendrId : vendrId,
				ajax : 'true'
			}, function(data) {
			
				var html = '<option value="-1">All</option>';

				var len = data.length;
				for (var i = 0; i < len; i++) {
					html += '<option value="' + data[i].rmId + '">'
							+data[i].itemDesc+'</option>';
				}
				html += '</option>';
				$('#rm_item_list').html(html);
				$("#rm_item_list").trigger("chosen:updated");
			});
		  
		});
	
	$( "#rm_item_list" ).change(function() {
		  var item = $("#rm_item_list").val();
		  var vendrId = $("#vendId").val();
		 // alert(item);
			if(item==-1){
		  $.getJSON('${getVendorItems}', {

			  vendrId : vendrId,
				ajax : 'true'
			}, function(data) {
			
				var html = '<option value="-1">All</option>';

				var len = data.length;
				for (var i = 0; i < len; i++) {
					html += '<option selected="selected" value="' + data[i].rmId + '">'
							+data[i].itemDesc+'</option>';
				}
				html += '</option>';
				$('#rm_item_list').html(html);
				$("#rm_item_list").trigger("chosen:updated");
			});
	}
		});
	
		function search() {

			var fromDate = $("#fromDate").val();
			var toDate = $("#toDate").val();
			var vendrId = $("#vendId").val();
			var rmItemList = $("#rm_item_list").val();
			var taxableAmt=0;
			var taxAmt=0;
			var totalAmt=0;
			$('#loader').show();

			$
					.getJSON(
							'${getVendorItemPurchaseList}',

							{

								fromDate : fromDate,
								toDate : toDate,
								vendrId : vendrId,
								rmItemList : rmItemList,
								ajax : 'true'

							},
							function(data) {

								$('#table1 td').remove();
								$('#loader').hide();

								if (data == "") {
									alert("No records found !!");
									document.getElementById("PDFButton").disabled = true;
									document.getElementById("expExcel").disabled = true;

								}
								document.getElementById("PDFButton").disabled = false;
								document.getElementById("expExcel").disabled = false;
								
								$
										.each(
												data,
												function(key, mrnList) {
													taxableAmt =  taxableAmt + parseFloat(mrnList.basicValue.toFixed(2));
													taxAmt = taxAmt + parseFloat(mrnList.taxValue.toFixed(2));
													totalAmt = totalAmt + parseFloat(mrnList.landingCost.toFixed(2));
													var tr = $('<tr></tr>');
													
															tr
															.append($(
																	'<td></td>')
																	.html(key + 1));
													tr
															.append($(
																	'<td></td>')
																	.html(
																			mrnList.mrnNo));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.vendorName));
													
													
													tr
															.append($(
																	'<td></td>')
																	.html(
																			mrnList.mrnDate));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.billNo));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.itemDesc));
													
													tr
															.append($(
																	'<td></td>')
																	.html(
																			mrnList.approveQty));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.basicValue.toFixed(2)));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.taxValue.toFixed(2)));
													
													tr
													.append($(
															'<td></td>')
															.html(
																	mrnList.landingCost.toFixed(2)));
													
													
												
													$('#table1 tbody').append(
															tr);
													
													
											
													
												});
								document.getElementById("ttl_taxable").value = taxableAmt.toFixed(2);
								document.getElementById("ttl_tax").value = taxAmt.toFixed(2);
								document.getElementById("grand_ttl").value = totalAmt.toFixed(2);
							});
		}
	</script>
	
	<script type="text/javascript">
		function genPdf() {
			
			var fromDate = document.getElementById("fromDate").value;
			var toDate = document.getElementById("toDate").value;
			
			window.open('${pageContext.request.contextPath}/getVendorItemPurchaseList/'
					+ fromDate + '/' + toDate);
			document.getElementById("PDFButton").disabled = true;
		}
	</script>
	
	<script type="text/javascript">
		function exportToExcel() {
			window.open("${pageContext.request.contextPath}/exportToExcel");
			document.getElementById("expExcel").disabled = true;
		}
	</script>

</body>
</html>