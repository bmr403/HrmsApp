jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L24').addClass('active');
						jQuery("ul.U24").show();
						jQuery("ul.U24").find('li#LI29').addClass('active');
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

			var data = JSON.parse(localStorage.getItem('editLeave_Data'));
			var employeeLeaveId = '';
			if (data !== null) {
				jQuery("#gemsEmployeeLeaveId").val(data.selectedGemsEmployeeLeaveId);
				jQuery("#gemsEmployeeLeaveLineId").val(data.gemsEmployeeLeaveLineId);
				jQuery("#gemsEmployeeMasterId").val(data.selectedGemsEmployeeMasterId);
				jQuery("#gemsEmployeeCompOffFrom").val(data.gemsEmployeeCompOffFrom);
				jQuery("#gemsEmployeeCompOffTo").val(data.gemsEmployeeCompOffTo);
				jQuery("#currentLeavel").val(data.currentLeavel);
				jQuery("#fromDate").val(data.fromDate);
				jQuery("#toDate").val(data.toDate);
				jQuery("#employeeDutyResumeDate").val(data.employeeDutyResumeDate);
				jQuery("#employeeDutyResumeDate").val(data.employeeDutyResumeDate);
				jQuery("#contactNumber").val(data.contactNumber);
				jQuery("#addressDuringLeave").val(data.addressDuringLeave);
				jQuery("#leave_remarks").val(data.reasonForLeave);
				employeeLeaveId = data.selectedGemsEmployeeLeaveId;
				var total_days = daydiff(parseDate(jQuery('#fromDate').val()), parseDate(jQuery('#toDate').val()));
				if (jQuery('#fromDate').val() == jQuery('#toDate').val() )
					 {
						jQuery("#total_days").val('1');
					 }
					 else
					 {
						jQuery("#total_days").val(total_days + 1);
					 }
				var total_eligible_days = daydiff(parseDate(jQuery('#gemsEmployeeCompOffFrom').val()), parseDate(jQuery('#gemsEmployeeCompOffTo').val()));
				
				if (jQuery('#gemsEmployeeCompOffFrom').val() != '')
				{
					if (jQuery('#gemsEmployeeCompOffFrom').val() == jQuery('#gemsEmployeeCompOffTo').val() )
					 {
						jQuery("#compofftotal_days").val('1');
					 }
					 else
					 {
						jQuery("#compofftotal_days").val(total_eligible_days + 1);
					 }
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

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
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

			function empLeaveHistory_jQueryDataTableAjax(serviceURL) {

					console.log(serviceURL);
					var empLeaveHistory_table = jQuery("#empLeaveHistory_table")
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
														"mData" : "approvedStatus"
													},
													{
														"mData" : "statusDate"
													},
													{
														"mData" : "lineComments"
													},
													{
														"mData" : "selectedApproverName"
													}
													],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeLeaveHistoryList.action?userId='
							+ userId+'&selectedGemsEmployeeLeaveId='+employeeLeaveId;
					searchCode = "";
					searchDescription = "";
					
					empLeaveHistory_jQueryDataTableAjax(serviceURL);

			jQuery.validator.addMethod("greaterThan", 
						function(value, element, params) {

							if (!/Invalid|NaN/.test(new Date(value))) {
								return new Date(value) > new Date(jQuery(params).val());
							}

							return isNaN(value) && isNaN(jQuery(params).val()) 
								|| (Number(value) > Number(jQuery(params).val())); 
						},'Must be greater than {0}.');
					

					var approvedStatus = "";
					jQuery('#leaveApproveBtn, #leaveRejectBtn').click(function () {
						if (this.id == 'leaveApproveBtn') {
							approvedStatus = "Approved";
						}
						else if (this.id == 'leaveRejectBtn') {
							approvedStatus = "Rejected";
						}
					});

					jQuery("#employee-leaveapproval-form").validate({
    
						// Specify the validation rules
						rules: {
							approver_remarks : "required"
							
							

							
						},
						
						// Specify the validation error messages
						messages: {
							

							
						},
						
						submitHandler: function(form) {
							
							var selectedGemsEmployeeLeaveId = jQuery("#gemsEmployeeLeaveId").val();
							var gemsEmployeeLeaveLineId = jQuery("#gemsEmployeeLeaveLineId").val();
							var gemsEmployeeMasterId = jQuery("#gemsEmployeeMasterId").val();
							var currentLeavel = jQuery("#currentLeavel").val();							
							var approver_remarks = jQuery("#approver_remarks").val();
							
							var serviceURL3 = envConfig.serviceBaseURL+ '/employee/approveGemsEmployeeLeave.action?userId='+userId;
							jQuery.ajax({
								url : serviceURL3,
								dataType : "json",
								data : {
									selectedGemsEmployeeLeaveId : selectedGemsEmployeeLeaveId,
									gemsEmployeeLeaveLineId : gemsEmployeeLeaveLineId,
									currentLeavel : currentLeavel,
									approver_remarks : approver_remarks,
									approvedStatus : approvedStatus
								},
								type : 'POST',
								success : function(response) {
									window.location.href = "leaveApprovalList.html";
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
