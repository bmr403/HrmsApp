jQuery("#dependentDateOfBirth").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					
	   jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
	  
	  
	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));

	  var userId = sessionStorage.getItem("userId");

	jQuery('#empDependent_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
	

		jQuery('#ok_dependentbtn').click(function(e) {
						jQuery('#success_employeedependent_modal').modal('hide');
					});
					jQuery('#error_dependentbtn').click(function(e) {
						jQuery('#error_employeedependent_modal').modal('hide');
					});

	jQuery("#addEmployeeDependentBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveGemsEmployeeDependentDetail.action?userId='+userId;
	
	var gemsEmployeeMasterId = jQuery("#empDependentGemsEmployeeMasterId").val();	
	var gemsEmployeeDependentDetailId = jQuery("#gemsEmployeeDependentDetailId").val();

	var dependentName = jQuery("#dependentName").val();
	var dependentRelationship = jQuery("#dependentRelationship").val();
	var dependentDateOfBirth = jQuery("#dependentDateOfBirth").val();
	var dependentactiveStatus = jQuery('#dependentactiveStatus').is(':checked') ? dependentactiveStatus = "on" : dependentactiveStatus = "off";
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			gemsEmployeeDependentDetailId : gemsEmployeeDependentDetailId,
			dependentName	: dependentName,
			dependentRelationship		: dependentRelationship,
			dependentDateOfBirth		: dependentDateOfBirth,
			dependentactiveStatus : dependentactiveStatus
			

		},
		type : 'POST',
		success : function(response) {
			var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeDependentDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
			employeeDependent_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-dependent-form')[0].reset();
					
					// setting id
			document.getElementById("empDependentGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employeedependent_modal').modal('toggle');
				jQuery('#success_employeedependent_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employeedependent_modal').modal('toggle');
				jQuery('#error_employeedependent_modal').modal('view');
																
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
							+ '/employee/viewEmployeeDependentDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeDependent_jQueryDataTableAjax(serviceURL);
					document.getElementById("empDependentGemsEmployeeMasterId").value = gemsEmployeeMasterId;
	 
	 
	 
	 
	 
	 
	 
	 
	
	


function employeeDependent_jQueryDataTableAjax(serviceURL) {
						
						
						
						var employeeExpList_table = jQuery("#employeeDependentList_table")
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
														"mData" : "dependentName"
													},
													{
														"mData" : "dependentRelationship"
													},
													{
														"mData" : "dependentDateOfBirth"
													},
													{
														"mData" : "gemsEmployeeDependentDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeDependentDetailId) {
															return '<a href = "#" onClick = "editDependentInfo('
																	+ gemsEmployeeDependentDetailId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteDependentInfo('
																	+ gemsEmployeeDependentDetailId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

function deleteDependentInfo(gemsEmployeeDependentDetailId) {
	localStorage.setItem('gemsEmployeeDependentDetailId', gemsEmployeeDependentDetailId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteGemsEmployeeDependentDetail.action?userId='+userId+'&objectId='
			+ gemsEmployeeDependentDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeDependentDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeDependent_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-dependent-form')[0].reset();
			document.getElementById("empDependentGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}
}

function editDependentInfo(gemsEmployeeDependentDetailId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getGemsEmployeeDependentDetail.action?userId=' + userId + '&gemsEmployeeDependentDetailId='
			+ gemsEmployeeDependentDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeDependentDetailId : gemsEmployeeDependentDetailId
		},
		type : 'Post',
		success : function(response) {
			var data = JSON.stringify(response.data);
				
			if (data !== null) {
				jQuery('#empDependentGemsEmployeeMasterId').val(response.data.selectedGemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.selectedGemsEmployeeMasterId);
				jQuery('#gemsEmployeeDependentDetailId').val(response.data.gemsEmployeeDependentDetailId);
				localStorage.setItem('gemsEmployeeDependentDetailId', response.data.gemsEmployeeDependentDetailId);		
				jQuery("#dependentName").val(response.data.dependentName);
				jQuery("#dependentRelationship").val(response.data.dependentRelationship);
				jQuery("#dependentDateOfBirth").val(response.data.dependentDateOfBirth);
				response.data.dependentactiveStatus ? jQuery("#dependentactiveStatus").prop('checked', true) : jQuery("#dependentactiveStatus").prop('checked', false);
				var dependentactiveStatus = jQuery('#dependentactiveStatus').is(':checked') ? dependentactiveStatus = "on" : dependentactiveStatus = "off";	
				
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