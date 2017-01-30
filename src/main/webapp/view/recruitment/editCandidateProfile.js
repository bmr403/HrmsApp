jQuery(document)
		.ready(
				function() {
					
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI82').addClass('active');
					});
				var userId = sessionStorage.getItem("userId");
					var maxLength = 500;  
					jQuery('#gemsCandidateKeySkill').keyup(function() {  
					  var textlen = maxLength - jQuery(this).val().length;  
					  jQuery('#rchars').text(textlen);  
					}); 

					 jQuery('#gemsCandidateKeySkill').keypress(function( e ) {
							if(e.which === 32 && this.value.length == 0) 
									return false;
					}); 
					
					jQuery('#file').bind('change', function() {
						var fsize = (this.files[0].size/1024/1024).toFixed(2);
						if (fsize > 2.00)
						{
							jQuery('#fileError').text('Upload file size should be less than 2 MB');
							jQuery('#fileError').show();
							return false;
						}
						else
						{
							jQuery('#fileError').hide();
						}
						//alert('This file size is: ' + (this.files[0].size/1024/1024).toFixed(2) + " MB");
					});
					var data = JSON.parse(localStorage
							.getItem('editCandidateProfile_Data'));
					if (data !== null) {
						jQuery('#gemsCandidateMasterId').val(data.gemsCandidateMasterId);
						localStorage.setItem('gemsCandidateMasterId', data.gemsCandidateMasterId);
						jQuery("#gemsCandidateCode").val(data.gemsCandidateCode);
						jQuery("#gemsCandidateFirstName").val(data.gemsCandidateFirstName);
						jQuery("#gemsCandidateLastName").val(data.gemsCandidateLastName);
						jQuery("#gemsCandidateEmail").val(data.gemsCandidateEmail);
						jQuery("#gemsCandidateMobile").val(data.gemsCandidateMobile);
						jQuery("#gemsCandidateEducation").val(data.gemsCandidateEducation);
						jQuery("#gemsCandidateCurrentDesignation").val(data.gemsCandidateCurrentDesignation);
						jQuery("#gemsCandidateCurrentLocation").val(data.gemsCandidateCurrentLocation);
						jQuery("#gemsCandidateExperience").val(data.gemsCandidateExperience);
						jQuery("#gemsCandidateCurrentCtc").val(data.gemsCandidateCurrentCtc);
						jQuery("#gemsCandidateExpectedCtc").val(data.gemsCandidateExpectedCtc);
						jQuery("#gemsCandidateKeySkill").val(data.gemsCandidateKeySkill);
						jQuery("#gemsCandidateNoticePeriod").val(data.gemsCandidateNoticePeriod);
						
						
					}
					jQuery("#candidate-general-form").validate({
    
						// Specify the validation rules
						rules: {
							
							gemsCandidateFirstName: "required",
							gemsCandidateLastName: "required",
							gemsCandidateEmail: "required",							
							gemsCandidateMobile: "required",
							gemsCandidateExperience: "required",
							gemsCandidateKeySkill: "required"
							
							
							
						},
						
						// Specify the validation error messages
						messages: {
					

							
						}
					});

					jQuery("form").submit(function(e) {
						/*var gemsCandidateCode = jQuery("#gemsCandidateCode").val();
						var gemsCandidateFirstName = jQuery("#gemsCandidateFirstName").val();	
						var gemsCandidateLastName = jQuery("#gemsCandidateLastName").val();
						var gemsCandidateEmail = jQuery("#gemsCandidateEmail").val();
						var gemsCandidateMobile = jQuery("#gemsCandidateMobile").val();
						var gemsCandidateEducation = jQuery("#gemsCandidateEducation").val();
						var gemsCandidateCurrentDesignation = jQuery("#gemsCandidateCurrentDesignation").val();
						var gemsCandidateCurrentLocation = jQuery("#gemsCandidateCurrentLocation").val();
						var gemsCandidateExperience = jQuery("#gemsCandidateExperience").val();
						var gemsCandidateCurrentCtc = jQuery("#gemsCandidateCurrentCtc").val();
						var gemsCandidateExpectedCtc = jQuery("#gemsCandidateExpectedCtc").val();
						var gemsCandidateKeySkill = jQuery("#gemsCandidateKeySkill").val();
						var gemsCandidateNoticePeriod = jQuery("#gemsCandidateNoticePeriod").val();*/

						var serviceURL = envConfig.serviceBaseURL
												+ '/recruitment/saveCandidate.action?userId='
												+ userId
										var formData = new FormData(jQuery(this)[0]);
										
										

										jQuery
													.ajax({
														url : serviceURL,
														cache : false,
														dataType : 'json',
														data : formData,
														type : 'POST',
														enctype : 'multipart/form-data',
														processData : false,
														contentType : false,
														success: function(result){
															window.location.href="candidateProfiles.html";
														},
														fail: function(e, data) {
														  alert('Fail!');
														}
										});


					});
						
						
					
					
					var userId = sessionStorage.getItem("userId");
					
					jQuery('#profile_cancel_btn').click(function(event) {
						window.location.href = 'candidateProfiles.html';
					});
					
					jQuery('#gemsCandidateCode').focus();
				
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "candidateProfiles.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "candidateProfiles.html";
					});

	

					
					
					
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}



