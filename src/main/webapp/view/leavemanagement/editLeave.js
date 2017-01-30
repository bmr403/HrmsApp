jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI68').addClass('active');
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

			jQuery('#errorDialog').hide();

			jQuery('#employeeleave_cancel_btn').click(function(event) {
						window.location.href = 'myLeaveList.html';
					});
			
			
			jQuery("#fromDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#toDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});

					jQuery("#gemsEmployeeCompOffFrom").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
					jQuery("#gemsEmployeeCompOffTo").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});


					jQuery("#employeeDutyResumeDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
			
					jQuery("#toDate").change( function(){
					
					 var total_days = daydiff(parseDate(jQuery('#fromDate').val()), parseDate(jQuery('#toDate').val()));
							//	alert("Number of days between the dates shown are: " + total_days);
					 if (jQuery('#fromDate').val() == jQuery('#toDate').val() )
					 {
						jQuery("#total_days").val('1');
					 }
					 else
					 {
						jQuery("#total_days").val(total_days + 1);
					 }
					 
					}); 
					/*jQuery('#fromDate').on('focus', function() {

					});*/
					jQuery("#fromDate").change( function(){
					
						if (jQuery("#fromDate").val() == "")
						{
							jQuery("#total_days").prop('disabled',true);
							jQuery("#total_days").val('');
						}
						else
						{
							jQuery("#total_days").prop('disabled',false);
						}
						
					});
					
					/*jQuery("#total_days").keypress(function(event){ 
						return isNumberKey(event, this);
					});*/
					
					jQuery("#total_days").change( function(){
					
					var totalDays = jQuery("#total_days").val();
					var total_days1 = daydiff(parseDate(jQuery('#fromDate').val()), parseDate(jQuery('#toDate').val()));
					total_days1 = total_days1 + 1;
					if ((totalDays >0) && (totalDays <1)) 
					{
						totalDays = 1;
					}
					if (totalDays != total_days1)
					{
						jQuery("#totalDaysDialog").dialog({width:400,height:75});
						jQuery("#fromDate").val('');
						jQuery("#toDate").val('');
						jQuery("#total_days").prop('disabled',true);
						
						

					}

					});

					jQuery("#gemsEmployeeCompOffTo").change( function(){
					
					 var total_days = daydiff(parseDate(jQuery('#gemsEmployeeCompOffFrom').val()), parseDate(jQuery('#gemsEmployeeCompOffTo').val()));
							//	alert("Number of days between the dates shown are: " + total_days);
					 if (jQuery('#gemsEmployeeCompOffFrom').val() == jQuery('#gemsEmployeeCompOffTo').val() )
					 {
						jQuery("#compofftotal_days").val('1');
					 }
					 else
					 {
						jQuery("#compofftotal_days").val(total_days + 1);
					 }
					 
					}); 
					/*jQuery('#fromDate').on('focus', function() {

					});*/
					jQuery("#gemsEmployeeCompOffFrom").change( function(){
					
						if (jQuery("#gemsEmployeeCompOffFrom").val() == "")
						{
							jQuery("#compofftotal_days").prop('disabled',true);
							jQuery("#compofftotal_days").val('');
						}
						else
						{
							jQuery("#compofftotal_days").prop('disabled',false);
						}		
					});
					
					/*jQuery("#total_days").keypress(function(event){ 
						return isNumberKey(event, this);
					});*/
					
					jQuery("#compofftotal_days").change( function(){
					
					var totalDays = jQuery("#compofftotal_days").val();
					var total_days1 = daydiff(parseDate(jQuery('#gemsEmployeeCompOffFrom').val()), parseDate(jQuery('#gemsEmployeeCompOffTo').val()));
					total_days1 = total_days1 + 1;
					if ((totalDays >0) && (totalDays <1)) 
					{
						totalDays = 1;
					}
					if (totalDays != total_days1)
					{
						jQuery("#totalDaysDialog").dialog({width:400,height:75});
						jQuery("#gemsEmployeeCompOffFrom").val('');
						jQuery("#gemsEmployeeCompOffTo").val('');
						jQuery("#compofftotal_days").prop('disabled',true);
						
						

					}

					});

			var arr = [];
			var leaveTypeArray;

			var data = JSON.parse(localStorage.getItem('editLeave_Data'));
			if (data !== null) {
				
				jQuery("#gemsEmployeeLeaveId").val(data.gemsEmployeeLeaveId);
				jQuery("#gemsEmployeeMasterId").val(data.selectedGemsEmployeeMasterId);
				jQuery("#gemsEmployeeCompOffFrom").val(data.gemsEmployeeCompOffFrom);
				jQuery("#gemsEmployeeCompOffTo").val(data.gemsEmployeeCompOffTo);
				jQuery("#fromDate").val(data.fromDate);
				jQuery("#toDate").val(data.toDate);
				jQuery("#employeeDutyResumeDate").val(data.employeeDutyResumeDate);
				jQuery("#employeeDutyResumeDate").val(data.employeeDutyResumeDate);
				jQuery("#contactNumber").val(data.contactNumber);
				jQuery("#addressDuringLeave").val(data.addressDuringLeave);
				jQuery("#leave_remarks").val(data.reasonForLeave);
				
				if (!(data.currentStatus == "Draft"))
				{
					jQuery("#addLeaveBtn").hide();
					jQuery("#addLeaveSubmitBtn").hide();
					jQuery("#employeeleave_cancel_btn").hide();
				}
				if (data.gemsEmployeeCompOffFrom == '')
				{
					jQuery('#comp-off_row').hide();
				}
				else
				{
					var total_compOffEligibleDays = daydiff(parseDate(jQuery('#gemsEmployeeCompOffFrom').val()), parseDate(jQuery('#gemsEmployeeCompOffTo').val()));
					total_compOffEligibleDays = total_compOffEligibleDays + 1;
					jQuery("#compofftotal_days").val(total_compOffEligibleDays);
				}
				
				var total_leaveDays = daydiff(parseDate(jQuery('#fromDate').val()), parseDate(jQuery('#toDate').val()));
				total_leaveDays = total_leaveDays + 1;
				jQuery("#total_days").val(total_leaveDays);
				
				

				var selectedGemsEmployeeLeaveSummaryId = data.selectedGemsEmployeeLeaveSummaryId;

				jQueryDropDownLeaveType = jQuery('#dropdown_leavetype');
					var serviceURL5 = envConfig.serviceBaseURL
							+ '/employee/getGemsLeaveSummaryByEmployee.action?userId='
							+ userId;
					jQuery.ajax({
						url : serviceURL5,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							//var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							leaveTypeArray = jQuery.map(arr, function(val, key) {
									return {
										gemsEmployeeLeaveSummaryId : val.gemsEmployeeLeaveSummaryId,
										leaveBalance : val.leaveBalance
									};
								});

							var result = jQuery.map(arr, function(val, key) {
								return {
									gemsEmployeeLeaveSummaryId : val.gemsEmployeeLeaveSummaryId,
									leaveSummaryCodeBalance : val.leaveSummaryCodeBalance
								};
							});

							// iterate over the data and append a select option
							
							jQuery.each(result, function(key, val) {
								
								if (val.gemsEmployeeLeaveSummaryId == selectedGemsEmployeeLeaveSummaryId)
								{
									jQueryDropDownLeaveType.append('<option id="'
										+ val.gemsEmployeeLeaveSummaryId + '" selected>'
										+ val.leaveSummaryCodeBalance + '</option>');
									var selectedLeaveTypeValue = val.leaveSummaryCodeBalance;
						
									if (selectedLeaveTypeValue.includes("Comp") == true)
									{
										jQuery('#comp-off_row').show();
										jQuery('#leaveTypeString').val('COMPOFF');
									}
									else
									{
										jQuery('#comp-off_row').hide();
									}
								}
								else
								{
									jQueryDropDownLeaveType.append('<option id="'
										+ val.gemsEmployeeLeaveSummaryId + '">'
										+ val.leaveSummaryCodeBalance + '</option>');
								}
								
							})
							
							jQuery("#dropdown_leavetype").focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								window.location.href = "../../view/login/sessionoutlogin.html";

							}
						}
					});

				
		

			} // end of edit data if condition

			jQuery("#dropdown_leavetype").change( function(){
					
						var selectedLeaveType = jQuery(jQuery("#dropdown_leavetype")).find('option:selected').attr('id');
						jQuery.each(leaveTypeArray, function(key, val) {
							if (val.gemsEmployeeLeaveSummaryId == selectedLeaveType)
							{
								jQuery("#leaveBalance").val(val.leaveBalance);
								//alert(val.gemsEmployeeLeaveSummaryId);
								//alert(val.leaveBalance);
							}
						});

						var selectedLeaveTypeValue = this.value;
						
						if (selectedLeaveTypeValue.includes("Comp") == true)
						{
							jQuery('#comp-off_row').show();
							jQuery('#leaveTypeString').val('COMPOFF');
							

						}

						

					});

			

			function empLeaveSummary_jQueryDataTableAjax(serviceURL) {

					console.log(serviceURL);
					var empLeaveSummary_table = jQuery("#employeeleaveSummary_table")
								.DataTable(
										{
											"sAjaxSource" : serviceURL,
											"bProcessing" : false,
											"bServerSide" : true,
											"bPaginate" : true,
											"bFilter" : true,
											"searching" : false,
											"bSort" : false,
											"bDestroy" : true,
											"bJQueryUI" : false,
											/*"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "searchLeaveTypeCode",
													"value" : searchCode
												}, {
													"name" : "searchLeaveTypeDescription",
													"value" : searchDescription
												});
											},*/
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 10,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
												if (oSettings.fnRecordsTotal() <= 10) {
													jQuery('.dataTables_length')
															.hide();
													jQuery(
															'.dataTables_paginate')
															.hide();
												} else {
													jQuery('.dataTables_length')
															.show();
													jQuery(
															'.dataTables_paginate')
															.show();
												}
											},
											"aoColumns" : [
													{
														"mData" : "selected_leavetype"
													},
													{
														"mData" : "leaveEntitled"
													},
													{
														"mData" : "leaveTaken"
													},
													{
														"mData" : "leaveBalance"
													},
													{
														"mData" : "leaveScheduled"
													}
													,
													{
														"mData" : "lopDays"
													}
													],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/getGemsLeaveSummaryByEmployee.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					empLeaveSummary_jQueryDataTableAjax(serviceURL);

			jQuery.validator.addMethod("greaterThan", 
						function(value, element, params) {

							if (!/Invalid|NaN/.test(new Date(value))) {
								return new Date(value) > new Date(jQuery(params).val());
							}

							return isNaN(value) && isNaN(jQuery(params).val()) 
								|| (Number(value) > Number(jQuery(params).val())); 
						},'Must be greater than {0}.');
					

					var leave_submit_type = "Draft";
					jQuery('#addLeaveBtn, #addLeaveSubmitBtn').click(function () {
						if (this.id == 'addLeaveBtn') {
							leave_submit_type = "Draft";
						}
						else if (this.id == 'addLeaveSubmitBtn') {
							leave_submit_type = "In Progress";
						}
					});

					jQuery("#employee-leave-form").validate({
    
						// Specify the validation rules
						rules: {
							fromDate: "required",
							toDate: "required",
							employeeDutyResumeDate: "required",
							contactNumber : "required",
							addressDuringLeave : "required",
							leave_remarks : "required"
							
							

							
						},
						
						// Specify the validation error messages
						messages: {
							

							
						},
						
						submitHandler: function(form) {
							var gemsEmployeeLeaveId = jQuery("#gemsEmployeeLeaveId").val();
							var gemsEmployeeMasterId = jQuery("#gemsEmployeeMasterId").val();
							var fromDate = jQuery("#fromDate").val();
							var toDate = jQuery("#toDate").val();
							var employeeDutyResumeDate = jQuery("#employeeDutyResumeDate").val();
							var selected_leavesummarytype = jQuery(jQuery("#dropdown_leavetype")).find('option:selected').attr('id');
							
							var contactNumber = jQuery("#contactNumber").val();
							var addressDuringLeave = jQuery("#addressDuringLeave").val();
							var leave_remarks = jQuery("#leave_remarks").val();
							var serviceURL3 = envConfig.serviceBaseURL+ '/employee/saveGemsEmployeeLeave.action?userId='+userId;
							var total_days = jQuery("#total_days").val();
							var leaveBalance = jQuery("#leaveBalance").val();

							jQuery.ajax({
								beforeSend: function (request) {

									var leaveTypeString = jQuery('#leaveTypeString').val();
									if (!((typeof leaveTypeString == 'undefined') || (leaveTypeString == '')))
									{
										var gemsEmployeeCompOffTo = jQuery("#gemsEmployeeCompOffTo").val();
										var selectedCompOffToDate;
										if (!(typeof compOffToDate == 'undefined'))
										{
											var selectedCompOffToDatePart = gemsEmployeeCompOffTo.split("/");
											var selectedCompOffToDate = new Date(selectedCompOffToDatePart[2], (selectedCompOffToDatePart[0] - 1), selectedCompOffToDatePart[1]);	
											var fromDate = jQuery("#fromDate").val();
											var selectedFromDatePart = fromDate.split("/");
											var selectedFromDate = new Date(selectedFromDatePart[2], (selectedFromDatePart[0] - 1), selectedFromDatePart[1]);
											if (selectedFromDate <= selectedCompOffToDate)
											{
												jQuery("#errorDialog").dialog({
												autoOpen: false,
												width: 600,
												height: 70,
												buttons: {
													Close: function()
													{
														jQuery(this).dialog("close");
													}
												}
												});
												 jQuery("#errorDialog")
												.dialog("open");
												return false;
											}
											var totalEligibleDays = jQuery("#compofftotal_days").val();
											var totalAppliedDays = jQuery("#total_days").val();
											if (totalAppliedDays > totalEligibleDays)
											{
												jQuery("#errorDialog").dialog({
												autoOpen: false,
												width: 600,
												//height: 70,
												buttons: {
													Close: function()
													{
														jQuery(this).dialog("close");
													}
												}
												});
												jQuery('#errorDialog span').text('Total leave days should be less than or equal to total comp-off eligible days');
												 jQuery("#errorDialog")
												.dialog("open");
												return false;
											}

										}
									}
									if (typeof leaveTypeString == 'undefined')
									{
										if (total_days > leaveBalance)
										{
											if (confirm("Total days of leave exceeding the available leave balance for the leave type. Remaining days will be considered as LOP, Do you want to proceed?")) {
											
											}
											else
												return false;
										}
									}
									
									
									
								},
								url : serviceURL3,
								dataType : "json",
								data : {
									gemsEmployeeLeaveId : gemsEmployeeLeaveId,
									gemsEmployeeMasterId : gemsEmployeeMasterId,
									fromDate : fromDate,
									toDate : toDate,
									employeeDutyResumeDate	: employeeDutyResumeDate,
									selected_leavesummarytype : selected_leavesummarytype,
									contactNumber : contactNumber,
									addressDuringLeave : addressDuringLeave,
									reasonForLeave : leave_remarks,
									leave_submit_type : leave_submit_type
								},
								type : 'POST',
								success : function(response) {
									window.location.href = "myLeaveList.html";
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
						}
					});


			
});		
		
		
		
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
function daydiff(first, second) {
    return (second-first)/(1000*60*60*24)
}
function parseDate(str) {
    var mdy = str.split('/')
    return new Date(mdy[2], mdy[0]-1, mdy[1]);
}
function isNumberKey(evt, element) {
  var charCode = (evt.which) ? evt.which : event.keyCode;
  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charcode == 8))
    return false;
  else {
    var len = jQuery(element).val().length;
    var index = jQuery(element).val().indexOf('.');
    if (index > 0 && charCode == 46) {
      return false;
    }
    if (index > 0) {
      var CharAfterdot = (len + 1) - index;
      if (CharAfterdot > 3) {
        return false;
      }
    }

  }
  return true;
}