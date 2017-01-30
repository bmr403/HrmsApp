jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L86').addClass('active');
						jQuery("ul.U86").show();
						jQuery("ul.U86").find('li#LI87').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
	
			 jQuery('#payslipDate').monthpicker();

			 jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();

			jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "importData.html";
					});
			jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "importData.html";
					});
			
			jQuery('#file').bind('change', function() {
				var fsize = (this.files[0].size/1024/1024).toFixed(2);
				if (fsize > 5.00)
				{
					jQuery('#fileError').text('Upload file size should be less than 5 MB');
					jQuery('#fileError').show();
					return false;
				}
				else
				{
					jQuery('#fileError').hide();
				}
				//alert('This file size is: ' + (this.files[0].size/1024/1024).toFixed(2) + " MB");
			});
			
			jQuery("form").submit(function(e) {
												e.preventDefault();
												var import_doc = jQuery("#import_doc").val();
												var serviceURL = envConfig.serviceBaseURL
														+ '/data/importData.action?userId='
														+ userId
												var formData = new FormData(jQuery(this)[0]);
												
												if (jQuery('#file').val()=='')
												{
													jQuery('#fileError').text('Please upload the document');
													jQuery('#fileError').show();
													return false;
												}
												else
												{
													jQuery('#fileError').hide();
												}
												

												jQuery
															.ajax({
																url : serviceURL,
																cache : false,
																dataType : 'text',
																data : formData,
																type : 'POST',
																enctype : 'multipart/form-data',
																processData : false,
																contentType : false,
																success : function(data) {
																	jQuery('#success_import_modal').modal('toggle');
																	jQuery('#success_import_modal').modal('view');
																	
																},
																failure : function(data) {
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
