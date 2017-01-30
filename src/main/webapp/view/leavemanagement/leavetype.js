jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI25').addClass('active');
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
			
			function leaveType_jQueryDataTableAjax(serviceURL,
							searchCode, searchDescription) {

			

						
						console.log(serviceURL);
						var leaveTypeList_table = jQuery("#leaveTypeList_table")
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
													"name" : "searchLeaveTypeCode",
													"value" : searchCode
												}, {
													"name" : "searchLeaveTypeDescription",
													"value" : searchDescription
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
														"mData" : "leaveTypeCode"
													},
													{
														"mData" : "leaveTypeCodeDescription"
													},
													{
														"mData" : "activeStatus"
													},
													{
														"mData" : "gemsLeaveTypeMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsLeaveTypeMasterId) {
															return '<a href = "#" onClick = "editLeaveType('
																	+ gemsLeaveTypeMasterId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteLeaveType('
																	+ gemsLeaveTypeMasterId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/leavemanagement/viewLeaveTypeList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					leaveType_jQueryDataTableAjax(serviceURL, searchCode,
							searchDescription);
					
					
					
					function leaveTypeTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/leavemanagement/viewLeaveTypeList.action?userId='
								+ userId;
						searchCode = jQuery('#leaveTypeCode_search').val();
						searchDescription = jQuery('#leaveTypeDescription_search').val();
						leaveType_jQueryDataTableAjax(serviceURL, searchCode,
								searchDescription);

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
									
									
									
	
	function editLeaveType(gemsLeaveTypeMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/master/getRoleById.action?userId='+userId+'&gemsRoleMasterId='
			+ gemsRoleMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'GET',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editRole_Data = response.data;
			localStorage.setItem('editRole_Data', JsonStringify_Data);
			window.location.href = "editrole.html";
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



function deleteRole(gemsRoleMasterId) {

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
