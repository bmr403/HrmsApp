 jQuery('#payslipDate_Search').monthpicker();

	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");

	
	jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
	
	 
					jQuery("#empDocGemsEmployeeMasterId").val(localStorage.getItem('gemsEmployeeMasterId'));
					
					var serviceURL = envConfig.serviceBaseURL
					+ '/employee/viewPaySlipList.action?userId='
					+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					searchPaySlipDate = "";
					searchEmployeeCode = "";
					
					mypaySlip_jQueryDataTableAjax(serviceURL, searchPaySlipDate,
							searchEmployeeCode);
					
					function mypaySlipTable_Search() {
						/*var serviceURL = envConfig.serviceBaseURL
								+ '/employee/viewPaySlipList.action?userId='
								+ userId;*/
						searchPaySlipDate = jQuery('#payslipDate_Search').val();
						searchEmployeeCode = jQuery('#employeeCode_Search').val();
						mypaySlip_jQueryDataTableAjax(serviceURL, searchPaySlipDate,
								searchEmployeeCode);

					}
					
					jQuery("#search_payslip_btn").click(function(e) {

						e.preventDefault();
						mypaySlipTable_Search();

					});
					jQuery("#resetsearch_payslip_btn").click(function(e) {
						
						jQuery('#payslipDate_Search').val("");
						jQuery('#employeeCode_Search').val("");
						mypaySlipTable_Search();

					});
					


					function mypaySlip_jQueryDataTableAjax(serviceURL,
							paySlipDate, employeeCode) {

						
						console.log(serviceURL);
						var mypayslip_table = jQuery("#mypayslip_table")
								.DataTable(
										{
											"sAjaxSource" : serviceURL,
											"bProcessing" : false,
											"bServerSide" : true,
											"bPaginate" : true,
											"bFilter" : true,
											"searching" : false,
											"bSort" : false,
											"bDestroy" : true,
											"bJQueryUI" : false,
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "searchPaySlipDate",
													"value" : paySlipDate
												}, {
													"name" : "searchEmployeeCode",
													"value" : employeeCode
												});
											},
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 10,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
												if (oSettings.fnRecordsTotal() <= 10) {
													jQuery('.dataTables_length')
															.hide();
													jQuery(
															'.dataTables_paginate')
															.hide();
												} else {
													jQuery('.dataTables_length')
															.show();
													jQuery(
															'.dataTables_paginate')
															.show();
												}
											},
											"aoColumns" : [
											        {
											        	"mData" : "selectedEmployeeCode"
											        },
													{
														"mData" : "selectedGemsEmployeeMasterName"
													},
													{
														"mData" : "paySlipDate"
													},
													{
														"mData" : "gemsEmployeePayslipDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeePayslipDetailId) {
															return '<a href = "#" onClick = "downloadEmpPaySlip('
																	+ gemsEmployeePayslipDetailId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-download\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Download\"></span></a>';
														}
													} ],

										});
					}

					function downloadEmpPaySlip(gemsEmployeePayslipDetailId) {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/employee/downloadPaySlip.action?userId='+userId+'&objectId='
								+ gemsEmployeePayslipDetailId;
						
						window.location.href=serviceURL;	

					}	
