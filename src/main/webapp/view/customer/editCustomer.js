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

					jQuery( document ).ajaxError(function() {
				window.location.href = "../../view/login/sessionoutlogin.html";
			});

					 jQuery('#gemsCustomerDescription').keypress(function( e ) {
							if(e.which === 32 && this.value.length == 0) 
									return false;
					}); 
					 jQuery("#customerContact").load("customerContact.html");
					 jQuery("#customerDocument").load("customerDocument.html");
					 
					 
					jQuery(document).ready(function(){
   jQuery.noConflict();
});
					 
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					var data = JSON.parse(localStorage
							.getItem('editCustomer_Data'));
					if (data !== null) {
						
						jQuery('#gemsCustomerMasterId').val(data.gemsCustomerMasterId);
						localStorage.setItem('gemsCustomerMasterId', data.gemsCustomerMasterId);
						jQuery("#gemsCustomerName").val(data.gemsCustomerName);
						jQuery("#gemsCustomerCode").val(data.gemsCustomerCode);
						jQuery("#gemsCustomerPhone").val(data.gemsCustomerPhone);
						jQuery("#gemsCustomerPhone1").val(data.gemsCustomerPhone1);
						jQuery("#gemsCustomerFax").val(data.gemsCustomerFax);
						jQuery("#gemsCustomerWeb").val(data.gemsCustomerWeb);
						jQuery("#gemsCustomerDescription").val(data.gemsCustomerDescription);
						jQuery("#permanentAddressStreet1").val(data.permanentAddressStreet1);
						jQuery("#permanentAddressStreet2").val(data.permanentAddressStreet2);
						jQuery("#permanentAddressCity").val(data.permanentAddressCity);
						jQuery("#permanentAddressState").val(data.permanentAddressState);
						jQuery("#permanentAddressCountry").val(data.permanentAddressCountry);
						jQuery("#permanentAddressZipCode").val(data.permanentAddressZipCode);
						jQuery("#correspondenseAddressStreet1").val(data.correspondenseAddressStreet1);
						jQuery("#correspondenseAddressStreet2").val(data.correspondenseAddressStreet2);
						jQuery("#correspondenseAddressCity").val(data.correspondenseAddressCity);
						jQuery("#correspondenseAddressState").val(data.correspondenseAddressState);
						jQuery("#correspondenseAddressCountry").val(data.correspondenseAddressCountry);
						jQuery("#correspondenseAddressZipCode").val(data.correspondenseAddressZipCode);
						

						


					} 
					
					jQuery('#editCustomer_cancel_btn').click(function(event) {
						window.location.href = 'customer.html';
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
	var gemsCustomerMasterId = jQuery("#gemsCustomerMasterId").val();

	


	
	//alert(gemsProjectTypeCode+" : "+gemsProjectTypeDesc+":"+projectTypeisActive);
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			gemsCustomerMasterId : gemsCustomerMasterId,
			gemsCustomerCode : gemsCustomerCode,
			gemsCustomerName : gemsCustomerName,
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
			var JsonStringify_Data = JSON.stringify(response.data);
			//var editEmployee_Data = response.data;
			localStorage.setItem('editCustomer_Data', JsonStringify_Data);
			var gemsCustomerMasterId = response.data.gemsCustomerMasterId;
			localStorage.getItem('gemsCustomerMasterId', gemsCustomerMasterId);
			window.location.href = "editCustomer.html";
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
					

					

					function hideAllErrors() {
												
					}
					

				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}