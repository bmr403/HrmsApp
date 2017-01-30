jQuery(document).ready(
		function() {
			jQuery(".active").removeClass("active");
			jQuery("#dashboard").addClass("active");
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);

			

			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			var heightArray = jQuery(".gridblog .inner").map( function(){ return  jQuery(this).height();}).get();
			  var maxHeight = Math.max.apply( Math, heightArray);
			  jQuery(".gridblog .inner").height(maxHeight);

			var userId = sessionStorage.getItem("userId");

			var roleCode = sessionStorage.getItem("roleCode");

			if (roleCode == "Administrator")
			{
				jQuery("#admindashboard").show();
				jQuery("#employeedashboard").hide();
			}
			else
			{
				jQuery("#admindashboard").hide();
				jQuery("#employeedashboard").show();
				//jQuery("#loading-div-background").hide();
			}

			var obj = '';
			
			
	
			var serviceURL = envConfig.serviceBaseURL
							+ '/viewAllServices.action?userId='
							+ userId;
			console.log(serviceURL);
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        
					        success: function(response){
					            // we have the response
								if(response.success == true)
					            {
									
									permissionList = "<ul class=\"nav nav-tabs nav-stacked\" id=\"nav\">";
									var parentComponentArray = new Array();
									for(i =0 ; i < response.data.length ; i++)
									{
										if (response.data[i].parentComponentId == 0)
										{
											parentComponentArray.push(""+response.data[i].gemsComponentMasterId+"||"+response.data[i].componentDescription+"");
										}
										
									}
									
									

									sname ="<li class=\"nav-header\">Navigation Menu</li><li class=\"\" id=\"dashboard\"><a href=\"../dashboard/dashboard.html\"><i class=\"fa fa-tachometer\"></i>&nbsp;DashBoard</a></li>";  

									
									for(i =0 ; i < parentComponentArray.length ; i++)
									{
										var parentCompArray = parentComponentArray[i].split('||');
										var parentCompId = parentCompArray[0];
										var parentCompName = parentCompArray[1];
										
										sname += "<li class=\"dropdown\" id=\"L"+parentCompId+"\"><a href=\"#\"><i class=\"fa fa-th-list\"> </i>&nbsp;"+parentCompName+"</a>";
										
										sname += "<ul class=\"U"+parentCompId+"\" style=\"display:none;\">";
										for(j =0 ; j < response.data.length ; j++)
										{
											if (response.data[j].parentComponentId == parentCompId)
											{
												
												//response.data[j].componentUrl
												sname += "<li id=\"LI"+response.data[j].gemsComponentMasterId+"\"><a href=\""+response.data[j].componentUrl+"\">"+response.data[j].componentDescription+"</a></li>";
												
											}
										}
										sname += "</ul></li>";
										

									}
									
									permissionList += sname;
									permissionList +=  "</ul>";
									
									sessionStorage.setItem("permissionList",permissionList);
									jQuery("#orgoradminuser_menu_id22").html(permissionList);
									
									
					            	
					            }
					            else
					            {
					            	
					            	window.location.href = "../../";
					            }
					         },
					         error: function(e){
								window.location.href = "../../view/login/sessionoutlogin.html";
					         }
					    });

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					myLeaveList_jQueryDataTableAjax(serviceURL, searchCode);
					
			function myLeaveList_jQueryDataTableAjax(serviceURL,
							fromDate,toDate) {

			

						
						console.log(serviceURL);
						
						var myLeaveList_table = jQuery("#myLeaveList_dashboardtable")
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
													"name" : "fromDate",
													"value" : fromDate
												},{
													"name" : "toDate",
													"value" : toDate
												});
											},
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 5,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
													var myleaveTotalRecords = oSettings.fnRecordsTotal();
													if (myleaveTotalRecords ==1)
													{
														jQuery('#myLeaveList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (myleaveTotalRecords ==2)
													{
														jQuery('#myLeaveList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (myleaveTotalRecords ==3)
													{
														jQuery('#myLeaveList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (myleaveTotalRecords ==4)
													{
														jQuery('#myLeaveList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
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
														"mData" : "selected_leavetype"
													},
													{
														"mData" : "fromDate"
													},
													{
														"mData" : "toDate"
													},
													{
														"mData" : "currentStatus"
													}
													 ],

										});
										
							
					
			
										
					}
					
					function leaveApprovalList_jQueryDataTableAjax(serviceURL,
							fromDate,toDate) {

			

						
						console.log(serviceURL);
						var myLeaveList_table = jQuery("#leaveApprovalList_dashboardtable")
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
													"name" : "fromDate",
													"value" : fromDate
												},{
													"name" : "toDate",
													"value" : toDate
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
												    var approveleaveTotalRecords = oSettings.fnRecordsTotal();
													if (approveleaveTotalRecords ==1)
													{
														jQuery('#leaveApprovalList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (approveleaveTotalRecords ==2)
													{
														jQuery('#leaveApprovalList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (approveleaveTotalRecords ==3)
													{
														jQuery('#leaveApprovalList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr><tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
													else if (approveleaveTotalRecords ==4)
													{
														jQuery('#leaveApprovalList_dashboardtable').append("<tr ><td>&nbsp;</td><td></td><td></td><td></td></tr>");
													}
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
														"mData" : "selectedGemsEmployeeMasterName"
													},
													{
														"mData" : "selected_leavetype"
													},
													{
														"mData" : "duration"
													},
													{
														"mData" : "gemsEmployeeLeaveLineId",
														"bSortable" : false,
														"mRender" : function(
																gemsEmployeeLeaveLineId) {
															return '<a href = "#" onClick = "editLeaveApproval('
																	+ gemsEmployeeLeaveLineId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveApprovalList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					leaveApprovalList_jQueryDataTableAjax(serviceURL, searchCode);
					
			
		
		
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
	
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
function editLeaveApproval(gemsEmployeeLeaveLineId) {
	
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/getEmployeeLeaveLineById.action?userId='+userId;

	jQuery.ajax({
		url : serviceURL,
		data : {
			gemsEmployeeLeaveLineId : gemsEmployeeLeaveLineId
		},
		dataType : "json",
		type : 'POST',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			localStorage.setItem('editLeave_Data', JsonStringify_Data);
			window.location.href = "../leavemanagement/editLeaveApproval.html";
		},
		failure : function(data) {
			//window.location.href = "../../";
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				//window.location.href = "../../";
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});

}
