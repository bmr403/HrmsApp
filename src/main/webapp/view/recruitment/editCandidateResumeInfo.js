jQuery(document)
		.ready(
				function() {
					
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI84').addClass('active');
					});
					var userId = sessionStorage.getItem("userId");

					var fullDate = new Date();
					var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);
					var currentDate = twoDigitMonth + "/" + fullDate.getDate() + "/" + fullDate.getFullYear();

					
					
					
					var data = JSON.parse(localStorage
							.getItem('editCandidateResumeInfo'));
					if (data !== null) {
						jQuery('#gemsRecruitmentRequirementCandidateId').val(data.gemsRecruitmentRequirementCandidateId);
						localStorage.setItem('gemsRecruitmentRequirementCandidateId', data.gemsRecruitmentRequirementCandidateId);
						jQuery("label[for='candidateName']").html(data.candidateName);
						jQuery("label[for='candidateEmail']").html(data.candidateEmail);
						jQuery("label[for='candidateContactInfo']").html(data.candidateContactInfo);
						jQuery("label[for='currentDate']").html(currentDate);
						



						
						
						
						dropdown_status = jQuery('#dropdown_status');
							var serviceURL = envConfig.serviceBaseURL
									+ '/master/viewGemsCandidateStatusMaster.action?userId='
									+ userId;
							var selectedGemsCandidateStatusMasterId = data.selectedGemsCandidateStatusMasterId;
							jQuery.ajax({
								url : serviceURL,
								dataType : 'json',
								type : 'GET',
								success : function(data) {
									var JsonStringify_Data = JSON.stringify(data);
									var obj = jQuery.parseJSON(JsonStringify_Data);
		
									var arr = [];
									jQuery.each(obj, function(i, e) {
										jQuery.each(e, function(key, val) {
											arr.push(val);
										});
									});
		
									var result = jQuery.map(arr, function(val, key) {
										return {
											gemsCandidateStatusMasterId : val.gemsCandidateStatusMasterId,
											statusDescription : val.statusDescription
										};
									});
		
									// iterate over the data and append a select option
									if (selectedGemsCandidateStatusMasterId == 0)
									{
										dropdown_status.append('<option id="0">Select Customer</option>');
									}
									jQuery.each(result, function(key, val) {
										
										
										if (val.gemsCustomerMasterId == selectedGemsCandidateStatusMasterId)
										{
											dropdown_status.append('<option id="'
													+ val.gemsCandidateStatusMasterId + '" selected>'
													+ val.statusDescription + ' </option>');
										}
										else
										{
											dropdown_status.append('<option id="'
													+ val.gemsCandidateStatusMasterId + '">'
													+ val.statusDescription + '</option>');
										}
										
										
										
									})
									jQuery("#dropdown_status").focus();
								},
								error : function() {
								},
								statusCode : {
									403 : function(xhr) {
										alert("Session will be Expired");
										window.location.href = "../../";
		
									}
								}
							});



						
					}
					jQuery("#candidate-resumeinfo-form").validate({
    
						// Specify the validation rules
						rules: {
							
						},
						
						// Specify the validation error messages
						messages: {
					

							
						}
					});

					jQuery("form").submit(function(e) {
						


					});
						
						
					
					
					var userId = sessionStorage.getItem("userId");
					
					jQuery('#candidateprofile_cancel_btn').click(function(event) {
						window.location.href = 'assignCandidates.html';
					});
					
					
				
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



