jQuery(document).ready(
		function() {
			jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L33').addClass('active');
						jQuery("ul.U33").show();
						jQuery("ul.U33").find('li#LI72').addClass('active');
			});
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			var serviceURL = envConfig.serviceBaseURL
					+ '/dashboardview/getTotalCountExecution.action?userId='+ userId;
			console.log(serviceURL);
			
			
			var serviceURL = envConfig.serviceBaseURL
							+ '/viewAllServices.action?userId='
							+ userId;
			console.log(serviceURL);
							jQuery.ajax({
					        type: "GET",
							url: serviceURL,
					        
					        success: function(response){
					            // we have the response
								if(response.success == true)
					            {
									
									permissionList = "<ul class=\"nav nav-tabs nav-stacked\">";
									var parentComponentArray = new Array();
									for(i =0 ; i < response.data.length ; i++)
									{
										if (response.data[i].parentComponentId == 0)
										{
											parentComponentArray.push(""+response.data[i].gemsComponentMasterId+"||"+response.data[i].componentDescription+"");
										}
										
									}
									
									

									sname ="<li class=\"nav-header\">Navigation Menu</li><li class=\"\" id=\"dashboard\"><a href=\"../dashboard/dashboard.html\"><i class=\"fa fa-tachometer\"></i>&nbsp;DashBoard</a></li>";  

									
									for(i =0 ; i < parentComponentArray.length ; i++)
									{
										var parentCompArray = parentComponentArray[i].split('||');
										var parentCompId = parentCompArray[0];
										var parentCompName = parentCompArray[1];
										
										sname += "<li class=\"dropdown\" id=\""+parentCompId+"\"><a href=\"#\"><i class=\"fa fa-th-list\"> </i>&nbsp;"+parentCompName+"</a>";
										
										sname += "<ul>";
										for(j =0 ; j < response.data.length ; j++)
										{
											if (response.data[j].parentComponentId == parentCompId)
											{
												
												//response.data[j].componentUrl
												sname += "<li id="+response.data[j].gemsComponentMasterId+"><a href=\""+response.data[j].componentUrl+"\">"+response.data[j].componentDescription+"</a></li>";
												
											}
										}
										sname += "</ul></li>";
										

									}
									
									permissionList += sname;
									permissionList +=  "</ul>";
									//console.log(permissionList);
									jQuery("#orgoradminuser_menu_id22").html(permissionList);
					            	
					            }
					            else
					            {
					            	alert(response.status);
									
					            }
					         },
					         error: function(e){
								 
					             window.location.href = "../../view/login/sessionoutlogin.html";
					         }
					    });
						
						
						var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/employee/viewEmployeeList.action?userId='+userId;
		
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		type : 'GET',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
		//alert("Emp Master lISt is : "+JsonStringify_Data);
		jQuery("#employeesItemList").html(""); //reset option
		jQuery.each(response.data, function(index, value){	
			//jQuery("#employeesItemList").append('<option id="'+ value.gemsEmployeeMasterId + '" value=" '+value.empName+' " >'+ value.empName +'</option>');
			jQuery("#employeeName").append('<option id="'+ value.gemsEmployeeMasterId + '" value=" '+value.empName+' " >'+ value.empName +'</option>');			
				});	
				// employeesItemList.bootstrapDualListbox('refresh', true);
			jQuery('#assign_project_modal').modal('toggle');
			jQuery('#assign_project_modal').modal('view');
			customunblockUI();
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}
		
	});
	customunblockUI();


			// date picker
			jQuery('#projectStartDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});
			
			// date picker
			jQuery('#projectEndDate').datepicker({
		changeMonth: true,
     changeYear: true
	
	});
		
	jQuery("#addAssigneesButton").click(function(e) {
		jQuery('#projectResourceRow').show('1000');
		
	});
	
	jQuery("#save_Assinees_Button").click(function(e) {
		jQuery('#projectResourceRow').show('1000');
		
	});
	var assignEmployee = [],index = 0;
	jQuery("#save_Assinees_Button").click(function(e) {
		
		var employeeName = jQuery('#employeeName').val();
		var projectStartDate = jQuery('#projectStartDate').val();
		var projectEndDate = jQuery('#projectEndDate').val();
		var projectBillingRate = jQuery('#projectBillingRate').val();

		
		assignEmployee.push({
            employeeName : employeeName, 
            startDate :  projectStartDate,
			endDate : projectEndDate,
			billingRate : projectBillingRate
        });
		
		alert(JSON.stringify(assignEmployee));
		var selectedRow = "";
		
		jQuery(assignEmployee).each(function(i, v){ 
			alert(v.employeeName);
		selectedRow = '<div class="col-md-3" id="selectedEmployeeName"><b> Employee Name :</b> '+v.employeeName+ '</div> '
					+'<div class="col-md-3" id="selectedStartDate"> <b> Start Date : </b> '+v.startDate+'</div>'
					+'<div class="col-md-3" id="selectedEndDate"> <b> End Date : </b> '+v.endDate+'</div> '
					+'<div class="col-md-3" id="selectedProjectBillingType"> <b> Billing Rate : </b> '+v.billingRate+'</div> ';
			jQuery("#asigneesList").append(selectedRow);
		});
		
		jQuery('#projectResourceRow').hide('fast');
		
	});
			
			
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
