jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI74').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			
						
				function projectResource_jQueryDataTableAjax(projectMapGrid_serviceURL,
							selectedGemsProjectMasterId, selectedGemsEmployeeMasterId) {
						var userId = sessionStorage.getItem("userId");
						//var projectMapGrid_serviceURL = envConfig.serviceBaseURL + '/project/viewProjectResourceList.action?userId='+ userId;
						console.log(projectMapGrid_serviceURL);
						var projectResourceMap_Table = jQuery("#projectResourceMap_Table").DataTable(
										{
											"sAjaxSource" : projectMapGrid_serviceURL,
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
													"name" : "selectedGemsProjectMasterId",
													"value" : selectedGemsProjectMasterId
												}, {
													"name" : "selectedGemsEmployeeMasterId",
													"value" : selectedGemsEmployeeMasterId
												});
											},
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
														"mData" : "projectName"
													},
													{
														"mData" : "resourceStartDate"
													},
													{
														"mData" : "resourceEndDate"
													}
												
													 ],

										});	
		}										
										
		
	
	
		var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL + '/project/viewProjectResourceList.action?userId=' + userId+'&showMyProject=true';
					selectedGemsProjectMasterId = "";
					selectedGemsEmployeeMasterId = "";
					
				projectResource_jQueryDataTableAjax(serviceURL, selectedGemsProjectMasterId, selectedGemsEmployeeMasterId);
					
					
					
					function projectResourceTable_Search(selectedGemsProjectMasterId,selectedGemsEmployeeMasterId) {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/project/viewProjectResourceList.action?userId=' + userId+'&showMyProject=true';
						
						projectResource_jQueryDataTableAjax(serviceURL, selectedGemsProjectMasterId, selectedGemsEmployeeMasterId);

					}
					jQuery("#search_projectResource_btn").click(function(e) {

						e.preventDefault();
						selectedGemsProjectMasterId = jQuery('#projectName_search').val();
						selectedGemsEmployeeMasterId = jQuery('#employee_search').val();
						projectResourceTable_Search(selectedGemsProjectMasterId,selectedGemsEmployeeMasterId);

					});
					
					jQuery("#resetsearch_projectResource_btn").click(function(e) {
						
						selectedGemsProjectMasterId = jQuery('#projectName_search').val("");
						selectedGemsEmployeeMasterId = jQuery('#employee_search').val("");
						projectResourceTable_Search(selectedGemsProjectMasterId,selectedGemsEmployeeMasterId);

					});
					
					});
					
					
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
