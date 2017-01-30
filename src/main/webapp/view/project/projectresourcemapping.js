jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI72').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);

			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			
			var projectName = localStorage.getItem("projectName");

			if (!((typeof projectName == 'undefined') || (projectName == '') || (projectName == null)))
			{
				jQuery('#searchProjectName').val(projectName);
				var serviceURL = envConfig.serviceBaseURL + '/project/viewProjectResourceList.action?userId=' + userId;
				searchProjectName = projectName;
				searchResourceFirstName = "";
				searchResourceLastName = "";
				searchProjectResourceActive = "";
				projectResource_jQueryDataTableAjax(serviceURL, searchProjectName,searchResourceFirstName,searchResourceLastName,searchProjectResourceActive);
			}
			
			
			
				function projectResource_jQueryDataTableAjax(projectMapGrid_serviceURL,
							searchProjectName, searchResourceFirstName,searchResourceLastName,searchProjectResourceActive) {
						var userId = sessionStorage.getItem("userId");
						localStorage.removeItem('projectName');
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
													"name" : "searchProjectName",
													"value" : searchProjectName
												}, {
													"name" : "searchResourceFirstName",
													"value" : searchResourceFirstName
												},{
													"name" : "searchResourceLastName",
													"value" : searchResourceLastName
												}, {
													"name" : "searchProjectResourceActive",
													"value" : searchProjectResourceActive
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
														"mData" : "selected_employee"
													},
													{
														"mData" : "resourceStartDate"
													},
													{
														"mData" : "resourceEndDate"
													},
													{
														"mData" : "projectBillingRate"
													},
													{
														"mData" : "projectResourceActiveString"
													}
													,
													{
														"mData" : "gemsProjectResourceId",
														"bSortable" : false,
														"mRender" : function(
																gemsProjectResourceId) {
															return '<a href = "#" onClick = "editProjectResource('
																	+ gemsProjectResourceId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteProjectResource('
																	+ gemsProjectResourceId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
														}
													}
													 ],

										});	
		}										
										
	jQuery("#projectResourceDelete_Ok").click(function(e){
		e.preventDefault();		
		jQuery('#delete_ProjectResourceModal').modal('toggle');
		jQuery('#delete_ProjectResourceModal').modal('hide');
		
	});		
	
	
	
					
					
					
					function projectResourceTable_Search(searchProjectName,searchResourceFirstName,searchResourceLastName,searchProjectResourceActive) {
						
						var userId = sessionStorage.getItem("userId");
						
						var serviceURL = envConfig.serviceBaseURL
								+ '/project/viewProjectResourceList.action?userId=' + userId;
						
						projectResource_jQueryDataTableAjax(serviceURL, searchProjectName,searchResourceFirstName,searchResourceLastName,searchProjectResourceActive);

					}
					jQuery("#delete_projectresource_cancel").click(function(e) {

						localStorage.removeItem("gemsProjectResourceId");
					
					});
					jQuery("#search_projectResource_btn").click(function(e) {

						e.preventDefault();
						searchProjectName = jQuery('#searchProjectName').val();
						searchResourceFirstName = jQuery('#searchResourceFirstName').val();
						searchResourceLastName = jQuery('#searchResourceLastName').val();
						searchProjectResourceActive = jQuery('#searchProjectResourceActive').is(':checked') ? searchProjectResourceActive = "on" : searchProjectResourceActive = "off";
						projectResourceTable_Search(searchProjectName,searchResourceFirstName,searchResourceLastName,searchProjectResourceActive);

					});
					
					jQuery("#resetsearch_projectResource_btn").click(function(e) {
						e.preventDefault();
						jQuery('#searchProjectName').val('');
						jQuery('#searchResourceFirstName').val('');
						jQuery('#searchResourceLastName').val('');
						searchProjectResourceActive = "on";
						projectResourceTable_Search();

					});
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("gemsProjectResourceId");
						window.location.href = "projectresourcemapping.html";
						
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("gemsProjectResourceId");
						window.location.href = "projectresourcemapping.html";
					});

					jQuery("#delete_projectresource_btn")
							.click(
									function(e) {
						e.preventDefault();
							var userId = sessionStorage.getItem("userId");
						var gemsProjectResourceId = localStorage.getItem('gemsProjectResourceId');
						var serviceURL = envConfig.serviceBaseURL+'/project/deleteGemsProjectResourceMaster.action';

						jQuery.ajax({
							url : serviceURL,
							dataType : "json",
							data : {
								objectId : gemsProjectResourceId,
								userId : userId
							},
							type : 'POST',
							success : function(response) {

								jQuery("#delete_ProjectResourceModal").hide();
								jQuery("#loading-div-background").hide();
								var responseTextFlag = response.success;
								var JsonStringify_Data = JSON.stringify(response.data);
								var editCountry_Data = response.data;
								if (responseTextFlag == true) {

																
																jQuery('#success_delete_modal').modal('toggle');
																jQuery('#success_delete_modal').modal('view');
															} else {
																
																jQuery('#error_delete_modal').modal('toggle');
																jQuery('#error_delete_modal').modal('view');
																
															}
								
							},
							failure : function(data) {
								jQuery('#error_delete_modal').modal('toggle');
								jQuery('#error_delete_modal').modal('view');
								localStorage.removeItem("gemsProjectResourceId");
								window.location.href = "../../";

							},
							statusCode : {
								403 : function(xhr) {
									jQuery('#error_delete_modal').modal('toggle');
									jQuery('#error_delete_modal').modal('view');
									localStorage.removeItem("gemsProjectResourceId");
									window.location.href = "../../view/login/sessionoutlogin.html";

								}
							}

						});

					});


					
					});
					
					function editProjectResource(gemsProjectResourceId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/project/getProjectResourceObject.action?userId=' + userId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsProjectResourceId : gemsProjectResourceId
		},
		type : 'POST',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			//alert("In Edit : "+JsonStringify_Data);
			var editProjectResource_Data = response.data;
			localStorage.setItem('editProjectResource_Data', JsonStringify_Data);
			window.location.href = "editProjectresourcemapping.html";
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				alert("Session will be Expired");
				window.location.href = "../../";

			}
		}

	});

}
function deleteProjectResource(gemsProjectResourceId) {

	localStorage.setItem('gemsProjectResourceId', gemsProjectResourceId);
	jQuery('#delete_ProjectResourceModal').modal('toggle');
	jQuery('#delete_ProjectResourceModal').modal('view');
	

}	

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
