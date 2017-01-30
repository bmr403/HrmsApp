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

			jQueryGemsProject = jQuery('#editGemsProject1');
			jQueryGemsEmployee = jQuery('#editGemsEmployee1');

			

			var userId = sessionStorage.getItem("userId");
			var obj = '';
			
			var serviceURL = envConfig.serviceBaseURL
			+ '/project/viewProjectList.action?userId='+userId+'&showAllProjects=true';
			console.log(serviceURL);

			var serviceURL1 = envConfig.serviceBaseURL
			+ '/employee/viewEmployeeList.action?userId='+userId+'&showAllEmployees=true&employeeFlag=true';
			console.log(serviceURL);
			
					
					var data = JSON.parse(localStorage.getItem('editProjectResource_Data'));
					//alert("Editi data : "+JSON.stringify(data));
					if (data !== null) {
						
					//alert("Editi data : "+JSON.stringify(data));
						//jQuery('#gemsCountryMasterId').val(data.projectTypeMasterId);
						localStorage.setItem('gemsProjectResourceId', data.gemsProjectResourceId);

						//alert(data.selectedGemsEmployeeMasterId);
						
					//	jQuery("#editGemsProject1 option[id='" + data.selectedGemsProjectMasterId + "']").attr("selected","selected");

					//	jQuery("#editGemsEmployee1 option[id='" + data.selectedGemsEmployeeMasterId + "']").attr("selected","selected");
						
						

						jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								if (value.gemsProjectMasterId == data.selectedGemsProjectMasterId)
								{
									jQueryGemsProject.append('<option id="'+ value.gemsProjectMasterId + '" selected>'+ value.projectName +'</option>'); 
								}
								else
								{
									jQueryGemsProject.append('<option id="'+ value.gemsProjectMasterId + '">'+ value.projectName +'</option>'); 
								}
							});	
					        },
					         error: function(e){
					            window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
						
						jQuery.ajax({
					        type: "GET",
							url: serviceURL1,
					        success: function(response){
								
								jQuery.each(response.data, function(index, value){
								if (value.gemsEmployeeMasterId == data.selectedGemsEmployeeMasterId)
								{
									jQueryGemsEmployee.append('<option id="'+ value.gemsEmployeeMasterId + '" selected>'+ value.employeeLastName +' '+value.employeeFirstName +'</option>'); 
								}
								else
								{
									jQueryGemsEmployee.append('<option id="'+ value.gemsEmployeeMasterId + '">'+ value.employeeLastName +' '+value.employeeFirstName +'</option>'); 
								}
								
							});	
					        },
					         error: function(e){
					             window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
			
		
						jQuery("#editGemsResorceStartDate").val(data.resourceStartDate);
						jQuery("#editGemsResorceEndDate").val(data.resourceEndDate);
						jQuery("#inActiveFrom").val(data.inActiveFrom);			
						
						jQuery("#editGemsResorceBillingRate").val(data.projectBillingRate);
						data.activeStatus ? jQuery("#gemsResorceisActive").prop('checked', true) : jQuery("#gemsResorceisActive").prop('checked', false);
						
		
					} else {
					}

			

			
							
						
						
							
						
	customunblockUI();


			// date picker
			jQuery('#editGemsResorceStartDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});
			
			// date picker
			jQuery('#editGemsResorceEndDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});
			
			jQuery("#inActiveFrom").datepicker({
				changeMonth: true,
				changeYear: true,
				yearRange: "-50:+5"
			});
			
	
	
jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectresourcemapping.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectresourcemapping.html";
					});
	
jQuery("#edit_SaveAssigneeButton").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL+ '/project/saveGemsProjectResource.action?userId='+userId;
	var gemsResorceisActive = jQuery('#gemsResorceisActive').is(':checked') ? gemsResorceisActive = "on" : gemsResorceisActive = "off";
	
	 jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsProjectResourceId : localStorage.getItem('gemsProjectResourceId'),
			projectBillingRate : jQuery("#gemsResorceBillingRate").val(),
			resourceStartDate : jQuery("#gemsResorceStartDate").val(),
			resourceEndDate : jQuery("#gemsResorceEndDate").val(),
			selectedGemsProjectMasterId : jQuery("#editGemsProject1").children(":selected").attr("id"),
			selectedEmployeeMasterId : jQuery("#editGemsEmployee1").children(":selected").attr("id"),
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
			window.location.href = "../dashboard/dashboard.html";
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	}); 
			
			
		});
		
		jQuery("#editAssignee_CancelButton").click(function(e){
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
