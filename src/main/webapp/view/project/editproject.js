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
			jQuery("#gemsEditProjectStartDate,#gemsEditProjectEndDate").datepicker({
			});
			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			
			
		//	jQueryGemBusinessType = jQuery('#gemEditBusinessType');
			jQueryGemsProjectType = jQuery('#gemsEditProjectType');
			jQueryGemsCustomer = jQuery('#gemsEditCustomer');
			
			
			var userId = sessionStorage.getItem("userId");
			/*var serviceURL = envConfig.serviceBaseURL
						+ '/master/viewBusinessUnitList.action?userId='+userId;
			console.log(serviceURL);
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
						
						
					var data = JSON.parse(localStorage.getItem('editProject_Data'));
					if (data !== null) {
					//alert(JSON.stringify(data));
						//jQuery('#gemsCountryMasterId').val(data.projectTypeMasterId);
						localStorage.setItem('project_editId', data.gemsProjectMasterId);
						jQuery("#gemsEditProjectCode").val(data.projectCode);
						jQuery("#gemsEditProjectName").val(data.projectName);
						jQuery("#gemsEditProjectDesc").val(data.projectDescription);
						
		jQuery("#gemsEditProjectStatus option[value='" + data.projectStatus + "']").attr("selected","selected");			
						//jQuery("#gemsEditProjectStatus").val(data.projectStatus);
						jQuery("#gemsEditBillingType").val(data.billingType);
						jQuery("#gemsEditProjectStartDate").val(data.projectStartDate);
						jQuery("#gemsEditProjectEndDate").val(data.projectEndDate);
						jQuery("#gemsEditProjectCost").val(data.projectCost);
						
					//	jQuery("#gemEditBusinessType").val(data.selected_bu);
		//	jQuery("#gemEditBusinessType option[id='" + data.selectedGemsBusinessUnitId + "']").attr("selected","selected");
			jQuery("#gemsEditProjectType option[id='" + data.selectedProjectTypeMasterId + "']").attr("selected","selected");
			//alert(data.selectedGemsCustomerMasterId);
			jQuery("#gemsEditCustomer option[id='" + data.selectedGemsCustomerMasterId + "']").attr("selected","selected");
			
			data.activeStatus ? jQuery("#editprojectisActive").prop('checked', true) : jQuery("#editprojectisActive").prop('checked', false); // Why always If Else , Try Ternary condition which is more fatser.
			//alert(JSON.stringify(data));
					} else {
					}
					var enteredProjectName = "";
					
					jQuery("#editProjectBtn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage.getItem("userId");
									var serviceURL = envConfig.serviceBaseURL+'/project/saveGemsProjectMaster.action?userId='+userId;
										if (jQuery('#gemsEditProjectCode').val() == '') {
											jQuery('#gemsEditProjectCode').css({
												"border" : "1px solid red",
											});
											//jQuery('#editProjectTypeCodeError').show();
											jQuery('#gemsEditProjectCode').focus();
											return false;
										}  else {
											console.log(serviceURL);
											//hideAllErrors();
											var projectId_edit = localStorage.getItem('project_editId');
											//alert(jQuery('#gemsEditProjectTypeDesc').val()+" : "+projectTypeId_edit+" : "+jQuery('#gemsEditProjectTypeCode').val());
											
											var isActiveStatus = jQuery('#editprojectisActive').is(':checked') ? isActiveStatus = "on" : isActiveStatus = "off";
											jQuery.ajax({
														url : serviceURL,
														dataType : "json",
														type : 'POST',
														data : {
														gemsProjectMasterId : projectId_edit,
														projectCode : jQuery("#gemsEditProjectCode").val(),
			projectName : jQuery("#gemsEditProjectName").val(),
			projectDescription : jQuery("#gemsEditProjectDesc").val(),
			projectStatus : jQuery("#gemsEditProjectStatus").val(),
			billingType : jQuery("#gemsEditBillingType").val(),
			projectStartDate : jQuery("#gemsEditProjectStartDate").val(),
			projectEndDate : jQuery("#gemsEditProjectEndDate").val(),
			projectCost : jQuery("#gemsEditProjectCost").val(),
			//selected_bu : jQuery("#gemEditBusinessType").children(":selected").attr("id"),
			selected_projecttype : jQuery("#gemsEditProjectType").children(":selected").attr("id"),
			selected_customer : jQuery("#gemsEditCustomer").children(":selected").attr("id"),
			activeStatus : isActiveStatus,
															userId:userId
														},
														success : function(data) {

															var responseTextFlag = data.success;

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
										}
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
		
		
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
