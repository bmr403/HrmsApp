jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI72').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			var serviceURL = envConfig.serviceBaseURL
					+ '/dashboardview/getTotalCountExecution.action?userId='+ userId;
			console.log(serviceURL);
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var serviceURL = envConfig.serviceBaseURL
			+ '/project/viewProjectList.action?userId='+userId+'&showAllProjects=true';
			console.log(serviceURL);
			jQuery("#gemsProjectName").append('<option id="0">Select Project</option>'); 
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
							jQuery("#gemsProjectName").append('<option id="'+ value.gemsProjectMasterId + '">'+ value.projectName +'</option>'); 
							});	
					        },
					         error: function(e){
					            window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
						
						jQuery("#gemsEmployeeName").append('<option id="0">Select Employee</option>'); 
						var serviceURL = envConfig.serviceBaseURL
			+ '/employee/viewEmployeeList.action?userId='+userId+'&showAllEmployees=true&employeeFlag=true';
			console.log(serviceURL);
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
					jQuery("#gemsEmployeeName").append('<option id="'+ value.gemsEmployeeMasterId + '">'+ value.employeeFirstName +' '+value.employeeLastName +'</option>'); 
							});	
					        },
					         error: function(e){
					            window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
						
	customunblockUI();


			// date picker
			jQuery('#gemsResorceStartDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});
			
			// date picker
			jQuery('#gemsResorceEndDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});

	jQuery("#inActiveFrom").datepicker({
				changeMonth: true,
				changeYear: true,
				yearRange: "-50:+5"
			});
			jQuery('#inActiveFrom').datepicker().datepicker('setDate', 'today');
		
	jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectresourcemapping.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectresourcemapping.html";
					});

jQuery("#addAssigneeButton").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL+ '/project/saveGemsProjectResource.action?userId='+userId;
	var gemsResorceisActive = jQuery('#gemsResorceisActive').is(':checked') ? projectisActive = "on" : projectisActive = "off";
	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			projectBillingRate : jQuery("#gemsResorceBillingRate").val(),
			resourceStartDate : jQuery("#gemsResorceStartDate").val(),
			resourceEndDate : jQuery("#gemsResorceEndDate").val(),
			selectedGemsProjectMasterId : jQuery("#gemsProjectName").children(":selected").attr("id"),
			selectedEmployeeMasterId : jQuery("#gemsEmployeeName").children(":selected").attr("id"),
			activeStatus : gemsResorceisActive,
			inActiveFrom : jQuery("#inActiveFrom").val()
		},
		type : 'POST',
		success : function(response) {
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {				
				jQuery("#loading-div-background").hide();
				jQuery('#success_projectresource_modal').modal('toggle');
				jQuery('#success_projectresource_modal').modal('view');
				
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_projectresource_modal').modal('toggle');
				jQuery('#error_projectresource_modal').modal('view');																		
			}
		},
		failure : function(data) {
			jQuery("#loading-div-background").hide();
				jQuery('#error_projectresource_modal').modal('toggle');
				jQuery('#error_projectresource_modal').modal('view');		
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});
			
			
		});

jQuery("#addAssignee_CancelButton").click(function(e){
	e.preventDefault();	
		window.location.href = "projectresourcemapping.html";
	});

		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
