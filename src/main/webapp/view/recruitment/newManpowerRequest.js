jQuery(document)
		.ready(
				function() {
					
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI83').addClass('active');
					});
				
					jQuery("#requestDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#requestValidFrom").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#requestValidTo").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					
					var userId = sessionStorage.getItem("userId");
					
					var mr_submit_type = "Draft";
					jQuery('#addRequestBtn, #addRequestSubmitBtn').click(function () {
						if (this.id == 'addRequestBtn') {
							mr_submit_type = "Draft";
						}
						else if (this.id == 'addRequestSubmitBtn') {
							mr_submit_type = "Submitted";
						}

						

					});
					
					jQuery("#mprequest-general-form").validate({
    
						// Specify the validation rules
						rules: {
							jobCode: "required",
							jobProfile: "required",
							profileExperienceMax: "required",
							profileExperienceMin: "required",
							maxSalary: "required",
							minimumSalary: "required",
							education: "required",
							jobLocation: "required",
							jobPosition: "required",
							numberOfResources : "required"
							
									
							
						},
						
						// Specify the validation error messages
						messages: {
					

							
						},
						
						submitHandler: function(form) {
							var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/recruitment/saveManpowerRequest.action?userId='+userId;
	
	var jobCode = jQuery("#jobCode").val();
	var requestType = jQuery("#requestType").val();
	var gemsCustomer = jQuery(jQuery("#gemsCustomer")).find('option:selected').attr('id');
	var dropdown_department = jQuery(jQuery("#dropdown_department")).find('option:selected').attr('id');
	var requestValidFrom = jQuery("#requestValidFrom").val();	
	var requestValidTo = jQuery("#requestValidTo").val();	
	var jobPosition = jQuery("#jobPosition").val();
	var jobLocation = jQuery("#jobLocation").val();
	var employmentType = jQuery("#employmentType").val();
	var education = jQuery("#education").val();
	var minimumSalary = jQuery("#minimumSalary").val();
	var maxSalary = jQuery("#maxSalary").val();
	var profileExperienceMin = jQuery("#profileExperienceMin").val();
	var profileExperienceMax = jQuery("#profileExperienceMax").val();
	var numberOfResources = jQuery("#numberOfResources").val();
	var jobProfile = CKEDITOR.instances['jobProfile'].getData();
	
	

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			jobCode : jobCode,
			requestType : requestType,
			gemsCustomer : gemsCustomer,
			dropdown_department : dropdown_department,
			requestValidFrom :requestValidFrom,
			requestValidTo : requestValidTo,
			jobPosition : jobPosition,
			jobLocation : jobLocation,
			employmentType : employmentType,
			education : education,
			minimumSalary : minimumSalary,
			maxSalary : maxSalary,
			profileExperienceMin : profileExperienceMin,
			profileExperienceMax : profileExperienceMax,
			numberOfResources : numberOfResources,
			jobProfile : jobProfile,
			mr_submit_type : mr_submit_type

		},
		type : 'POST',
		success : function(response) {
			window.location.href = "manpowerRequest.html";	
		},
		failure : function(data) {
			window.location.href = "../dashboard/dashboard.html";
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
					
					var userId = sessionStorage.getItem("userId");
					
					jQuery('#request_cancel_btn').click(function(event) {
						window.location.href = 'manpowerRequest.html';
					});
					
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "manpowerRequest.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "manpowerRequest.html";
					});

					jQueryGemsCustomer = jQuery('#gemsCustomer');
					var serviceURL = envConfig.serviceBaseURL
						+ '/customer/viewCustomerList.action?userId='+userId;
					console.log(serviceURL);
					jQueryGemsCustomer.append('<option id="0">Select Customer</option>'); 
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        success: function(response){
								//alert(JSON.stringify(response.data));
								jQuery.each(response.data, function(index, value){
								
					jQueryGemsCustomer.append('<option id="'+ value.gemsCustomerMasterId + '">'+ value.gemsCustomerCodeName +'</option>'); 
							});	
					        },
					         error: function(e){
					             alert('Error1: ' +e);
					        }
					    });

					departmentDownList = jQuery('#dropdown_department');
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewDepartmentList.action?userId='
							+ userId;
					console.log(serviceURL);
					
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
									gemsDepartmentId : val.gemsDepartmentId,
									departmentCode : val.departmentCode
								};
							});

							// iterate over the data and append a select option
							departmentDownList.append('<option id="0">Select Department</option>');
							jQuery.each(result, function(key, val) {
								departmentDownList.append('<option id="'
										+ val.gemsDepartmentId + '">'
										+ val.departmentCode + '</option>');
								
								
							})
							
							jQuery("#dropdown_department").focus();
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

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}



