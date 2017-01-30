jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI27').addClass('active');
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
			
			
			jQuerySelectedEmployeestatus = jQuery('#selected_employeestatus');
			jQuerySelectedLeaveType = jQuery('#selected_leavetype');


			var serviceURL1 = envConfig.serviceBaseURL
				+ '/master/viewEmploymentStatusList.action?userId='
				+ userId;
				jQuery.ajax({
						url : serviceURL1,
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
									gemsEmploymentStatusId : val.gemsEmploymentStatusId,
									statusCodeDescription : val.statusCodeDescription
								};
							});

							// iterate over the data and append a select option
							jQuery.each(result, function(key, val) {
								jQuerySelectedEmployeestatus.append('<option id="'
										+ val.gemsEmploymentStatusId + '">'
										+ val.statusCodeDescription + '</option>');
								
								
							})
							
							jQuery("#selected_employeestatus").focus();
							

						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
				});
			
			jQuery('#selected_employeestatus').on('change', function() {
			//alert( jQuery(jQuery("#selected_employeestatus")).find('option:selected').attr('id')); 
			
			jQuery("#selected_leavetype1").hide();
			jQuery("#selected_leavetype").show();

			
			
			var serviceURL = envConfig.serviceBaseURL
			+ '/leavemanagement/viewLeaveTypeListNotAttachedToEmpStatus.action?userId='+userId;
			console.log(serviceURL);
				jQuery.ajax({
		        type: "GET",
				url: serviceURL,
				data:{
					searchEmployeeStatusCode :  jQuery(jQuery("#selected_employeestatus")).find('option:selected').attr('id')
				},
		        success: function(response){
					//alert(JSON.stringify(response.data));
					jQuery.each(response.data, function(index, value){
					
						jQuerySelectedLeaveType.append('<option id="'+ value.gemsLeaveTypeMasterId + '">'+ value.leaveTypeCodeDescription +'</option>'); 
				});	
		        },
		         error: function(e){
		             alert('Error1: ' +e);
		        },
				statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
				}
			});



			});



			var data = JSON.parse(localStorage.getItem('editLeaveTypeSummar_Data'));
			if (data !== null) {
				
				jQuery("#leaveSummayMasterId").val(data.leaveSummayMasterId);
				jQuery("#totalDays").val(data.totalDays);
				jQuery("#selected_leavetype1").val(data.selected_leavetype);
				jQuery("#selected_employeestatus option[id='" + data.selectedGemsEmploymentStatusId + "']").attr("selected","selected");
				jQuery("#selected_leavetype option[id='" + data.selectedGemsLeaveTypeMasterId + "']").attr("selected","selected");
		

			}


			
});		
		
		
		
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
