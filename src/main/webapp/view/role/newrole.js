jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L2').addClass('active');
						jQuery("ul.U2").show();
						jQuery("ul.U2").find('li#LI36').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			jQuery("#gemsProjectEndDate,#gemsProjectStartDate").datepicker({
			});
		
			
				jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "rolelist.html";
					});
	jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newrole.html";
					});
	
	jQuery("#addRoleBtn").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/master/saveRole.action?userId='+userId;
	var activeStatus = jQuery('#activeStatus').is(':checked') ? projectisActive = "on" : projectisActive = "off";
	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			roleCode : jQuery("#roleCode").val(),
			roleName : jQuery("#roleName").val(),
			activeStatus : activeStatus
		},
		type : 'POST',
		success : function(response) {
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				jQuery('#success_role_modal').modal('toggle');
				jQuery('#success_role_modal').modal('view');
				

			}
			else
			{
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
				alert("Session will be Expired");
				window.location.href = "../../";

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
