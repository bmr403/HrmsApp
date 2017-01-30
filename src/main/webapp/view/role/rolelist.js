jQuery(document).ready(
		function() {
				jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L2').addClass('active');
						jQuery("ul.U2").show();
						jQuery("ul.U2").find('li#LI36').addClass('active');
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
			
		function role_jQueryDataTableAjax(serviceURL,
							searchCode, searchDescription) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var roleList_table = jQuery("#roleList_table")
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
													"name" : "searchRoleCode",
													"value" : searchCode
												}, {
													"name" : "searchRoleName",
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
														"mData" : "roleCode"
													},
													{
														"mData" : "roleName"
													},
													{
														"mData" : "activeStatus"
													},
													{
														"mData" : "gemsRoleMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsRoleMasterId) {
															return '<a href = "#" onClick = "editRole('
																	+ gemsRoleMasterId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteRole('
																	+ gemsRoleMasterId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewRoleList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					role_jQueryDataTableAjax(serviceURL, searchCode,
							searchDescription);
					
					
					
					function roleTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/master/viewRoleList.action?userId='
								+ userId;
						searchCode = jQuery('#roleCode_search').val();
						searchDescription = jQuery('#roleDescription_search').val();
						role_jQueryDataTableAjax(serviceURL, searchCode,
								searchDescription);

					}
					jQuery("#search_role_btn").click(function(e) {

						e.preventDefault();
						roleTable_Search();

					});
					jQuery("#resetsearch_role_btn").click(function(e) {
						
						jQuery('#roleCode_search').val("");
						jQuery('#roleDescription_search').val("");
						roleTable_Search();

					});		
					
					jQuery("#delete_rolelist_btn")
							.click(
									function(e) {
						e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsRoleMasterId = localStorage
												.getItem('gemsRoleMasterId');
										alert(gemsRoleMasterId);
										var serviceURL = envConfig.serviceBaseURL
												+ '/master/deleteRole.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_roleList_modal').modal(
												'toggle');
										jQuery('#delete_roleList_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsRoleMasterId : gemsRoleMasterId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsRoleMasterId);
														window.location.href = "rolelist.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsRoleMasterId);
														window.location.href = "rolelist.html";
													},
													statusCode : {
														403 : function(xhr) {
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});

					});

									});
									
									
									
	
	function editRole(gemsRoleMasterId) {
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
				alert("Session will be Expired");
				window.location.href = "../../";

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
