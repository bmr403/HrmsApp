jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI68').addClass('active');
			});
			jQuery('#errorDialog').hide();

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
														"mData" : "currentStatus"
													},
													{
														"mData" : "gemsEmployeeLeaveId",
														"bSortable" : false,
														"mRender" : function(
																data,type,row,meta) {
															return '<a href = "#" onClick = "editLeave('
																	+ row.gemsEmployeeLeaveId
																	+ ',\''
																	+ row.currentStatus
																	+ '\');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteLeave('
																	+ row.gemsEmployeeLeaveId
																	+ ',\''
																	+ row.currentStatus
																	+ '\');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					myLeaveList_jQueryDataTableAjax(serviceURL, searchCode);
					
					
					
					function myLeaveList_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveList.action?userId='
							+ userId;
						fromDate = jQuery('#fromDate').val();
						toDate = jQuery('#toDate').val();
						leaveTypeSummary_jQueryDataTableAjax(serviceURL, fromDate,toDate);

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
					
					jQuery("#delete_leavelist_btn")
							.click(
									function(e) {
						e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsEmployeeLeaveId = localStorage
												.getItem('gemsEmployeeLeaveId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/employee/deleteGemsEmployeeLeave.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_leave_modal').modal(
												'toggle');
										jQuery('#delete_leave_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsEmployeeLeaveId : gemsEmployeeLeaveId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsEmployeeLeaveId);
														window.location.href = "myLeaveList.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsEmployeeLeaveId);
														window.location.href = "myLeaveList.html";
													},
													statusCode : {
														403 : function(xhr) {
															window.location.href = "../../view/login/sessionoutlogin.html";

														}
													}

												});

					});

									});
									
									
									
	
	function editLeave(gemsEmployeeLeaveId,currentStatus) {
	
	if (currentStatus == 'Draft')
	{
		var userId = sessionStorage.getItem("userId");

		var serviceURL = envConfig.serviceBaseURL
				+ '/employee/getEmployeeLeaveById.action?userId='+userId;

		jQuery.ajax({
			url : serviceURL,
			data : {
				gemsEmployeeLeaveId : gemsEmployeeLeaveId
			},
			dataType : "json",
			type : 'POST',
			success : function(response) {
				var JsonStringify_Data = JSON.stringify(response.data);
				localStorage.setItem('editLeave_Data', JsonStringify_Data);
				window.location.href = "editLeave.html";
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
	else
	{
		jQuery("#errorDialog").dialog({
		autoOpen: false,
		width: 600,
												//height: 70,
												buttons: {
													Close: function()
													{
														jQuery(this).dialog("close");
													}
												}
												});
												jQuery("#errorDialog")
												.dialog("open");
												return false;
	}

	

}



function deleteLeave(gemsEmployeeLeaveId,currentStatus) {
	
	
	if (currentStatus == 'Draft')
	{
		localStorage.setItem('gemsEmployeeLeaveId', gemsEmployeeLeaveId);
		jQuery('#delete_leave_modal').modal('toggle');
		jQuery('#delete_leave_modal').modal('view');
	}
	else
	{
		jQuery("#errorDialog").dialog({
		autoOpen: false,
		width: 600,
												//height: 70,
												buttons: {
													Close: function()
													{
														jQuery(this).dialog("close");
													}
												}
												});
												jQuery("#errorDialog")
												.dialog("open");
												return false;
	}

	
	

}						
	
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
