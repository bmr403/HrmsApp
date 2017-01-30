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

			jQuery('#selected_employeestatus').on('change', function() {
			//alert( jQuery(jQuery("#selected_employeestatus")).find('option:selected').attr('id')); 
			
			
			var serviceURL = envConfig.serviceBaseURL
			+ '/leavemanagement/viewLeaveTypeListNotAttachedToEmpStatus.action?userId='+userId;
			jQuerySelectedLeaveType.append('<option id="0">Select Leave Type</option>');
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
							jQuerySelectedEmployeestatus.append('<option id="0">Select Employment Status</option>');
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
								alert("Session will be Expired");
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
				});
				
			
			
			
	jQuery("#addProjectTypeBtn").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/leavemanagement/saveLeaveSummary.action?userId='+userId;
	var totalDays = jQuery("#totalDays").val();
	var selected_employeestatus = jQuery(jQuery("#selected_employeestatus")).find('option:selected').attr('id');
	var selected_leavetype = jQuery(jQuery("#selected_leavetype")).find('option:selected').attr('id')
	
	var activeStatus = jQuery('#activeStatus').is(':checked') ? projectTypeisActive = "on" : projectTypeisActive = "off";
	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			totalDays : totalDays,
			selected_leavetype : selected_leavetype,
			selected_employeestatus : selected_employeestatus,
			activeStatus : activeStatus
		},
		type : 'POST',
		success : function(response) {
			window.location.href = "leavetypesummary.html";
		},
		failure : function(data) {
			window.location.href = "../dashboard/dashboard.html";
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
