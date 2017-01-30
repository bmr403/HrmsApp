var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));

	  var userId = sessionStorage.getItem("userId");

	jQuery('#empBank_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
	

		jQuery('#ok_bankbtn').click(function(e) {
						jQuery('#success_employeebank_modal').modal('hide');
					});
					jQuery('#error_bankbtn').click(function(e) {
						jQuery('#error_employeebank_modal').modal('hide');
					});

						jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});


	jQuery("#addEmployeeBankBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveEmployeeBankDetail.action?userId='+userId;
	
	var gemsEmployeeMasterId = jQuery("#empbankGemsEmployeeMasterId").val();	
	var gemsEmpBankDetailId = jQuery("#gemsEmpBankDetailId").val();

	var bankName = jQuery("#bankName").val();
	var bankAccountNumber = jQuery("#bankAccountNumber").val();
	var bankAccountRoutingNo = jQuery("#bankAccountRoutingNo").val();
	var activeStatus = jQuery('#activeStatus').is(':checked') ? activeStatus = "on" : activeStatus = "off";
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			gemsEmpBankDetailId : gemsEmpBankDetailId,
			bankName	: bankName,
			bankAccountNumber		: bankAccountNumber,
			bankAccountRoutingNo		: bankAccountRoutingNo,
			activeStatus : activeStatus
			

		},
		type : 'POST',
		success : function(response) {
			var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeBankDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
			employeeBank_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-bank-form')[0].reset();
					
					// setting id
			document.getElementById("empbankGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employeebank_modal').modal('toggle');
				jQuery('#success_employeebank_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employeebank_modal').modal('toggle');
				jQuery('#error_employeebank_modal').modal('view');
																
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
							+ '/employee/viewEmployeeBankDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeBank_jQueryDataTableAjax(serviceURL);
					document.getElementById("empbankGemsEmployeeMasterId").value = gemsEmployeeMasterId;
	 
	 
	 
	 
	 
	 
	 
	 
	
	


function employeeBank_jQueryDataTableAjax(serviceURL) {
						
						
						
						var employeeBankList_table = jQuery("#employeeBank_table")
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
														"mData" : "bankName"
													},
													{
														"mData" : "bankAccountNumber"
													},
													{
														"mData" : "bankAccountType"
													},
													{
														"mData" : "bankAccountRoutingNo"
													},
													{
														"mData" : "gemsEmpBankDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmpBankDetailId) {
															return '<a href = "#" onClick = "editBankInfo('
																	+ gemsEmpBankDetailId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteBankInfo('
																	+ gemsEmpBankDetailId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

function deleteBankInfo(gemsEmpBankDetailId) {
	localStorage.setItem('gemsEmpBankDetailId', gemsEmpBankDetailId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteEmployeeBankDetail.action?userId='+userId+'&objectId='
			+ gemsEmpBankDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeBankDetailList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeBank_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-bank-form')[0].reset();
			document.getElementById("empbankGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
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

function editBankInfo(gemsEmpBankDetailId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeBankDetail.action?userId=' + userId + '&gemsEmpBankDetailId='
			+ gemsEmpBankDetailId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'Get',
		success : function(response) {
			var data = JSON.stringify(response.data);
				
			if (data !== null) {
				jQuery('#empbankGemsEmployeeMasterId').val(response.data.selectedGemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.selectedGemsEmployeeMasterId);
				jQuery('#gemsEmpBankDetailId').val(response.data.gemsEmpBankDetailId);
				localStorage.setItem('gemsEmpBankDetailId', response.data.gemsEmpBankDetailId);		
				jQuery("#bankName").val(response.data.bankName);
				jQuery("#bankAccountNumber").val(response.data.bankAccountNumber);
				jQuery("#bankAccountRoutingNo").val(response.data.bankAccountRoutingNo);
				response.data.activeStatus ? jQuery("#activeStatus").prop('checked', true) : jQuery("#activeStatus").prop('checked', false);
				var activeStatus = jQuery('#activeStatus').is(':checked') ? activeStatus = "on" : activeStatus = "off";			
				
				
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