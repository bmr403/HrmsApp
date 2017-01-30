jQuery(document)
		.ready(
				function() {


					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI83').addClass('active');
					});
					


					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					
					
					
					



					function mr_jQueryDataTableAjax(serviceURL,
							searchJobCode, searchRequestType,searchCustomerName) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var mrList_table = jQuery("#mrList_table")
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
													"name" : "searchJobCode",
													"value" : searchJobCode
												}, {
													"name" : "searchRequestType",
													"value" : searchRequestType
												},{
													"name" : "searchCustomerName",
													"value" : searchCustomerName
												});
											},
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 10,
											columnDefs : [ 
											{
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
														"mData" : "requestType"														
													},
													{
														"mData" : "jobCode"														
													},
													{
														"mData" : "requestDate"														
													},
													{
														"mData" : "jobPosition"
													},
													{
														"mData" : "jobLocation"
													},
													{
														"mData" : "selected_customer"
													},
													{
														"mData" : "requestStatus"
													},	
													{
														"mData" : "gemsMapowerRequestId",
														"bSortable" : false,
														"mRender" : function(
																gemsMapowerRequestId) {
															return '<a href = "#" onClick = "editManpowerRequest('
																	+ gemsMapowerRequestId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteManpowerRequest('
																	+ gemsMapowerRequestId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewManpowerRequestList.action?userId='
							+ userId;
					searchRequestType = "";
					searchCustomerName = "";
					searchJobCode = "";
					
					mr_jQueryDataTableAjax(serviceURL,searchJobCode, searchRequestType,searchCustomerName);
					
					function mrTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/recruitment/viewManpowerRequestList.action?userId='
								+ userId;
						searchJobCode = jQuery('#searchJobCode').val();
						searchRequestType = jQuery('#searchRequestType').val();
						searchCustomerName = jQuery('#searchCustomerName').val();
						mr_jQueryDataTableAjax(serviceURL,searchJobCode,
								searchRequestType,searchCustomerName);

					}
					jQuery("#search_requestlist_btn").click(function(e) {

						e.preventDefault();
						mrTable_Search();

					});
					jQuery("#resetsearch_request_btn").click(function(e) {
						
						jQuery('#searchJobCode').val("");
						jQuery('#searchRequestType').val("");
						jQuery('#searchCustomerName').val("");
						mrTable_Search();

					});

					jQuery("#delete_mr_btn")
							.click(
									function(e) {

										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsMapowerRequestId = localStorage
												.getItem('gemsMapowerRequestId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/recruitment/deleteManpowerRequest.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_mr_modal').modal(
												'toggle');
										jQuery('#delete_mr_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsMapowerRequestId : gemsMapowerRequestId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsMapowerRequestId);
														window.location.href = "manpowerRequest.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsMapowerRequestId);
														window.location.href = "manpowerRequest.html";
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
function deleteManpowerRequest(gemsMapowerRequestId) {
	localStorage.setItem('gemsMapowerRequestId', gemsMapowerRequestId);
	jQuery('#delete_mr_modal').modal('toggle');
	jQuery('#delete_mr_modal').modal('view');

}
function editManpowerRequest(gemsMapowerRequestId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/recruitment/getManpowerRequestInfo.action?userId=' + userId + '&gemsMapowerRequestId='
			+ gemsMapowerRequestId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsMapowerRequestId : gemsMapowerRequestId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editmr_Data = response.data;
			localStorage.setItem('editmr_Data', JsonStringify_Data);
			var gemsMapowerRequestId = response.data.gemsMapowerRequestId;
			localStorage.getItem('gemsMapowerRequestId', gemsMapowerRequestId);
			window.location.href = "editManpowerRequest.html";
			
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
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}

