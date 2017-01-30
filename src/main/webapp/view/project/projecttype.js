jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI34').addClass('active');
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
			
			
			
		
		
		
		function projectType_jQueryDataTableAjax(serviceURL,
							searchCode, searchDescription,searchProjectTypeActive) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var projectType_table = jQuery("#projectType_table")
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
													"name" : "searchProjectTypeCode",
													"value" : searchCode
												}, {
													"name" : "searchProjectTypeDescription",
													"value" : searchDescription
												},{
													"name" : "searchProjectTypeActive",
													"value" : searchProjectTypeActive
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
														"mData" : "projectTypeCode"
													},
													{
														"mData" : "projectTypeDescription"
													},
													{
														"mData" : "projectTypeActiveStatusString"
													},
													{
														"mData" : "projectTypeMasterId",
														"bSortable" : false,
														"mRender" : function(
																projectTypeMasterId) {
															return '<a href = "#" onClick = "projectTypeDetail('
																	+ projectTypeMasterId
																	+ ');" id=\"detail_btn\"><span class="glyphicon glyphicon-info-sign" title=\"Details\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = "#" onClick = "editProjectType('
																	+ projectTypeMasterId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteProjectType('
																	+ projectTypeMasterId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}
													}
													],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/project/viewProjectTypeList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					searchProjectTypeActive = "";
					projectType_jQueryDataTableAjax(serviceURL, searchCode,
							searchDescription,searchProjectTypeActive);
					
					
					
					function projectTypeTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/project/viewProjectTypeList.action?userId='
								+ userId;
						searchCode = jQuery('#projectTypeCode_search').val();
						searchDescription = jQuery('#projectTypeDescription_search').val();
						searchProjectTypeActive = jQuery('#searchProjectTypeActive').is(':checked') ? searchProjectTypeActive = "on" : searchProjectTypeActive = "off";
						projectType_jQueryDataTableAjax(serviceURL, searchCode,
								searchDescription,searchProjectTypeActive);

					}
					jQuery("#search_projectType_btn").click(function(e) {

						e.preventDefault();
						projectTypeTable_Search();

					});
					jQuery("#resetsearch_projectType_btn").click(function(e) {
						
						jQuery('#projectTypeCode_search').val("");
						jQuery('#projectTypeDescription_search').val("");
						projectTypeTable_Search();

					});
					jQuery("#delete_projecttype_cancel").click(function(e) {

						localStorage.removeItem("projectTypeMasterId");
					
					});
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("projectTypeMasterId");
														window.location.href = "projecttype.html";
						
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("projectTypeMasterId");
						window.location.href = "projecttype.html";
					});
					jQuery("#delete_projectType_btn").click(function(e) {

										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var projectTypeMasterId = localStorage
												.getItem('projectTypeMasterId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/project/deleteGemsProjectTypeMaster.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_projectType_modal').modal(
												'toggle');
										jQuery('#delete_projectType_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														objectId : projectTypeMasterId
													},
													type : 'GET',
													success : function(response) {
														jQuery("#delete_projectType_modal").hide();
														jQuery("#loading-div-background").hide();

														if(response.success == true){
															jQuery('#success_delete_modal').modal('toggle');
															jQuery('#success_delete_modal').modal('view');
															
														} else {
															jQuery('#error_delete_modal').modal('toggle');
															jQuery('#error_delete_modal').modal('view');
														}
														
													},
													failure : function(data) {
														jQuery('#error_delete_modal').modal('toggle');
														jQuery('#error_delete_modal').modal('view');
													},
													statusCode : {
														403 : function(xhr) {
															localStorage.removeItem("projectTypeMasterId");
															window.location.href = "../../view/login/sessionoutlogin.html";

														}
													}

												});
									});
									
									});
	function projectTypeDetail(projectTypeMasterId) {
		localStorage.setItem('projectTypeMasterId', projectTypeMasterId);		
		window.location.href="projectlist.html";
		
	}

	function editProjectType(projectTypeMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/projectType/getProjectTypeObjById.action?userId='+userId+'&projectTypeMasterId='
			+ projectTypeMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'GET',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editProjectType_Data = response.data;
			localStorage.setItem('editProjectType_Data', JsonStringify_Data);
			window.location.href = "editprojecttype.html";
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				alert("Session will be Expired");
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});

}

function deleteProjectType(projectTypeMasterId) {

	localStorage.setItem('projectTypeMasterId', projectTypeMasterId);
	jQuery('#delete_projectType_modal').modal('toggle');
	jQuery('#delete_projectType_modal').modal('view');

}

		
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
