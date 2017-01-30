jQuery(document)
		.ready(
				function() {

					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI26').addClass('active');
					});
					jQuery(document).ajaxStart(customblockUI);
				    jQuery(document).ajaxStop(customunblockUI);
					jQuery("#fromDate").val('');
					jQuery("#toDate").val('');
					jQuery("#total_days").val('');
					jQuery("#total_days").prop('disabled',true);
					jQuery('#comp-off_row').hide();
					jQuery('#errorDialog').hide();

					jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
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
					jQuery("#employeeDutyResumeDate").datepicker({
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
					
					var userId = sessionStorage.getItem("userId");
					var gemsEmployeeMasterId = sessionStorage.getItem("gemsEmployeeMasterId");

					jQuery('#employeeleave_cancel_btn').click(function(event) {
						window.location.href = 'myLeaveList.html';
					});
					
					var arr = [];
					var leaveTypeArray;
					jQuerySelectedLeaveType = jQuery('#dropdown_leavetype');

					var serviceURL1 = envConfig.serviceBaseURL
					+ '/employee/getGemsLeaveSummaryByEmployee.action?userId='
					+ userId;
					jQuery.ajax({
							url : serviceURL1,
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
								jQuerySelectedLeaveType.append('<option id="0">Select Leave Type</option>');
								jQuery.each(result, function(key, val) {
									jQuerySelectedLeaveType.append('<option id="'
											+ val.gemsEmployeeLeaveSummaryId + '">'
											+ val.leaveSummaryCodeBalance + '</option>');
									
									
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


	jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "myLeaveList.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "myLeaveList.html";
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

					jQuery.validator.addMethod("greaterThanEqual", 
						function(value, element, params) {

							if (!/Invalid|NaN/.test(new Date(value))) {
								return new Date(value) >= new Date(jQuery(params).val());
							}

							return isNaN(value) && isNaN(jQuery(params).val()) 
								|| (Number(value) >= Number(jQuery(params).val())); 
						},'Must be greater than equal {0}.');

					
					

					var leave_submit_type = "Draft";
					jQuery('#addLeaveBtn, #addLeaveSubmitBtn').click(function () {
						if (this.id == 'addLeaveBtn') {
							leave_submit_type = "Draft";
						}
						else if (this.id == 'addLeaveSubmitBtn') {
							leave_submit_type = "In Progress";
						}

						/*var total_days = jQuery("#total_days").val();
							var leaveBalance = jQuery("#leaveBalance").val();
							if (total_days > leaveBalance)
							{
								jQuery("#leaveBalanceDialog").dialog({
									width:400,
									height:200,
									 buttons : {
										"Confirm" : function() {
										  window.location.href = targetUrl;
										},
										"Cancel" : function() {
										  jQuery(this).dialog("close");
										  return false;
										}
									  }
								});
								
							}*/

					});

					jQuery("#employee-leave-form").validate({
    
						// Specify the validation rules
						rules: {
							fromDate: "required",
							toDate: "required",
							employeeDutyResumeDate: "required",
							contactNumber : "required",
							addressDuringLeave : "required",
							leave_remarks : "required",
							toDate: { greaterThanEqual: "#fromDate" },
							employeeDutyResumeDate: { greaterThan: "#toDate" }
							
						},
						
						// Specify the validation error messages
						messages: {
							

							
						},
						
						submitHandler: function(form) {
							
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
							var gemsEmployeeCompOffFrom = jQuery("#gemsEmployeeCompOffFrom").val();
							var gemsEmployeeCompOffTo = jQuery("#gemsEmployeeCompOffTo").val();
							
						
				

							jQuery.ajax({
								beforeSend: function (request) {
									
									var leaveTypeString = jQuery('#leaveTypeString').val();
									
									if (!((typeof leaveTypeString == 'undefined') || (leaveTypeString == '')))
									{
										
										var gemsEmpCompOffFrom = jQuery("#gemsEmployeeCompOffFrom").val();
										var gemsEmployeeCompOffTo = jQuery("#gemsEmployeeCompOffTo").val();
										
										if ((gemsEmpCompOffFrom == '') || (gemsEmployeeCompOffTo == ''))
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
												jQuery('#errorDialog span').text('For Leave Type Comp Off, Comp-Off Eligibility From/To dates are mandatory');
												 jQuery("#errorDialog")
												.dialog("open");
												return false;
										}


										var selectedCompOffToDate;
										if (!(typeof gemsEmployeeCompOffTo == 'undefined'))
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
									fromDate : fromDate,
									toDate : toDate,
									employeeDutyResumeDate	: employeeDutyResumeDate,
									selected_leavesummarytype : selected_leavesummarytype,
									contactNumber : contactNumber,
									addressDuringLeave : addressDuringLeave,
									reasonForLeave : leave_remarks,
									leave_submit_type : leave_submit_type,
									total_days : total_days,
									leaveBalance : leaveBalance,
									gemsEmployeeCompOffFrom : gemsEmployeeCompOffFrom,
									gemsEmployeeCompOffTo : gemsEmployeeCompOffTo
										
								},
								type : 'POST',
								success : function(response) {
									var responseTextFlag = response.success;
									if (responseTextFlag == true) {				
										
										jQuery('#success_leave_modal').modal('toggle');
										jQuery('#success_leave_modal').modal('view');
										
									} else {
										jQuery('#error_leave_modal').modal('toggle');
										jQuery('#error_leave_modal').modal('view');																		
									}


									window.location.href = "myLeaveList.html";
									},
								failure : function(data) {
									jQuery("#loading-div-background").hide();
										jQuery('#error_leave_modal').modal('toggle');
										jQuery('#error_leave_modal').modal('view');	
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
function parseDate(str) {
    var mdy = str.split('/')
    return new Date(mdy[2], mdy[0]-1, mdy[1]);
}
function daydiff(first, second) {
    return (second-first)/(1000*60*60*24)
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

