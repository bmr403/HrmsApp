
	  jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");




	

	
	 
	
		
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeEducationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeEducation_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("gemsEmployeeMasterId").value = gemsEmployeeMasterId;
	 
	 
	 
	 
	 
	 
	 
	 
	
	


function employeeEducation_jQueryDataTableAjax(serviceURL) {
						
						
						
						var educationList_table = jQuery("#myEducationList_table")
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
													}													
													 ],

										});
					}



