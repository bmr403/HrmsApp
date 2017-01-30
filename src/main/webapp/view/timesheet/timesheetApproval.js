jQuery(document).ready(
    function() {
         jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L69').addClass('active');
						jQuery("ul.U69").show();
						jQuery("ul.U69").find('li#LI71').addClass('active');
			});
        jQuery(document).ajaxStart(customblockUI);
        jQuery(document).ajaxStop(customunblockUI);

		jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

		jQuery('#selectedTimeMonthYear').monthpicker();
        // date picker
        jQuery('#datepicker').datepicker();

        // tabbed widget
        jQuery('.tabbedwidget').tabs();
        var sortType = "desc";
        var userId = sessionStorage.getItem("userId");
        var serviceURL = envConfig.serviceBaseURL + '/timesheet/viewTimeSheetApprovalList.action?userId=' + userId;

        console.log(serviceURL);
		function timesheet_jQueryDataTableAjax(selectedTimeMonthYear) {

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
                },{
                    "mData": "employeeName"
                }, {
                    "mData": "totalHours"
                },{
                    "mData": "timesheetApprovedStatus"
                }, {
                    "mData": "gemsEmployeeTimeSheetHeaderId",
                    "bSortable": false,
                    "mRender": function(
                        gemsEmployeeTimeSheetHeaderId) {
						return '<a href = "#" onClick = "editTimeSheet(' +
                        gemsEmployeeTimeSheetHeaderId +
                            ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "exportTimeSheet(' +
                            gemsEmployeeTimeSheetHeaderId +
                            ');"  id=\"download_btn\"><span class=\"glyphicon glyphicon-download-alt \" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Export to Excel\"></span></a>';
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
            var selectedMonthYear = response.data[0].timeMonthYear;
			var employeeMasterId = response.data[0].employeeMasterId;
			var gemsEmployeeTimeSheetHeaderId = response.data[0].gemsEmployeeTimeSheetHeaderId;
            localStorage.setItem('timeMonthYear', selectedMonthYear);
			localStorage.setItem('employeeMasterId', employeeMasterId);
			localStorage.setItem('gemsEmployeeTimeSheetHeaderId', gemsEmployeeTimeSheetHeaderId);
            window.location.href = "approveTimeSheet.html";

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

    var userId = sessionStorage.getItem("userId");

    var serviceURL = envConfig.serviceBaseURL +
        '/timesheet/deleteTimeSheet.action?userId=' + userId + '&gemsEmployeeTimeSheetHeaderId=' +
        gemsEmployeeTimeSheetHeaderId;
    jQuery.ajax({
        url: serviceURL,
        dataType: "json",
        type: 'GET',
        success: function(response) {
            if (response.success == true) {
                window.location.href = "timesheet.html";
            } else {
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