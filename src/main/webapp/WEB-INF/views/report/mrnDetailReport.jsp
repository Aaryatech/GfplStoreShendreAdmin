<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>
<%-- 
	<c:url var="getIndentListReport" value="/getIndentListReport"></c:url>
	<c:url var="getIndentListExportToExcel"
		value="/getIndentListExportToExcel"></c:url> --%>


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
								<i class="fa fa-table"></i>MRN Report
							</h3>
							<div class="box-tool">
								<a href="${pageContext.request.contextPath}"></a> <a
									data-action="collapse" href="#"><i class="fa fa-chevron-up"></i></a>
							</div>

						</div>

						<div class="box-content">

							<%-- <div class="box-content">

								<div class="col-md-2">From Date*</div>
								<div class="col-md-3">
									<input id="fromDate" class="form-control date-picker"
										placeholder="From Date" value="${date}" name="fromDate"
										type="text" required>


								</div>
								<div class="col-md-1"></div>
								<div class="col-md-2">To Date*</div>
								<div class="col-md-3">
									<input id="toDate" class="form-control date-picker"
										placeholder="To Date" value="${date}" name="toDate"
										type="text" required>


								</div>


							</div>
							<br> <br>						
					
							<div class="form-group">
								<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-5">
									<input type="button" class="btn btn-primary" value="Search "
										onclick="search()">
								</div>

							</div> --%>

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
											<th class="col-sm-1">Sr no.</th>
											<th class="col-md-1">MRN Date</th>
											<th class="col-md-1">MRN No.</th>
											<th class="col-md-1">Vendor Name</th>
											<th class="col-md-1">Bill No</th>
											<th class="col-md-1">Item Name</th>
											<th class="col-md-1">Qty.</th>

										</tr>
									</thead>
									<tbody>

										<c:forEach items="${mrnList}" var="list" varStatus="count">

									
											<tr>
												<td class="col-md-1"><c:out value="${count.index+1}" /></td>
												<td class="col-md-1"><c:out value="${list.mrnDate}" /></td>
												<td class="col-md-1"><c:out value="${list.mrnNo}" /></td>
												<td class="col-md-1"><c:out value="${list.vendorName}" /></td>
												<td class="col-md-1"><c:out value="${list.billNo}" /></td>
												<td class="col-md-1"><c:out value="${list.itemDesc}" /></td>
												<td class="col-md-1"><c:out value="${list.approveQty}" /></td>
											</tr>
										</c:forEach>
 
									</tbody>

								</table>
								<div class="form-group" id="range">



									<div class="col-sm-3  controls">
										<input type="button" id="expExcel" class="btn btn-primary"
											value="EXPORT TO Excel"
											onclick="exportToExcel();">
									</div>
								</div>
								<button class="btn btn-primary" value="PDF" id="PDFButton"
									onclick="genPdf()">PDF</button>



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
		function search() {

			var fromDate = $("#fromDate").val();
			var toDate = $("#toDate").val();
			var catIdList = $("#catIdList").val();
			var isMonthly = $("#isMonthly").val();
			var isDev = $("#isDev").val();

			$('#loader').show();

			$.getJSON('${getIndentListReport}',

			{

				fromDate : fromDate,
				toDate : toDate,
				catIdList : catIdList,
				isMonthly : isMonthly,
				isDev : isDev,

				ajax : 'true'

			}, function(data) {
				//alert(data);

				$('#table1 td').remove();
				$('#loader').hide();
				document.getElementById("expExcel").disabled = false;
				document.getElementById("PDFButton").disabled = false;

				if (data == "") {
					alert("No records found !!");
					document.getElementById("expExcel").disabled = true;
					document.getElementById("PDFButton").disabled = true;

				}

				$.each(data, function(key, itemList) {

					var tr = $('<tr></tr>');
					tr.append($('<td></td>').html(key + 1));
					tr.append($('<td></td>').html(itemList.indMNo));
					tr.append($('<td></td>').html(itemList.indMDate));
					tr.append($('<td></td>').html(itemList.catDesc));
					 
					var modType;
					if (itemList.indMStatus == 0) {
						modType = "Pending";
					} else if (itemList.indMStatus == 1) {
						modType = "Partial Pending";
					} else if (itemList.indMStatus == 2) {
						modType = "Closed";
					}else if (itemList.indMStatus == 9) {
						modType = "1st Approved Pending";
					}else if (itemList.indMStatus == 7) {
						modType = "2nd Approved Pending";
					}
					tr.append($('<td></td>').html(modType));
					if (itemList.indMStatus == 3) {
						tr.append($('<td></td>').html("-"));
					} else {
						tr.append($('<td></td>').html(itemList.noOfDays));
					}
					$('#table1 tbody').append(tr);
				})

			});
		}
	</script>

	<script type="text/javascript">
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcel");
			document.getElementById("expExcel").disabled = true;
		}
	</script>

	<script type="text/javascript">
		function genPdf() {		
			window.open('${pageContext.request.contextPath}/showMrnDetailPdf');
			//document.getElementById("expExcel").disabled = true;
		}
	</script>
</body>
</html>