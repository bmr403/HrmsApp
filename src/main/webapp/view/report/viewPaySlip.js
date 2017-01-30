jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L30').addClass('active');
						jQuery("ul.U30").show();
						jQuery("ul.U30").find('li#LI77').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			 jQuery('#payslipDate_Search').monthpicker();

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			function paySlip_jQueryDataTableAjax(serviceURL,
					paySlipDate, employeeCode) {

				
				console.log(serviceURL);
				var payslip_table = jQuery("#payslip_table")
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
															+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-download btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Download\"></span></a>';
												}
											} ],

								});
			}
			
			var serviceURL = envConfig.serviceBaseURL
			+ '/employee/viewPaySlipList.action?userId='
			+ userId;
			searchPaySlipDate = "";
			searchEmployeeCode = "";
			
			paySlip_jQueryDataTableAjax(serviceURL, searchPaySlipDate,
					searchEmployeeCode);
			
			function paySlipTable_Search() {
				/*var userId = sessionStorage.getItem("userId");
				var serviceURL = envConfig.serviceBaseURL
						+ '/employee/viewPaySlipList.action?userId='
						+ userId;*/
				searchPaySlipDate = jQuery('#payslipDate_Search').val();
				searchEmployeeCode = jQuery('#employeeCode_Search').val();
				paySlip_jQueryDataTableAjax(serviceURL, searchPaySlipDate,
						searchEmployeeCode);

			}
			
			jQuery("#search_payslip_btn").click(function(e) {

				e.preventDefault();
				paySlipTable_Search();

			});
			jQuery("#resetsearch_payslip_btn").click(function(e) {
				
				jQuery('#payslipDate_Search').val("");
				jQuery('#employeeCode_Search').val("");
				paySlipTable_Search();

			});
			
			
		
});

	function downloadEmpPaySlip(gemsEmployeePayslipDetailId) {
				var userId = sessionStorage.getItem("userId");
				var serviceURL = envConfig.serviceBaseURL
						+ '/employee/downloadPaySlip.action?userId='+userId+'&objectId='
						+ gemsEmployeePayslipDetailId;
				
				window.location.href=serviceURL;	

			}					
	
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
