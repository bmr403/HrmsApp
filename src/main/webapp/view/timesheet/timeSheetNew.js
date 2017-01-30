jQuery(document).ready(function() {

	  jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L69').addClass('active');
						jQuery("ul.U69").show();
						jQuery("ul.U69").find('li#LI70').addClass('active');
			});
	
    jQuery('#demo-1').monthpicker();

    jQuery('#dialog').hide();
	jQuery('#errorDialogBox').hide();
	jQuery('#errorDialog').hide();
	
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
					var projectResourceInActiveFrom = v.inActiveFrom;
					var projectResourceIsActive = v.activeStatus;
					var projectStartDate = v.resourceStartDate;
					var projectEndDate = v.resourceEndDate;
					if (projectResourceIsActive == false)
					{
						if (!(projectResourceInActiveFrom == ''))
						{
							projectsArray[projectMasterId] = projectCode+'_'+projectStartDate+'_'+projectEndDate+'_'+projectResourceInActiveFrom;
						}
					}
					else
					{
						projectsArray[projectMasterId] = projectCode+'_'+projectStartDate+'_'+projectEndDate+'_'+projectResourceInActiveFrom;
					}
                    //projectsArray.push({projectMasterId : projectName}); 
                    // global variable
                });
            });

        },
        failure: function(data) {
            alert("In failure");
        }
		,
            statusCode: {
                403: function(xhr) {
                   window.location.href = "../../view/login/sessionoutlogin.html";

                }
            }
    });

    sessionStorage.setItem("projectsArray", projectsArray);



    // Time sheet rows



    var rowCount = 0;

 var weekdays = new Array(7);
        weekdays[0] = "Sun";
        weekdays[1] = "Mon";
        weekdays[2] = "Tue";
        weekdays[3] = "Wed";
        weekdays[4] = "Thu";
        weekdays[5] = "Fri";
        weekdays[6] = "Sat";

    jQuery('#addProject_button').on('click', function() {

		var selectedMonthYear = document.getElementById("demo-1").value;
		
		if ((typeof selectedMonthYear == 'undefined') || (selectedMonthYear == ''))
		{
			jQuery("#errorDialogBox").dialog({
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
			
			jQuery("#errorDialogBox").dialog("open");
			return false;
		}


		jQuery("#form_save_error_message").hide();
		jQuery("#form_save_error_message1").hide();
		jQuery("#submitted_message").hide();
		jQuery("#approved_message").hide();

        if (document.getElementById("rowCount").value == '') {

            rowCount = 0;
        } else {

            rowCount = Number(document.getElementById("rowCount").value);
        }

        rowCount++;

        var projectInfo = "";
        projectInfo += "<tr id=\"rowCount" + rowCount + "\">";
        projectInfo += "<td><a href=\"#\"  class=\"remove\"><span class=\"fa fa-trash-o fa-2x\">";
        projectInfo += "</td><td>";
        projectInfo += "<select name=\"project_" + rowCount + "\" id=\"project_" + rowCount + "\" class=\"form-control\" style=\"width: 170px;\" >";
        projectInfo += "<option id=\"0\">Select Project</option>";

        jQuery.each(projectsArray, function(projectIndex, projectValue) {

            var projectValueArray = projectValue.split("_");
			var projectNameValue = "";
			var projectStartDate = "";
			var projectEndDate = "";
			//if (projectValueArray.length > 1)
			//{
				projectNameValue = projectValueArray[0];
			//}

            projectInfo += "<option id=" + projectIndex + ">" + projectNameValue + "</option>";
        });

        projectInfo += "</select>";
        projectInfo += "</td>";
        projectInfo += "<td><input type=\"text\" class=\"form-control\" style=\"width: 100px;\" name=\"task_" + rowCount + "\" id=\"task_" + rowCount + "\" /></td>";
		projectInfo += "<td><input type=\"text\" readonly class=\"form-control\" style=\"width: 50px;\" name=\"total_" + rowCount + "\" id=\"total_" + rowCount + "\"/></td>";
        projectInfo += "</tr>";

        jQuery('#projectInfo').append(projectInfo);
	
		jQuery("#project_" + rowCount + "").change(function () {

			var selectedProjectValue = this.value;
			
			var selectedYear = sessionStorage.getItem("selectedYear");
			var selectedMonth = sessionStorage.getItem("selectedMonth");
			var days = getDaysInMonth(selectedMonth, selectedYear);


			jQuery.each(projectsArray, function(projectIndex, projectValue) {
				var projectValueArray = projectValue.split("_");
				var projectNameValue = "";
				
				projectNameValue = projectValueArray[0];
				if (projectNameValue == selectedProjectValue)
				{
						var inActvieDateStr = '';
						var projectStartDateString = '';
						var projectEndDateString = '';
						projectStartDateString = projectValueArray[1];
						projectEndDateString = projectValueArray[2];
						
						
						var projectStartDateStringSplit = projectStartDateString.split("/");
						var projectStartDate = new Date(projectStartDateStringSplit[2], (projectStartDateStringSplit[0] - 1), projectStartDateStringSplit[1]);

						var projectEndDateStringSplit = projectEndDateString.split("/");
						var projectEndDate = new Date(projectEndDateStringSplit[2], (projectEndDateStringSplit[0] - 1), projectEndDateStringSplit[1]);
						
						

						inActvieDateStr = projectValueArray[3];	
						var inActiveDate;
						
						if (!(typeof inActvieDateStr == 'undefined') || !(inActvieDateStr == ''))
						{
							var dateParts = inActvieDateStr.split("/");
							inActiveDate = new Date(dateParts[2], (dateParts[0] - 1), dateParts[1]);
						}
						
						
						var timeInfo = "";
						timeInfo += "<tr id=\"rowCount" + rowCount + "\">";
						for (i = 0; i < days.length; i++) {
							
							var dayOfMonthString = weekdays[days[i].getDay()];
							
							var date = (days[i].getMonth() + 1) + '/' + days[i].getDate() + '/' + days[i].getFullYear();
							
							var selectedDatePart = date.split("/");
							var selectedDate = new Date(selectedDatePart[2], (selectedDatePart[0] - 1), selectedDatePart[1]);
							timeInfo += "<td class=\"dialog\">";

							if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
							{
								if (selectedDate >= inActiveDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px; background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								else
								{
									
									if ((selectedDate >= projectStartDate) || (selectedDate <= projectEndDate))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px; background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";	
									}
									else
									{
										timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px; background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									
								}
								
							}
							else
							{
								if (selectedDate >= inActiveDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								else
								{
									
									if ((selectedDate >= projectStartDate) || (selectedDate <= projectEndDate))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									else
									{
										timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									
								}
								
							}
							timeInfo +="</td>";            
							timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
						}
					  //  timeInfo += "<td><input type=\"text\" class=\"form-control\" style=\"width: 50px;\" name=\"total_" + rowCount + "\" id=\"total_" + rowCount + "\"/></td>";
						timeInfo += "</tr>";
						jQuery('#timeInfo').append(timeInfo);



					
				}

			});
			

		});
		

        jQuery(document).on('click', '.remove', function() {
            var trIndex = jQuery(this).closest("tr").index();
            alert(trIndex);
            jQuery(this).closest("tr").remove();
            var rowCount = jQuery('#timesheet_table tr').length;
            row = rowCount - 2;
        });
        document.getElementById("rowCount").value = rowCount;


    });

    //////////////////// on Change 

	



    jQuery("#demo-1").change(function() {
        jQuery('#projectInfo').children('tr').remove();
        jQuery('#timeInfo').children('tr').remove();
		 jQuery('#timeInfoColCount').children('tr').remove();
        document.getElementById("rowCount").value = "0";

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
       // dayString += "<th style=\"width:10%\" align=\"center\">Total Hours</th>";
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
					timeInfoColCount += "<input type=\"text\" readonly class=\"form-control timesheet_lastrow\" style=\"width: 50px;\" id=\"time_lastrow_" + date + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
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
		
		getTimeRecords(selectedMonthYear, days, projectsArray,weekdays);

		


    });




    /////////////////// button Actions

	var rowString = "";


	jQuery('#timesheet_panel').on('click', '#timesheet_save_button', function(e) {
        e.preventDefault();
        var userId = sessionStorage.getItem("userId");
        var serviceURL = envConfig.serviceBaseURL + '/timesheet/saveTimesheet.action?timeSheetStatus=DRAFT&userId=' + userId;
		var answers = [];
        var colCount = 0;
        jQuery.each(jQuery('.form-control'), function() {
            if ((this.id.startsWith("project_")) || (this.id.startsWith("task_")) || (this.id.startsWith("time_")) || (this.id.startsWith("comment_")) || (this.id.startsWith("total_"))) {
                if (colCount != 0) {
                    rowString += "|";
                }

                rowString += "" + this.id + "~" + jQuery(this).val() + "";
                colCount++;
            }
            //answers.push(rowString);
        });
		if ((document.getElementById("demo-1").value == null) || (document.getElementById("demo-1").value == ""))
		{

			jQuery('#form_save_error_message1').show();
			return false;
		}
		if ((document.getElementById("project_1") != null) && (document.getElementById("task_1") != null))
		{
			if ((document.getElementById("project_1").value != null) && (document.getElementById("task_1").value != null))
			{
				jQuery('#form_save_error_message1').hide();
				jQuery('#form_save_error_message').hide();
			}
			else
			{
				jQuery('#form_save_error_message1').hide();
				jQuery('#form_save_error_message').show();
				return false;
			}
		}
		else
		{
			jQuery('#form_save_error_message1').hide();
			jQuery('#form_save_error_message').hide();
			return false;
		}
		
		
        jQuery.ajax({
            url: serviceURL,
            dataType: "json",
            data: {
                rowCount: jQuery("#rowCount").val(),
                selectedMonthYear: jQuery("#demo-1").val(),
				gemsEmployeeTimeSheetHeaderId:jQuery("#gemsEmployeeTimeSheetHeaderId").val(),
                dataString: rowString

            },
            type: 'POST',
            success: function(response) {
				
				var responseTextFlag = response.success;

															if (responseTextFlag == true) {
																jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').show();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#success_timesheet_modal').modal('toggle');
																jQuery('#success_timesheet_modal').modal('view');
																window.location.href = "timesheet.html";
															} else {
																
																jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').show();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																window.location.href = "timesheetNew.html";
															}
                
            },
            failure: function(data) {
				
				jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').show();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
                window.location.href = "timesheetNew.html";
            },
            statusCode: {
                403: function(xhr) {
                    alert("Session will be Expired");
                    window.location.href = "../../view/login/sessionoutlogin.html";
                }
            }

        });




    });

	jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "timesheet.html";
					});
	jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "timesheetNew.html";
					});

    jQuery('#timesheet_panel').on('click', '#timesheet_submit_button', function(e) {
        e.preventDefault();
        var userId = sessionStorage.getItem("userId");
        var serviceURL = envConfig.serviceBaseURL + '/timesheet/saveTimesheet.action?timeSheetStatus=SUBMITTED&userId=' + userId;
		var answers = [];
        var colCount = 0;
        jQuery.each(jQuery('.form-control'), function() {
            if ((this.id.startsWith("project_")) || (this.id.startsWith("task_")) || (this.id.startsWith("time_")) || (this.id.startsWith("comment_")) || (this.id.startsWith("total_"))) {
                if (colCount != 0) {
                    rowString += "|";
                }

                rowString += "" + this.id + "~" + jQuery(this).val() + "";
                colCount++;
            }
            //answers.push(rowString);
        });

		if ((document.getElementById("demo-1").value == null) || (document.getElementById("demo-1").value == ""))
		{

			jQuery('#form_save_error_message1').show();
			return false;
		}
		if ((document.getElementById("project_1") != null) && (document.getElementById("task_1") != null))
		{
			if ((document.getElementById("project_1").value != null) && (document.getElementById("task_1").value != null))
			{
				jQuery('#form_save_error_message1').hide();
				jQuery('#form_save_error_message').hide();
			}
			else
			{
				jQuery('#form_save_error_message1').hide();
				jQuery('#form_save_error_message').show();
				return false;
			}
		}
		else
		{
			jQuery('#form_save_error_message1').hide();
			jQuery('#form_save_error_message').hide();
			return false;
		}
		
		

        jQuery.ajax({
            url: serviceURL,
            dataType: "json",
            data: {
                rowCount: jQuery("#rowCount").val(),
                selectedMonthYear: jQuery("#demo-1").val(),
				gemsEmployeeTimeSheetHeaderId:jQuery("#gemsEmployeeTimeSheetHeaderId").val(),
                dataString: rowString

            },
            type: 'POST',
            success: function(response) {
				var responseTextFlag = response.success;
                if (responseTextFlag == true) {

																jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').hide();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#success_timesheet_modal').modal('toggle');
																jQuery('#success_timesheet_modal').modal('view');
																window.location.href = "timesheet.html";
															} else {
																jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').hide();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																window.location.href = "timesheetNew.html";
															}
				
            },
            failure: function(data) {
                jQuery('#form_save_error_message1').hide();
																jQuery('#form_save_error_message').hide();
																jQuery('#form_save_error_message1').hide();
																jQuery("#submitted_message").hide();
																jQuery("#approved_message").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
				window.location.href = "timesheetNew.html";
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

	var selectedDatePart = date.split("/");
	var selectedDate = new Date(selectedDatePart[2], (selectedDatePart[0] - 1), selectedDatePart[1]);
	var today = new Date();

	if (selectedDate > today)
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
	}
	else
	{
		jQuery("#dialog").dialog({
        autoOpen: false,
		width: 600,
        height: 430,
        buttons: {
            Save: function() {
				
                var hours = document.getElementById("hours").value;
                var comments = document.getElementById("comments").value.replace(/ +(?= )/g,'');
				
				if (jQuery('#hours').val() == '') {
						jQuery('#hours').css({
												"border" : "1px solid red",
				});
				
				if (jQuery('#comments').val() == '') {
						jQuery('#comments').css({
												"border" : "1px solid red",
				});
				return false;
				}
				else
				{
					jQuery('#comments').css({
												"border" : "",
											});
				}

				return false;
				}
				else
				{
					jQuery('#hours').css({
												"border" : "",
											});
				}

				if (jQuery('#comments').val() == '') {
						jQuery('#comments').css({
												"border" : "1px solid red",
				});
				return false;
				}
				else
				{
					jQuery('#comments').css({
												"border" : "",
											});
				}
				
				if ((jQuery('#hours').val() >=24 ) ||(jQuery('#hours').val() <=0 )) {
						jQuery('#hours').css({
												"border" : "1px solid red",
				});
				return false;
				}

                document.getElementById("time_" + typeString).value = hours;
                document.getElementById("comment_" + typeString).value = comments;
                var sum = 0;

                jQuery('tr:nth-child(' + (rowCou) + ')').find(".timesheet_day").each(function() {
                    if (!isNaN(this.value) && this.value.length != 0) {
                        sum += parseFloat(this.value);
                        //alert(sumR[i]);
                    }
                });
				
				var colSum = 0;
				var totalRow = document.getElementById("rowCount").value;
				for (var k=1;k<=totalRow ;k++ )
				{
					if (k != rowCou)
					{
						if (!isNaN(document.getElementById("time_"+k+"_" + date).value) && document.getElementById("time_"+k+"_" + date).value.length != 0) 
						{
							colSum += parseFloat(document.getElementById("time_"+k+"_" + date).value);	

						}						
							
					}
					else
					{
						colSum += parseFloat(document.getElementById("hours").value);
					}
					
				}

				document.getElementById("time_lastrow_" + date).value = colSum;

                document.getElementById("total_" + rowCou).value = sum;
                document.getElementById("hours").value = "";
                document.getElementById("time_date").value = "";
                document.getElementById("comments").value = "";

                jQuery(this).dialog("close");
            }
        },
        open: function(event, ui) {
            jQuery("#time_date").val(date);
            var time_hours = document.getElementById("time_" + typeString).value;
            jQuery("#hours").val(time_hours);
            var time_hours_comments = document.getElementById("comment_" + typeString).value;
            jQuery("#comments").val(time_hours_comments);
        }
		});
		 jQuery("#dialog")
        .dialog("open");
	}



    //jQuery("#dialog").dialog("open");
   

	var maxLength = 500;  
    jQuery('#comments').keyup(function() {  
      var textlen = maxLength - jQuery(this).val().length;  
      jQuery('#rchars').text(textlen);  
    }); 

	 jQuery('#comments').keypress(function( e ) {
            if(e.which === 32 && this.value.length == 0) 
                    return false;
    }); 
	
}




function getTimeRecords(selectedMonthYear, days, projectsArray,weekdays) {
    ///////////////////////////////////////////////////// get time sheet records for selected month/year////////////////////////////////////////////////////

	jQuery('#form_save_error_message1').hide();
	jQuery('#form_save_error_message').hide();

	jQuery('#submitted_message').hide();
	jQuery('#approved_message').hide();

    var projectsArrayList = {};
    var userId = sessionStorage.getItem("userId");


    var timesheetArray = {};
    var serviceURL = envConfig.serviceBaseURL + '/timesheet/viewTimeSheetList.action?userId=' + userId + '&timeMonthYear=' + selectedMonthYear;


    console.log(serviceURL);
    jQuery.ajax({
        url: serviceURL,
        type: 'GET',
        success: function(response) {

			if (response.data[0] != null)
			{
			

            var JsonStringify_Data = JSON.stringify(response);

            var selecteDateArray = selectedMonthYear.split('/');

            var selectedMonth = parseInt(selecteDateArray[0]) - 1;

            var selectedYear = parseInt(selecteDateArray[1]);
			
			

			document.getElementById("gemsEmployeeTimeSheetHeaderId").value = response.data[0].gemsEmployeeTimeSheetHeaderId;
	
			
			if (typeof response.data[0] === "undefined")
			{
				jQuery('#submitted_message').hide();
					jQuery('#approved_message').hide();
					jQuery("#timesheet_save_button").show();
					jQuery("#timesheet_submit_button").show();
					jQuery("#addProject_button").show();
					jQuery('#submitted_message').hide();
					jQuery('#approved_message').hide();
			}
			else
			{
				var timesheetApprovedStatus = response.data[0].timesheetApprovedStatus;

				if ((timesheetApprovedStatus == "SUBMITTED") || (timesheetApprovedStatus == "APPROVED"))
				{
					
					jQuery("#timesheet_save_button").hide();
					jQuery("#timesheet_submit_button").hide();
					jQuery("#addProject_button").hide();
					if (timesheetApprovedStatus == "SUBMITTED")
					{
						jQuery('#submitted_message').show();
					}
					if (timesheetApprovedStatus == "APPROVED")
					{
						jQuery('#approved_message').show();
					}
					
					 
				}
				else
				{
					jQuery('#submitted_message').hide();
					jQuery('#approved_message').hide();
					jQuery("#timesheet_save_button").show();
					jQuery("#timesheet_submit_button").show();
					jQuery("#addProject_button").show();
				}
			}
			



            var map = new Object();
            var rowCount = 0;
			var col1Count = 0;var col2Count = 0;var col3Count = 0;var col4Count = 0;var col5Count = 0;var col6Count = 0;var col7Count = 0;var col8Count = 0;var col9Count = 0;var col10Count = 0;var col11Count = 0;var col12Count = 0;var col13Count = 0;var col14Count = 0;var col15Count = 0;var col16Count = 0;var col17Count = 0;var col18Count = 0;var col19Count = 0;var col20Count = 0;var col21Count = 0;var col22Count = 0;var col23Count = 0;var col24Count = 0;var col25Count = 0;var col26Count = 0;var col27Count = 0;var col28Count = 0;var col29Count = 0;var col30Count = 0;var col31Count = 0;
            jQuery.each(response.data, function(index, value) {
                rowCount++;
				
				
                var projectMasterId = value.selectedGemsProjectMasterId;
                var projectMasterDesc = value.selected_project;
				var projectResourceActiveStatus = value.projectResourceActiveStatus;
                var inActiveFrom = value.inActiveFrom;
				var projectStartDateString = value.resourceStartDate;
				var projectEndDateString = value.resourceEndDate;
                var inActiveDate;
                
                if (inActiveFrom)
				{
					var dateParts = inActiveFrom.split("/");
					inActiveDate = new Date(dateParts[2], (dateParts[0] - 1), dateParts[1]);
				}

				var projectStartDateStringSplit = projectStartDateString.split("/");
				var projectStartDate = new Date(projectStartDateStringSplit[2], (projectStartDateStringSplit[0] - 1), projectStartDateStringSplit[1]);

				var projectEndDateStringSplit = projectEndDateString.split("/");
				var projectEndDate = new Date(projectEndDateStringSplit[2], (projectEndDateStringSplit[0] - 1), projectEndDateStringSplit[1]);

				var projectInfo = "";
                projectInfo += "<tr id=\"rowCount" + rowCount + "\">";
                projectInfo += "<td><a href=\"#\"  class=\"remove\"><span class=\"fa fa-trash-o fa-2x\">";
                projectInfo += "</td><td>";
                projectInfo += "<select name=\"project_" + rowCount + "\" id=\"project_" + rowCount + "\" class=\"form-control\" style=\"width: 170px;\">";
                projectInfo += "<option id=\"0\">Select Project</option>";

                jQuery.each(projectsArray, function(projectIndex, projectValue) {
					
					 var projectValueArray = projectValue.split("_");
					 var projectNameValue = "";
					 projectNameValue = projectValueArray[0];
                    if (projectIndex == projectMasterId) {
                        projectInfo += "<option id=" + projectIndex + " selected>" + projectNameValue + "</option>";
                    } else {
                        projectInfo += "<option id=" + projectIndex + ">" + projectNameValue + "</option>";
                    }

                });

                projectInfo += "</select>";
                projectInfo += "</td>";
                projectInfo += "<td><input type=\"text\" class=\"form-control\" style=\"width: 100px;\" name=\"task_" + rowCount + "\" id=\"task_" + rowCount + "\" value=\"" + value.taskDescription + "\" /></td>";
				projectInfo += "<td><input type=\"text\" readonly class=\"form-control\" style=\"width: 50px;\" name=\"total_" + rowCount + "\" id=\"total_" + rowCount + "\" value=\"" + value.totalHours + "\" /></td>";
				projectInfo += "</tr>";

                jQuery('#projectInfo').append(projectInfo);


                var days = getDaysInMonth(selectedMonth, selectedYear);

				

                var timeInfo = "";
                timeInfo += "<tr id=\"rowCount" + rowCount + "\">";
				
                for (i = 0; i < days.length; i++) {

					var dayOfMonthString = weekdays[days[i].getDay()];

                    var date = (days[i].getMonth() + 1) + '/' + days[i].getDate() + '/' + days[i].getFullYear();

					var selectedDatePart = date.split("/");
					var selectedDate = new Date(selectedDatePart[2], (selectedDatePart[0] - 1), selectedDatePart[1]);

					timeInfo += "<td class=\"dialog\">";
			

                    var dayOfMon = days[i].getDate();
                    switch ("" + dayOfMon + "") {
                        case '1':
                            var timeValue = value.timeDay1;
                            var commentValue = value.commentDay1;
							
							console.log(selectedDate);
							console.log(inActiveDate);
							console.log(projectStartDate);
							console.log(projectEndDate);

							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}						
							
							col1Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col1Count;

							break;

                        case '2':
                            var timeValue = value.timeDay2;
                            var commentValue = value.commentDay2;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}

                            col2Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col2Count;


                            break;

                        case '3':
                            var timeValue = value.timeDay3;
                            var commentValue = value.commentDay3;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
							
							col3Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col3Count;
                            break;

                        case '4':

                            var timeValue = value.timeDay4;
                            var commentValue = value.commentDay4;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}				
                            
                            col4Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col4Count;
                            break;

                        case '5':
                            var timeValue = value.timeDay5;
                            var commentValue = value.commentDay5;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}							
                            
							col5Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col5Count;
                            break;

                        case '6':
                            var timeValue = value.timeDay6;
                            var commentValue = value.commentDay6;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}		
                            
                            
							col6Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col6Count;
                            break;

                        case '7':
                            var timeValue = value.timeDay7;
                            var commentValue = value.commentDay7;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}		
                            
                            
							col7Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col7Count;
                            break;

                        case '8':
                            var timeValue = value.timeDay8;
                            var commentValue = value.commentDay8;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}		
                            
                            
							col8Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col8Count;
                            break;

                        case '9':
                            var timeValue = value.timeDay9;
                            var commentValue = value.commentDay9;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}		
                            
                            col9Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col9Count;
                            break;

                        case '10':
                            var timeValue = value.timeDay10;
                            var commentValue = value.commentDay10;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
                            
							col10Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col10Count;
                            break;

                        case '11':
                            var timeValue = value.timeDay11;
                            var commentValue = value.commentDay11;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
                            
                            
							col11Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col11Count;
                            break;

                        case '12':
                            var timeValue = value.timeDay12;
                            var commentValue = value.commentDay12;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col12Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col12Count;
                            break;

                        case '13':
                            var timeValue = value.timeDay13;
                            var commentValue = value.commentDay13;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
                            col13Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col13Count;
                            break;

                        case '14':
                            var timeValue = value.timeDay14;
                            var commentValue = value.commentDay14;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col14Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col14Count;
                            break;

                        case '15':
                            var timeValue = value.timeDay15;
                            var commentValue = value.commentDay15;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
                            
							col15Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col15Count;
                            break;

                        case '16':
                            var timeValue = value.timeDay16;
                            var commentValue = value.commentDay16;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
                            
							col16Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col16Count;
                            break;

                        case '17':
                            var timeValue = value.timeDay17;
                            var commentValue = value.commentDay17;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col17Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col17Count;
                            break;

                        case '18':
                            var timeValue = value.timeDay18;
                            var commentValue = value.commentDay18;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col18Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col18Count;
                            break;

                        case '19':
                            var timeValue = value.timeDay19;
                            var commentValue = value.commentDay19;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            col19Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col19Count;
                            break;

                        case '20':
                            var timeValue = value.timeDay20;
                            var commentValue = value.commentDay20;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
                            
							col20Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col20Count;
                            break;

                        case '21':
                            var timeValue = value.timeDay21;
                            var commentValue = value.commentDay21;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}
                            
							col21Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col21Count;
                            break;

                        case '22':
                            var timeValue = value.timeDay22;
                            var commentValue = value.commentDay22;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col22Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col22Count;
                            break;

                        case '23':
                            var timeValue = value.timeDay23;
                            var commentValue = value.commentDay23;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col23Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col23Count;
                            break;

                        case '24':
                            var timeValue = value.timeDay24;
                            var commentValue = value.commentDay24;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col24Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col24Count;
                            break;

                        case '25':
                            var timeValue = value.timeDay25;
                            var commentValue = value.commentDay25;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col25Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col25Count;
                            break;

                        case '26':
                            var timeValue = value.timeDay26;
                            var commentValue = value.commentDay26;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col26Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col26Count;
                            break;

                        case '27':
                            var timeValue = value.timeDay27;
                            var commentValue = value.commentDay27;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}		
                            
							col27Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col27Count;
                            break;

                        case '28':
                            var timeValue = value.timeDay28;
                            var commentValue = value.commentDay28;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}

							col28Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col28Count;
                            break;

                        case '29':
                            var timeValue = value.timeDay29;
                            var commentValue = value.commentDay29;
							if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col29Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col29Count;
                            break;

                        case '30':
                            var timeValue = value.timeDay30;
                            var commentValue = value.commentDay30;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col30Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col30Count;
                            break;

                        case '31':
                            var timeValue = value.timeDay31;
                            var commentValue = value.commentDay31;
                            if (inActiveDate)
							{
								if (selectedDate >= inActiveDate || selectedDate <= projectStartDate || selectedDate >= projectEndDate)
								{
									timeInfo += "<input type=\"text\" disabled class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"\">";
								}
								else
								{
								
									if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
									{
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

									}
									else
									{
										
										timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
									}
									timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";

								}
							}
							else
							{
								if ((dayOfMonthString == "Sun") || (dayOfMonthString == "Sat"))
								{
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;background-color : #d1d1d1;\" id=\"time_" + rowCount + "_" + date + "\" value =\"0\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";

								}
								else
								{
									
									timeInfo += "<input type=\"text\" class=\"form-control timesheet_day\" style=\"width: 50px;\" id=\"time_" + rowCount + "_" + date + "\" value =\"" + timeValue + "\" onclick=\"myfun('" + date + "','" + rowCount + "_" + date + "','" + rowCount + "');\"/>";
								}
								timeInfo += "<input type=\"hidden\" class=\"form-control\"  id=\"comment_" + rowCount + "_" + date + "\" value =\"" + commentValue + "\">";
							}	
                            
							col31Count += parseFloat(timeValue);
							document.getElementById("time_lastrow_" + date).value = col31Count;
                            break;




                        default:
                            ""
                    }




                }
                timeInfo += "</td>";
                
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

					
				
			}
			else
			{
				jQuery('#submitted_message').hide();
					jQuery('#approved_message').hide();
					jQuery("#timesheet_save_button").show();
					jQuery("#timesheet_submit_button").show();
					jQuery("#addProject_button").show();
					jQuery('#submitted_message').hide();
					jQuery('#approved_message').hide();
			}

				








        },
        failure: function(data) {
            alert("In failure");
        }
		,
        statusCode: {
            403: function(xhr) {
                window.location.href = "../../view/login/sessionoutlogin.html";

            }
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
