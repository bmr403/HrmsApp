
	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");

	jQuery('#empSkill_cancel_btn').click(function(event) {
						window.location.href = 'employee.html';
					});
	jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

	jQuery('#ok_skillbtn').click(function(e) {
						jQuery('#success_employeeskill_modal').modal('hide');
					});
					jQuery('#error_skillbtn').click(function(e) {
						jQuery('#error_employeeskill_modal').modal('hide');
					});

	jQuery("#addEmployeeSkillBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/employee/saveEmployeeSkillDetail.action?userId='+userId;
	
	var gemsEmployeeMasterId = jQuery("#empSkillGemsEmployeeMasterId").val();
	
	var gemsEmployeeSkillId = jQuery("#gemsEmployeeSkillId").val();
	var skillName = jQuery("#skillName").val();
	var versionNo = jQuery("#versionNo").val();
	var experienseInMonths = jQuery("#experienseInMonths").val();
	var lastUsed = jQuery("#lastUsed").val();
	var isPrimarySkill = jQuery('#isPrimarySkill').is(':checked') ? isPrimarySkill = "on" : isPrimarySkill = "off";
	var activeStatus = jQuery('#activeStatus').is(':checked') ? activeStatus = "on" : activeStatus = "off";
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeMasterId : gemsEmployeeMasterId,
			gemsEmployeeSkillId : gemsEmployeeSkillId,
			skillName	: skillName,
			versionNo		: versionNo,
			experienseInMonths		: experienseInMonths,
			lastUsed	: lastUsed,
			isPrimarySkill	: isPrimarySkill,
			activeStatus : activeStatus

		},
		type : 'POST',
		success : function(response) {
			var gemsEmployeeMasterId = localStorage.getItem('gemsEmployeeMasterId', gemsEmployeeMasterId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeSkillList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
			employeeSkill_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-skill-form')[0].reset();
					
					// setting id
			document.getElementById("empSkillGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery("#loading-div-background").hide();
				jQuery('#success_employeeskill_modal').modal('toggle');
				jQuery('#success_employeeskill_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_employeeskill_modal').modal('toggle');
				jQuery('#error_employeeskill_modal').modal('view');
																
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
							+ '/employee/viewEmployeeSkillList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeSkill_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("empSkillGemsEmployeeMasterId").value = gemsEmployeeMasterId;
	 


function employeeSkill_jQueryDataTableAjax(serviceURL) {
						
						
						
						var skillList_table = jQuery("#employeeSkillList_table")
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
														"mData" : "skillName"
													},
													{
														"mData" : "versionNo"
													},
													{
														"mData" : "experienseInMonths"
													},
													{
														"mData" : "isPrimarySkill"
													},
													
													{
														"mData" : "gemsEmployeeSkillId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeSkillId) {
															return '<a href = "#" onClick = "editSkill('
																	+ gemsEmployeeSkillId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteSkill('
																	+ gemsEmployeeSkillId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}

function deleteSkill(gemsEmployeeSkillId) {
	localStorage.setItem('gemsEmployeeSkillId', gemsEmployeeSkillId);
	var userId = sessionStorage.getItem("userId");
	
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/deleteEmployeeSkillDetail.action?userId='+userId+'&objectId='
			+ gemsEmployeeSkillId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeSkillId : gemsEmployeeSkillId
		},
		type : 'Post',
		success : function(response) {
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeSkillList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+localStorage.getItem('gemsEmployeeMasterId');					
					
			employeeSkill_jQueryDataTableAjax(serviceURL);
			jQuery('#employee-skill-form')[0].reset();
			document.getElementById("empSkillGemsEmployeeMasterId").value = gemsEmployeeMasterId;
			
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

function editSkill(gemsEmployeeSkillId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeSkillDetail.action?userId=' + userId + '&gemsEmployeeSkillId='
			+ gemsEmployeeSkillId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsEmployeeSkillId : gemsEmployeeSkillId
		},
		type : 'Post',
		success : function(response) {
			var data = JSON.stringify(response.data);
						
			//window.location.href = "employeeSkill.html";
			if (data !== null) {
				response.data.isPrimarySkill ? jQuery("#isPrimarySkill").prop('checked', true) : jQuery("#isPrimarySkill").prop('checked', false);
				var isPrimarySkill = jQuery('#isPrimarySkill').is(':checked') ? isPrimarySkill = "on" : isPrimarySkill = "off";
				response.data.activeStatus ? jQuery("#activeStatus").prop('checked', true) : jQuery("#activeStatus").prop('checked', false);
				var activeStatus = jQuery('#activeStatus').is(':checked') ? activeStatus = "on" : activeStatus = "off";			
				jQuery('#empSkillGemsEmployeeMasterId').val(response.data.gemsEmployeeMasterId);
				localStorage.setItem('gemsEmployeeMasterId', response.data.gemsEmployeeMasterId);
				jQuery('#gemsEmployeeSkillId').val(response.data.gemsEmployeeSkillId);
				localStorage.setItem('gemsEmployeeSkillId', response.data.gemsEmployeeSkillId);		
				jQuery("#isPrimarySkill").val(isPrimarySkill);
				jQuery("#skillName").val(response.data.skillName);
				jQuery("#versionNo").val(response.data.versionNo);
				jQuery("#experienseInMonths").val(response.data.experienseInMonths);
				jQuery("#lastUsed").val(response.data.lastUsed);		
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