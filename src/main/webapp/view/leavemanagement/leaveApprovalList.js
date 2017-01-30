jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI29').addClass('active');
			});

			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			 jQuery("#fromDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#toDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});

			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
	

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			var userId = sessionStorage.getItem("userId");
			
		function myLeaveList_jQueryDataTableAjax(serviceURL,
							fromDate,toDate) {

			

						
						console.log(serviceURL);
						var myLeaveList_table = jQuery("#myLeaveList_table")
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
													"name" : "fromDate",
													"value" : fromDate
												},{
													"name" : "toDate",
													"value" : toDate
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
														"mData" : "selectedGemsEmployeeMasterName"
													},
													{
														"mData" : "selected_leavetype"
													},
													{
														"mData" : "fromDate"
													},
													{
														"mData" : "toDate"
													},
													{
														"mData" : "employeeDutyResumeDate"
													},
													{
														"mData" : "contactNumber"
													},
													{
														"mData" : "approvedStatus"
													},
													{
														"mData" : "gemsEmployeeLeaveLineId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeLeaveLineId) {
															return '<a href = "#" onClick = "editLeaveApproval('
																	+ gemsEmployeeLeaveLineId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveApprovalList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					myLeaveList_jQueryDataTableAjax(serviceURL, searchCode);
					
					
					
					function myLeaveList_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveApprovalList.action?userId='
							+ userId;
						fromDate = jQuery('#fromDate').val();
						toDate = jQuery('#toDate').val();
						myLeaveList_jQueryDataTableAjax(serviceURL, fromDate,toDate);

					}
					jQuery("#search_myleavelist_btn").click(function(e) {

						e.preventDefault();
						myLeaveList_Search();

					});
					jQuery("#resetsearch_myleavelist_btn").click(function(e) {
						
						jQuery('#fromDate').val("");
						jQuery('#toDate').val("");
						leaveTypeTable_Search();

					});		
					
					jQuery("#delete_leavetypelist_btn")
							.click(
									function(e) {
						e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsLeaveTypeMasterId = localStorage
												.getItem('gemsLeaveTypeMasterId');
										alert(gemsRoleMasterId);
										var serviceURL = envConfig.serviceBaseURL
												+ '/leavemanagement/deleteLeaveType.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_leavetypelist_modal').modal(
												'toggle');
										jQuery('#delete_leavetypelist_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsLeaveTypeMasterId : gemsLeaveTypeMasterId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsLeaveTypeMasterId);
														window.location.href = "leavetype.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsLeaveTypeMasterId);
														window.location.href = "leavetype.html";
													},
													statusCode : {
														403 : function(xhr) {
															window.location.href = "../../view/login/sessionoutlogin.html";

														}
													}

												});

					});

									});
									
									
									
	
	function editLeaveApproval(gemsEmployeeLeaveLineId) {
	
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeLeaveLineById.action?userId='+userId;

	jQuery.ajax({
		url : serviceURL,
		data : {
			gemsEmployeeLeaveLineId : gemsEmployeeLeaveLineId
		},
		dataType : "json",
		type : 'POST',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			localStorage.setItem('editLeave_Data', JsonStringify_Data);
			window.location.href = "editLeaveApproval.html";
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});

}



function deleteLeaveTypeSummary(leaveSummayMasterId) {

	localStorage.setItem('gemsRoleMasterId', gemsRoleMasterId);
	jQuery('#delete_roleList_modal').modal('toggle');
	jQuery('#delete_roleList_modal').modal('view');
	

}						
	
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
