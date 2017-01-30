jQuery(document)
		.ready(
				function() {
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L9').addClass('active');
						jQuery("ul.U9").show();
						jQuery("ul.U9").find('li#LI51').addClass('active');
					});

					 jQuery("#empEducation").load("employeeEducation.html");
					 jQuery("#empSkill").load("employeeSkill.html");
					 jQuery("#empExperiense").load("employeeExperiense.html");
					 jQuery("#empDependent").load("employeeDependent.html");
					 jQuery("#empDocument").load("employeeDocument.html");
					 jQuery("#empBankAccount").load("employeeBankAccountDetail.html");
					
					  jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
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
					
					


					

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					

					var userId = sessionStorage.getItem("userId");
					var data = JSON.parse(localStorage
							.getItem('editEmployee_Data'));
					if (data !== null) {
						
						jQuery('#gemsEmployeeMasterId').val(data.gemsEmployeeMasterId);
						localStorage.setItem('gemsEmployeeMasterId', data.gemsEmployeeMasterId);
						jQuery("#employeeFirstName").val(data.employeeFirstName);
						jQuery("#employeeLastName").val(data.employeeLastName);
						jQuery("#employeeCode").val(data.employeeCode);
						jQuery("#pfAccountNumber").val(data.pfAccountNumber);
						jQuery("#panCardNumber").val(data.panCardNumber);
						jQuery("#employeeDob").val(data.employeeDob);
						jQuery("#personalContactNumber").val(data.personalContactNumber);
						jQuery("#officeContactNumber").val(data.officeContactNumber);
						jQuery("#personalEmailId").val(data.personalEmailId);
						jQuery("#officialEmailid").val(data.officialEmailid);
						jQuery("#permanentAddressStreet1").val(data.permanentAddressStreet1);
						jQuery("#permanentAddressStreet2").val(data.permanentAddressStreet2);
						jQuery("#permanentAddressCity").val(data.permanentAddressCity);
						jQuery("#permanentAddressState").val(data.permanentAddressState);
						jQuery("#permanentAddressCountry").val(data.permanentAddressCountry);
						jQuery("#permanentAddressZipCode").val(data.permanentAddressZipCode);
						jQuery("#correspondenseAddressStreet1").val(data.correspondenseAddressStreet1);
						jQuery("#correspondenseAddressStreet2").val(data.correspondenseAddressStreet2);
						jQuery("#correspondenseAddressCity").val(data.correspondenseAddressCity);
						jQuery("#correspondenseAddressState").val(data.correspondenseAddressState);
						jQuery("#correspondenseAddressCountry").val(data.correspondenseAddressCountry);
						jQuery("#correspondenseAddressZipCode").val(data.correspondenseAddressZipCode);
						jQuery("#joinedDate").val(data.joinedDate);
						jQuery("#confirmationDate").val(data.confirmationDate);
						jQuery("#contractStartDate").val(data.contractStartDate);
						jQuery("#contractEndDate").val(data.contractEndDate);						
						jQuery("#ssnNumber").val(data.ssnNumber);
						data.activeStatus ? jQuery("#editEmployeeIsActive").prop('checked', true) : jQuery("#editEmployeeIsActive").prop('checked', false);
						//jQuery("#employeeLocation").val(data.employeeLocation);
						
						jQueryDropDownLocation = jQuery('#employeeLocation');
						if (data.employeeLocation == "INDIA")
						{
							jQueryDropDownLocation.append('<option id="INDIA" selected>INDIA</option>');
							jQueryDropDownLocation.append('<option id="USA">USA</option>');
						}
						else if (data.employeeLocation == "USA")
						{
							jQueryDropDownLocation.append('<option id="INDIA" >INDIA</option>');
							jQueryDropDownLocation.append('<option id="USA" selected>USA</option>');
						}
						else
						{
							jQueryDropDownLocation.append('<option id="0">Select Work Location</option>');
							jQueryDropDownLocation.append('<option id="INDIA">INDIA</option>');
							jQueryDropDownLocation.append('<option id="USA">USA</option>');
						}

						jQuery("#employeeLocation").focus();						
						

						var selectedRoleId = data.selectedRoleId;
						var selectedCurrentEmployeeStatusId = data.currentEmployeeStatusId;
						var selectedReportingManagerId = data.currentReportingToId;

						var selectedDepartmentId = data.currentDepartmentId;
						var selectedDesignationId = data.currentDesignationId;

						currentStatusList = jQuery('#dropdown_currentstatus');
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
								if (selectedCurrentEmployeeStatusId == 0)
								{
									currentStatusList.append('<option id="0">Select Employee Status</option>');
								}
								jQuery.each(result, function(key, val) {
									
									
									
									if (val.gemsEmploymentStatusId == selectedCurrentEmployeeStatusId)
									{
										currentStatusList.append('<option id="'
											+ val.gemsEmploymentStatusId + '" selected>'
											+ val.statusCodeDescription + '</option>');
									}
									else
									{
										currentStatusList.append('<option id="'
											+ val.gemsEmploymentStatusId + '">'
											+ val.statusCodeDescription + '</option>');
									}
									
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
								if (selectedReportingManagerId == 0)
								{
									reportingManagerList.append('<option id="0">Select Reporting Manager</option>');
								}
								jQuery.each(result, function(key, val) {
									
									
									if (val.gemsEmployeeMasterId == selectedReportingManagerId)
									{
										reportingManagerList.append('<option id="'
												+ val.gemsEmployeeMasterId + '" selected>'
												+ val.employeeCodeName + ' </option>');
									}
									else
									{
										reportingManagerList.append('<option id="'
												+ val.gemsEmployeeMasterId + '">'
												+ val.employeeCodeName + '</option>');
									}
									
									
									
								})
								jQuery("#dropdown_reportingmanager").focus();
							},
							error : function() {
								window.location.href = "../../view/login/sessionoutlogin.html";
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
								if (selectedRoleId == 0)
								{
									applicationRoleList.append('<option id="0">Select Application Role</option>');
								}
								jQuery.each(result, function(key, val) {
									
									
									if (val.gemsRoleMasterId == selectedRoleId)
									{
										applicationRoleList.append('<option id="'
												+ val.gemsRoleMasterId + '" selected>'
												+ val.roleName + ' </option>');
									}
									else
									{
										applicationRoleList.append('<option id="'
												+ val.gemsRoleMasterId + '">'
												+ val.roleName + '</option>');
									}
									
									
									
								})
								jQuery("#dropdown_role").focus();
							},
							error : function() {
								window.location.href = "../../view/login/sessionoutlogin.html";
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
							if (selectedReportingManagerId == 0)
							{
								departmentDownList.append('<option id="0">Select Department</option>');
							}
							jQuery.each(result, function(key, val) {
								if (val.gemsDepartmentId == selectedDepartmentId)
								{
									departmentDownList.append('<option id="'
										+ val.gemsDepartmentId + '" selected>'
										+ val.departmentCode + '</option>');
								}
								else
								{
									departmentDownList.append('<option id="'
										+ val.gemsDepartmentId + '">'
										+ val.departmentCode + '</option>');
								}
								
								
								
							})
							
							jQuery("#dropdown_department").focus();
						},
						error : function() {
							window.location.href = "../../view/login/sessionoutlogin.html";
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
							if (selectedDesignationId == 0)
							{
								designationDownList.append('<option id="0">Select Designation</option>');
							}
							jQuery.each(result, function(key, val) {

								if (val.gemsDesignationId == selectedDesignationId)
								{
									designationDownList.append('<option id="'
										+ val.gemsDesignationId + '" selected>'
										+ val.gemsDesignationCode + '</option>');
								}
								else
								{
									designationDownList.append('<option id="'
										+ val.gemsDesignationId + '">'
										+ val.gemsDesignationCode + '</option>');
								}
								
								
								
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



					} 
					var enteredProjectName = "";

					

					jQuery('#editEmployee_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
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
							employeeLocation: "required",
							employeeCode: "required",
							employeeDob: "required",
							personalContactNumber: "required",
							officeContactNumber: "required",
							personalEmailId: "required",
							officialEmailid: "required",
							dropdown_currentstatus: "required",
							dropdown_reportingmanager: "required",
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
	
	var employeeCode = jQuery("#employeeCode").val();	
	var panCardNumber = jQuery("#panCardNumber").val();
	var pfAccountNumber = jQuery("#pfAccountNumber").val();	
	var ssnNumber = jQuery("#ssnNumber").val();
	var employeeDob = jQuery("#employeeDob").val();
	var joinedDate = jQuery("#joinedDate").val();
	var confirmationDate = jQuery("#confirmationDate").val();
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
	
	var gemsEmployeeMasterId = jQuery("#gemsEmployeeMasterId").val();
	
	var isActiveStatus = jQuery('#editEmployeeIsActive').is(':checked') ? isActiveStatus = "on" : isActiveStatus = "off";
	


	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			employeeLocation : employeeLocation,
			ssnNumber : ssnNumber,
			employeeFirstName : employeeFirstName,
			employeeLastName : employeeLastName,
			employeeCode	: employeeCode,
			employeeDob		: employeeDob,
			joinedDate		: joinedDate,
			confirmationDate	: confirmationDate,
			contractStartDate	: contractStartDate,
			contractEndDate		: contractEndDate,
			permanentAddressStreet1	: permanentAddressStreet1,
			panCardNumber : panCardNumber,
			pfAccountNumber	: pfAccountNumber,
			permanentAddressStreet2 : permanentAddressStreet2,
			permanentAddressCity	: permanentAddressCity,
			permanentAddressState	: permanentAddressState,
			permanentAddressCountry	: permanentAddressCountry,
			permanentAddressZipCode	: permanentAddressZipCode,
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
			activeStatus : isActiveStatus

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
			localStorage.setItem('editEmployee_Data', JsonStringify_Data);

			
		},
		failure : function(data) {
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
					

					

					function hideAllErrors() {
												
					}
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editEmployee.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editEmployee.html";
					});

				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}