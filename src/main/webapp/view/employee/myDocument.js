jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

	  var gemsEmployeeMasterId = JSON.parse(localStorage.getItem('gemsEmployeeMasterId'));
	  var userId = sessionStorage.getItem("userId");

	
	
	

	
	 
					jQuery("#empDocGemsEmployeeMasterId").val(localStorage.getItem('gemsEmployeeMasterId'));
					
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeImmigrationList.action?userId='
							+ userId+'&gemsEmployeeMasterId='+gemsEmployeeMasterId;
					
					
					employeeDoc_jQueryDataTableAjax(serviceURL);
					//document.getElementById("gemsEmployeeMasterId").value = gemsEmployeeMasterId;
					


function employeeDoc_jQueryDataTableAjax(serviceURL) {
						
						
						
						var employeeDocList_table = jQuery("#myDocList_table")
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
														"mData" : "documentType"
													},
													{
														"mData" : "documentNumber"
													},
													{
														"mData" : "issuedDate"
													},
													{
														"mData" : "expiryDate"
													},
													{
														"mData" : "issuedBy"
													},													
													{
														"mData" : "gemsEmployeeImmigrationDetailId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeImmigrationDetailId) {
															return '<a href = \"#\" onClick = "downloadEmpDoc('
																	+ gemsEmployeeImmigrationDetailId
																	+ ');"  id=\"edit_btn\"><span class=\"glyphicon glyphicon-download title=\"Download\"></span></a>';
														}
													} ],

										});
					}

function downloadEmpDoc(gemsEmployeeImmigrationDetailId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/downloadDocument.action?userId='+userId+'&objectId='
			+ gemsEmployeeImmigrationDetailId;
	
	window.location.href=serviceURL;	

}
