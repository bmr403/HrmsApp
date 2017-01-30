jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI73').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
	
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			var userId = sessionStorage.getItem("userId");

	jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "runAllocation.html";
					});
	jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "runAllocation.html";
					});

			jQuery("#run_allocation_btn").click(function(e) {
			e.preventDefault();



		var serviceURL = envConfig.serviceBaseURL
				+ '/employee/runEmployeeLeaveSummary.action?userId='+userId;

		jQuery.ajax({
			url : serviceURL,
			dataType : "json",
			type : 'GET',
			success : function(response) {
				if(response.success == true){
				
				jQuery('#success_allocation_modal').modal('toggle');
				jQuery('#success_allocation_modal').modal('view');
				

				} else {
					jQuery('#error_modal').modal('toggle');
					jQuery('#error_modal').modal('view');
				}
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
