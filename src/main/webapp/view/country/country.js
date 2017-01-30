jQuery(document)
		.ready(
				function() {
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L2').addClass('active');
						jQuery("ul.U2").show();
						jQuery("ul.U2").find('li#LI5').addClass('active');
			});

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					function country_jQueryDataTableAjax(serviceURL,
							searchCode, searchDescription) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var countryListTable = jQuery("#countryList_table")
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
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "searchCode",
													"value" : searchCode
												}, {
													"name" : "searchDescription",
													"value" : searchDescription
												});
											},
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
														"mData" : "gemsCountryCode"
													},
													{
														"mData" : "gemsCountryDescription"
													},
													{
														"mData" : "lastModifiedString"
													},
													{
														"mData" : "activeStatus"
													},
													{
														"mData" : "gemsCountryMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsCountryMasterId) {
															return '<a href = "#" onClick = "editCountry('
																	+ gemsCountryMasterId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteCountry('
																	+ gemsCountryMasterId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/master/viewCountryList.action?userId='
							+ userId;
					searchCode = "";
					searchDescription = "";
					
					country_jQueryDataTableAjax(serviceURL, searchCode,
							searchDescription);
					
					
					
					function countryTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/master/viewCountryList.action?userId='
								+ userId;
						searchCode = jQuery('#countryName_search').val();
						searchDescription = jQuery('#countryDescription_search').val();
						country_jQueryDataTableAjax(serviceURL, searchCode,
								searchDescription);

					}
					jQuery("#search_country_btn").click(function(e) {

						e.preventDefault();
						countryTable_Search();

					});
					jQuery("#resetsearch_country_btn").click(function(e) {
						
						jQuery('#searchCode').val("");
						jQuery('#searchDescription').val("");
						countryTable_Search();

					});

					jQuery("#delete_country_btn")
							.click(
									function(e) {

										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsCountryMasterId = localStorage
												.getItem('gemsCountryMasterId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/master/deleteGemsCountryMaster.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_country_modal').modal(
												'toggle');
										jQuery('#delete_country_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsCountryMasterId : gemsCountryMasterId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsCountryMasterId);
														window.location.href = "country.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsCountryMasterId);
														window.location.href = "login.html";
													},
													statusCode : {
														403 : function(xhr) {
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});
									});
				});
function editCountry(gemsCountryMasterId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/country/getCountryInfo.action?userId=' + userId + '&gemsCountryMasterId='
			+ gemsCountryMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCountryMasterId : gemsCountryMasterId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editCountry_Data = response.data;
			localStorage.setItem('editCountry_Data', JsonStringify_Data);
			window.location.href = "editCountry.html";
		},
		failure : function(data) {
			window.location.href = "../../";

		},
		statusCode : {
			403 : function(xhr) {
				alert("Session will be Expired");
				window.location.href = "../../";

			}
		}

	});

}
function deleteCountry(gemsCountryMasterId) {
	localStorage.setItem('gemsCountryMasterId', gemsCountryMasterId);
	jQuery('#delete_country_modal').modal('toggle');
	jQuery('#delete_country_modal').modal('view');

}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}