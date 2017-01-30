jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI34').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();
			
			var userId = sessionStorage.getItem("userId");
			jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projecttype.html";
					});
			
	jQuery("#addProjectTypeBtn").click(function(e){
		e.preventDefault();		
		//alert(userId);	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/project/saveGemsProjectTypeMaster.action?userId='+userId;
	var gemsProjectTypeCode = jQuery("#gemsProjectTypeCode").val();
	var gemsProjectTypeDesc = jQuery("#gemsProjectTypeDesc").val();
	var projectTypeisActive = jQuery('#projectTypeisActive').is(':checked') ? projectTypeisActive = "on" : projectTypeisActive = "off";
	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			projectTypeCode : gemsProjectTypeCode,
			projectTypeDescription : gemsProjectTypeDesc,
			activeStatus : projectTypeisActive
		},
		type : 'POST',
		success : function(response) {
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {

																jQuery("#loading-div-background").hide();
																jQuery('#success_projecttype_modal').modal('toggle');
																jQuery('#success_projecttype_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_projecttype_modal').modal('toggle');
																jQuery('#error_projecttype_modal').modal('view');
																
															}
		},
		failure : function(data) {
			window.location.href = "../dashboard/dashboard.html";
		},
		statusCode : {
			403 : function(xhr) {
				alert("Session will be Expired");
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
