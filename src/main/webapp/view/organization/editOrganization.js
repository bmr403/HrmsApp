jQuery(document)
		.ready(
				function() {
					jQuery("#5").addClass("active");
					
					

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});

					var userId = sessionStorage.getItem("userId");
					var data = JSON.parse(localStorage
							.getItem('editCountry_Data'));
					if (data !== null) {

						jQuery('#gemsCountryMasterId').val(data.gemsCountryMasterId);
						localStorage.setItem('countryId_edit', data.gemsCountryMasterId);
						jQuery("#gemsCountryCode").val(data.gemsCountryCode);
						jQuery("#gemsCountryDescription").val(data.gemsCountryDescription);
						
						if (data.activeStatus == "true")
						{
							jQuery('.activeStatus')[0].checked = true;			
						}
						else
						{
							
							jQuery('.activeStatus')[0].checked = false;			
							
						}
						

						
												
						jQuery('#newCountry_div').hide('fast');
						jQuery('#editCountry_div').show('fast');

					} else {
						jQuery('#editCountry_div').hide('fast');
						jQuery('#newCountry_div').show('fast');
					}
					var enteredProjectName = "";

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					jQuery("#gemsCountryCode").on("input", function() {
						jQuery('#editCountryCodeError').hide();
						jQuery('#editCountryCodeValue').hide();
						jQuery(gemsCountryCode).css({
							"border" : "",
						});
					});

					jQuery('#editCountry_cancel_btn').click(function(event) {
						jQuery('#gemsCountryCode').val('');
						jQuery('#gemsCountryDescription').val('');
						window.location.href = 'country.html';
					});
					jQuery("#saveChanges_btn")
							.click(
									function(e) {
										e.preventDefault();

										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/master/saveGemsCountryMaster.action?userId='
												+ userId;
										
									
										
										if (jQuery('#gemsCountryCode').val() == '') {
											jQuery('#gemsCountryCode').css({
												"border" : "1px solid red",
											});
											jQuery('#editCountryCodeError')
													.show();
											jQuery('#gemsCountryCode').focus();
											return false;
										}  else {
											console.log(serviceURL);
											hideAllErrors();
											var countryId_edit = localStorage
													.getItem('countryId_edit');
											jQuery
													.ajax({
														url : serviceURL,
														dataType : "json",
														cache : false,
														type : 'POST',
														data : {

															gemsCountryMasterId : countryId_edit,
															gemsCountryCode : jQuery(
																	'#gemsCountryCode')
																	.val(),
															gemsCountryDescription : jQuery(
																	'#gemsCountryDescription').val(),
															createdBy : userId,
															updatedBy : userId,
															activeStatus : jQuery(
															'#activeStatus')
															.val(),
															userId : userId
														},
														success : function(data) {

															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																jQuery('#editCountryCodeError').hide();
																jQuery('#editCountryCodeErrorValue').hide();
																jQuery("#loading-div-background").hide();
																jQuery('#success_country_modal').modal('toggle');
																jQuery('#success_country_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																jQuery('#editCountryCodeError').hide();
																jQuery('#editCountryCodeErrorValue').hide();
															}
														},
														failure : function(data) {
															window.location.href = "dashboard.html";
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

					jQuery('#gemsCountryCode').focus();
					jQuery("#gemsCountryCode").on("input", function() {
						jQuery('#editCountryCodeError').hide();
						jQuery('#editCountryCodeErrorValue').hide();
						jQuery('#gemsCountryCode').css({
							"border" : "1px solid #0866c6",
						});
					});

					

					function hideAllErrors() {
						jQuery('#editCountryCodeError').hide();
						jQuery('#editCountryCodeErrorValue').hide();						
					}
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "country.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editCountry.html";
					});

				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}