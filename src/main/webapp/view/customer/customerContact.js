
	  var gemsCustomerMasterId = JSON.parse(localStorage.getItem('gemsCustomerMasterId'));
	  var userId = sessionStorage.getItem("userId");

	  jQuery('#contactdelete_success_message').hide();
	  jQuery('#contactdelete_error_message').hide();

	  jQuery('#contactsave_success_message').hide();
	  jQuery('#contactsave_error_message').hide();

	jQuery('#customerContactCancelBtn').click(function(event) {
						window.location.href = 'customer.html';
					});
	
jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
	

	jQuery("#addCustomerContactBtn").click(function(e){
		e.preventDefault();	
		
		jQuery('#contactdelete_success_message').hide();
		jQuery('#contactdelete_error_message').hide();

		jQuery('#contactsave_success_message').hide();
		jQuery('#contactsave_error_message').hide();

		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/customer/saveCustomerContact.action?userId='+userId;
	
	var gemsCustomerMasterId = jQuery("#contactgemsCustomerMasterId").val();
	
	var gemsCustomerContactId = jQuery("#gemsCustomerContactId").val();
	var firstName = jQuery("#firstName").val();
	var lastName = jQuery("#lastName").val();
	var contactEmail = jQuery("#contactEmail").val();
	var contactPhone = jQuery("#contactPhone").val();
	var contactMobile = jQuery("#contactMobile").val();
	var contactDepartment = jQuery("#contactDepartment").val();
	var contactDesignation = jQuery("#contactDesignation").val();
	var isPrimaryContact = jQuery('#isPrimaryContact').is(':checked') ? isPrimaryContact = "on" : isPrimaryContact = "off";
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCustomerMasterId : gemsCustomerMasterId,
			gemsCustomerContactId : gemsCustomerContactId,
			firstName	: firstName,
			contactEmail : contactEmail,
			lastName		: lastName,
			contactPhone		: contactPhone,
			contactMobile	: contactMobile,
			isPrimaryContact	: isPrimaryContact,
			contactDepartment : contactDepartment,
			contactDesignation : contactDesignation

		},
		type : 'POST',
		success : function(response) {
			
			jQuery('#contactsave_success_message').show();
	  

			var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/customer/viewCustomerContactList.action?userId='
							+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
					
					
			customerContact_jQueryDataTableAjax(serviceURL);
			jQuery('#customer-contact-form')[0].reset();
					
					// setting id
			document.getElementById("contactgemsCustomerMasterId").value = gemsCustomerMasterId;
			document.getElementById("gemsCustomerContactId").value = "";
			
			



		},
		failure : function(data) {
			
			jQuery('#contactsave_error_message').show();
			var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/customer/viewCustomerContactList.action?userId='
							+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
					
					
			customerContact_jQueryDataTableAjax(serviceURL);
			jQuery('#customer-contact-form')[0].reset();
					
					// setting id
			document.getElementById("contactgemsCustomerMasterId").value = gemsCustomerMasterId;
			document.getElementById("gemsCustomerContactId").value = "";
			
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});
	});
	 
	
		
					var serviceURL = envConfig.serviceBaseURL
							+ '/customer/viewCustomerContactList.action?userId='
							+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
					
					
					customerContact_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("contactgemsCustomerMasterId").value = gemsCustomerMasterId;
					
					
					

function customerContact_jQueryDataTableAjax(serviceURL) {
						
						
						
						var customerContactList_table = jQuery("#customerContactList_table")
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
														"mData" : "contactName"
													},
													{
														"mData" : "contactNumber"
													},
													{
														"mData" : "contactEmail"
													},
													{
														"mData" : "contactDepartment"
													},
													{
														"mData" : "contactDesignation"
													},
													
													{
														"mData" : "gemsCustomerContactId",
														"bSortable" : false,
														"mRender" : function(
																gemsCustomerContactId) {
															return '<a href = "#" onClick = "editContact('
																	+ gemsCustomerContactId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteContact('
																	+ gemsCustomerContactId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}


function deleteContact(gemsCustomerContactId) {

	jQuery('#contactdelete_success_message').hide();
	jQuery('#contactdelete_error_message').hide();

	jQuery('#contactsave_success_message').hide();
	jQuery('#contactsave_error_message').hide();


	localStorage.setItem('gemsCustomerContactId', gemsCustomerContactId);
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL + '/customer/deleteCustomerContact.action?userId='+userId;
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														objectId : gemsCustomerContactId
													},
													type : 'GET',
													success : function(response) {
														jQuery('#contactdelete_success_message').show();
														var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
														var serviceURL = envConfig.serviceBaseURL
																		+ '/customer/viewCustomerContactList.action?userId='
																		+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																
																
														customerContact_jQueryDataTableAjax(serviceURL);
														jQuery('#customer-contact-form')[0].reset();
																
																// setting id
														document.getElementById("contactgemsCustomerMasterId").value = gemsCustomerMasterId;
														document.getElementById("gemsCustomerContactId").value = "";
														
														
													},
													failure : function(data) {
														jQuery('#contactdelete_error_message').show();
														var gemsCustomerMasterId = localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
														var serviceURL = envConfig.serviceBaseURL
																		+ '/customer/viewCustomerContactList.action?userId='
																		+ userId+'&gemsCustomerMasterId='+gemsCustomerMasterId;
																
																
														customerContact_jQueryDataTableAjax(serviceURL);
														jQuery('#customer-contact-form')[0].reset();
																
																// setting id
														document.getElementById("contactgemsCustomerMasterId").value = gemsCustomerMasterId;
														document.getElementById("gemsCustomerContactId").value = "";
													},
													statusCode : {
														403 : function(xhr) {
															window.location.href = "../../view/login/sessionoutlogin.html";

														}
													}

												});

}


function editContact(gemsCustomerContactId)
{
	jQuery('#contactdelete_success_message').hide();
	jQuery('#contactdelete_error_message').hide();

	jQuery('#contactsave_success_message').hide();
	jQuery('#contactsave_error_message').hide();

	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/customer/getCustomerContactInfo.action?userId=' + userId + '&gemsCustomerContactId='
			+ gemsCustomerContactId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCustomerContactId : gemsCustomerContactId
		},
		type : 'Post',
		success : function(response) {
			var data = JSON.stringify(response.data);
			if (data !== null) {
				response.data.isPrimaryContact ? jQuery("#isPrimaryContact").prop('checked', true) : jQuery("#isPrimaryContact").prop('checked', false);
				var isPrimaryContact = jQuery('#isPrimaryContact').is(':checked') ? isPrimaryContact = "on" : isPrimaryContact = "off";
				jQuery('#contactgemsCustomerMasterId').val(response.data.selectedGemsCustomerMasterId);
				localStorage.setItem('gemsCustomerMasterId', response.data.selectedGemsCustomerMasterId);
				jQuery('#gemsCustomerContactId').val(response.data.gemsCustomerContactId);
				//localStorage.setItem('gemsCustomerContactId', response.data.gemsCustomerContactId);		
				jQuery("#isPrimaryContact").val(isPrimaryContact);
				jQuery("#contactEmail").val(response.data.contactEmail);
				jQuery("#contactMobile").val(response.data.contactMobile);
				jQuery("#contactPhone").val(response.data.contactPhone);
				jQuery("#firstName").val(response.data.firstName);		
				jQuery("#lastName").val(response.data.lastName);
				jQuery("#contactDesignation").val(response.data.contactDesignation);		
				jQuery("#contactDepartment").val(response.data.contactDepartment);
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