jQuery(document)
		.ready(
				function() {
					jQuery("#5").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");
					
					jQuery('#newCountry_cancel_btn').click(function(event) {
						jQuery('#gemsCountryCode').val('');
						jQuery('#gemsCountryDescription').val('');
						window.location.href = 'country.html';
					});
					jQuery('#gemsCountryCode').val("");
					jQuery('#gemsCountryDescription').val("");
					jQuery('#activeStatus').val("");

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");

					jQuery('#gemsCountryCode').val('');
					jQuery('#gemsCountryCode').focus();
					jQuery("#addCountryBtn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
										+ '/master/saveGemsCountryMaster.action?userId='
										+ userId;
										
										var gemsCountryCode = jQuery('#gemsCountryCode')
												.val();

										var countryIsActive = jQuery('#countryIsActive').is(':checked') ? countryIsActive = "on" : countryIsActive = "off";

										if (jQuery('#gemsCountryCode').val() == '') {
											jQuery('#gemsCountryCode').css({
												"border" : "1px solid red",
											});
											jQuery('#gemsCountryCode').focus();
											return false;
										} 
										else {
											console.log(serviceURL);
											jQuery
													.ajax({
														url : serviceURL,
														cache : false,
														dataType : "json",
														data : {
															gemsCountryCode : jQuery(
																	'#gemsCountryCode')
																	.val(),
															gemsCountryDescription : jQuery(
																	'#gemsCountryDescription')
																	.val(),
															activeStatus : countryIsActive,
															createdBy : userId,
															updatedBy : userId,
															userId : userId
														},
														type : 'Post',
														success : function(data) {
															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																
																jQuery("#loading-div-background").hide();
																jQuery('#success_country_modal').modal('toggle');
																jQuery('#success_country_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																
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
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "country.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newCountry.html";
					});
					jQuery('#gemsCountryCode').focus();
					
					var data = JSON.parse(localStorage
							.getItem('editCountry_Data'));
					if (data == null) {
						jQuery('#addCountryBtn_id').show();
						jQuery('#editCountryBtn_id').hide();
					} else {
						jQuery('#addCountryBtn_id').hide();
						jQuery('#editCountryBtn_id').show();
					}
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}