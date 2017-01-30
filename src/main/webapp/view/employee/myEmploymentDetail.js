jQuery(document)
		.ready(
				function() {
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L9').addClass('active');
						jQuery("ul.U9").show();
						jQuery("ul.U9").find('li#LI13').addClass('active');
					});

					jQuery("#myEducation").load("myEducation.html");
					 jQuery("#mySkill").load("mySkill.html");
					 jQuery("#myExperiense").load("myExperiense.html");
					 jQuery("#myDependent").load("myDependent.html");
					 jQuery("#myDocument").load("myDocument.html");
					 jQuery("#myBankAccount").load("myBankAccountDetail.html");
					 jQuery("#mySalarySlips").load("mySalarySlip.html");

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
					
		//jQuery('#payslipDate_Search').monthpicker();
		jQuery('#datepicker').datepicker();

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					jQuery("#employee-general-form :input").prop("disabled", true);

					 jQuery(window).load(function() {
						
						 var serviceURL = envConfig.serviceBaseURL
						+ '/employee/myEmploymentInfo.action?userId=' + userId;

						jQuery.ajax({
							url : serviceURL,
							dataType : "json",
							type : 'Get',
							success : function(response) {
								
								var JsonStringify_Data = JSON.stringify(response.data);
								var editEmployee_Data = response.data;
								
								//localStorage.setItem('editEmployee_Data', JsonStringify_Data);
								var gemsEmployeeMasterId = response.data.gemsEmployeeMasterId;
								localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
								//window.location.href = "myEmployeeDetail.html";
								jQuery('#gemsEmployeeMasterId').val(editEmployee_Data.gemsEmployeeMasterId);
								localStorage.setItem('gemsEmployeeMasterId', editEmployee_Data.gemsEmployeeMasterId);
								jQuery("#employeeFirstName").val(editEmployee_Data.employeeFirstName);
								jQuery("#employeeLastName").val(editEmployee_Data.employeeLastName);
								jQuery("#employeeCode").val(editEmployee_Data.employeeCode);
								jQuery("#employeeDob").val(editEmployee_Data.employeeDob);
								jQuery("#personalContactNumber").val(editEmployee_Data.personalContactNumber);
								jQuery("#officeContactNumber").val(editEmployee_Data.officeContactNumber);
								jQuery("#personalEmailId").val(editEmployee_Data.personalEmailId);
								jQuery("#officialEmailid").val(editEmployee_Data.officialEmailid);
								jQuery("#permanentAddressStreet1").val(editEmployee_Data.permanentAddressStreet1);
								jQuery("#permanentAddressStreet2").val(editEmployee_Data.permanentAddressStreet2);
								jQuery("#permanentAddressCity").val(editEmployee_Data.permanentAddressCity);
								jQuery("#permanentAddressState").val(editEmployee_Data.permanentAddressState);
								jQuery("#permanentAddressCountry").val(editEmployee_Data.permanentAddressCountry);
								jQuery("#permanentAddressZipCode").val(editEmployee_Data.permanentAddressZipCode);
								jQuery("#correspondenseAddressStreet1").val(editEmployee_Data.correspondenseAddressStreet1);
								jQuery("#correspondenseAddressStreet2").val(editEmployee_Data.correspondenseAddressStreet2);
								jQuery("#correspondenseAddressCity").val(editEmployee_Data.correspondenseAddressCity);
								jQuery("#correspondenseAddressState").val(editEmployee_Data.correspondenseAddressState);
								jQuery("#correspondenseAddressCountry").val(editEmployee_Data.correspondenseAddressCountry);
								jQuery("#correspondenseAddressZipCode").val(editEmployee_Data.correspondenseAddressZipCode);
								jQuery("#joinedDate").val(editEmployee_Data.joinedDate);
								jQuery("#pfAccountNumber").val(editEmployee_Data.pfAccountNumber);
								jQuery("#panCardNumber").val(editEmployee_Data.panCardNumber);
								jQuery("#confirmationDate").val(editEmployee_Data.confirmationDate);
								jQuery("#contractStartDate").val(editEmployee_Data.contractStartDate);
								jQuery("#contractEndDate").val(editEmployee_Data.contractEndDate);

								var selectedRoleId = editEmployee_Data.selectedRoleId;
								var selectedRole = editEmployee_Data.selectedRole;
								var selectedCurrentEmployeeStatusId = editEmployee_Data.currentEmployeeStatusId;
								var selectedStatusDescription = editEmployee_Data.currentEmployeeStatusName;
								var selectedReportingManagerId = editEmployee_Data.currentReportingToId;
								var selectedReportingManager = editEmployee_Data.currentReportingToName;
								var selectedDepartmentId = editEmployee_Data.currentDepartmentId;
								var selectedDepartmentName = editEmployee_Data.currentDepartmentName;
								var selectedDesignationId = editEmployee_Data.currentDesignationId;
								var selectedDesignationName = editEmployee_Data.currentDesignationName
								
								jQueryDropDownLocation = jQuery('#employeeLocation');
								if (editEmployee_Data.employeeLocation == "INDIA")
								{
									jQueryDropDownLocation.append('<option id="INDIA" selected>INDIA</option>');
									jQueryDropDownLocation.append('<option id="USA">USA</option>');
								}
								else if (editEmployee_Data.employeeLocation == "USA")
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

								currentStatusList = jQuery('#dropdown_currentstatus');
								if (selectedCurrentEmployeeStatusId == 0)
								{
									currentStatusList.append('<option id="0">Select Employee Status</option>');
								}
								else
								{
									currentStatusList.append('<option id="'
													+ selectedCurrentEmployeeStatusId + '" selected>'
													+ selectedStatusDescription + '</option>');
								}
								
								
								reportingManagerList = jQuery('#dropdown_reportingmanager');
								if (selectedReportingManagerId == 0)
								{
									reportingManagerList.append('<option id="0">Select Reporting Manager</option>');
								}
								else
								{
									reportingManagerList.append('<option id="'
														+ selectedReportingManagerId + '" selected>'
														+ selectedReportingManager + ' </option>');
								}
								

								applicationRoleList = jQuery('#dropdown_role');
								if (selectedRoleId == 0)
								{
											applicationRoleList.append('<option id="0">Select Application Role</option>');
								}
								else
								{
									applicationRoleList.append('<option id="'
														+ selectedRoleId + '" selected>'
														+ selectedRole + ' </option>');
								}
								

								

							departmentDownList = jQuery('#dropdown_department');
							if (selectedDepartmentId == 0)
								{
											departmentDownList.append('<option id="0">Select Department</option>');
								}
								else
								{
									departmentDownList.append('<option id="'
														+ selectedDepartmentId + '" selected>'
														+ selectedDepartmentName + ' </option>');
								}

							

								designationDownList = jQuery('#dropdown_designation');
								if (selectedDesignationId == 0)
								{
										designationDownList.append('<option id="0">Select Designation</option>');
								}
								else
								{
									designationDownList.append('<option id="'
												+ selectedDesignationId + '" selected>'
												+ selectedDesignationName + '</option>');
								}












								
							}, // end of success function
							failure : function(data) {
								window.location.href = "../../";

							},
							statusCode : {
								403 : function(xhr) {
									window.location.href = "../../view/login/sessionoutlogin.html";

								}
							}

						});


					
					
					

					 });
				

					
					

					

					

					
					
					
					
					

					


				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}