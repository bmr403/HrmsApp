jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L2').addClass('active');
						jQuery("ul.U2").show();
						jQuery("ul.U2").find('li#LI36').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			

			// date picker
			jQuery('#datepicker').datepicker();
			
			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			
						
						
					var data = JSON.parse(localStorage.getItem('editRole_Data'));
					if (data !== null) {
					//alert(JSON.stringify(data));
						//jQuery('#gemsCountryMasterId').val(data.projectTypeMasterId);
						
						localStorage.setItem('#gemsRoleMasterId', data.gemsRoleMasterId);
						jQuery("#roleName").val(data.roleName);
						jQuery("#roleCode").val(data.roleCode);
						jQuery("#gemsRoleMasterId").val(data.gemsRoleMasterId);
						
			
						data.activeStatus ? jQuery("#activeStatus").prop('checked', true) : jQuery("#activeStatus").prop('checked', false); // Why always If Else , Try Ternary condition which is more fatser.
			
					} else {
					}
					var enteredProjectName = "";
					
					jQuery("#editRoleBtn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage.getItem("userId");
									var serviceURL = envConfig.serviceBaseURL+'/master/saveRole.action?userId='+userId;
										if (jQuery('#roleCode').val() == '') {
											jQuery('#roleCode').css({
												"border" : "1px solid red",
											});
											//jQuery('#editProjectTypeCodeError').show();
											jQuery('#roleCode').focus();
											return false;
										}  else {
											console.log(serviceURL);
											//hideAllErrors();
											var gemsRoleMasterId = jQuery("#gemsRoleMasterId").val();
											alert(gemsRoleMasterId);
											var activeStatus = jQuery('#activeStatus').is(':checked') ? activeStatus = "on" : activeStatus = "off";
											jQuery.ajax({
														url : serviceURL,
														dataType : "json",
														type : 'POST',
														data : {
														gemsRoleMasterId : gemsRoleMasterId,
														roleCode : jQuery("#roleCode").val(),
														roleName : jQuery("#roleName").val(),
														activeStatus : activeStatus,
															userId:userId
														},
														success : function(data) {

															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																jQuery("#loading-div-background").hide();
																jQuery('#success_projectType_modal').modal('toggle');
																jQuery('#success_projectType_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																
															}
															window.location.href = "rolelist.html";
														},
														failure : function(data) {
															jQuery("#loading-div-background").hide();
															jQuery('#error_modal').modal('toggle');
															jQuery('#error_modal').modal('view');
															window.location.href = "rolelist.html";
														},
														statusCode : {
															403 : function(xhr) {
																alert("Session will be Expired");
																window.location.href = "../../";

															}
														}
													});
										}
									});

					 
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "rolelist.html";
					});

					jQuery('#role_cancel_btn').click(function(e) {
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
