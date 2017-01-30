jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI27').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			var userId = sessionStorage.getItem("userId");
			
		function leaveTypeSummary_jQueryDataTableAjax(serviceURL,
							searchCode) {

			

						
						console.log(serviceURL);
						var leaveTypeSummaryList_table = jQuery("#leaveTypesummaryList_table")
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
													"name" : "searchEmployeeStatusCode",
													"value" : searchCode
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
														"mData" : "selected_employeestatus"
													},
													{
														"mData" : "selected_leavetype"
													},
													{
														"mData" : "totalDays"
													},
													{
														"mData" : "leaveSummayMasterId",
														"bSortable" : false,
														"mRender" : function(
																leaveSummayMasterId) {
															return '<a href = "#" onClick = "editLeaveTypeSummary('
																	+ leaveSummayMasterId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteLeaveTypeSummary('
																	+ leaveSummayMasterId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/leavemanagement/viewLeaveSummaryList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					leaveTypeSummary_jQueryDataTableAjax(serviceURL, searchCode);
					
					
					
					function leaveTypeSummaryTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/leavemanagement/viewLeaveSummaryList.action?userId='
								+ userId;
						searchCode = jQuery('#leaveTypeSummaryCode_search').val();
						leaveTypeSummary_jQueryDataTableAjax(serviceURL, searchCode);

					}
					jQuery("#search_leavetype_btn").click(function(e) {

						e.preventDefault();
						leaveTypeTable_Search();

					});
					jQuery("#resetsearch_leavetype_btn").click(function(e) {
						
						jQuery('#leaveTypeCode_search').val("");
						jQuery('#leaveTypeDescription_search').val("");
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
									
									
									
	
	function editLeaveTypeSummary(leaveSummayMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/leavemanagement/getLeaveManagementInfo.action?userId='+userId;

	jQuery.ajax({
		url : serviceURL,
		data : {
			leaveSummayMasterId : leaveSummayMasterId
		},
		dataType : "json",
		type : 'POST',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			localStorage.setItem('editLeaveTypeSummar_Data', JsonStringify_Data);
			window.location.href = "editleaveTypeSummary.html";
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
