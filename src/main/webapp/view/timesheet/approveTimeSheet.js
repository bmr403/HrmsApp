jQuery(document).ready(function() {

	
	jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L69').addClass('active');
						jQuery("ul.U69").show();
						jQuery("ul.U69").find('li#LI71').addClass('active');
			});

    jQuery('#demo-1').monthpicker();

    jQuery('#dialog').hide();

	jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});


    jQuery(".active").removeClass("active");
    jQuery("#dashboard").addClass("active");
    jQuery(document).ajaxStart(customblockUI);
    jQuery(document).ajaxStop(customunblockUI);
    var userId = sessionStorage.getItem("userId");
    var obj = '';
    // date picker
    jQuery('#datepicker').datepicker();

    // tabbed widget
    jQuery('.tabbedwidget').tabs();

    var projectsArray = {};
    var serviceURL = envConfig.serviceBaseURL + '/project/viewMyProjects.action?userId=' + userId;
    console.log(serviceURL);
    jQuery.ajax({
        url: serviceURL,
        type: 'GET',
        success: function(response) {
            var JsonStringify_Data = JSON.stringify(response);
            //var obj = jQuery.parseJSON(response);
            //	alert(JsonStringify_Data);
            jQuery.each(response, function(key, value) {
                jQuery.each(value, function(k, v) {
                    var projectMasterId = v.selectedGemsProjectMasterId;
                    var projectCode = v.selected_project;
                    projectsArray[projectMasterId] = projectCode;
                    //projectsArray.push({projectMasterId : projectName}); 
                    // global variable
                });
            });

        },
        failure: function(data) {
            alert("In failure");
        },
								statusCode : {
									403 : function(xhr) {
										window.location.href = "../../view/login/sessionoutlogin.html";

									}
								}
    });

	var weekdays = new Array(7);
        weekdays[0] = "Sun";
        weekdays[1] = "Mon";
        weekdays[2] = "Tue";
        weekdays[3] = "Wed";
        weekdays[4] = "Thu";
        weekdays[5] = "Fri";
        weekdays[6] = "Sat";

	var timesheetApprovedStatus = "";

    jQuery(window).load(function() {

        
		var empTimeMonthYear = localStorage.getItem('timeMonthYear');

		timesheetApprovedStatus = localStorage.getItem('timesheetApprovedStatus');

		var employeeMasterId = localStorage.getItem('employeeMasterId');

		var gemsEmployeeTimeSheetHeaderId = localStorage.getItem('gemsEmployeeTimeSheetHeaderId');

		document.getElementById("gemsEmployeeTimeSheetHeaderId").value = gemsEmployeeTimeSheetHeaderId;
		

        
        document.getElementById("demo-1").value = empTimeMonthYear;
		
		var selectedMonth = document.getElementById("demo-1").value;


        var selecteDateArray = selectedMonth.split('/');

        var selectedMonth = parseInt(selecteDateArray[0]) - 1;

        var selectedYear = parseInt(selecteDateArray[1]);
        

        // Since no month has fewer than 28 days
        var days = getDaysInMonth(selectedMonth, selectedYear);
        var dayString = "";
        for (i = 0; i < days.length; i++) {
            var dayOfMonth = weekdays[days[i].getDay()];
            // var date = days[i].getDate();
            var date = (days[i].getMonth() + 1) + '/' + days[i].getDate() + '/' + days[i].getFullYear();
            dayString += "<th style=\"width:10%\" align=\"center\">" + date + "<br>" + dayOfMonth + "</th>";
        }
        //dayString += "<th style=\"width:10%\" align=\"center\">Total Hours</th>";
        document.getElementById("timesheet_heading").innerHTML = dayString;


		// Column count
		var timeInfoColCount = "";

		timeInfoColCount += "<tr id=\"last_row\">";

		for (i = 0; i < days.length; i++) {
				
				var dayOfMonthString = weekdays[days[i].getDay()];
				
				var date = (days[i].getMonth() + 1) + '/' + days[i].getDate() + '/' + days[i].getFullYear();
				timeInfoColCount += "<td class=\"dialog\">";
				if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
				{
					
					timeInfoColCount += "<input type=\"text\" class=\"form-control timesheet_lastrow\"  readonly style=\"width: 50px;\" id=\"time_lastrow_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
				}
				else
				{
					timeInfoColCount += "<input type=\"text\" readonly class=\"form-control timesheet_lastrow\" readonly style=\"width: 50px;\" id=\"time_lastrow_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
				}
				timeInfoColCount +="</td>";            
				
			}
		
		timeInfoColCount += "</tr>";
		jQuery('#timeInfoColCount').append(timeInfoColCount);


		sessionStorage.setItem("selectedMonth", selectedMonth);
        sessionStorage.setItem("selectedYear", selectedYear);

		var projectHeading = "";
        projectHeading += "<th>&nbsp;</th><th>Project&nbsp;&nbsp;<font color=\"red\">*</font></th><th>Task Type&nbsp;&nbsp;<font color=\"red\">*</font></th><th style=\"width:10%\" align=\"center\">Total Hours</th>";
        document.getElementById("timesheet_heading1").innerHTML = projectHeading;

        var selectedMonthYear = document.getElementById("demo-1").value;

        getTimeRecords(selectedMonthYear, days, projectsArray,weekdays,employeeMasterId);


		

    });




    // Time sheet rows



    var rowCount = 0;

    
   


	

    /////////////////// submit button
	
		var maxLength = 500;  
    jQuery('#timesheet_remarks').keyup(function() {  
      var textlen = maxLength - jQuery(this).val().length;  
      jQuery('#rchars').text(textlen);  
    }); 

	 jQuery('#timesheet_remarks').keypress(function( e ) {
            if(e.which === 32 && this.value.length == 0) 
                    return false;
    }); 

    var rowString = "";

jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "timesheetApproval.html";
					});
	jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "timesheetApproval.html";
	});

	jQuery('#timesheet_panel').on('click', '#timesheet_submit_button', function(e) {
        e.preventDefault();
        var userId = sessionStorage.getItem("userId");
        var serviceURL = envConfig.serviceBaseURL + '/timesheet/approveTimesheet.action?userId=' + userId;
		
		var timeSheetIsApproved = jQuery('#timeSheetIsApproved').is(':checked') ? timeSheetIsApproved = "on" : timeSheetIsApproved = "off";

		if ((document.getElementById("demo-1").value == null) || (document.getElementById("demo-1").value == ""))
		{

			jQuery('#form_save_error_message1').show();
			return false;
		}
		if ((document.getElementById("timeSheetIsApproved").value == null) || (document.getElementById("timeSheetIsApproved").value == ""))
		{

			jQuery('#form_save_error_message').show();
			jQuery('#form_save_error_message1').hide();
			return false;
		}
		if ((document.getElementById("timesheet_remarks").value == null) || (document.getElementById("timesheet_remarks").value == ""))
		{

			jQuery('#form_save_error_message').show();
			jQuery('#form_save_error_message1').hide();
			return false;
		}
		
        jQuery.ajax({
            url: serviceURL,
            dataType: "json",
            data: {
                rowCount: jQuery("#rowCount").val(),
                selectedMonthYear: jQuery("#demo-1").val(),
				gemsEmployeeTimeSheetHeaderId: jQuery("#gemsEmployeeTimeSheetHeaderId").val(),
				timeSheetIsApproved: timeSheetIsApproved,
				timesheet_remarks: jQuery("#timesheet_remarks").val().replace(/ +(?= )/g,'')

            },
            type: 'POST',
            success: function(response) {
				var responseTextFlag = response.success;
                if (responseTextFlag == true) {

																jQuery('#form_save_error_message').hide();
																jQuery('#form_save_error_message1').hide();
																jQuery('#success_timesheet_modal').modal('toggle');
																jQuery('#success_timesheet_modal').modal('view');
																window.location.href = "timesheetApproval.html";
															} else {
																jQuery('#form_save_error_message').hide();
																jQuery('#form_save_error_message1').hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																window.location.href = "timesheetApproval.html";
															}
            },
            failure: function(data) {
				jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').hide();
																jQuery('#form_save_error_message1').hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
                window.location.href = "timesheetApproval.html";
            },
            statusCode: {
                403: function(xhr) {
                    window.location.href = "../../view/login/sessionoutlogin.html";

                }
            }

        });




    });


   




});




function myfun(date, typeString, rowCou) {

    jQuery("#dialog").dialog({
        autoOpen: false,
		width: 600,
        height: 430,
		open: function(event, ui) {
            jQuery("#time_date").val(date);
            var time_hours = document.getElementById("time_" + typeString).value;
            jQuery("#hours").val(time_hours);
            var time_hours_comments = document.getElementById("comment_" + typeString).value;
            jQuery("#comments").val(time_hours_comments);
        }
    });
    //jQuery("#dialog").dialog("open");
    jQuery("#dialog")
        .dialog("open");


	
}




