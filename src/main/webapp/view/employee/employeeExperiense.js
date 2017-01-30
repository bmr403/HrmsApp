jQuery("#fromDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#toDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
	  
	jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
	   
	  
	  
	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));

	  var userId = sessionStorage.getItem("userId");

	jQuery('#empExp_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
	
jQuery('#ok_Expbtn').click(function(e) {
						jQuery('#success_employeeExp_modal').modal('hide');
					});
					jQuery('#error_Expbtn').click(function(e) {
						jQuery('#error_employeeExp_modal').modal('hide');
					});
	

	jQuery("#addEmployeeExpBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveGemsEmployeeWorkExp.action?userId='+userId;
	
	var gemsEmployeeMasterId = jQuery("#empExpGemsEmployeeMasterId").val();	
	var gemsEmpWorkExpId = jQuery("#gemsEmpWorkExpId").val();
    var expActiveStatus = jQuery('#expActiveStatus').is(':checked') ? expActiveStatus = "on" : expActiveStatus = "off";
	var companyName = jQuery("#companyName").val();
	var jobTitle = jQuery("#jobTitle").val();
	var fromDate = jQuery("#fromDate").val();
	var toDate = jQuery("#toDate").val();
	var totalExpInMonth = jQuery("#totalExpInMonth").val();
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			gemsEmpWorkExpId : gemsEmpWorkExpId,
			companyName	: companyName,
			jobTitle		: jobTitle,
			fromDate		: fromDate,
			toDate	: toDate,
			expActiveStatus : expActiveStatus
			

		},
		type : 'POST',
		success : function(response) {
			var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewGemsEmployeeWorkExpList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
			employeeWorkExp_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-workexp-form')[0].reset();
					
					// setting id
			document.getElementById("empExpGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employeeExp_modal').modal('toggle');
				jQuery('#success_employeeExp_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employeeExp_modal').modal('toggle');
				jQuery('#error_employeeExp_modal').modal('view');
																
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
							+ '/employee/viewGemsEmployeeWorkExpList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeWorkExp_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("empExpGemsEmployeeMasterId").value = gemsEmployeeMasterId;
	 
	 
	 
	 
	 
	 
	 
	 
	
	


function employeeWorkExp_jQueryDataTableAjax(serviceURL) {
						
						
						
						var employeeExpList_table = jQuery("#employeeExpList_table").DataTable(
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
														"mData" : "companyName"
													},
													{
														"mData" : "jobTitle"
													},
													{
														"mData" : "fromDate"
													},
													{
														"mData" : "toDate"
													},
													{
														"mData" : "totalExpInMonth"
													},													
													{
														"mData" : "gemsEmpWorkExpId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmpWorkExpId) {
															return '<a href = "#" onClick = "editWorkExp('
																	+ gemsEmpWorkExpId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteWorkExp('
																	+ gemsEmpWorkExpId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

function deleteWorkExp(gemsEmpWorkExpId) {
	localStorage.setItem('gemsEmpWorkExpId', gemsEmpWorkExpId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteGemsEmployeeWorkExp.action?userId='+userId+'&objectId='
			+ gemsEmpWorkExpId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewGemsEmployeeWorkExpList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeWorkExp_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-workexp-form')[0].reset();
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

function editWorkExp(gemsEmpWorkExpId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getGemsEmployeeWorkExpDetail.action?userId=' + userId + '&gemsEmpWorkExpId='
			+ gemsEmpWorkExpId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmpWorkExpId : gemsEmpWorkExpId
		},
		type : 'Post',
		success : function(response) {
			var data = JSON.stringify(response.data);
					
			if (data !== null) {
				jQuery('#empExpGemsEmployeeMasterId').val(response.data.gemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.gemsEmployeeMasterId);
				jQuery('#gemsEmpWorkExpId').val(response.data.gemsEmpWorkExpId);
				localStorage.setItem('gemsEmpWorkExpId', response.data.gemsEmpWorkExpId);		
				jQuery("#companyName").val(response.data.companyName);
				jQuery("#jobTitle").val(response.data.jobTitle);
				jQuery("#fromDate").val(response.data.fromDate);
				jQuery("#toDate").val(response.data.toDate);
				jQuery("#totalExpInMonth").val(response.data.totalExpInMonth);
				response.data.expActiveStatus ? jQuery("#expActiveStatus").prop('checked', true) : jQuery("#expActiveStatus").prop('checked', false);
				var expActiveStatus = jQuery('#expActiveStatus').is(':checked') ? expActiveStatus = "on" : expActiveStatus = "off";		
				
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