
	  var gemsMapowerRequestId = JSON.parse(localStorage.getItem('gemsMapowerRequestId'));
	  var userId = sessionStorage.getItem("userId");

	jQuery('#cancelInterviewerBtn').click(function(event) {
						window.location.href = 'manpowerRequest.html';
					});
	
	jQuery("#interviewDate").datepicker({
						changeMonth: true,
						changeYear: true,
						yearRange: "-50:+5"
					});
	jQuery('#interviewTime').blur(function(){
        var validTime = jQuery(this).val().match(/^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/);
        if (!validTime) {
           // $(this).val('').focus().css('background', '#fdd');
        } else {
            //$(this).css('background', 'transparent');
        }
    });

	interviewerList = jQuery('#dropdown_interviewer');
					var serviceURL = envConfig.serviceBaseURL
							+ '/employee/viewEmployeeList.action?userId='
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
									gemsEmployeeMasterId : val.gemsEmployeeMasterId,
									employeeCodeName : val.employeeCodeName
								};
							});

							// iterate over the data and append a select option
							interviewerList.append('<option id="0">Select Interviewer</option>');
							jQuery.each(result, function(key, val) {
								
								interviewerList.append('<option id="'
											+ val.gemsEmployeeMasterId + '">'
											+ val.employeeCodeName + '</option>');
								
								
								
								
							})
							jQuery("#dropdown_interviewer").focus();
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

	jQuery("#addInterviewerBtn").click(function(e){
		e.preventDefault();	
		var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/recruitment/saveManpowerRequestInterview.action?userId='+userId;
	
	var gemsMapowerRequestInterviewId = jQuery("#gemsMapowerRequestInterviewId").val();
	var dropdown_interviewer = jQuery(jQuery("#dropdown_interviewer")).find('option:selected').attr('id');
	var interviewDate = jQuery("#interviewDate").val();
	var interviewTime = jQuery("#interviewTime").val();
	var levelNo = jQuery("#levelNo").val();
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsMapowerRequestInterviewId : gemsMapowerRequestInterviewId,
			gemsMapowerRequestId : gemsMapowerRequestId,
			dropdown_interviewer	: dropdown_interviewer,
			interviewDate : interviewDate,
			interviewTime		: interviewTime,
			levelNo : levelNo

		},
		type : 'POST',
		success : function(response) {
			var gemsMapowerRequestId = localStorage.getItem('gemsMapowerRequestId', gemsMapowerRequestId);
			var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewManpowerRequestInterviewerList.action?userId='
							+ userId+'&gemsMapowerRequestId='+gemsMapowerRequestId;
					
					
			interviewer_jQueryDataTableAjax(serviceURL);
			jQuery('#interviewer-form')[0].reset();
					
					// setting id
			document.getElementById("gemsMapowerRequestId").value = gemsMapowerRequestId;
			
			



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
	});

	var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewManpowerRequestInterviewerList.action?userId='
							+ userId+'&gemsMapowerRequestId='+gemsMapowerRequestId;
					
					
					interviewer_jQueryDataTableAjax(serviceURL);
					
					document.getElementById("gemsMapowerRequestId").value = gemsMapowerRequestId;
	 


function interviewer_jQueryDataTableAjax(serviceURL) {
						
						
						
						var interviewerList_table = jQuery("#interviewerList_table")
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
													"name" : "searchRoleCode",
													"value" : searchCode
												}, {
													"name" : "searchRoleName",
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
														"mData" : "interviewerName"
													},
													{
														"mData" : "interviewDate"
													},
													{
														"mData" : "levelNo"
													},
													{
														"mData" : "gemsMapowerRequestInterviewId",
														"bSortable" : false,
														"mRender" : function(
																gemsMapowerRequestInterviewId) {
															return '<a href = "#" onClick = "editContact('
																	+ gemsMapowerRequestInterviewId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteContact('
																	+ gemsMapowerRequestInterviewId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a> &nbsp;&nbsp;&nbsp;&nbsp;';
														}
													} ],

										});
					}
