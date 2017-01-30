jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L69').addClass('active');
						jQuery("ul.U69").show();
						jQuery("ul.U69").find('li#LI90').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
	
			
			jQuery("#demo-1").monthpicker({				
			}).on("change", function() {
				
				
				var selectedMonthYear = this.value;
				weekList = jQuery('#weekList');
				var serviceURL = envConfig.serviceBaseURL
							+ '/timesheet/getWeeksList.action?userId='
							+ userId;
				console.log(serviceURL);
					
				jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						data : {
							selectedMonthYear : selectedMonthYear
						},
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
									rowId : val.rowId,
									weekStartEndString : val.weekStartEndString
								};
							});

							// iterate over the data and append a select option
							weekList.append('<option id="0">Select Week</option>');
							jQuery.each(result, function(key, val) {
								weekList.append('<option id="'
										+ val.weekStartEndString + '">'
										+ val.weekStartEndString + '</option>');
								
								
							})
							
							jQuery("#weekList").focus();
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


			});
			

			

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			jQuery("#unlock_timesheet_btn").click(function(e) {

				e.preventDefault();
				unLockTimeSheet();

			});
			jQuery("#resetGen_timesheet_btn").click(function(e) {
				
				jQuery('#timesheet_monthyear').val("");
				jQuery('#timesheet_empcode').val("");				

			});

			jQuery('#ok_btn').click(function(e) {
				e.preventDefault();
				window.location.href = "lockUnlock.html";
			});

			jQuery('#error_btn').click(function(e) {
				e.preventDefault();
				window.location.href = "lockUnlock.html";
			});
			
			
			
			function unLockTimeSheet() {
				
				selectedMonthYear = jQuery('#demo-1').val();
				timesheet_empcode = jQuery('#timesheet_empcode').val();
				weekDuration = jQuery(jQuery("#weekList")).find('option:selected').attr('id');
				
				var serviceURL = envConfig.serviceBaseURL
				+ '/timesheet/lockUnlockTimeSheet.action?userId='+userId;

				jQuery.ajax({
					url : serviceURL,
					dataType : "json",
					data : {
						selectedMonthYear : selectedMonthYear,
						timesheet_empcode : timesheet_empcode,
						weekDuration : weekDuration
						
					},
					type : 'POST',
					success : function(response) {
						if(response.success == true){
						jQuery('#success_timesheetlock_modal').modal('toggle');
						jQuery('#success_timesheetlock_modal').modal('view');
						

						} else {
							jQuery('#error_timesheetlock_modal').modal('toggle');
							jQuery('#error_timesheetlock_modal').modal('view');
						}
					},
					failure : function(data) {
						jQuery('#error_timesheetlock_modal').modal('toggle');
						jQuery('#error_timesheetlock_modal').modal('view');

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

				
	
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
