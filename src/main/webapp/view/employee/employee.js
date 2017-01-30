jQuery(document)
		.ready(
				function() {


					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L9').addClass('active');
						jQuery("ul.U9").show();
						jQuery("ul.U9").find('li#LI51').addClass('active');
					});
					
					jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					
					jQuery("#employeeDob").datepicker({
						maxDate : "+6M"
					});
					jQuery("#joinedDate").datepicker({
						maxDate : "+6M"
					});
					jQuery("#confirmationDate").datepicker({
						maxDate : "+6M"
					});
					jQuery("#contactStartDate").datepicker({
						maxDate : "+6M"
					});
					jQuery("#contractEndDate").datepicker({
						maxDate : "+6M"
					});

					
					var userId = sessionStorage.getItem("userId");

					jQuery("#employeeList_table tbody").delegate("tr", "click", function() {
					var firstCellText = jQuery("td:first", this).text();
					var fourthCellText = jQuery("td:eq(6)", this).text();
						//alert("First Cell: " + firstCellText + "\nFourth Cell: " + fourthCellText);
					editGeneralInfo(firstCellText);
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
							window.location.href = "../login/sessionoutlogin.html";
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});


					function employee_jQueryDataTableAjax(serviceURL,searchEmpCode,
								searchEmpFirstName,searchEmpLastName,searchEmpMobile,dropdown_currentstatus,dropdown_department,searchEmpEmail,searchEmpActive,searchEmployeeLocation) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var employeeListTable = jQuery("#employeeList_table")
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
													"name" : "searchEmpCode",
													"value" : searchEmpCode
												}, {
													"name" : "searchEmpFirstName",
													"value" : searchEmpFirstName
												},{
													"name" : "searchEmpLastName",
													"value" : searchEmpLastName
												},{
													"name" : "searchEmpMobile",
													"value" : searchEmpMobile
												},{
													"name" : "dropdown_currentstatus",
													"value" : dropdown_currentstatus
												},{
													"name" : "dropdown_department",
													"value" : dropdown_department
												},{
													"name" : "searchEmpEmail",
													"value" : searchEmpEmail
												},{
													"name" : "searchEmpActive",
													"value" : searchEmpActive
												},{
													"name" : "searchEmployeeLocation",
													"value" : searchEmployeeLocation
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
														"mData" : "employeeCode"														
													},
													{
														"mData" : "empName"
													},
													{
														"mData" : "contactNumber"
													},
													{
														"mData" : "contactEmail"
													},
													{
														"mData" : "activeStatusString"														
													},
													{
														"mData" : "selected_employeestatusdesc"
													},
													{
														"mData" : "gemsEmployeeMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeMasterId) {
																	return '<a href = "#" onClick = "editEmployee('
																	+ gemsEmployeeMasterId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteEmployee('
																	+ gemsEmployeeMasterId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeList.action?userId='
							+ userId;
					searchEmpCode = "";
					searchEmpFirstName = "";
					searchEmpLastName = "";
					searchEmpMobile = "";
					dropdown_currentstatus = "";
					dropdown_department = "";
					searchEmpEmail = "";
					searchEmpActive = "";
					searchEmployeeLocation = "";

					/*employee_jQueryDataTableAjax(serviceURL,searchEmpCode,
								searchEmpFirstName,searchEmpLastName,searchEmpMobile,dropdown_currentstatus,dropdown_department,searchEmpEmail,searchEmpActive,searchEmployeeLocation);*/
					
					function employeeTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/employee/viewEmployeeList.action?userId='
								+ userId;
						searchEmpCode = jQuery('#searchEmpCode').val();
						searchEmpFirstName = jQuery('#searchEmpFirstName').val();
						searchEmpLastName = jQuery('#searchEmpLastName').val();
						searchEmpMobile = jQuery('#searchEmpMobile').val();
						dropdown_currentstatus = jQuery(jQuery("#dropdown_currentstatus")).find('option:selected').attr('id');
						dropdown_department = jQuery(jQuery("#dropdown_department")).find('option:selected').attr('id');
						searchEmpEmail = jQuery('#searchEmpEmail').val();
						searchEmpActive = jQuery('#searchEmpActive').is(':checked') ? searchEmpActive = "on" : searchEmpActive = "off";
						searchEmployeeLocation = jQuery('#searchEmployeeLocation').val();
						if (searchEmployeeLocation == 'Select Work Location')
						{
							searchEmployeeLocation = '0';
						}

						employee_jQueryDataTableAjax(serviceURL,searchEmpCode,
								searchEmpFirstName,searchEmpLastName,searchEmpMobile,dropdown_currentstatus,dropdown_department,searchEmpEmail,searchEmpActive,searchEmployeeLocation);

					}
					jQuery("#search_employeelist_btn").click(function(e) {

						e.preventDefault();
						employeeTable_Search();

					});
					jQuery("#resetsearch_employee_btn").click(function(e) {
						
						jQuery('#searchEmpCode').val("");
						jQuery('#searchEmpFirstName').val("");
						jQuery('#searchEmpLastName').val("");
						jQuery('#searchEmpMobile').val("");
						jQuery('#dropdown_currentstatus').val("0");
						jQuery('#searchEmpEmail').val("");
						jQuery('#searchEmployeeLocation').val("0");
						jQuery('#dropdown_department').val("0");						
						jQuery('#searchEmpSkill').val("");

					});

					
				});
