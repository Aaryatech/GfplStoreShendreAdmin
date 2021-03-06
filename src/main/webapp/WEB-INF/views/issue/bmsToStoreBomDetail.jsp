<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">





<style>
#disableMe {
	pointer-events: none;
}
</style>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>


<body>
	<c:url var="gateEntryList" value="/gateEntryList"></c:url>
	<c:url var="withPoRef" value="/withPoRef"></c:url>
	<c:url var="withPoRefDate" value="/withPoRefDate"></c:url>
	<c:url var="getSubDeptList" value="/getSubDeptList"></c:url>
	<c:url var="genrateNo" value="/genrateNo" />

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
						<i class="fa fa-file-o"></i>Bill of Material Request Detailed
					</h1>
				</div>
			</div>
			<!-- END Page Title -->

			<div class="row">
				<div class="col-md-12">

					<div class="box">
						<div class="box-title">
							<h3>
								<i class="fa fa-table"></i>Bill of Material Request Detailed
							</h3>
							<div class="box-tool">
								<a
									href="${pageContext.request.contextPath}/getBomListBmsToStore">Back
									to List</a> <a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a>
							</div>

						</div>


						<div class="box-content">

							<form id="completproduction"
								action="${pageContext.request.contextPath}/approvedBomFromStore"
								method="post">
								<div class="box-content">
									<div class="col-md-2">BOM Request Date</div>

									<div class="col-md-3">
										<input type="text" id="mix_date" name="mix_date"
											value="${billOfMaterialHeader.reqDate}" class="form-control"
											readonly>
									</div>



								</div>
								<br>

								<div class="box-content">

									<div class="col-md-2">Status</div>
									<div class="col-md-3">
										<c:choose>
											<c:when test="${billOfMaterialHeader.status==0}">
												<c:set var="sts" value="Pending"></c:set>
											</c:when>
											<c:when test="${billOfMaterialHeader.status==1}">
												<c:set var="sts" value="Approved"></c:set>
											</c:when>
											<c:when test="${billOfMaterialHeader.status==2}">
												<c:set var="sts" value="Rejected"></c:set>
											</c:when>
											<c:when test="${billOfMaterialHeader.status==3}">
												<c:set var="sts" value="Approved Rejected"></c:set>
											</c:when>
											<c:when test="${billOfMaterialHeader.status==4}">
												<c:set var="sts" value="Request Closed"></c:set>
											</c:when>
										</c:choose>
										<input type="text" id="statuss" name="status" value="${sts}"
											class="form-control" readonly>
									</div>
								</div>
								<br>

								<div class="box-content">


									<div class="col-md-2">From Department</div>
									<div class="col-md-3">
										<input class="form-control" id="time_slot" size="16"
											type="text" name="time_slot"
											value="${billOfMaterialHeader.fromDeptName}" readonly />
									</div>


								</div>
								<br>
								<div class="box-content">


									<div class="col-md-2">To Department</div>
									<div class="col-md-3">
										<input class="form-control" id="time_slot" size="16"
											type="text" name="time_slot"
											value="${billOfMaterialHeader.toDeptName}" readonly />
									</div>
									<input class="form-control " id="settingvalue" size="16"
										type="hidden" name="settingvalue" value="${settingvalue}"
										readonly />


								</div>
								<br>

								<div class="box-content">

									<div class="col-md-2">Issue No*</div>
									<div class="col-md-3">

										<input id="issueNo" class="form-control"
											placeholder="Issue No" value="1" name="issueNo" type="text"
											readonly> <input id="poTyped" value="1"
											name="poTyped" id="poTyped" type="hidden" readonly> <input
											type="hidden" value="1" id="type" name="type">
									</div>

									<%-- <div class="col-md-2">Type*</div>
									<div class="col-md-3">
									
											<select name="poTyped" id="poTyped"   class="form-control chosen" onchange="getInvoiceNo()"    >
												  <option value="" >Select  Type</option>
														<c:forEach items="${typeList}" var="typeList">
															 
																	<option value="${typeList.typeId}"  >${typeList.typeName}</option>
															 
														</c:forEach>
														</select>
								 </div> --%>

								</div>
								<br>

								<div class="box-content">

									<div class="col-md-2">Issue Date*</div>
									<div class="col-md-3">
										<input id="issueDate" class="form-control"
											placeholder="Issue Date" name="issueDate" type="date"
											value="${date}" onchange="getInvoiceNo()" required> <input
											id="stockDateDDMMYYYY" value="${stockDateDDMMYYYY}"
											name="stockDateDDMMYYYY" type="hidden">
									</div>

									<div class="col-md-2">Select Account Head</div>
									<div class="col-md-3">
										<select class="form-control chosen" name="acc" id="acc"
											required>
											<option value="">Select Account Head</option>

											<c:forEach items="${accountHeadList}" var="accountHeadList">
												<option value="${accountHeadList.accHeadId}"><c:out
														value="${accountHeadList.accHeadDesc}"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>


								</div>
								<br>
								<div class="box-content">

									<div class="col-md-2">Select Department</div>
									<div class="col-md-3">
										<select class="form-control chosen" name="deptId"
											onchange="getSubDeptListByDeptId()" id="deptId" required>
											<option value="">Select Department</option>

											<c:forEach items="${deparmentList}" var="deparmentList">
												<option value="${deparmentList.deptId}">${deparmentList.deptCode}
													&nbsp;&nbsp;&nbsp; ${deparmentList.deptDesc}</option>
											</c:forEach>
										</select>
									</div>

									<div class="col-md-2">Select Sub Department</div>
									<div class="col-md-3">
										<select class="form-control chosen" name="subDeptId"
											id="subDeptId" required>

										</select>
									</div>

								</div>
								<br> <input id=issueSlipNo name="issueSlipNo" type="hidden"
									value="1" required><br>




								<div class=" box-content">
									<div class="row">
										<div class="col-md-12 table-responsive">
											<table class="table table-bordered table-striped fill-head "
												style="width: 100%" id="table_grid">
												<thead>
													<tr>
														<th width="7%">Sr.No.</th>
														<th>Name</th>
														<th width="10%">request Qty</th>
														<th width="20%">Available Qty</th>
														<th width="10%">issue Qty</th>
														<c:choose>
															<c:when test="${billOfMaterialHeader.status!=0}">
																<th>Return Qty</th>
																<th>Reject Qty</th>
															</c:when>
														</c:choose>



													</tr>
												</thead>
												<tbody class="abc">
													<c:set var="srNo" value="0" />
													<c:forEach items="${bomwithdetaild}" var="bomwithdetaild"
														varStatus="count">

														<tr>
															<td><c:out value="${count.index+1}" /></td>
															<c:set var="srNo" value="${srNo+1}" />
															<td><c:out value="${bomwithdetaild.rmName}" /></td>

															<td><c:out value="${bomwithdetaild.rmReqQty}" /></td>
															<td>${bomwithdetaild.totalAvaiQty}<input
																type="hidden"
																name="availableQty${bomwithdetaild.reqDetailId}"
																id="availableQty${bomwithdetaild.reqDetailId}"
																class="form-control"
																value="${bomwithdetaild.totalAvaiQty}"></td>

															<c:choose>
																<c:when test="${billOfMaterialHeader.status==0}">
																	<td><input type="text"
																		name="issue_qty${bomwithdetaild.reqDetailId}"
																		class="form-control rate1"
																		value="${bomwithdetaild.rmIssueQty}"
																		onchange="qtyValidation(${bomwithdetaild.reqDetailId})"
																		id="issue_qty${bomwithdetaild.reqDetailId}" /></td>
																</c:when>
																<c:otherwise>
																	<td><c:out value="${bomwithdetaild.rmIssueQty}" /></td>
																</c:otherwise>
															</c:choose>

															<c:choose>
																<c:when test="${billOfMaterialHeader.status!=0}">

																	<td><c:out value="${bomwithdetaild.returnQty}" /></td>

																	<td><c:out value="${bomwithdetaild.rejectedQty}" /></td>


																</c:when>
															</c:choose>
													</c:forEach>


												</tbody>
											</table>
										</div>
									</div>
								</div>



								<c:choose>

									<c:when test="${billOfMaterialHeader.status==0}">
										<div align="center" class="form-group">
											<div
												class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-5">
												<c:choose>
													<c:when test="${flag==1}">
														<input type="submit" class="btn btn-primary"
															value="Approved" onclick="check();">
													</c:when>
												</c:choose>
											</div>
										</div>
									</c:when>

									<%-- <c:when test="${billOfMaterialHeader.status==1}">
										<div align="center" class="form-group">
											<div
												class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-5">

												<a
													href="${pageContext.request.contextPath}/rejectiontoStore?reqId=${billOfMaterialHeader.reqId}&settingvalue=${settingvalue}">
													<input type="button" class="btn btn-primary"
													value="For Rejection And return">
												</a>

											</div>
										</div>


									</c:when>

									<c:when test="${billOfMaterialHeader.status ==2}">
										<div align="center" class="form-group">
											<div
												class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-5">


												<a
													href="${pageContext.request.contextPath}/approveRejectedByStore?reqId=${billOfMaterialHeader.reqId}"><i
													class="fa fa-check"></i>Approve Rejected</a>

											</div>
										</div>


									</c:when> --%>


								</c:choose>




								<div class="box-content"></div>
								<br> <br> <br>



							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END Main Content -->
		<footer>
		<p>2019 © MONGINIS.</p>
		</footer>

		<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
			class="fa fa-chevron-up"></i></a>

		<!-- END Content -->
	</div>
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
	
	$('.rate1').on('input', function() {
		 this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
		});
	
		function qtyValidation(id) {
			 
			
			var issue_qty = parseFloat(document.getElementById("issue_qty"+id).value);
			
			var availableQty = parseFloat(document
					.getElementById("availableQty"+id).value);
			  
			if (issue_qty > availableQty) {
				alert("enter valid Qty");
				document.getElementById("issue_qty"+id).value = 0;
			}

		}
	</script>


	<script type="text/javascript">
		function getInvoiceNo() {

			var date = $("#issueDate").val();
			var toDateValue = date.split('-');
			var type = $("#poTyped").val();
			var min = toDateValue[2] + "-" + (toDateValue[1]) + "-"
					+ toDateValue[0];

			$.getJSON('${genrateNo}', {

				catId : 1,
				docId : 2,
				date : min,
				typeId : type,
				ajax : 'true',

			}, function(data) {

				document.getElementById("issueNo").value = data.message;
				document.getElementById("type").value = type;
				getBatchByItemId();
			});

		}

		function check() {

			var acc = $("#acc").val();
			var deptId = $("#deptId").val();
			var subDeptId = $("#subDeptId").val();

			if (acc == "" || acc == null) {

				alert("Please Select Account Head ");
			}

			else if (deptId == "" || deptId == null) {

				alert("Please Select Department ");
			}

			else if (subDeptId == "" || subDeptId == null) {

				alert("Please Select Sub Department  ");
			}

		}
		
		function getSubDeptListByDeptId() {

			var deptId = document.getElementById("deptId").value;

			$.getJSON('${getSubDeptList}', {

				deptId : deptId,
				ajax : 'true'
			}, function(data) {

				var html = '<option value="">Select Sub Department</option>';

				var len = data.length;
				for (var i = 0; i < len; i++) {
					html += '<option value="' + data[i].subDeptId + '">'
							+ data[i].subDeptCode + ' &nbsp;&nbsp;&nbsp; '
							+ data[i].subDeptDesc + '</option>';
				}
				html += '</option>';
				$('#subDeptId').html(html);
				$("#subDeptId").trigger("chosen:updated");
			});
		}
	</script>
</body>
</html>