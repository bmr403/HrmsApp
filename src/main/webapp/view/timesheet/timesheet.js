jQuery(document).ready(
    function() {
       jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L69').addClass('active');
						jQuery("ul.U69").show();
						jQuery("ul.U69").find('li#LI70').addClass('active');
			});
        jQuery(document).ajaxStart(customblockUI);
        jQuery(document).ajaxStop(customunblockUI);

		jQuery('#selectedTimeMonthYear').monthpicker();

        // date picker
        jQuery('#datepicker').datepicker();
		
		jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
        // tabbed widget
        jQuery('.tabbedwidget').tabs();
        var sortType = "desc";
        var userId = sessionStorage.getItem("userId");
        var serviceURL = envConfig.serviceBaseURL + '/timesheet/viewTimeSheetHeaderList.action?userId=' + userId;

		

		function timesheet_jQueryDataTableAjax(
							selectedTimeMonthYear) {

        console.log(serviceURL);
        var timeSheetList_table = jQuery("#timeSheetList_table")
            .DataTable({
                "sAjaxSource": serviceURL,
                "bProcessing": false,
                "bServerSide": true,
                "bPaginate": true,
                "bFilter": true,
                "searching": false,
                "bSort": false,
                "bDestroy": true,
                "bJQueryUI": false,
                 "fnServerParams" : function(aoData) {
                	aoData.push({
                		"name" : "selectedTimeMonthYear",
                		"value" : selectedTimeMonthYear
                	});
                } ,
                "sPaginationType": 'simple_numbers',
                "iDisplayStart": 0,
                "iDisplayLength": 10,
                columnDefs: [{
                    orderable: false,
                    targets: -1
                }],
                "fnDrawCallback": function(
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
                "aoColumns": [{
                    "mData": "timeSheetMonthYear"
                }, {
                    "mData": "totalHours"
                },{
                    "mData": "timesheetApprovedStatus"
                }, {
                    "mData": "gemsEmployeeTimeSheetHeaderId",
					
                    "bSortable": false,
                    "mRender": function(
                        gemsEmployeeTimeSheetHeaderId,timesheetApprovedStatus) {
						
						if (timesheetApprovedStatus == "SUBMITTED")
						{
							return '<a href = \"#\" onClick = "exportTimeSheet(' +
                            gemsEmployeeTimeSheetHeaderId +
                            ');"  id=\"download_btn\"><span class=\"glyphicon glyphicon-download-alt btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a>';
						}
						else
						{
							return '<a href = "#" onClick = "editTimeSheet('
																	+ gemsEmployeeTimeSheetHeaderId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteTimeSheet('
																	+ gemsEmployeeTimeSheetHeaderId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></span></a>';

							
						}

                        
                    }
                }],

            });
		}
			selectedTimeMonthYear = "";
			timesheet_jQueryDataTableAjax(selectedTimeMonthYear); // default load
			function timesheetTable_Search() {
						var userId = sessionStorage.getItem("userId");
						selectedTimeMonthYear = jQuery('#selectedTimeMonthYear').val();
						timesheet_jQueryDataTableAjax(selectedTimeMonthYear);

			}
			jQuery("#search_project_btn").click(function(e) {

						e.preventDefault();
						timesheetTable_Search();

			});

			jQuery("#resetsearch_timesheet_btn").click(function(e) {
						
						jQuery('#selectedTimeMonthYear').val("");
						timesheetTable_Search();

					});		
			jQuery("#delete_timesheet_btn")
							.click(
									function(e) {
						e.preventDefault();
				var userId = sessionStorage.getItem("userId");
				var gemsEmployeeTimeSheetHeaderId = localStorage.getItem('gemsEmployeeTimeSheetHeaderId');
				
				
				var serviceURL = envConfig.serviceBaseURL +
					'/timesheet/deleteTimeSheet.action?userId=' + userId;

				jQuery('#delete_timesheet_modal').modal(
												'toggle');
				jQuery('#delete_timesheet_modal').modal(
												'hide');
				jQuery.ajax({
					url: serviceURL,
					dataType: "json",
					data : {
						gemsEmployeeTimeSheetHeaderId : gemsEmployeeTimeSheetHeaderId
					},
					type: 'GET',
					success: function(response) {
						if (response.success == true) {
							localStorage.removeItem(gemsEmployeeTimeSheetHeaderId);
							window.location.href = "timesheet.html";
						} else {
							localStorage.removeItem(gemsRoleMasterId);
							window.location.href = "../../";
						}
					},
					failure: function(data) {
						window.location.href = "../../";

					},
					statusCode: {
						403: function(xhr) {
							window.location.href = "../../view/login/sessionoutlogin.html";

						}
					}

				});
	
			});


    });

function editTimeSheet(gemsEmployeeTimeSheetHeaderId) {

    

	var userId = sessionStorage.getItem("userId");
    //alert("timeSheet Header Id is :"+timeSheetWeekDetailId);
    var serviceURL = envConfig.serviceBaseURL + '/timesheet/viewTimeSheetList.action?userId=' + userId + '&gemsEmployeeTimeSheetHeaderId=' + gemsEmployeeTimeSheetHeaderId;

    jQuery.ajax({
        url: serviceURL,
        dataType: "json",
        type: 'GET',
        success: function(response) {
            var JsonStringify_Data = JSON.stringify(response.data);

            //var editProject_Data = response.data;
            //localStorage.setItem('editProject_Data', JsonStringify_Data);
			var selectedMonthYear = "";
			var timesheetApprovedStatus = "";
			if (typeof response.data[0] === "undefined")
			{
				selectedMonthYear = "";
				timesheetApprovedStatus = "DRAFT";
				activeStatus = "1";
			}
			else
			{
				selectedMonthYear = response.data[0].timeMonthYear;
				activeStatus = response.data[0].activeStatus;
				timesheetApprovedStatus = response.data[0].timesheetApprovedStatus;
			}
            localStorage.setItem('timesheetApprovedStatus', timesheetApprovedStatus);
			localStorage.setItem('activeStatus', activeStatus);
            localStorage.setItem('timeMonthYear', selectedMonthYear);
            window.location.href = "editTimeSheet.html";

        },
        failure: function(data) {
            window.location.href = "../../";

        },
        statusCode: {
            403: function(xhr) {
                window.location.href = "../../view/login/sessionoutlogin.html";

            }
        }

    });

}



function deleteTimeSheet(gemsEmployeeTimeSheetHeaderId) {

	localStorage.setItem('gemsEmployeeTimeSheetHeaderId', gemsEmployeeTimeSheetHeaderId);
	jQuery('#delete_timesheet_modal').modal('toggle');
	jQuery('#delete_timesheet_modal').modal('view');
	

}	

function exportTimeSheet(gemsEmployeeTimeSheetHeaderId)
{
	     var userId = sessionStorage.getItem("userId");
		var serviceURL = envConfig.serviceBaseURL +'/timesheet/exportTimeSheetTOExcel.action?userId=' + userId + '&gemsEmployeeTimeSheetHeaderId=' +gemsEmployeeTimeSheetHeaderId;

		window.open(serviceURL, '_blank');
}


jQuery("#timesheetExpMonthYear").datepicker({
    showOtherMonths: false,
    changeMonth: true,
    changeYear: true,
    selectOtherMonths: true
});

function customblockUI() {
    jQuery("#loading-div-background").show();
}

function customunblockUI() {
    jQuery("#loading-div-background").hide();
}