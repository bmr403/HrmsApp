jQuery(document)
		.ready(
				function() {


					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L78').addClass('active');
						jQuery("ul.U78").show();
						jQuery("ul.U78").find('li#LI84').addClass('active');
					});
					
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					

					
					var candidateProfileList_table = '';
					



					function assignCandidate_jQueryDataTableAjax(serviceURL,gemsMapowerRequestId) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						assignCandidateList_table = jQuery("#assignCandidateList_table")
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
													"name" : "gemsMapowerRequestId",
													"value" : gemsMapowerRequestId
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
														"mData" : "candidateName"														
													},
													{
														"mData" : "candidateContactInfo"														
													},
													{
														"mData" : "gemsCandidateMasterId",
														"bSortable" : false,
														"mRender" : function(
																gemsCandidateMasterId) {
															return '<a href = \"#\" onClick = "downloadResume('
																	+ gemsCandidateMasterId
																	+ ');"  id=\"edit_btn\"><span class="glyphicon glyphicon-download" title=\"Download\"></a>';
														}
													},
													{
														"mData" : "selectedGemsCandidateStatusMaster"
													},	
													{
														"mData" : "gemsRecruitmentRequirementCandidateId",
														"bSortable" : false,
														"mRender" : function(
																gemsRecruitmentRequirementCandidateId) {
															return '<a href = \"#\" onClick = "editCandidateResumeInfo('
																	+ gemsRecruitmentRequirementCandidateId
																	+ ');"  id=\"edit_btn\"><span class="glyphicon glyphicon-pencil" title=\"Edit\"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteCandidateProfile('
																	+ gemsRecruitmentRequirementCandidateId
																	+ ');"  id=\"delete_btn\"><span class="glyphicon glyphicon-trash" title=\"Delete\"></a>';
														}
													} ],

										});

					

					 
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/recruitment/viewResumeByRequest.action?userId='
							+ userId;
					assignCandidate_jQueryDataTableAjax(serviceURL);
					
					function candidateprofileTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/recruitment/viewResumeByRequest.action?userId='
								+ userId;
						gemsMapowerRequestId = jQuery('#searchName').val();
						var gemsMapowerRequestId = jQuery(jQuery("#dropdown_req")).find('option:selected').attr('id');
						assignCandidate_jQueryDataTableAjax(serviceURL,gemsMapowerRequestId);

					}
					jQuery("#search_assigncandidate_btn").click(function(e) {

						e.preventDefault();
						candidateprofileTable_Search();

					});					
					jQuery("#resetsearch_assigncandidate_btn").click(function(e) {
						
						window.location.href="assignCandidates.html";

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
function editCandidateResumeInfo(gemsRecruitmentRequirementCandidateId) 
{
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/recruitment/getCandidateResumeInfo.action?userId=' + userId + '&gemsRecruitmentRequirementCandidateId='
			+ gemsRecruitmentRequirementCandidateId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsRecruitmentRequirementCandidateId : gemsRecruitmentRequirementCandidateId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editCandidateProfile_Data = response.data;
			localStorage.setItem('editCandidateResumeInfo', JsonStringify_Data);
			var gemsRecruitmentRequirementCandidateId = response.data.gemsRecruitmentRequirementCandidateId;
			localStorage.getItem('gemsRecruitmentRequirementCandidateId', gemsRecruitmentRequirementCandidateId);
			window.location.href = "editCandidateResumeInfo.html";
			
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

