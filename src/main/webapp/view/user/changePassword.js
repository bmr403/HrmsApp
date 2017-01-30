jQuery(document)
		.ready(
				function() {

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					var userId = sessionStorage.getItem("userId");
					jQuery("#submitBtn")
							.click(
									function(e) {
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
												+ '/master/changePassword.action?userId='
												+ userId;
										console.log(serviceURL);
										
										
											jQuery
													.ajax({
														type : 'POST',
														dataType : 'json',
														url : serviceURL,
														data : {
															oldPassword : jQuery('#oldPassword').val(),
															newPassword : jQuery('#newPassword').val(),
															confirmPassword : jQuery('#confirmPassword').val()
														},

														success : function(data) {
															var responseTextFlag = data.success;
															if (responseTextFlag == true)
															{
																jQuery("#loading-div-background").hide();
																jQuery('#success_changepassword_modal').modal('toggle');
																jQuery('#success_changepassword_modal').modal('view');																
															} 
															else {
																jQuery("#loading-div-background").hide();
																//jQuery(".modal-body #returnmessage").val( data.message );
																jQuery(".returnmessage").text(data.message);
																jQuery('#error_changepassword_modal').modal('toggle');
																jQuery('#error_changepassword_modal').modal('view');	
																
															}
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
										
									});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "changePassword.html";
					});
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "../../";
					});

					
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}