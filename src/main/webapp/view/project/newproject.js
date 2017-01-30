jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI35').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var obj = '';
			

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			
			//jQueryGemBusinessType = jQuery('#gemBusinessType');
			jQueryGemsProjectType = jQuery('#gemsProjectType');
			jQueryGemsCustomer = jQuery('#gemsCustomer');
			
			
			var userId = sessionStorage.getItem("userId");
			/*var serviceURL = envConfig.serviceBaseURL
						+ '/master/viewBusinessUnitList.action?userId='+userId;
			console.log(serviceURL);
			jQueryGemBusinessType.append('<option id="0">Select Business Unit</option>'); 
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
					jQueryGemBusinessType.append('<option id="'+ value.gemsBusinessUnitId + '">'+ value.gemsBuDescription +'</option>'); 
							});	
					        },
					         error: function(e){
					             alert('Error1: ' +e);
					        }
					});*/
					
		
			var serviceURL = envConfig.serviceBaseURL
						+ '/project/viewProjectTypeList.action?userId='+userId;
			console.log(serviceURL);
			jQueryGemsProjectType.append('<option id="0">Select Project Type</option>'); 
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
					jQueryGemsProjectType.append('<option id="'+ value.projectTypeMasterId + '">'+ value.projectTypeDescription +'</option>'); 
							});	
					        },
					         error: function(e){
					            window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
						
						
			var serviceURL = envConfig.serviceBaseURL
						+ '/customer/viewCustomerList.action?userId='+userId+'showAllCustomers=true';
			console.log(serviceURL);
			jQueryGemsCustomer.append('<option id="0">Select Customer</option>'); 
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
					jQueryGemsCustomer.append('<option id="'+ value.gemsCustomerMasterId + '">'+ value.gemsCustomerCodeName +'</option>'); 
							});	
					        },
					         error: function(e){
					            window.location.href = "../../view/login/sessionoutlogin.html";
					        }
					    });
						
						
			
		
			jQuery("#gemsProjectEndDate,#gemsProjectStartDate").datepicker({
			});

		jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectlist.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectlist.html";
					});

					jQuery('#editProject_cancel_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projectlist.html";
					});
			
	jQuery("#addProjectBtn").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/project/saveGemsProjectMaster.action?userId='+userId;
	var projectisActive = jQuery('#projectisActive').is(':checked') ? projectisActive = "on" : projectisActive = "off";
	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			projectCode : jQuery("#gemsProjectCode").val(),
			projectName : jQuery("#gemsProjectName").val(),
			projectDescription : jQuery("#gemsProjectDesc").val(),
			projectStatus : jQuery("#gemsProjectStatus").val(),
			billingType : jQuery("#gemsBillingType").val(),
			projectStartDate : jQuery("#gemsProjectStartDate").val(),
			projectEndDate : jQuery("#gemsProjectEndDate").val(),
			projectCost : jQuery("#gemsProjectCost").val(),
		//	selected_bu : jQuery("#gemBusinessType").children(":selected").attr("id"),
			selected_projecttype : jQuery("#gemsProjectType").children(":selected").attr("id"),
			selected_customer : jQuery("#gemsCustomer").children(":selected").attr("id"),
			
			activeStatus : projectisActive
		},
		type : 'POST',
		success : function(response) {
				var responseTextFlag = response.success;
			if (responseTextFlag == true) {

																jQuery("#loading-div-background").hide();
																jQuery('#success_project_modal').modal('toggle');
																jQuery('#success_project_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_project_modal').modal('toggle');
																jQuery('#error_project_modal').modal('view');
																
															}
		},
		failure : function(data) {
			jQuery("#loading-div-background").hide();
			jQuery('#error_modal').modal('toggle');
			jQuery('#error_modal').modal('view');
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});
});		
		
		
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
