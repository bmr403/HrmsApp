jQuery(document)
		.ready(
				function() {
					
					jQuery(window).load(function() {
						jQuery('ul#nav').find('li#L61').addClass('active');
						jQuery("ul.U61").show();
						jQuery("ul.U61").find('li#LI62').addClass('active');
					});
				
					var maxLength = 500;  
					jQuery('#gemsCustomerDescription').keyup(function() {  
					  var textlen = maxLength - jQuery(this).val().length;  
					  jQuery('#rchars').text(textlen);  
					}); 

					 jQuery('#gemsCustomerDescription').keypress(function( e ) {
							if(e.which === 32 && this.value.length == 0) 
									return false;
					}); 
					jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});
					
					jQuery("#customer-general-form").validate({
    
						// Specify the validation rules
						rules: {
							gemsCustomerName: "required",
							gemsCustomerCode: "required",
							gemsCustomerPhone: "required",
							permanentAddressStreet1: "required",							
							permanentAddressCity: "required",
							permanentAddressState: "required",							
							permanentAddressCountry: "required",
							permanentAddressZipCode: "required",
							correspondenseAddressStreet1: "required",
							correspondenseAddressCity: "required",
							correspondenseAddressState: "required",
							correspondenseAddressCountry: "required",
							correspondenseAddressZipCode: "required"
							
							
						},
						
						// Specify the validation error messages
						messages: {
					

							
						},
						
						submitHandler: function(form) {
							var userId = sessionStorage.getItem("userId");
	//alert("In Delete timeSheet Header Id is :"+timeSheetWeekDetailId);
	var serviceURL = envConfig.serviceBaseURL+ '/customer/saveCustomer.action?userId='+userId;
	
	var gemsCustomerName = jQuery("#gemsCustomerName").val();
	var gemsCustomerCode = jQuery("#gemsCustomerCode").val();	
	var gemsCustomerPhone = jQuery("#gemsCustomerPhone").val();
	var gemsCustomerPhone1 = jQuery("#gemsCustomerPhone1").val();
	var gemsCustomerFax = jQuery("#gemsCustomerFax").val();
	var gemsCustomerWeb = jQuery("#gemsCustomerWeb").val();
	var gemsCustomerDescription = jQuery("#gemsCustomerDescription").val();
	var permanentAddressStreet1 = jQuery("#permanentAddressStreet1").val();
	var permanentAddressStreet2 = jQuery("#permanentAddressStreet2").val();
	var permanentAddressCity = jQuery("#permanentAddressCity").val();
	var permanentAddressState = jQuery("#permanentAddressState").val();
	var permanentAddressCountry = jQuery("#permanentAddressCountry").val();
	var permanentAddressZipCode = jQuery("#permanentAddressZipCode").val();
	var correspondenseAddressStreet1 = jQuery("#correspondenseAddressStreet1").val();
	var correspondenseAddressStreet2 = jQuery("#correspondenseAddressStreet2").val();
	var correspondenseAddressCity = jQuery("#correspondenseAddressCity").val();
	var correspondenseAddressState = jQuery("#correspondenseAddressState").val();
	var correspondenseAddressCountry = jQuery("#correspondenseAddressCountry").val();
	var correspondenseAddressZipCode = jQuery("#correspondenseAddressZipCode").val();
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCustomerName : gemsCustomerName,
			gemsCustomerCode : gemsCustomerCode,
			gemsCustomerPhone : gemsCustomerPhone,
			gemsCustomerPhone1	: gemsCustomerPhone1,
			gemsCustomerFax		: gemsCustomerFax,
			gemsCustomerWeb		: gemsCustomerWeb,
			gemsCustomerDescription	: gemsCustomerDescription,
			permanentAddressStreet1	: permanentAddressStreet1,
			permanentAddressStreet2 : permanentAddressStreet2,
			permanentAddressCity	: permanentAddressCity,
			permanentAddressState	: permanentAddressState,
			permanentAddressCountry	: permanentAddressCountry,
			permanentAddressZipCode	: permanentAddressZipCode,
			correspondenseAddressStreet1 : correspondenseAddressStreet1,
			correspondenseAddressStreet2 : correspondenseAddressStreet2,
			correspondenseAddressCity	: correspondenseAddressCity,
			correspondenseAddressState	: correspondenseAddressState,
			correspondenseAddressCountry : correspondenseAddressCountry,
			correspondenseAddressZipCode : correspondenseAddressZipCode
			

		},
		type : 'POST',
		success : function(response) {
			var responseTextFlag = response.success;
			if (responseTextFlag == true) {
				
				var JsonStringify_Data = JSON.stringify(response.data);
				var editCustomer_Data = response.data;
				localStorage.setItem('editCustomer_Data', JsonStringify_Data);
				var gemsCustomerMasterId = response.data.gemsCustomerMasterId;
				localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);

				jQuery("#loading-div-background").hide();
				jQuery('#success_customer_modal').modal('toggle');
				jQuery('#success_customer_modal').modal('view');
			} else {
				jQuery("#loading-div-background").hide();
				jQuery('#error_customer_modal').modal('toggle');
				jQuery('#error_customer_modal').modal('view');
																
			}
			
			//window.location.href = "editCustomer.html";	
		},
		failure : function(data) {
			window.location.href = "../dashboard/dashboard.html";
		},
		statusCode : {
			403 : function(xhr) {
				window.location.href = "../../view/login/sessionoutlogin.html";

			}
		}

	});
						}
					});
					
					var userId = sessionStorage.getItem("userId");
					
					jQuery('#newCustomer_cancel_btn').click(function(event) {
						window.location.href = 'customer.html';
					});
					
					jQuery('#gemsCustomerName').focus();			
					
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editCustomer.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newCustomer.html";
					});

	

					
					
					
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}



