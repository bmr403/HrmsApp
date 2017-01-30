jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
				jQuery('ul#nav').find('li#L33').addClass('active');
				jQuery("ul.U33").show();
				jQuery("ul.U33").find('li#LI35').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			jQuery.fn.dataTable.ext.errMode = 'none';
			var userId = sessionStorage.getItem("userId");
			var obj = '';

			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();

			var userId = sessionStorage.getItem("userId");


			dropdown_projecttype = jQuery('#dropdown_projecttype');
			dropdown_customer = jQuery('#dropdown_customer');

			var projectTypeMasterId = localStorage.getItem("projectTypeMasterId");

			var customerId = localStorage.getItem("customerId");


			var userId = sessionStorage.getItem("userId");

			if ( !(typeof projectTypeMasterId == 'undefined') || !(projectTypeMasterId == '') || !(projectTypeMasterId == null) || !(customerId == null) || !(customerId == '') || !(typeof customerId == 'undefined'))
			{
				var serviceURL = envConfig.serviceBaseURL
				+ '/project/viewProjectList.action?userId='
				+ userId;
				searchCode = "";
				searchName = "";
				searchProjectActive = "";
				billingType = "";
				projectStatus = "";
				projectTypeMasterId = localStorage.getItem("projectTypeMasterId");
				customerId = localStorage.getItem("customerId");
				project_jQueryDataTableAjax(serviceURL, searchCode,searchName,customerId,projectTypeMasterId,billingType,projectStatus,searchProjectActive);
			}

			//if (!(((typeof projectTypeMasterId == 'undefined') || (projectTypeMasterId == '')) || ((typeof customerId == 'undefined') || (customerId == ''))))
			//	{


			//	}


			var serviceURL = envConfig.serviceBaseURL
			+ '/project/viewProjectTypeList.action?userId='+userId;
			console.log(serviceURL);
			dropdown_projecttype.append('<option id="">Select Project Type</option>');
			jQuery.ajax({
				type: "GET",
				url: serviceURL,
				success: function(response){
					//alert(JSON.stringify(response.data));
					jQuery.each(response.data, function(index, value){
						if (!((typeof projectTypeMasterId == 'undefined') || (projectTypeMasterId == '')))
						{
							if (value.projectTypeMasterId == projectTypeMasterId)
							{
								dropdown_projecttype.append('<option id="'+ value.projectTypeMasterId + '" selected>'+ value.projectTypeDescription +'</option>'); 
							}
							else
							{
								dropdown_projecttype.append('<option id="'+ value.projectTypeMasterId + '">'+ value.projectTypeDescription +'</option>'); 
							}
						}
						else
						{
							dropdown_projecttype.append('<option id="'+ value.projectTypeMasterId + '">'+ value.projectTypeDescription +'</option>'); 
						}
					});	
				},
				error: function(e){
					window.location.href = "../../";
				}
			});
			var serviceURL = envConfig.serviceBaseURL
			+ '/customer/viewCustomerList.action?userId='+userId;
			console.log(serviceURL);
			dropdown_customer.append('<option id="">Select Customer</option>');
			jQuery.ajax({
				type: "GET",
				url: serviceURL,
				success: function(response){
					//alert(JSON.stringify(response.data));
					jQuery.each(response.data, function(index, value){

						if (!((typeof customerId == 'undefined') || (customerId == '') || (customerId == null)))
						{
							if (value.gemsCustomerMasterId == customerId)
							{
								dropdown_customer.append('<option id="'+ value.gemsCustomerMasterId + '" selected>'+ value.gemsCustomerCodeName +'</option>'); 
							}
							else
							{
								dropdown_customer.append('<option id="'+ value.gemsCustomerMasterId + '">'+ value.gemsCustomerCodeName +'</option>'); 
							}
						}
						else
						{
							dropdown_customer.append('<option id="'+ value.gemsCustomerMasterId + '">'+ value.gemsCustomerCodeName +'</option>'); 
						}


					});	
				},
				error: function(e){
					window.location.href = "../../";
				}
			});

			function project_jQueryDataTableAjax(serviceURL, searchCode,searchName,customerId,projectTypeMasterId,billingType,projectStatus,searchProjectActive) {

				var userId = sessionStorage.getItem("userId");
				localStorage.removeItem('projectTypeMasterId');
				localStorage.removeItem('customerId');						
				console.log(serviceURL);
				var projectList_table = jQuery("#projectList_table")
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
									"name" : "searchProjectCode",
									"value" : searchCode
								}, {
									"name" : "searchProjectName",
									"value" : searchName
								}, {
									"name" : "searchProjectActive",
									"value" : searchProjectActive
								}, {
									"name" : "projectTypeMasterId",
									"value" : projectTypeMasterId
								},{
									"name" : "billingType",
									"value" : billingType
								},{
									"name" : "projectStatus",
									"value" : projectStatus
								},{
									"name" : "customerId",
									"value" : customerId
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
							            	   "mData" : "selected_customer"
							               },
							               {
							            	   "mData" : "selected_projecttype"
							               },
							               {
							            	   "mData" : "projectCode"
							               },
							               {
							            	   "mData" : "projectName"
							               },
							               {
							            	   "mData" : "projectStatus"
							               },
							               {
							            	   "mData" : "billingType"
							               },
							               {
							            	   "mData" : "projectActiveStatusString"
							               },
							               {
							            	   "mData" : "gemsProjectMasterId",
							            	   "bSortable" : false,
							            	   "mRender" : function(
							            			   data,type,row,meta) {
							            		   return '<a href = "#" onClick = "projectDetail('
							            		   + row.gemsProjectMasterId
							            		   + ',\''
							            		   + row.projectName
							            		   + '\');" id=\"detail_btn\"><span class=\"glyphicon glyphicon-info-sign\" title=\"Details\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = "#" onClick = "editProject('
							            		   + row.gemsProjectMasterId
							            		   + ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteProject('
							            		   + row.gemsProjectMasterId
							            		   + ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';
							            	   }			
							               }
							               ],

						});
			}



			function projectTable_Search() {
				var userId = sessionStorage.getItem("userId");
				var serviceURL = envConfig.serviceBaseURL
				+ '/project/viewProjectList.action?userId='
				+ userId;
				searchCode = jQuery('#projectCode_search').val();
				searchName = jQuery('#projectDescription_search').val();
				searchProjectActive = jQuery('#searchProjectActive').is(':checked') ? searchProjectActive = "on" : searchProjectActive = "off";
				projectTypeMasterId = jQuery(jQuery("#dropdown_projecttype")).find('option:selected').attr('id');
				customerId = jQuery(jQuery("#dropdown_customer")).find('option:selected').attr('id');
				billingType = jQuery(jQuery("#gemsBillingType")).find('option:selected').attr('id');
				projectStatus = jQuery(jQuery("#gemsProjectStatus")).find('option:selected').attr('id');
				project_jQueryDataTableAjax(serviceURL, searchCode,searchName,customerId,projectTypeMasterId,billingType,projectStatus,searchProjectActive);

			}
			jQuery("#search_project_btn").click(function(e) {

				e.preventDefault();
				projectTable_Search();

			});
			jQuery("#resetsearch_project_btn").click(function(e) {

				jQuery('#projectCode_search').val("");
				jQuery('#projectDescription_search').val("");
				projectTable_Search();

			});	
			jQuery("#delete_project_cancel").click(function(e) {

						localStorage.removeItem("gemsProjectMasterId");
					
			});
			jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("gemsProjectMasterId");
						window.location.href = "projectlist.html";
						
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						localStorage.removeItem("gemsProjectMasterId");
						window.location.href = "projectlist.html";
					});

			jQuery("#delete_project_btn")
			.click(
					function(e) {
						e.preventDefault();
						var userId = sessionStorage.getItem("userId");
						var gemsProjectMasterId = localStorage.getItem('gemsProjectMasterId');
						var serviceURL = envConfig.serviceBaseURL
						+ '/project/deleteGemsProjectMaster.action?userId='+userId+'&objectId='
						+ gemsProjectMasterId;

						jQuery.ajax({
							url : serviceURL,
							dataType : "json",
							type : 'GET',
							success : function(response) {
								jQuery("#delete_projectList_modal").hide();
								jQuery("#loading-div-background").hide();

								if(response.success == true){
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

							},
							statusCode : {
								403 : function(xhr) {
									localStorage.removeItem("gemsProjectMasterId");
									window.location.href = "../../view/login/sessionoutlogin.html";

								}
							}

						});

					});
		});



