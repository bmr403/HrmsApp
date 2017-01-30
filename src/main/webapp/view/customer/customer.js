jQuery(document)
		.ready(
				function() {


					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L61').addClass('active');
						jQuery("ul.U61").show();
						jQuery("ul.U61").find('li#LI62').addClass('active');
					});
					


					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					
					jQuery( document ).ajaxError(function() {
						window.location.href = "../../view/login/sessionoutlogin.html";
					});
					
					



					function customer_jQueryDataTableAjax(serviceURL,
							searchCustomerCode, searchCustomerName,searchCustomerPhone) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var customerListTable = jQuery("#customerList_table")
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
													"name" : "searchCustomerCode",
													"value" : searchCustomerCode
												}, {
													"name" : "searchCustomerName",
													"value" : searchCustomerName
												},{
													"name" : "searchCustomerPhone",
													"value" : searchCustomerPhone
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
														"mData" : "gemsCustomerCode"														
													},
													{
														"mData" : "gemsCustomerName"
													},
													{
														"mData" : "gemsCustomerPhone"
													},
													{
														"mData" : "gemsCustomerMasterId",
														"bSortable" : false,
														"mRender" : function(
																data,type,row,meta) {
															return '<a href = "#" onClick = "customerProjectDetail('
																	+ row.gemsCustomerMasterId
																	+ ');" id=\"detail_btn\"><span class=\"glyphicon glyphicon-info-sign\" title=\"Details\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = "#" onClick = "editCustomer('
																	+ row.gemsCustomerMasterId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteCustomer('
																	+ row.gemsCustomerMasterId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}			
													}
													],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/customer/viewCustomerList.action?userId='
							+ userId;
					searchCustomerCode = "";
					searchCustomerName = "";
					searchCustomerPhone = "";
					
					customer_jQueryDataTableAjax(serviceURL,searchCustomerCode, searchCustomerName,searchCustomerPhone);
					
					function customerTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/customer/viewCustomerList.action?userId='
								+ userId;
						searchCustomerCode = jQuery('#searchCustomerCode').val();
						searchCustomerName = jQuery('#searchCustomerName').val();
						searchCustomerPhone = jQuery('#searchCustomerPhone').val();
						customer_jQueryDataTableAjax(serviceURL,searchCustomerCode,
								searchCustomerName,searchCustomerPhone);

					}
					jQuery("#search_customerlist_btn").click(function(e) {

						e.preventDefault();
						customerTable_Search();

					});
					jQuery("#resetsearch_customer_btn").click(function(e) {
						
						jQuery('#searchCustomerCode').val("");
						jQuery('#searchCustomerName').val("");
						jQuery('#searchCustomerPhone').val("");
						customerTable_Search();

					});
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem('gemsCustomerMasterId');
						window.location.href = "customer.html";	
						
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem('gemsCustomerMasterId');
						window.location.href = "customer.html";	
					});

					
				});

function customerProjectDetail(gemsCustomerMasterId) {
		localStorage.setItem('customerId', gemsCustomerMasterId);		
		window.location.href="../project/projectlist.html";
		
}
function deleteCustomer(gemsCustomerMasterId) {
	localStorage.setItem('gemsCustomerMasterId', gemsCustomerMasterId);
	jQuery('#delete_customer_modal').modal('toggle');
	jQuery('#delete_customer_modal').modal('view');

}
jQuery("#delete_customer_btn").click(
	function(e) {
	e.preventDefault();
	var gemsCustomerMasterId = localStorage.getItem("gemsCustomerMasterId");
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/customer/deleteCustomer.action?userId='+userId+'&objectId='
			+ gemsCustomerMasterId;
	
	
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			
			jQuery("#delete_customer_modal").hide();
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
				localStorage.setItem('gemsCustomerMasterId');
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});

	

});
function editCustomer(gemsCustomerMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/customer/getCustomerInfo.action?userId=' + userId + '&gemsCustomerMasterId='
			+ gemsCustomerMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCustomerMasterId : gemsCustomerMasterId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editCustomer_Data = response.data;
			localStorage.setItem('editCustomer_Data', JsonStringify_Data);
			var gemsCustomerMasterId = response.data.gemsCustomerMasterId;
			localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
			window.location.href = "editCustomer.html";
			
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
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}

