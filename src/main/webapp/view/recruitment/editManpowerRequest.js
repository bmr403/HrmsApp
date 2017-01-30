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
					
					jQuery("#manpowerReqInterview").load("manpowerReqInterview.html");
					
					var userId = sessionStorage.getItem("userId");
						
					
						var data = JSON.parse(localStorage.getItem('editmr_Data'));
						jQueryGemsCustomer = jQuery('#gemsCustomer');
						jQueryDropDownDocumentType = jQuery("#requestType");
						
						if (data !== null) {
							
							if ((data.requestStatus =="Submitted") || (data.requestStatus =="Approved"))
							{
								jQuery("#addRequestBtn").hide();
								jQuery("#addRequestSubmitBtn").hide();
								jQuery("#request_cancel_btn").hide();
							}

							jQuery('#gemsMapowerRequestId').val(data.gemsMapowerRequestId);
							localStorage.setItem('gemsMapowerRequestId', data.gemsMapowerRequestId);
							jQuery("#jobCode").val(data.jobCode);
							jQueryDropDownDocumentType = jQuery('#requestType');
							if (data.requestType == "INTERNAL")
							{
								jQueryDropDownDocumentType.append('<option id="INTERNAL" selected>INTERNAL</option>');
								jQueryDropDownDocumentType.append('<option id="EXTERNAL">EXTERNAL</option>');
							}
							else if (data.requestType == "EXTERNAL")
							{
								jQueryDropDownDocumentType.append('<option id="INTERNAL" >INTERNAL</option>');
								jQueryDropDownDocumentType.append('<option id="EXTERNAL" selected>EXTERNAL</option>');
							}
							else
							{
								jQueryDropDownDocumentType.append('<option id="0" >Select Recruiment Type</option><option id="INTERNAL" >INTERNAL</option>');
								jQueryDropDownDocumentType.append('<option id="EXTERNAL" selected>EXTERNAL</option>');
							}
							jQuery("#requestType").focus();

							customerList = jQuery('#gemsCustomer');
							var serviceURL = envConfig.serviceBaseURL
									+ '/customer/viewCustomerList.action?userId='
									+ userId;
							console.log(serviceURL);
							var selectedGemsCustomerMasterId = data.selectedGemsCustomerMasterId;
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
											gemsCustomerMasterId : val.gemsCustomerMasterId,
											gemsCustomerCodeName : val.gemsCustomerCodeName
										};
									});
		
									// iterate over the data and append a select option
									if (selectedGemsCustomerMasterId == 0)
									{
										customerList.append('<option id="0">Select Customer</option>');
									}
									jQuery.each(result, function(key, val) {
										
										
										if (val.gemsCustomerMasterId == selectedGemsCustomerMasterId)
										{
											customerList.append('<option id="'
													+ val.gemsCustomerMasterId + '" selected>'
													+ val.gemsCustomerCodeName + ' </option>');
										}
										else
										{
											customerList.append('<option id="'
													+ val.gemsCustomerMasterId + '">'
													+ val.gemsCustomerCodeName + '</option>');
										}
										
										
										
									})
									jQuery("#gemsCustomer").focus();
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
							
							var currentDepartmentId = data.currentDepartmentId;
							
							departmentList = jQuery('#dropdown_department');
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
									if (currentDepartmentId == 0)
									{
										departmentList.append('<option id="0">Select Department</option>');
									}
									jQuery.each(result, function(key, val) {
										
										
										if (val.gemsDepartmentId == currentDepartmentId)
										{
											departmentList.append('<option id="'
													+ val.gemsDepartmentId + '" selected>'
													+ val.departmentCode + ' </option>');
										}
										else
										{
											departmentList.append('<option id="'
													+ val.gemsDepartmentId + '">'
													+ val.departmentCode + '</option>');
										}
										
										
										
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
							jQuery("#numberOfResources").val(data.numberOfResources);
							jQuery("#requestValidFrom").val(data.requestValidFrom);
							jQuery("#requestValidTo").val(data.requestValidTo);
							jQuery("#jobPosition").val(data.jobPosition);
							jQuery("#jobLocation").val(data.jobLocation);
							
							employmentType = jQuery('#employmentType');
							if (data.employmentType == "PERMANENT")
							{
								jQueryDropDownDocumentType.append('<option id="PERMANENT" selected>PERMANENT</option>');
								jQueryDropDownDocumentType.append('<option id="CONTRACT">CONTRACT</option><option id="C2H">C2H</option>');
							}
							else if (data.employmentType == "CONTRACT")
							{
								jQueryDropDownDocumentType.append('<option id="PERMANENT" >PERMANENT</option>');
								jQueryDropDownDocumentType.append('<option id="CONTRACT" selected>CONTRACT</option><option id="C2H">C2H</option>');
							}
							else if (data.employmentType == "C2H")
							{
								jQueryDropDownDocumentType.append('<option id="PERMANENT" >PERMANENT</option>');
								jQueryDropDownDocumentType.append('<option id="CONTRACT" >CONTRACT</option><option id="C2H" selected>C2H</option>');
							}
							else
							{
								jQueryDropDownDocumentType.append('<option id="0">Select Employment Type</option><option id="PERMANENT" >PERMANENT</option>');
								jQueryDropDownDocumentType.append('<option id="CONTRACT" >CONTRACT</option><option id="C2H">C2H</option>');
							}
							jQuery("#employmentType").focus();

							jQuery("#education").val(data.education);
							jQuery("#minimumSalary").val(data.minimumSalary);
							jQuery("#maxSalary").val(data.maxSalary);

							jQuery("#profileExperienceMin").val(data.profileExperienceMin);
							jQuery("#profileExperienceMax").val(data.profileExperienceMax);
							jQuery("#jobProfile").val(data.jobProfile);
							


						} 
					
					
					
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
	var gemsMapowerRequestId = jQuery("#gemsMapowerRequestId").val();
	var jobCode = jQuery("#jobCode").val();
	var education = jQuery("#education").val();
	var requestType = jQuery("#requestType").val();
	var requestDate = jQuery("#requestDate").val();	
	var jobPosition = jQuery("#jobPosition").val();
	var jobLocation = jQuery("#jobLocation").val();
	var employmentType = jQuery("#employmentType").val();
	var minimumSalary = jQuery("#minimumSalary").val();
	var maxSalary = jQuery("#maxSalary").val();
	var dropdown_department = jQuery(jQuery("#dropdown_department")).find('option:selected').attr('id');
	var gemsCustomer = jQuery(jQuery("#gemsCustomer")).find('option:selected').attr('id');
	var profileExperienceMin = jQuery("#profileExperienceMin").val();
	var profileExperienceMax = jQuery("#profileExperienceMax").val();
	var requestValidFrom = jQuery("#requestValidFrom").val();
	var requestValidTo = jQuery("#requestValidTo").val();
	var numberOfResources = jQuery("#numberOfResources").val();
	var jobProfile = CKEDITOR.instances['jobProfile'].getData();

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsMapowerRequestId : gemsMapowerRequestId,
				gemsCustomer : gemsCustomer,
			jobCode : jobCode,
			education : education,
			requestType : requestType,
			requestDate : requestDate,
			jobPosition : jobPosition,
			jobLocation	: jobLocation,
			numberOfResources : numberOfResources,
			employmentType		: employmentType,
			minimumSalary		: minimumSalary,
			maxSalary	: maxSalary,
			dropdown_department	: dropdown_department,
			profileExperienceMin : profileExperienceMin,
			profileExperienceMax	: profileExperienceMax,
			requestValidFrom	: requestValidFrom,
			requestValidTo	: requestValidTo,
			jobProfile	: jobProfile,
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

	
					
					
					
					
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}



