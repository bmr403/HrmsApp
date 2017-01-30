jQuery(document).ready(function() {
		var userId = sessionStorage.getItem("userId");
		


var userEmail = sessionStorage.getItem("userEmail");
		var userName = sessionStorage.getItem("userName");
		var userId = sessionStorage.getItem("userId");
		jQuery("#userEmail_id").append(userEmail);
		jQuery("#userName_id").append(userName);
		
		jQuery('#logout_link').click(
												function() {

			

													var userId = sessionStorage
															.getItem("userId");
													var userToken = sessionStorage
															.getItem("userToken");
													var serviceURL = envConfig.serviceBaseURL
															+ '/user/logoutUser.action?userId='
															+ userId;
													console.log(serviceURL);
													jQuery
															.ajax({
																type : 'Post',
																url : serviceURL,
																dataType : "json",
																data : {
																	userToken : userToken
																},
																success : function(
																		data) {
																	//window.location.href = "../../";
																	window.location.href = "../../view/login/sessionoutlogin.html";
																},
																failure : function() {
																	//window.location.href = "dashboard.html";
																	window.location.href = "../login/sessionoutlogin.html";
																}
															});
												});
												
							jQuery(".active").removeClass("active");
						
					//side bar accordion menu
						jQuery('.leftmenu li').each(function() {
							if (jQuery(this).children('ul').length > 0) {
								jQuery(this).addClass('parent');
							}
						});

						jQuery('.leftmenu li.parent > a')
								.click(
										function(event) {
											jQuery(this).parent().siblings().find(
													'ul').slideUp('fast');

											if (jQuery(this).next('ul')
													.is(':hidden')) {
												event.preventDefault();
												jQuery(this).parent().children('ul')
														.slideDown('fast');
												jQuery(this).parent().children
														.siblings('ul')
														.slideUp('fast');
											}
										});
										
		jQuery('#default,#navyblue,#palegreen,#red,#green,#brown').click(function(e) {
		
		var colorSkin_Slected = jQuery(this).attr('id');
		// alert(colorSkin_Slected);
	var userId = sessionStorage.getItem("userId");
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
					+ '/user/saveChangeSkin.action?userId='
					+ userId;
					
			console.log(serviceURL);
										jQuery.ajax({
														url : serviceURL,
														type : 'Post',
														dataType : "json",
														data : {
														userId : userId,
														colorSkin: colorSkin_Slected
														},
														success : function(res) {
														var colorSkin = res.data.colorSkin;
														//alert(res.data.colorSkin);
														sessionStorage.setItem("colorSkin",colorSkin);
														if(colorSkin != null){
														jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+colorSkin+'.css" type="text/css" />');
														jQuery.cookie("skin-color", colorSkin, { path: '/' });
														}
														
														},
														failure : function(res) {
															//window.location.href = "../../";
															window.location.href = "../login/sessionoutlogin.html";
														}
													});
		
		}); 
		
		 var colorSkin = sessionStorage.getItem("colorSkin");
		//alert(colorSkin);
		
		jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+colorSkin+'.css" type="text/css" />');
		jQuery.cookie("skin-color", colorSkin, { path: '/' });
		
		
});