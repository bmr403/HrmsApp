
	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");


		jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

	jQuery('#empEducation_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});


					jQuery('#ok_educationbtn').click(function(e) {
						jQuery('#success_employeeeducation_modal').modal('hide');
					});
	jQuery('#error_educationbtn').click(function(e) {
						jQuery('#error_employeeeducation_modal').modal('hide');
					});
	
	course_dropdownList = jQuery('#course_dropdown');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewEducationMasterList.action?userId='
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
									gemsEducationMasterId : val.gemsEducationMasterId,
									educationCode : val.educationCode
								};
							});

							// iterate over the data and append a select option
							course_dropdownList.append('<option id="0">Select Education</option>');
							jQuery.each(result, function(key, val) {
								course_dropdownList.append('<option id="'
										+ val.gemsEducationMasterId + '">'
										+ val.educationCode + '</option>');
								
								
							})
							
							jQuery("#course_dropdown").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
	});
	

	jQuery("#addEmployeeEducationBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveEmployeeEducationDetail.action?userId='+userId;
	
	var gemsEmployeeMasterId = jQuery("#empExpGemsEmployeeMasterId").val();	
	var gemsEmployeeEducationDetailId = jQuery("#gemsEmployeeEducationDetailId").val();
	var course_dropdown = jQuery(jQuery("#course_dropdown")).find('option:selected').attr('id'); 
	
   
	var universityName = jQuery("#universityName").val();
	var yearOfPass = jQuery("#yearOfPass").val();
	var yearPercentage = jQuery("#yearPercentage").val();
	var isCourseRegular = jQuery('#isCourseRegular').is(':checked') ? isCourseRegular = "on" : isCourseRegular = "off";
	var educationActiveStatus = jQuery('#educationActiveStatus').is(':checked') ? educationActiveStatus = "on" : educationActiveStatus = "off";
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			gemsEmployeeEducationDetailId : gemsEmployeeEducationDetailId,
			course_dropdown	: course_dropdown,
			universityName		: universityName,
			yearOfPass		: yearOfPass,
			yearPercentage	: yearPercentage,
			isCourseRegular	: isCourseRegular,
			educationActiveStatus : educationActiveStatus

		},
		type : 'POST',
		success : function(response) {
			var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeEducationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
			employeeEducation_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-education-form')[0].reset();
					
					// setting id
			document.getElementById("empExpGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employeeeducation_modal').modal('toggle');
				jQuery('#success_employeeeducation_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employeeeducation_modal').modal('toggle');
				jQuery('#error_employeeeducation_modal').modal('view');																
			}



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
	});
	 
	
		
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeEducationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeEducation_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("empExpGemsEmployeeMasterId").value = gemsEmployeeMasterId;


function employeeEducation_jQueryDataTableAjax(serviceURL) {
						
						
						
						var educationList_table = jQuery("#employeeEducationList_table")
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
														"mData" : "selected_education"
													},
													{
														"mData" : "isCourseRegular"
													},
													{
														"mData" : "universityName"
													},
													{
														"mData" : "yearOfPass"
													},
													{
														"mData" : "yearPercentage"
													},													
													{
														"mData" : "gemsEmployeeEducationDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeEducationDetailId) {
															return '<a href = "#" onClick = "editEducation('
																	+ gemsEmployeeEducationDetailId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteEducation('
																	+ gemsEmployeeEducationDetailId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

function deleteEducation(gemsEmployeeEducationDetailId) {
	localStorage.setItem('gemsEmployeeEducationDetailId', gemsEmployeeEducationDetailId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteEmployeeEducationDetail.action?userId='+userId+'&objectId='
			+ gemsEmployeeEducationDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeEducationDetailId : gemsEmployeeEducationDetailId
		},
		type : 'Post',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeEducationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeEducation_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-education-form')[0].reset();
			document.getElementById("empExpGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
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

function editEducation(gemsEmployeeEducationDetailId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeEducationDetail.action?userId=' + userId + '&gemsEmployeeEducationDetailId='
			+ gemsEmployeeEducationDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeEducationDetailId : gemsEmployeeEducationDetailId
		},
		type : 'Post',
		success : function(response) {
			var data = JSON.stringify(response.data);
				
			if (data !== null) {
				response.data.isCourseRegular ? jQuery("#isCourseRegular").prop('checked', true) : jQuery("#isCourseRegular").prop('checked', false);
				var isCourseRegular = jQuery('#isCourseRegular').is(':checked') ? isCourseRegular = "on" : isCourseRegular = "off";

				response.data.educationActiveStatus ? jQuery("#educationActiveStatus").prop('checked', true) : jQuery("#educationActiveStatus").prop('checked', false);
				var educationActiveStatus = jQuery('#educationActiveStatus').is(':checked') ? educationActiveStatus = "on" : educationActiveStatus = "off";
				
				jQuery('#empExpGemsEmployeeMasterId').val(response.data.gemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.gemsEmployeeMasterId);
				
				jQuery('#gemsEmployeeEducationDetailId').val(response.data.gemsEmployeeEducationDetailId);
				localStorage.setItem('gemsEmployeeEducationDetailId', response.data.gemsEmployeeEducationDetailId);	
				
				jQuery("#gemsEmployeeEducationDetailId").val(response.data.gemsEmployeeEducationDetailId);
				jQuery("#isCourseRegular").val(isCourseRegular);

				jQuery("#yearPercentage").val(response.data.yearPercentage);
				jQuery("#yearOfPass").val(response.data.yearOfPass);

				jQuery("#universityName").val(response.data.universityName);
				

				jQuery('#course_dropdown').empty();

				course_dropdownList1 = jQuery('#course_dropdown');
				var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewEducationMasterList.action?userId='
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
									gemsEducationMasterId : val.gemsEducationMasterId,
									educationCode : val.educationCode
								};
							});

							// iterate over the data and append a select option
							
							jQuery.each(result, function(key, val) {
								if (val.gemsEducationMasterId == response.data.selectedGemsEducationMasterId)
								{
									course_dropdownList1.append('<option id="'
										+ val.gemsEducationMasterId + '" selected>'
										+ val.educationCode + '</option>');
								}
								else
								{
									course_dropdownList1.append('<option id="'
										+ val.gemsEducationMasterId + '">'
										+ val.educationCode + '</option>');
								}
								
							})
							
							jQuery("#course_dropdown").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								alert("Session will be Expired");
								window.location.href = "../../";

							}
						}
					});
						
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