jQuery(document)
		.ready(
				function() {
					
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L9').addClass('active');
						jQuery("ul.U9").show();
						jQuery("ul.U9").find('li#LI51').addClass('active');
					});
					 jQuery("#employeeDob").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#joinedDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#confirmationDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#contractStartDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#contractEndDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});

					jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
					
					jQuery.validator.addMethod("greaterThan", 
						function(value, element, params) {

							if (!/Invalid|NaN/.test(new Date(value))) {
								return new Date(value) > new Date(jQuery(params).val());
							}

							return isNaN(value) && isNaN(jQuery(params).val()) 
								|| (Number(value) > Number(jQuery(params).val())); 
						},'Must be greater than {0}.');

					jQuery.validator.addMethod("greaterThanEqual", 
						function(value, element, params) {

							if (!/Invalid|NaN/.test(new Date(value))) {
								return new Date(value) >= new Date(jQuery(params).val());
							}

							return isNaN(value) && isNaN(jQuery(params).val()) 
								|| (Number(value) >= Number(jQuery(params).val())); 
						},'Must be greater than {0}.');

					

					jQuery("#employee-general-form").validate({
    
						// Specify the validation rules
						rules: {
							employeeFirstName: "required",
							employeeLastName: "required",
							employeeCode: "required",
							employeeDob: "required",
							personalContactNumber: "required",
							officeContactNumber: "required",
							personalEmailId: "required",
							officialEmailid: "required",
							dropdown_currentstatus: "required",
							permanentAddressStreet1: "required",							
							permanentAddressCity: "required",
							permanentAddressState: "required",							
							permanentAddressCountry: "required",
							permanentAddressZipCode: "required",
							correspondenseAddressStreet1: "required",
							
							correspondenseAddressCity: "required",
							correspondenseAddressState: "required",
							correspondenseAddressCountry: "required",
							correspondenseAddressZipCode: "required",
							joinedDate: "required",
							confirmationDate: "required",
							contractStartDate: "required",
							contractEndDate: "required",
							contractStartDate : { greaterThanEqual: "#joinedDate" },
							contractEndDate: { greaterThan: "#contractStartDate" },
							confirmationDate: { greaterThan: "#contractEndDate" }

							
						},
						
						// Specify the validation error messages
						messages: {
							/*employeeFirstName: "Please enter your first name"
							employeeLastName: "Please enter your last name",
							employeeCode: "Please enter your employee code",
							employeeDob: "Please enter date of birth",
							personalContactNumber: "Please enter your personal contact no."*/
							//contractEndDate : "Contract end date should be greater than Contract start date"

							
						},
						
						submitHandler: function(form) {
							var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveEmployee.action?userId='+userId;
	
	var employeeFirstName = jQuery("#employeeFirstName").val();
	var employeeLastName = jQuery("#employeeLastName").val();
	var employeeLocation = jQuery("#employeeLocation").val();
	var ssnNumber = jQuery("#ssnNumber").val();
	var employeeCode = jQuery("#employeeCode").val();
	var employeeDob = jQuery("#employeeDob").val();
	var joinedDate = jQuery("#joinedDate").val();
	var confirmationDate = jQuery("#confirmationDate").val();
	var panCardNumber = jQuery("#panCardNumber").val();
	var pfAccountNumber = jQuery("#pfAccountNumber").val();	
	var contractStartDate = jQuery("#contractStartDate").val();
	var contractEndDate = jQuery("#contractEndDate").val();
	var permanentAddressStreet1 = jQuery("#permanentAddressStreet1").val();
	var permanentAddressStreet2 = jQuery("#permanentAddressStreet2").val();
	var permanentAddressCity = jQuery("#permanentAddressCity").val();
	var permanentAddressState = jQuery("#permanentAddressState").val();
	var permanentAddressCountry = jQuery("#permanentAddressCountry").val();
	var permanentAddressZipCode = jQuery("#permanentAddressZipCode").val();
	var correspondenseAddressStreet1 = jQuery("#correspondenseAddressStreet1").val();
	var correspondenseAddressStreet2 = jQuery("#correspondenseAddressStreet2").val();
	var correspondenseAddressCity = jQuery("#correspondenseAddressCity").val();
	var correspondenseAddressState = jQuery("#correspondenseAddressState").val();
	var correspondenseAddressCountry = jQuery("#correspondenseAddressCountry").val();
	var correspondenseAddressZipCode = jQuery("#correspondenseAddressZipCode").val();
	var personalContactNumber = jQuery("#personalContactNumber").val();
	var officeContactNumber = jQuery("#officeContactNumber").val();
	var personalEmailId = jQuery("#personalEmailId").val();
	var officialEmailid = jQuery("#officialEmailid").val();
	
	var dropdown_currentstatus = jQuery(jQuery("#dropdown_currentstatus")).find('option:selected').attr('id');
	var dropdown_reportingmanager = jQuery(jQuery("#dropdown_reportingmanager")).find('option:selected').attr('id');
	var dropdown_role = jQuery(jQuery("#dropdown_role")).find('option:selected').attr('id');	
	var dropdown_department = jQuery(jQuery("#dropdown_department")).find('option:selected').attr('id');
	var dropdown_designation = jQuery(jQuery("#dropdown_designation")).find('option:selected').attr('id');
	var isEmployeeActive = jQuery('#isEmployeeActive').is(':checked') ? isEmployeeActive = "on" : isEmployeeActive = "off";

	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			employeeFirstName : employeeFirstName,
			employeeLastName : employeeLastName,
			ssnNumber : ssnNumber,
			employeeLocation : employeeLocation,
			employeeCode	: employeeCode,
			employeeDob		: employeeDob,
			joinedDate		: joinedDate,
			confirmationDate	: confirmationDate,
			contractStartDate	: contractStartDate,
			contractEndDate		: contractEndDate,
			permanentAddressStreet1	: permanentAddressStreet1,
			permanentAddressStreet2 : permanentAddressStreet2,
			permanentAddressCity	: permanentAddressCity,
			permanentAddressState	: permanentAddressState,
			permanentAddressCountry	: permanentAddressCountry,
			permanentAddressZipCode	: permanentAddressZipCode,
			panCardNumber : panCardNumber,
			pfAccountNumber	: pfAccountNumber,
			correspondenseAddressStreet1 : correspondenseAddressStreet1,
			correspondenseAddressStreet2 : correspondenseAddressStreet2,
			correspondenseAddressCity	: correspondenseAddressCity,
			correspondenseAddressState	: correspondenseAddressState,
			correspondenseAddressCountry : correspondenseAddressCountry,
			correspondenseAddressZipCode : correspondenseAddressZipCode,
			personalContactNumber	: personalContactNumber,
			officeContactNumber : officeContactNumber,
			personalEmailId : personalEmailId,
			officialEmailid : officialEmailid,
			dropdown_currentstatus : dropdown_currentstatus,
			dropdown_reportingmanager : dropdown_reportingmanager,
			dropdown_department : dropdown_department,
			dropdown_designation : dropdown_designation,
			dropdown_role : dropdown_role,
			activeStatus : isEmployeeActive

		},
		type : 'POST',
		success : function(response) {
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employee_modal').modal('toggle');
				jQuery('#success_employee_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employee_modal').modal('toggle');
				jQuery('#error_employee_modal').modal('view');
																
			}


			var JsonStringify_Data = JSON.stringify(response.data);
			var editEmployee_Data = response.data;
			localStorage.setItem('editEmployee_Data', JsonStringify_Data);
			var gemsEmployeeMasterId = response.data.gemsEmployeeMasterId;
			localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			//window.location.href = "editEmployee.html";
			
			



		},
		failure : function(data) {
			jQuery("#loading-div-background").hide();
			jQuery('#error_employee_modal').modal('toggle');
			jQuery('#error_employee_modal').modal('view');
			window.location.href = "../dashboard/dashboard.html";
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});
						}
					});
					
					var userId = sessionStorage.getItem("userId");
					
					employeeStatusDropDownList = jQuery('#dropdown_currentstatus');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewEmploymentStatusList.action?userId='
							+ userId;
					console.log(serviceURL);
					
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsEmploymentStatusId : val.gemsEmploymentStatusId,
									statusCodeDescription : val.statusCodeDescription
								};
							});

							// iterate over the data and append a select option
							employeeStatusDropDownList.append('<option id="0">Select Employement Status</option>');
							jQuery.each(result, function(key, val) {
								employeeStatusDropDownList.append('<option id="'
										+ val.gemsEmploymentStatusId + '">'
										+ val.statusCodeDescription + '</option>');
								
								
							})
							
							jQuery("#dropdown_currentstatus").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});

					applicationRoleList = jQuery('#dropdown_role');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewAllRoles.action?userId='
							+ userId;
					console.log(serviceURL);
					
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsRoleMasterId : val.gemsRoleMasterId,
									roleName : val.roleName
								};
							});

							// iterate over the data and append a select option
							applicationRoleList.append('<option id="0">Select Application Role</option>');
							jQuery.each(result, function(key, val) {
								applicationRoleList.append('<option id="'
										+ val.gemsRoleMasterId + '">'
										+ val.roleName + '</option>');
								
								
							})
							
							jQuery("#dropdown_role").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});

					reportingManagerList = jQuery('#dropdown_reportingmanager');
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeList.action?userId='
							+ userId+'&showAllEmployees=true';
					console.log(serviceURL);
					
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsEmployeeMasterId : val.gemsEmployeeMasterId,
									employeeCodeName : val.employeeCodeName
								};
							});

							// iterate over the data and append a select option
							reportingManagerList.append('<option id="0">Select Reporting Manager</option>');
							jQuery.each(result, function(key, val) {
								
								reportingManagerList.append('<option id="'
											+ val.gemsEmployeeMasterId + '">'
											+ val.employeeCodeName + '</option>');
								
								
								
								
							})
							jQuery("#dropdown_reportingmanager").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});
					

					departmentDownList = jQuery('#dropdown_department');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewDepartmentList.action?userId='
							+ userId;
					console.log(serviceURL);
					
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsDepartmentId : val.gemsDepartmentId,
									departmentCode : val.departmentCode
								};
							});

							// iterate over the data and append a select option
							departmentDownList.append('<option id="0">Select Department</option>');
							jQuery.each(result, function(key, val) {
								departmentDownList.append('<option id="'
										+ val.gemsDepartmentId + '">'
										+ val.departmentCode + '</option>');
								
								
							})
							
							jQuery("#dropdown_department").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});

					designationDownList = jQuery('#dropdown_designation');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewDesignationList.action?userId='
							+ userId;
					console.log(serviceURL);
					
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsDesignationId : val.gemsDesignationId,
									gemsDesignationCode : val.gemsDesignationCode
								};
							});

							// iterate over the data and append a select option
							designationDownList.append('<option id="0">Select Designation</option>');
							jQuery.each(result, function(key, val) {
								designationDownList.append('<option id="'
										+ val.gemsDesignationId + '">'
										+ val.gemsDesignationCode + '</option>');
								
								
							})
							
							jQuery("#dropdown_designation").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});
					
					
					
					jQuery('#newEmployee_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
					
					jQuery('#employeeFirstName').focus();
				
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editEmployee.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newEmployee.html";
					});

	

					
					
					
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}