function editEmployee(gemsEmployeeMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeInfo.action?userId=' + userId + '&gemsEmployeeMasterId='
			+ gemsEmployeeMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editEmployee_Data = response.data;
			localStorage.setItem('editEmployee_Data', JsonStringify_Data);
			var gemsEmployeeMasterId = response.data.gemsEmployeeMasterId;
			localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			window.location.href = "editEmployee.html";
			
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

function leaveAllotement(gemsEmployeeMasterId) {

	var userId = sessionStorage.getItem("userId");

		var serviceURL = envConfig.serviceBaseURL
				+ '/employee/runEmployeeLeaveSummary.action?userId='+userId+'&gemsEmployeeMasterId='
				+ gemsEmployeeMasterId;

		jQuery.ajax({
			url : serviceURL,
			dataType : "json",
			type : 'GET',
			success : function(response) {
				if(response.success == true){
				window.location.href = "employee.html";
				} else {
					window.location.href = "../../";
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



function deleteEmployee(gemsEmployeeMasterId) {
	localStorage.setItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
	jQuery('#delete_employee_modal').modal('toggle');
	jQuery('#delete_employee_modal').modal('view');

}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
function editGeneralInfo(employeeCode)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeInfo.action?userId=' + userId + '&employeeCode='
			+ employeeCode;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			employeeCode : employeeCode
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editEmployee_Data = response.data;
			localStorage.setItem('editEmployee_Data', JsonStringify_Data);
			//window.location.href = "editEmployee.html";
			var data = JSON.parse(localStorage.getItem('editEmployee_Data'));
			if (data !== null) {

				jQuery('#gemsEmployeeMasterId').val(data.gemsEmployeeMasterId);
				localStorage.setItem('countryId_edit', data.gemsCountryMasterId);
				jQuery("#employeeFirstName").val(data.employeeFirstName);
				jQuery("#employeeLastName").val(data.employeeLastName);
				jQuery("#employeeCode").val(data.employeeCode);
				jQuery("#employeeDob").val(data.employeeDob);
				jQuery("#joinedDate").val(data.dateOfJoining);
				jQuery("#confirmationDate").val(data.confirmationDate);
				jQuery("#contactStartDate").val(data.contractStartDate);
				jQuery("#contractEndDate").val(data.contractEndDate);
				employeeStatusDropDown(data.currentEmployeeStatusId,data.currentEmployeeStatusName);
				employeeReportingManager(data.currentReportingToId,data.currentReportingToName);
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
function employeeStatusDropDown(currentEmployeeStatusId,currentEmployeeStatusName)
{
	
	if (typeof currentEmployeeStatusId == 'undefined')
	{

	}
	else
	{
		var userId = sessionStorage.getItem("userId");
	jQueryselect = jQuery('#dropdown_currentstatus');
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
							jQuery.each(result, function(key, val) {
								if (val.gemsEmploymentStatusId == currentEmployeeStatusId)
								{
									jQueryselect.append('<option id="'
										+ val.gemsEmploymentStatusId + '">'
										+ val.statusCodeDescription + ' selected</option>');
								}
								else
								{
									jQueryselect.append('<option id="'
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
	}
	
}

function employeeReportingManager(currentReportingToId,currentReportingToName)
{
	// reporting manager
	var userId = sessionStorage.getItem("userId");
					jQuerymanagerselect = jQuery('#dropdown_reportingmanager');
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeList.action?userId='
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
									gemsEmployeeMasterId : val.gemsEmployeeMasterId,
									employeeCodeName : val.employeeCodeName
								};
							});

							// iterate over the data and append a select option
							jQuery.each(result, function(key, val) {
								
								if (val.gemsEmployeeMasterId == currentReportingToId)
								{
									jQuerymanagerselect.append('<option id="'
											+ val.gemsEmployeeMasterId + '">'
											+ val.employeeCodeName + ' selected</option>');
								}
								else
								{
									jQuerymanagerselect.append('<option id="'
											+ val.gemsEmployeeMasterId + '">'
											+ val.employeeCodeName + '</option>');
								}
								
								
								
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
}