function projectDetail(gemsProjectMasterId,projectName) {
	localStorage.setItem('projectName', projectName);		
	window.location.href="projectresourcemapping.html";

}

function editProject(gemsProjectMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
	+ '/project/getProjectById.action?userId='+userId+'&gemsProjectMasterId='
	+ gemsProjectMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'GET',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editProject_Data = response.data;
			localStorage.setItem('editProject_Data', JsonStringify_Data);
			window.location.href = "editproject.html";
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

function deleteProject(gemsProjectMasterId) {

	localStorage.setItem('gemsProjectMasterId', gemsProjectMasterId);
	jQuery('#delete_projectList_modal').modal('toggle');
	jQuery('#delete_projectList_modal').modal('view');


}	



var employeesItemList = jQuery('.employeesItemList').bootstrapDualListbox({
	preserveSelectionOnMove: 'moved',
	showFilterInputs : false,
	moveOnSelect: false,
	nonSelectedFilter: '',
	filterOnValues : false,
	infoText : false
});



var global_gemsProjectMasterId = "";
/* function assignProject(gemsProjectMasterId) {

		var userId = sessionStorage.getItem("userId");
		global_gemsProjectMasterId = gemsProjectMasterId;
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/viewEmployeeList.action?userId='+userId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'GET',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
		//alert("Emp Master lISt is : "+JsonStringify_Data);
		jQuery("#employeesItemList").html(""); //reset option
		jQuery.each(response.data, function(index, value){	
			jQuery("#employeesItemList").append('<option id="'+ value.gemsEmployeeMasterId + '" value=" '+value.empName+' " >'+ value.empName +'</option>');
			jQuery("#employeeName").append('<option id="'+ value.gemsEmployeeMasterId + '" value=" '+value.empName+' " >'+ value.empName +'</option>');			
				});	
				 employeesItemList.bootstrapDualListbox('refresh', true);
			jQuery('#assign_project_modal').modal('toggle');
			jQuery('#assign_project_modal').modal('view');
			customunblockUI();
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
	customunblockUI();

	} */





jQuery('#projectStartDate').datepicker({
	changeMonth: true,
	changeYear: true

});

jQuery('#projectEndDate').datepicker({
	changeMonth: true,
	changeYear: true,
});



function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
