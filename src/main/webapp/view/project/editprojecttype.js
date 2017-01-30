jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI34').addClass('active');
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
			
			var userId = sessionStorage.getItem("userId");
			
			jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});
			
			
			var userId = sessionStorage.getItem("userId");
							
					jQuery("#gemsEditProjectTypeCode").val("");
					jQuery("#gemsEditProjectTypeDesc").val("");
					var userId = sessionStorage.getItem("userId");
					var data = JSON.parse(localStorage
							.getItem('editProjectType_Data'));
					if (data !== null) {

						jQuery('#gemsCountryMasterId').val(data.projectTypeMasterId);
						localStorage.setItem('projectTypeId_edit', data.projectTypeMasterId);
						jQuery("#gemsEditProjectTypeCode").val(data.projectTypeCode);
						jQuery("#gemsEditProjectTypeDesc").val(data.projectTypeDescription);
						if (data.activeStatus === 1){
							jQuery("#editProjectTypeisActive").prop('checked', true);
						}
						else
						{
							jQuery("#editProjectTypeisActive").prop('checked', false);					
							
						}
					}
					var enteredProjectName = "";
					
				 	jQuery("#gemsEditProjectTypeCode").on("input", function() {
						jQuery('#editProjectTypeCodeError').hide();
						jQuery('#editProjectTypeCodeAlreadyExists').hide();
						jQuery("#gemsEditProjectTypeCode").css({
							"border" : "",
						});
					});

					jQuery('#editProjectType_cancel_btn').click(function(event) {
						jQuery('#gemsEditProjectTypeCode').val('');
						jQuery('#gemsEditProjectTypeDesc').val('');
						window.location.href = 'country.html';
					}); 
					jQuery("#saveChanges_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage.getItem("userId");
									var serviceURL = envConfig.serviceBaseURL+'/project/saveGemsProjectTypeMaster.action?userId='+userId;
										if (jQuery('#gemsEditProjectTypeCode').val() == '') {
											jQuery('#gemsEditProjectTypeCode').css({
												"border" : "1px solid red",
											});
											jQuery('#editProjectTypeCodeError')
													.show();
											jQuery('#gemsEditProjectTypeCode').focus();
											return false;
										}  else {
											console.log(serviceURL);
											hideAllErrors();
											var projectTypeId_edit = localStorage.getItem('projectTypeId_edit');
											//alert(jQuery('#gemsEditProjectTypeDesc').val()+" : "+projectTypeId_edit+" : "+jQuery('#gemsEditProjectTypeCode').val());
											
											var isActiveStatus = jQuery('#editProjectTypeisActive').is(':checked') ? projectTypeisActive = "on" : projectTypeisActive = "off";
											jQuery.ajax({
														url : serviceURL,
														dataType : "json",
														type : 'POST',
														data : {
														projectTypeMasterId : projectTypeId_edit,
														projectTypeCode : jQuery('#gemsEditProjectTypeCode').val(),
															projectTypeDescription: jQuery('#gemsEditProjectTypeDesc').val(),
															activeStatus : isActiveStatus,
															userId:userId
														},
														success : function(data) {

															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																jQuery("#loading-div-background").hide();
																jQuery('#success_projecttype_modal').modal('toggle');
																jQuery('#success_projecttype_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_projecttype_modal').modal('toggle');
																jQuery('#error_projecttype_modal').modal('view');
																
															}
														},
														failure : function(data) {
															jQuery("#loading-div-background").hide();
																jQuery('#error_projecttype_modal').modal('toggle');
																jQuery('#error_projecttype_modal').modal('view');
														},
														statusCode : {
															403 : function(xhr) {
																window.location.href = "../../view/login/sessionoutlogin.html";

															}
														}
													});
										}
									});

					 jQuery('#gemsEditProjectTypeCode').focus();
					jQuery("#gemsEditProjectTypeCode").on("input", function() {
						jQuery('#editProjectTypeCodeError').hide();
						jQuery('#editProjectTypeCodeAlreadyExists').hide();
						jQuery('#gemsEditProjectTypeCode').css({
							"border" : "1px solid #0866c6",
						});
					}); 

					

					 function hideAllErrors() {
						jQuery('#editProjectTypeCodeError').hide();
						jQuery('#editProjectTypeCodeAlreadyExists').hide();						
					} 
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});

					jQuery('#editProjectType_cancel_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});
		
		
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
