jQuery(document)
		.ready(
				function() {


					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI82').addClass('active');
					});
					
					jQuery('#R21').hide();
					jQuery('#R11').hide();

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					

					
					var candidateProfileList_table = '';
					



					function candidateprofile_jQueryDataTableAjax(serviceURL,
							searchName, searchKeySkill) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						candidateProfileList_table = jQuery("#candidateProfileList_table")
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
													"name" : "searchName",
													"value" : searchName
												}, {
													"name" : "searchKeySkill",
													"value" : searchKeySkill
												});
											},
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 10,
											columnDefs : [ 
											{
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
														"mData" : "gemsCandidateMasterId",
														"bSortable" : false,
														"mRender" : function(
																data, type, full) {
															return full.gemsCandidateStatus == "Available" ? '<input type="checkbox" name="id" value="' +full.gemsCandidateMasterId + '">' : '<input type="checkbox" name="id" value="' +full.gemsCandidateMasterId + '" disabled>'
															
															
														}
													},
													{
														"mData" : "candidateName"														
													},
													{
														"mData" : "contactNumber"														
													},
													{
														"mData" : "gemsCandidateKeySkill"
													},
													
													{
														"mData" : "gemsCandidateExperience"
													},
													{
														"mData" : "gemsCandidateStatus"
													},	
													{
														"mData" : "gemsCandidateMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsCandidateMasterId) {
															return '<a href = "#" onClick = "editCandidateProfile('
																	+ gemsCandidateMasterId
																	+ ');" id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteCandidateProfile('
																	+ gemsCandidateMasterId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "downloadResume('
																	+ gemsCandidateMasterId
																	+ ');"  id=\"download_btn\"><span class="glyphicon glyphicon-download" title=\"Download\"></a>';
														}
													} ],

										});

										// Handle click on "Select all" control
					   jQuery('#example-select-all').on('click', function(){
						  // Get all rows with search applied
						  var rows = candidateProfileList_table.rows({ 'search': 'applied' }).nodes();
						  // Check/uncheck checkboxes for all rows in the table
						  jQuery('input[type="checkbox"]', rows).prop('checked', this.checked);
					   });

					   // Handle click on checkbox to set state of "Select all" control
					   jQuery('#example tbody').on('change', 'input[type="checkbox"]', function(){
						  // If checkbox is not checked
						  if(!this.checked){
							 var el = jQuery('#example-select-all').get(0);
							 // If "Select all" control is checked and has 'indeterminate' property
							 if(el && el.checked && ('indeterminate' in el)){
								// Set visual state of "Select all" control 
								// as 'indeterminate'
								el.indeterminate = true;
							 }
						  }
					   });
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewCandidateMasterList.action?userId='
							+ userId;
					searchName = "";
					searchKeySkill = "";
					
					
					candidateprofile_jQueryDataTableAjax(serviceURL,searchName, searchKeySkill);
					
					function candidateprofileTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/recruitment/viewCandidateMasterList.action?userId='
								+ userId;
						searchName = jQuery('#searchName').val();
						searchKeySkill = jQuery('#searchKeySkill').val();
						
						candidateprofile_jQueryDataTableAjax(serviceURL,searchName,
								searchKeySkill);

					}
					jQuery("#search_candidateprofile_btn").click(function(e) {

						e.preventDefault();
						candidateprofileTable_Search();

					});
					jQuery("#assign_candidate_request").click(function(e) {
						
						e.preventDefault();
						jQuery('#R2').hide();
						jQuery('#R1').hide();

						jQuery('#R21').show();
						jQuery('#R11').show();
						


					});
					jQuery("#reset_assigncandidateprofile_btn").click(function(e) {
						
						e.preventDefault();
						jQuery('#R2').show();
						jQuery('#R1').show();

						jQuery('#R21').hide();
						jQuery('#R11').hide();
						
						candidateprofileTable_Search();

					});
					jQuery("#resetsearch_candidateprofile_btn").click(function(e) {
						
						jQuery('#searchName').val("");
						jQuery('#searchKeySkill').val("");						
						candidateprofileTable_Search();

					});

					jQuery('#submit_assigncandidateprofile_btn').click(function(e) {

						e.preventDefault();
						var serviceURL = envConfig.serviceBaseURL+ '/recruitment/mapRequestCandidate.action?userId='+userId;
						var checkboxArray =[];

						 jQuery('#candidateProfileList_table').find('input[type="checkbox"]:checked').each(function () {
							checkboxArray.push(this.value);							

						 });
						 
						 jQuery.ajax({
							url : serviceURL,
							dataType : "json",
							data : {
								checkboxArray : checkboxArray.toString(),
								requirementId : jQuery("#dropdown_req").children(":selected").attr("id")
							},
							type : 'POST',
							success : function(response) {
								window.location.href="candidateProfiles.html";
							},
							failure : function(data) {
								alert('ssss');
							},
							statusCode : {
								403 : function(xhr) {
									alert("Session will be Expired");
									window.location.href = "../../";

								}
							}
						  });
						
						

					});

					requestList = jQuery('#dropdown_req');
					var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewManpowerRequestList.action?userId='
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
									gemsMapowerRequestId : val.gemsMapowerRequestId,
									requisitionNumber : val.requisitionNumber
								};
							});

							// iterate over the data and append a select option
							requestList.append('<option id="0">Select Manpower Request</option>');
							jQuery.each(result, function(key, val) {
								requestList.append('<option id="'
										+ val.gemsMapowerRequestId + '">'
										+ val.requisitionNumber + '</option>');
								
								
							})
							
							jQuery("#dropdown_req").focus();
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

					jQuery("#delete_candidateprofile_btn")
							.click(
									function(e) {

										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var gemsCandidateMasterId = localStorage
												.getItem('gemsCandidateMasterId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/recruitment/deleteCandidate.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_mr_modal').modal(
												'toggle');
										jQuery('#delete_mr_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														gemsCandidateMasterId : gemsCandidateMasterId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(gemsCandidateMasterId);
														window.location.href = "candidateProfiles.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(gemsCandidateMasterId);
														window.location.href = "candidateProfiles.html";
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
function downloadResume(gemsCandidateMasterId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/recruitment/downloadResume.action?userId='+userId+'&objectId='
			+ gemsCandidateMasterId;	
	//window.location.href=serviceURL;
	window.open(serviceURL, '_blank');
}

function deleteCandidateProfile(gemsCandidateMasterId) {
	localStorage.setItem('gemsCandidateMasterId', gemsCandidateMasterId);
	jQuery('#delete_candidate_modal').modal('toggle');
	jQuery('#delete_candidate_modal').modal('view');

}
function editCandidateProfile(gemsCandidateMasterId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/recruitment/getGemsCandidateInfo.action?userId=' + userId + '&objectId='
			+ gemsCandidateMasterId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCandidateMasterId : gemsCandidateMasterId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editCandidateProfile_Data = response.data;
			localStorage.setItem('editCandidateProfile_Data', JsonStringify_Data);
			var gemsCandidateMasterId = response.data.gemsCandidateMasterId;
			localStorage.getItem('gemsCandidateMasterId', gemsCandidateMasterId);
			window.location.href = "editCandidateProfile.html";
			
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
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}