function getTimeRecords(selectedMonthYear, days, projectsArray,weekdays,employeeMasterId) {
    ///////////////////////////////////////////////////// get time sheet records for selected month/year////////////////////////////////////////////////////


    var projectsArrayList = {};
    var userId = sessionStorage.getItem("userId");


    var timesheetArray = {};
    var serviceURL = envConfig.serviceBaseURL + '/timesheet/viewTimeSheetList.action?employeeMasterId='+employeeMasterId+'&userId=' + userId + '&timeMonthYear=' + selectedMonthYear;


    console.log(serviceURL);
    jQuery.ajax({
        url: serviceURL,
        type: 'GET',
        success: function(response) {
            var JsonStringify_Data = JSON.stringify(response);

            var selecteDateArray = selectedMonthYear.split('/');

            var selectedMonth = parseInt(selecteDateArray[0]) - 1;

            var selectedYear = parseInt(selecteDateArray[1]);

			

            var map = new Object();
            var rowCount = 0;
			var col1Count = 0;var col2Count = 0;var col3Count = 0;var col4Count = 0;var col5Count = 0;var col6Count = 0;var col7Count = 0;var col8Count = 0;var col9Count = 0;var col10Count = 0;var col11Count = 0;var col12Count = 0;var col13Count = 0;var col14Count = 0;var col15Count = 0;var col16Count = 0;var col17Count = 0;var col18Count = 0;var col19Count = 0;var col20Count = 0;var col21Count = 0;var col22Count = 0;var col23Count = 0;var col24Count = 0;var col25Count = 0;var col26Count = 0;var col27Count = 0;var col28Count = 0;var col29Count = 0;var col30Count = 0;var col31Count = 0;
            jQuery.each(response.data, function(index, value) {
                rowCount++;
				
				

                var projectMasterId = value.selectedGemsProjectMasterId;
                var projectMasterDesc = value.selected_project;


                var projectInfo = "";
                projectInfo += "<tr id=\"rowCount" + rowCount + "\">";
                projectInfo += "<td><a href=\"#\"  class=\"remove\"><span class=\"fa fa-trash-o fa-2x\">";
                projectInfo += "</td><td>";
                projectInfo += "<select name=\"project_" + rowCount + "\" id=\"project_" + rowCount + "\" class=\"form-control\" style=\"width: 170px;\" readonly>";
                projectInfo += "<option id=\"0\">Select Project</option>";

                jQuery.each(projectsArray, function(projectIndex, projectValue) {
                    if (projectIndex == projectMasterId) {
                        projectInfo += "<option id=" + projectIndex + " selected>" + projectValue + "</option>";
                    } else {
                        projectInfo += "<option id=" + projectIndex + ">" + projectValue + "</option>";
                    }

                });

                projectInfo += "</select>";
                projectInfo += "</td>";
                projectInfo += "<td><input type=\"text\" class=\"form-control\" style=\"width: 100px;\" name=\"task_" + rowCount + "\" id=\"task_" + rowCount + "\" value=\"" + value.taskDescription + "\" / readonly></td>";
                projectInfo += "<td><input type=\"text\" class=\"form-control\" readonly style=\"width: 50px;\" name=\"total_" + rowCount + "\" id=\"total_" + rowCount + "\" value=\"" + value.totalHours + "\" /></td>";
				projectInfo += "</tr>";

                jQuery('#projectInfo').append(projectInfo);


                var days = getDaysInMonth(selectedMonth, selectedYear);
                var timeInfo = "";
                timeInfo += "<tr id=\"rowCount" + rowCount + "\">";
                for (i = 0; i < days.length; i++) {

                    var date = (days[i].getMonth() + 1) + '/' + days[i].getDate() + '/' + days[i].getFullYear();

					var dayOfMonthString = weekdays[days[i].getDay()];

                    timeInfo += "<td class=\"dialog\">";


                    var dayOfMon = days[i].getDate();
                    switch ("" + dayOfMon + "") {
                        case '1':
                            var timeValue = value.timeDay1;
                            var commentValue = value.commentDay1;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
			
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							
							col1Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col1Count;

							break;

                        case '2':
                            var timeValue = value.timeDay2;
                            var commentValue = value.commentDay2;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col2Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col2Count;


                            break;

                        case '3':
                            var timeValue = value.timeDay3;
                            var commentValue = value.commentDay3;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col3Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col3Count;
                            break;

                        case '4':

                            var timeValue = value.timeDay4;
                            var commentValue = value.commentDay4;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col4Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col4Count;
                            break;

                        case '5':
                            var timeValue = value.timeDay5;
                            var commentValue = value.commentDay5;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col5Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col5Count;
                            break;

                        case '6':
                            var timeValue = value.timeDay6;
                            var commentValue = value.commentDay6;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col6Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col6Count;
                            break;

                        case '7':
                            var timeValue = value.timeDay7;
                            var commentValue = value.commentDay7;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col7Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col7Count;
                            break;

                        case '8':
                            var timeValue = value.timeDay8;
                            var commentValue = value.commentDay8;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col8Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col8Count;
                            break;

                        case '9':
                            var timeValue = value.timeDay9;
                            var commentValue = value.commentDay9;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col9Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col9Count;
                            break;

                        case '10':
                            var timeValue = value.timeDay10;
                            var commentValue = value.commentDay10;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col10Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col10Count;
                            break;

                        case '11':
                            var timeValue = value.timeDay11;
                            var commentValue = value.commentDay11;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col11Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col11Count;
                            break;

                        case '12':
                            var timeValue = value.timeDay12;
                            var commentValue = value.commentDay12;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col12Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col12Count;
                            break;

                        case '13':
                            var timeValue = value.timeDay13;
                            var commentValue = value.commentDay13;
							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col13Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col13Count;
                            break;

                        case '14':
                            var timeValue = value.timeDay14;
                            var commentValue = value.commentDay14;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col14Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col14Count;
                            break;

                        case '15':
                            var timeValue = value.timeDay15;
                            var commentValue = value.commentDay15;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col15Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col15Count;
                            break;

                        case '16':
                            var timeValue = value.timeDay16;
                            var commentValue = value.commentDay16;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col16Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col16Count;
                            break;

                        case '17':
                            var timeValue = value.timeDay17;
                            var commentValue = value.commentDay17;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col17Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col17Count;
                            break;

                        case '18':
                            var timeValue = value.timeDay18;
                            var commentValue = value.commentDay18;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col18Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col18Count;
                            break;

                        case '19':
                            var timeValue = value.timeDay19;
                            var commentValue = value.commentDay19;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col19Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col19Count;
                            break;

                        case '20':
                            var timeValue = value.timeDay20;
                            var commentValue = value.commentDay20;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col20Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col20Count;
                            break;

                        case '21':
                            var timeValue = value.timeDay21;
                            var commentValue = value.commentDay21;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col21Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col21Count;
                            break;

                        case '22':
                            var timeValue = value.timeDay22;
                            var commentValue = value.commentDay22;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col22Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col22Count;
                            break;

                        case '23':
                            var timeValue = value.timeDay23;
                            var commentValue = value.commentDay23;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col23Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col23Count;
                            break;

                        case '24':
                            var timeValue = value.timeDay24;
                            var commentValue = value.commentDay24;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col24Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col24Count;
                            break;

                        case '25':
                            var timeValue = value.timeDay25;
                            var commentValue = value.commentDay25;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col25Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col25Count;
                            break;

                        case '26':
                            var timeValue = value.timeDay26;
                            var commentValue = value.commentDay26;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col26Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col26Count;
                            break;

                        case '27':
                            var timeValue = value.timeDay27;
                            var commentValue = value.commentDay27;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col27Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col27Count;
                            break;

                        case '28':
                            var timeValue = value.timeDay28;
                            var commentValue = value.commentDay28;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col28Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col28Count;
                            break;

                        case '29':
                            var timeValue = value.timeDay29;
                            var commentValue = value.commentDay29;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col29Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col29Count;
                            break;

                        case '30':
                            var timeValue = value.timeDay30;
                            var commentValue = value.commentDay30;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col30Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col30Count;
                            break;

                        case '31':
                            var timeValue = value.timeDay31;
                            var commentValue = value.commentDay31;
                            if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
							else
							{
								timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
							}
                            timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							col31Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col31Count;
                            break;



                        default:
                            ""
                    }




                }
                timeInfo += "</td>";
              //  timeInfo += "<td><input type=\"text\" class=\"form-control\" style=\"width: 50px;\" name=\"total_" + rowCount + "\" id=\"total_" + rowCount + "\" value =\"" + value.totalHours + "\"/></td>";
                timeInfo += "</tr>";
                jQuery('#timeInfo').append(timeInfo);


                jQuery(document).on('click', '.remove', function() {
                    var trIndex = jQuery(this).closest("tr").index();
                    alert(trIndex);
                    jQuery(this).closest("tr").remove();
                    var rowCount = jQuery('#timesheet_table tr').length;
                    row = rowCount - 2;
                });
                document.getElementById("rowCount").value = rowCount;




            });

        },
        failure: function(data) {
            alert("In failure");
        }
    });




    //////////////////////////////////////////////////// end get time sheet records for selected month/year/////////////////////////////////////
}

function getDaysInMonth(selectedMonth, selectedYear) {
    var month = selectedMonth;
    var year = selectedYear;
    var date = new Date(year, month, 1);
    var days = [];
    console.log('month', month, 'date.getMonth()', date.getMonth())
    while (date.getMonth() == month) {
        //days.push((new Date(date)).toDateString());
        days.push((new Date(date)));
        date.setDate(date.getDate() + 1);
    }
    return days;

}

function customblockUI() {
    jQuery("#loading-div-background").show();
}

function customunblockUI() {
    jQuery("#loading-div-background").hide();
}