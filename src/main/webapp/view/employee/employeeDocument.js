jQuery("#issuedDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#expiryDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});

	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");

	  jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

	jQuery('#empDoc_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
	
	jQuery('#ok_docbtn').click(function(e) {
						jQuery('#error_empdoc_modal').modal('hide');
					});
	jQuery('#error_docbtn').click(function(e) {
						jQuery('#error_empdoc_modal').modal('hide');
					});
	
	jQuery('#file').bind('change', function() {
		var fsize = (this.files[0].size/1024/1024).toFixed(2);
		if (fsize > 1.00)
		{
			jQuery('#fileError').text('Upload file size should be less than 1 MB');
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
										var documentType = jQuery('#documentType').val();
										var gemsEmployeeMasterId = jQuery("#empDocGemsEmployeeMasterId").val();
										var gemsEmployeeImmigrationDetailId = jQuery("#gemsEmployeeImmigrationDetailId").val();
										var docActiveStatus = jQuery('#docActiveStatus').is(':checked') ? docActiveStatus = "on" : docActiveStatus = "off";
										
										var documentNumber = jQuery('#documentNumber').val();
										var issuedDate = jQuery('#issuedDate').val();
										var expiryDate = jQuery('#expiryDate').val();
										var issuedBy = jQuery('#issuedBy').val();
										var serviceURL = envConfig.serviceBaseURL
												+ '/employee/saveGemsEmployeeImmigration.action?userId='
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
										if (documentType == "Select Document Type")
										{
											jQuery('#documentTypeError').text('Please select document type');
											jQuery('#documentTypeError').show();
											return false;
										}
										else
										{
											jQuery('#documentTypeError').hide();
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
															var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
															var serviceURL = envConfig.serviceBaseURL
																			+ '/employee/viewEmployeeImmigrationList.action?userId='
																			+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
																	
																	
															employeeDoc_jQueryDataTableAjax(serviceURL);
															jQuery('#employee-document-form')[0].reset();
																	
																	// setting id
															document.getElementById("empDocGemsEmployeeMasterId").value = gemsEmployeeMasterId;
															
														},
														failure : function(data) {
															window.location.href = "dashboard.html";
														},
														statusCode : {
															403 : function(xhr) {
																window.location.href = "../../view/login/sessionoutlogin.html";
															}
														}
													});

													
										
									});

	
	 
					jQuery("#empDocGemsEmployeeMasterId").val(localStorage.getItem('gemsEmployeeMasterId'));
					
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeImmigrationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeDoc_jQueryDataTableAjax(serviceURL);
					//document.getElementById("gemsEmployeeMasterId").value = gemsEmployeeMasterId;
					


function employeeDoc_jQueryDataTableAjax(serviceURL) {
						
						
						
						var employeeDocList_table = jQuery("#employeeDocList_table")
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
														"mData" : "documentType"
													},
													{
														"mData" : "documentNumber"
													},
													{
														"mData" : "issuedDate"
													},
													{
														"mData" : "expiryDate"
													},
													{
														"mData" : "issuedBy"
													},													
													{
														"mData" : "gemsEmployeeImmigrationDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeImmigrationDetailId) {
															return '<a href = \"#\" onClick = "downloadEmpDoc('
																	+ gemsEmployeeImmigrationDetailId
																	+ ');"  id=\"edit_btn\"><span class=\"glyphicon glyphicon-download title=\"Download\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = "#" onClick = "editEmpDoc('
																	+ gemsEmployeeImmigrationDetailId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteEmpDoc('
																	+ gemsEmployeeImmigrationDetailId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}

function downloadEmpDoc(gemsEmployeeImmigrationDetailId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/downloadDocument.action?userId='+userId+'&objectId='
			+ gemsEmployeeImmigrationDetailId;
	
	window.location.href=serviceURL;	

}
function deleteEmpDoc(gemsEmployeeImmigrationDetailId) {
	localStorage.setItem('gemsEmployeeImmigrationDetailId', gemsEmployeeImmigrationDetailId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteGemsEmployeeImmigration.action?userId='+userId+'&objectId='
			+ gemsEmployeeImmigrationDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeImmigrationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeDoc_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-document-form')[0].reset();
			document.getElementById("empDocGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
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

function editEmpDoc(gemsEmployeeImmigrationDetailId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getGemsEmployeeImmigration.action?userId=' + userId + '&gemsEmployeeImmigrationDetailId='
			+ gemsEmployeeImmigrationDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeImmigrationDetailId : gemsEmployeeImmigrationDetailId
		},
		type : 'Get',
		success : function(response) {
			var data = JSON.stringify(response.data);
					
			if (data !== null) {
				jQuery('#empDocGemsEmployeeMasterId').val(response.data.selectedGemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.selectedGemsEmployeeMasterId);
				jQuery('#gemsEmployeeImmigrationDetailId').val(response.data.gemsEmployeeImmigrationDetailId);
				localStorage.setItem('gemsEmployeeImmigrationDetailId', response.data.gemsEmployeeImmigrationDetailId);	
				
				response.data.docActiveStatus ? jQuery("#docActiveStatus").prop('checked', true) : jQuery("#docActiveStatus").prop('checked', false);
				var docActiveStatus = jQuery('#docActiveStatus').is(':checked') ? docActiveStatus = "on" : docActiveStatus = "off";	

				jQuery("#documentNumber").val(response.data.documentNumber);
				jQuery("#documentType").val(response.data.documentType);
				jQuery("#issuedDate").val(response.data.issuedDate);
				jQuery("#expiryDate").val(response.data.expiryDate);
				jQuery("#issuedBy").val(response.data.issuedBy);
				jQueryDropDownDocumentType = jQuery('#documentType');
				if (respons.data.documentType == "RESUME")
				{
					jQueryDropDownDocumentType.append('<option id="RESUME" selected>Resume</option>');
					jQueryDropDownDocumentType.append('<option id="EMPLOYMENTCONTRACT">Employment Contract</option><option id="PASSPORT">Passport</option><option id="VISA">Visa</option>');
				}
				else if (respons.data.documentType == "EMPLOYMENTCONTRACT")
				{
					jQueryDropDownDocumentType.append('<option id="RESUME" >Resume</option>');
					jQueryDropDownDocumentType.append('<option id="EMPLOYMENTCONTRACT" selected>Employment Contract</option><option id="PASSPORT">Passport</option><option id="VISA">Visa</option>');
				}
				else if (respons.data.documentType == "PASSPORT")
				{
					jQueryDropDownDocumentType.append('<option id="RESUME" >Resume</option>');
					jQueryDropDownDocumentType.append('<option id="EMPLOYMENTCONTRACT">Employment Contract</option><option id="PASSPORT" selected>Passport</option><option id="VISA">Visa</option>');
				}
				else if (respons.data.documentType == "VISA")
				{
					jQueryDropDownDocumentType.append('<option id="RESUME" >Resume</option>');
					jQueryDropDownDocumentType.append('<option id="EMPLOYMENTCONTRACT">Employment Contract</option><option id="PASSPORT">Passport</option><option id="VISA" selected>Visa</option>');
				}
				jQuery("#documentType").focus();
				
				
			}
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