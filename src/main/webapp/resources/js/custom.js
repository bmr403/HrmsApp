jQuery.noConflict();

jQuery(document).ready(function(){
	
	// dropdown in leftmenu
	 jQuery('.leftmenu .dropdown > a').click(function(){
		if(!jQuery(this).next().is(':visible'))
			jQuery(this).next().slideDown('fast');
		else
			jQuery(this).next().slideUp('fast');	
		return false;
	}); 
	
	
	//jQuery("#orgoradminuser_menu_id22").click(function(e){
	//jQuery('.leftmenu li.parent > a').click(function(event) {
	/* jQuery( "#orgoradminuser_menu_id22" ).on( "click", function(e) {
		e.preventDefault();
			jQuery("ul").each(function(){
			//alert(jQuery(this).html());
			if(jQuery(this).attr("style")==="display:block;") {
				jQuery(this).removeAttr("style");
				//jQuery(e.target).next("ul").removeAttr("style");
			}
			 else {
				jQuery(e.target).next("ul").attr("style","display:block;");
			}
			
			});
			
	}); */
	
	
	jQuery("#orgoradminuser_menu_id22 ul > li.dropdown > a").live( "click", function(event) {
				jQuery(this).parent().siblings().find('ul').slideUp('fast');

				if (jQuery(this).next('ul').is(':hidden')) {
					event.preventDefault();
					jQuery(this).parent().children('ul').slideDown('fast');
					//jQuery(this).parent().children.siblings('ul').slideUp('fast');
					}
			});
										
	jQuery( "#orgoradminuser_menu_id22 ul > li.dropdown > a" ).live( "click", function(e) {
		e.preventDefault();
		
		if(jQuery(this).attr("style")==="display: block;") {
				jQuery(this).next().slideUp('fast');
			}
			 else {
				jQuery(this).next().slideDown('fast');
			}
		
	});
	
	
	
	if(jQuery.uniform) 
	   jQuery('input:checkbox, input:radio, .uniform-file').uniform();
		
	if(jQuery('.widgettitle .close').length > 0) {
		  jQuery('.widgettitle .close').click(function(){
					 jQuery(this).parents('.widgetbox').fadeOut(function(){
								jQuery(this).remove();
					 });
		  });
	}
	
	
   // add menu bar for phones and tablet
   jQuery('<div class="topbar"><a class="barmenu">'+
		    '</a><div class="chatmenu"></a></div>').insertBefore('.mainwrapper');
	
	jQuery('.topbar .barmenu').click(function() {
		  
		  var lwidth = '260px';
		  if(jQuery(window).width() < 340) {
					 lwidth = '260px';
		  }
		  
		  if(!jQuery(this).hasClass('open')) {
					 jQuery('.rightpanel, .headerinner, .topbar').css({marginLeft: lwidth},'fast');
					 jQuery('.logo, .leftpanel').css({marginLeft: 0},'fast');
					 jQuery(this).addClass('open');
		  } else {
					 jQuery('.rightpanel, .headerinner, .topbar').css({marginLeft: 0},'fast');
					 jQuery('.logo, .leftpanel').css({marginLeft: '-'+lwidth},'fast');
					 jQuery(this).removeClass('open');
		  }
	});
	
	jQuery('.topbar .chatmenu').click(function(){
		if(!jQuery('.onlineuserpanel').is(':visible')) {
			jQuery('.onlineuserpanel,#chatwindows').show();
			jQuery('.topbar .chatmenu').css({right: '210px'});
		} else {
			jQuery('.onlineuserpanel, #chatwindows').hide();
			jQuery('.topbar .chatmenu').css({right: '10px'});
		}
	});
	
	// show/hide left menu
	jQuery(window).resize(function() {
		  if(jQuery('.topbar').is(':visible')) {
				if(jQuery('.barmenu').hasClass('open')) {
						 jQuery('.rightpanel, .headerinner').css({marginLeft: '260px'});
						 jQuery('.logo, .leftpanel').css({marginLeft: 0});
				} else {
						 jQuery('.rightpanel, .headerinner').css({marginLeft: 0});
						 jQuery('.logo, .leftpanel').css({marginLeft: '-260px'});
				}
		  } else {
				jQuery('.rightpanel, .headerinner').css({marginLeft: '260px'});
				jQuery('.logo, .leftpanel').css({marginLeft: 0});
		  }
   });
	
	// dropdown menu for profile image
	jQuery('.userloggedinfo img').click(function(){
		  if(jQuery(window).width() < 480) {
					 var dm = jQuery('.userloggedinfo .userinfo');
					 if(dm.is(':visible')) {
								dm.hide();
					 } else {
								dm.show();
					 }
		  }
   });
	
	// change skin color
	jQuery('.skin-color a').click(function(){ return false; });
	jQuery('.skin-color a').hover(function(){
		var s = jQuery(this).attr('href');
		if(jQuery('#skinstyle').length > 0) {
			if(s!='default') {
				jQuery('#skinstyle').attr('href','../../resources/css/style.'+s+'.css');	
				jQuery.cookie('skin-color', s, { path: '/' });
			} else {
				jQuery('#skinstyle').remove();
				jQuery.cookie("skin-color", '', { path: '/' });
			}
		} else {
			if(s!='default') {
				jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+s+'.css" type="text/css" />');
				jQuery.cookie("skin-color", s, { path: '/' });
			}
		}
		return false;
	});
	
	// load selected skin color from cookie
	   /* if(jQuery.cookie('skin-color')) {
		var c = jQuery.cookie('skin-color');
		if(c) {
			jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+c+'.css" type="text/css" />');
			jQuery.cookie("skin-color", c, { path: '/' });
		}
	}  */
	
	
	// expand/collapse boxes
	if(jQuery('.minimize').length > 0) {
		  
		  jQuery('.minimize').click(function(){
					 if(!jQuery(this).hasClass('collapsed')) {
								jQuery(this).addClass('collapsed');
								jQuery(this).html("&#43;");
								jQuery(this).parents('.widgetbox')
										      .css({marginBottom: '20px'})
												.find('.widgetcontent')
												.hide();
					 } else {
								jQuery(this).removeClass('collapsed');
								jQuery(this).html("&#8211;");
								jQuery(this).parents('.widgetbox')
										      .css({marginBottom: '0'})
												.find('.widgetcontent')
												.show();
					 }
					 return false;
		  });
			  
	}
	
	// fixed right panel
	var winSize = jQuery(window).height();
	if(jQuery('.rightpanel').height() < winSize) {
		jQuery('.rightpanel').height(winSize);
	}
	
	
	// if facebook like chat is enabled
	if(jQuery.cookie('enable-chat')) {
		
		jQuery('body').addClass('chatenabled');
		jQuery.get('ajax/chat.html',function(data){
			jQuery('body').append(data);
		});
		
	} else {
		
		if(jQuery('.chatmenu').length > 0) {
			jQuery('.chatmenu').remove();
		}
		
	}
	
});