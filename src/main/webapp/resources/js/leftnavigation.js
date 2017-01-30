jQuery(document).ready(
		function() {
	var roleCode = sessionStorage.getItem("roleCode");

	if (sessionStorage.getItem("permissionList") === null) {

		alert('Permission List Empty');
 
	}
	else
	{
		jQuery("#orgoradminuser_menu_id22").html(sessionStorage.getItem("permissionList"));
		
		
		
		
	}

});