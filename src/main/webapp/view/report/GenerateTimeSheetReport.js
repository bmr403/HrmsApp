jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L30').addClass('active');
						jQuery("ul.U30").show();
						jQuery("ul.U30").find('li#LI89').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
	
			jQuery('#form_search_error_message1').hide();
			jQuery('#timesheet_monthyear').monthpicker({				
			}).on("change", function() {
				
				jQuery('#form_search_error_message1').hide();
			});

			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});


			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			jQuery("#generate_timesheet_btn").click(function(e) {

				e.preventDefault();
				generate_timesheetreport();

			});
			jQuery("#resetGen_timesheet_btn").click(function(e) {
				
				jQuery('#timesheet_monthyear').val("");
				jQuery('#timesheet_empcode').val("");				

			});

			jQuery("#download_timesheet_btn").click(function(e) {

				timeMonthYear = jQuery('#timesheet_monthyear').val();

				if (timeMonthYear == '')
				{
					jQuery('#form_search_error_message1').show();
					return false;
				}

				
				//alert(timeMonthYear.replace('/','-'));
				//window.location.href="http://localhost:8080/upload/timesheet/"+timeMonthYear.replace('/','-')+".zip";
				window.location.href="http://ec2-52-40-197-21.us-west-2.compute.amazonaws.com:8080/upload/timesheet/"+timeMonthYear.replace('/','-')+".zip";
				

			});
			
			
			jQuery('#ok_btn').click(function(e) {
				e.preventDefault();
				window.location.href = "GenerateTimeSheetReport.html";
				
			});

			jQuery('#error_btn').click(function(e) {
				e.preventDefault();
				window.location.href = "GenerateTimeSheetReport.html";
			});
			
			
			
			function generate_timesheetreport() {
				
				timeMonthYear = jQuery('#timesheet_monthyear').val();				

                                if (timeMonthYear == '')
                                {
                                        jQuery('#form_search_error_message1').show();
                                        return false;
                                }

                                timesheet_empcode = jQuery('#timesheet_empcode').val();

                                var serviceURL = envConfig.serviceBaseURL
                                + '/timesheet/generateTimeSheetReport.action?userId='+userId;

                                jQuery.ajax({
                                        url : serviceURL,
                                        dataType : "json",
                                        data : {
                                                timeMonthYear : timeMonthYear
                                        },
                                        type : 'GET',
                                        success : function(response) {
                                                if(response.success == true){
												jQuery('#success_timesheetreport_modal').modal('toggle');
                                                jQuery('#success_timesheetreport_modal').modal('view');
												
												

                                                } else {
                                                        jQuery('#error_timesheetreport_modal').modal('toggle');
                                                        jQuery('#error_timesheetreport_modal').modal('view');
                                                }
                                        },
                                        failure : function(data) {
                                                jQuery('#error_timesheetreport_modal').modal('toggle');
                                                jQuery('#error_timesheetreport_modal').modal('view');

                                        },
                                        statusCode : {
                                                403 : function(xhr) {
                                                        window.location.href = "../../view/login/sessionoutlogin.html";

                                                }
										}
										});
				
				
			}
			
			
		
});

				
	
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
