	 var gemsCustomerMasterId = JSON.parse(localStorage.getItem('gemsCustomerMasterId'));
	  var userId = sessionStorage.getItem("userId");

	jQuery('#customerDocumentCancelBtn').click(function(event) {
						window.location.href = 'customer.html';
					});
	
	jQuery('#docdelete_success_message').hide();
	jQuery('#docdelete_error_message').hide();

	jQuery('#docsave_success_message').hide();
	jQuery('#docsave_error_message').hide();

	jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

	jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

	jQuery('#file').bind('change', function() {
		var fsize = (this.files[0].size/1024/1024).toFixed(2);
		if (fsize > 2.00)
		{
			jQuery('#fileError').text('Upload file size should be less than 2 MB');
			jQuery('#fileError').show();
			return false;
		}
		else
		{
			jQuery('#fileError').hide();
		}
		//alert('This file size is: ' + (this.files[0].size/1024/1024).toFixed(2) + " MB");
	});
	
	jQuery("form").submit(function(e) {
										e.preventDefault();

										jQuery('#docdelete_success_message').hide();
										jQuery('#docdelete_error_message').hide();
										
										var gemsCustomerMasterId = jQuery("#documentgemsCustomerMasterId").val();
										var serviceURL = envConfig.serviceBaseURL
												+ '/customer/saveCustomerDocument.action?userId='
												+ userId
										var formData = new FormData(jQuery(this)[0]);
										
										if (jQuery('#file').val()=='')
										{
											jQuery('#fileError').text('Please upload the document');
											jQuery('#fileError').show();
											return false;
										}
										else
										{
											jQuery('#fileError').hide();
										}
										

										jQuery
													.ajax({
														url : serviceURL,
														cache : false,
														dataType : 'text',
														data : formData,
														type : 'POST',
														enctype : 'multipart/form-data',
														processData : false,
														contentType : false,
														success : function(data) {
															
															jQuery('#docsave_success_message').show();
															

															var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
															var serviceURL = envConfig.serviceBaseURL
																			+ '/customer/viewCustomerDocumentList.action?userId='
																			+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																	
																	
															customerDoc_jQueryDataTableAjax(serviceURL);
															jQuery('#customer-document-form')[0].reset();
																	
																	// setting id
															document.getElementById("documentgemsCustomerMasterId").value = gemsCustomerMasterId;
															
														},
														failure : function(data) {
															jQuery('#docsave_error_message').show();
															var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
															var serviceURL = envConfig.serviceBaseURL
																			+ '/customer/viewCustomerDocumentList.action?userId='
																			+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																	
																	
															customerDoc_jQueryDataTableAjax(serviceURL);
															jQuery('#customer-document-form')[0].reset();
																	
																	// setting id
															document.getElementById("documentgemsCustomerMasterId").value = gemsCustomerMasterId;
														},
														statusCode : {
															403 : function(xhr) {
																window.location.href = "../../view/login/sessionoutlogin.html";
															}
														}
													});

													
										
									});

	
	 
					jQuery("#documentgemsCustomerMasterId").val(localStorage.getItem('gemsCustomerMasterId'));
					
					var serviceURL = envConfig.serviceBaseURL
							+ '/customer/viewCustomerDocumentList.action?userId='
							+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
					
					
					customerDoc_jQueryDataTableAjax(serviceURL);
					//document.getElementById("gemsEmployeeMasterId").value = gemsEmployeeMasterId;
					


function customerDoc_jQueryDataTableAjax(serviceURL) {
						
						
						
						var customerDocumentList_table = jQuery("#customerDocumentList_table")
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
											/*"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "searchRoleCode",
													"value" : searchCode
												}, {
													"name" : "searchRoleName",
													"value" : searchDescription
												});
											},*/
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
														"mData" : "documentFileName"
													},
													{
														"mData" : "gemsCustomerDocumentId",
														"bSortable" : false,
														"mRender" : function(
																gemsCustomerDocumentId) {
															return '<a href = \"#\" onClick = "downloadCustomerDoc('
																	+ gemsCustomerDocumentId
																	+ ');"  id=\"edit_btn\"><span class=\"glyphicon glyphicon-download\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Download\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteCustomerDoc('
																	+ gemsCustomerDocumentId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}





function downloadCustomerDoc(gemsCustomerDocumentId) {

		jQuery('#docdelete_success_message').hide();
	jQuery('#docdelete_error_message').hide();

	jQuery('#docsave_success_message').hide();
	jQuery('#docsave_error_message').hide();

	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/customer/downloadCustomerDocument.action?userId='+userId+'&objectId='
			+ gemsCustomerDocumentId;	
	//window.location.href=serviceURL;
	window.open(serviceURL, '_blank');
}
function deleteCustomerDoc(gemsCustomerDocumentId) {
	var userId = sessionStorage.getItem("userId");
var serviceURL = envConfig.serviceBaseURL
			+ '/customer/deleteCustomerDocument.action?userId='+userId+'&objectId='
			+ gemsCustomerDocumentId;
jQuery('#docsave_success_message').hide();
jQuery('#docsave_error_message').hide();

jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'POST',
		success : function(response) {
			jQuery("#delete_customerdocument_modal").remove();
			
			jQuery("#loading-div-background").remove();
			
			jQuery('#docdelete_success_message').show();
	

			var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
															var serviceURL = envConfig.serviceBaseURL
																			+ '/customer/viewCustomerDocumentList.action?userId='
																			+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																	
																	
															customerDoc_jQueryDataTableAjax(serviceURL);
															jQuery('#customer-document-form')[0].reset();
																	
																	// setting id
															document.getElementById("documentgemsCustomerMasterId").value = gemsCustomerMasterId;
			
		},
		failure : function(data) {
			jQuery('#docdelete_error_message').show();
			var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
															var serviceURL = envConfig.serviceBaseURL
																			+ '/customer/viewCustomerDocumentList.action?userId='
																			+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																	
																	
															customerDoc_jQueryDataTableAjax(serviceURL);
															jQuery('#customer-document-form')[0].reset();
																	
																	// setting id
															document.getElementById("documentgemsCustomerMasterId").value = gemsCustomerMasterId;

		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});

}