/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Inventory module.
 *
 *  Inventory module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Inventory module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Inventory module.  If not, see <http://www.gnu.org/licenses/>.
 *
*/
String.prototype.replaceAll = function(
strTarget, // The substring you want to replace
strSubString // The string you want to replace in.
){
 var strText = this;
 var intIndexOfMatch = strText.indexOf( strTarget );
  
 // Keep looping while an instance of the target string
 // still exists in the string.
 while (intIndexOfMatch != -1){
 // Relace out the current instance.
 strText = strText.replace( strTarget, strSubString );
  
 // Get the index of any next matching substring.
 intIndexOfMatch = strText.indexOf( strTarget );
 }
  
 // Return the updated string with ALL the target strings
 // replaced out with the new substring.
 return( strText );
 };
STRING = 
{
		stringConvertVn : function(str)
		{
			 chars = new Array("a","A","e","E","o","O","u","U","i","I","d", "D","y","Y");
			 var uni = new Array(14);
			 uni[0] =new  Array("Ã¡","Ã ","áº¡","áº£","Ã£","Ã¢","áº¥","áº§", "áº­","áº©","áº«","Äƒ","áº¯","áº±","áº·","áº³","ï¿½ ï¿½");
			 uni[1] =new  Array("Ã�","Ã€","áº ","áº¢","Ãƒ","Ã‚","áº¤","áº¦", "áº¬","áº¨","áºª","Ä‚","áº®","áº°","áº¶","áº²","ï¿½ ï¿½");
			 uni[2] =new  Array("Ã©","Ã¨","áº¹","áº»","áº½","Ãª","áº¿","á»�" ,"á»‡","á»ƒ","á»…");
			 uni[3] =new  Array("Ã‰","Ãˆ","áº¸","áºº","áº¼","ÃŠ","áº¾","á»€" ,"á»†","á»‚","á»„");
			 uni[4] =new  Array("Ã³","Ã²","á»�","á»�","Ãµ","Ã´","á»‘","á»“", "á»™","á»•","á»—","Æ¡","á»›","á»�","á»£","á»Ÿ","á»¡","ï¿½ ï¿½");
			 uni[5] =new  Array("Ã“","Ã’","á»Œ","á»Ž","Ã•","Ã”","á»�","á»’", "á»˜","á»”","á»–","Æ ","á»š","á»œ","á»¢","á»ž","á» ","ï¿½ ï¿½");
			 uni[6] =new  Array("Ãº","Ã¹","á»¥","á»§","Å©","Æ°","á»©","á»«", "á»±","á»­","á»¯");
			 uni[7] =new  Array("Ãš","Ã™","á»¤","á»¦","Å¨","Æ¯","á»¨","á»ª", "á»°","á»¬","á»®");
			 uni[8] =new  Array("Ã­","Ã¬","á»‹","á»‰","Ä©");
			 uni[9] =new  Array("Ã�","ÃŒ","á»Š","á»ˆ","Ä¨");
			 uni[10] =new  Array("Ä‘");
			 uni[11] =new  Array("Ä�");
			 uni[12] =new  Array("Ã½","á»³","á»µ","á»·","á»¹");
			 uni[13] =new  Array("Ã�","á»²","á»´","á»¶","á»¸");
			// alert("aba".replace(/a/g,"i"));
			 
			for(i=0; i<=13; i++) {
				for(j=0;j<uni[i].length;j++){
					//uni[i][j].replace(/'"/g,'');
					str = str.replaceAll(uni[i][j],chars[i]);
				}
			}

			return str;
		},
		getPositionCharater : function(str)
		{
			var returns = '';
			chars = new Array("a","A","e","E","o","O","u","U","i","I","d", "D","y","Y");
			for(j=0;j<chars.length;j++){
				if(chars[j] == str){
					returns = chars[j];
					break;
				}
			}
		},
		
			
		/**
		Convert from "dd/mm/yyyy" to "mm/dd/yyyy"
		Aug 15th 2012 - Thai Chuong - Fixed issue #335
		*/
		convertDateFormat : function(myDate) {
			var date = myDate.split("/")[0];
			var month = myDate.split("/")[1];
			var year = myDate.split("/")[2];
			return month + "/" + date + "/" + year;
		}

};
SHOWTHICKBOX = {
		show : function(header ,link )
		{
			url = link+"?keepThis=false&TB_iframe=true&height=550&width=751&modal=true";
			tb_show(header,url,false);
		},
		showWithSize : function(header ,link, width, height )
		{
			url = link+"?keepThis=false&TB_iframe=true&height="+width+"&width="+height+"&modal=true";
			tb_show(header,url,false);
		}
};
LOADER = {
		load : function(url, container)
		{
			jQuery.get(
				url,
				function(data)
				{
					jQuery(container).html(data);
					tb_init("a.thickbox");
				}
			);
		},
		loadWithCallback : function(url, container,callback)
		{
			jQuery.get(
				url,
				function(data)
				{
					jQuery(container).html(data);
					if( callback != null)
						callback();
				}
			);
		}
};

SESSION = {
		checkSession: function ()
		{
			var data = jQuery.ajax(
					{
						type:"GET"
						,url: "checkSession.form"
						,async: false
						, cache : false
					}).responseText;
			if(data != undefined  && data != null && data !='' && data.indexOf('1')>0){
				return true;
			}
			alert('Sorry lost session please login again!');
			ACT.go("../../login.htm");
		}	
};

ACT =
{
	getParameter: function ( name )
	{
		name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
		
		var regexS = "[\\?&]"+name+"=([^&#]*)";
		var regex = new RegExp( regexS );
		var results = regex.exec( window.location.href );
		
		if( results == null )
		{
			return "";
		} 
		else 
		{
			return results[1];
		}
	},
	
	getToken : function()
	{
		return (new Date()).getTime() + '';
	},
	
	checkAll : function(item)
	{
		jQuery("." + jQuery(item).attr("id") + "s").each(
			function()
			{
				jQuery(this).attr({checked : jQuery(item).attr("checked")});
			}
		);
	},
	
	checkAllByClass : function(item)
	{
		jQuery("." + item.id + "s").each(
			function()
			{
				this.checked = item.checked;
			}
		);
	},
	
	setValue : function(item, val)
	{
		if(val!=null && val!="")
		{
			jQuery(item).val(val);
		}
	},
	
	openWindow : function(filename, winname, width, height, feature)
	{
		var features, top, left;
		var reOpera = /opera/i ;
		var winnameRequired = ((navigator.appName == "Netscape" && parseInt(navigator.appVersion) == 4) || reOpera.test(navigator.userAgent));
		
		left = (window.screen.width - width) / 2;
		top = (window.screen.height - height) / 2;
		
		if(feature == '')
			features = "width=" + width + ",height=" + height + ",top=" + top + ",left=" + left + ",status=false,location=false,resizable=false,menubar=false";
		else
			features = "width=" + width + ",height=" + height + ",top=" + top + ",left=" + left + "," + feature;
		
		newWindow = window.open(filename,winname,features);
		newWindow.focus();
		return newWindow;
	},
	
	go : function(link)
	{
		if(opener!=undefined) {
			jQuery(opener.location).attr({href : link});
		} else {
			jQuery(location).attr({href : link});
		}
	},
	
	alert : function(msg)
	{
		alert(msg);
	},
	
	preloadImages : function()
	{
		imgs = new Array();
		for(var i = 0; i<arguments.length; i++)
		{
			var img = new Image();
			img.src = arguments[i];
			imgs[imgs.length] = img;
		}
	},
	
	setUTF8 : function()
	{
		var charset = (typeof document.charset == "undefined" || document.charset == null) ? document.characterSet : document.charset;
		if( charset != "UTF-8" && charset != "utf-8" ) 
		{
			try 
			{
				if(typeof document.charset == "undefined" ) 
					document.characterSet = "UTF-8"; //FF, hopefully someday FF allows it
				else
					document.charset = "UTF-8"; //IE
			} catch (e) {}
			
		}
	},
	
	getClientSize : function()
	{
		var dimensions = {width: 0, height: 0};
	     if (document.documentElement) {
	         dimensions.width = document.documentElement.offsetWidth;
	         dimensions.height = document.documentElement.offsetHeight;
	     } else if (window.innerWidth && window.innerHeight) {
	         dimensions.width = window.innerWidth;
	         dimensions.height = window.innerHeight;
	     }
	     return dimensions;
	},
	
	setFullHeight : function(container, deltaHeight)
	{
		var clientSize = ACT.getClientSize();
		var pageHeight = clientSize.height - deltaHeight;
		
		if( pageHeight > jQuery(container).outerHeight())
		{
			jQuery(container).height(pageHeight);
			
			return true;
		}
		
		return false;
	},
	setFullHeightWithAuto : function(container, deltaHeight)
	{
		if(jQuery.browser.msie && jQuery.browser.version=="6.0")
		{
			deltaHeight += 4;
		}
		
		if(!ACT.setFullHeight(container, deltaHeight))
		{
			jQuery(container).height("auto");
			ACT.setFullHeight(container, deltaHeight);
		}
	},
	
	checkLogin : function(goLogin)
	{
		var data = jQuery.ajax(
		{
			type: "POST"
			, url: "ajax.check.html"
			, data: "ajax=1&action=checkLogin"
			, async: false
			, cache: false
		}).responseText;
		
		switch(data)
		{
			case "1":
				return true;
				break;
			case "0":
			default:
				if (goLogin && confirm("Do you want to login?"))
				{
					var link = "login.html";
					if(opener==null) {
						link += "?returnUrl=" + escape(jQuery(location).attr("href"));
					}
					ACT.go(link);
				}
				else
				{
					return false;
				}
				break;
		}
	},
	
	insertAtCursor: function(field, value) {
		tinyMCE.execCommand('mceInsertContent', false, value);
	},
	
	transPNG : function(items) 
	{
		// Fix background PNGs in IE6
		if (navigator.appVersion.match(/MSIE [0-6]\./)) 
		{
			jQuery(items).each(
				function () 
				{
					if (this.currentStyle.backgroundImage != 'none') 
					{
						var image = this.currentStyle.backgroundImage;
						image = this.currentStyle.backgroundImage.substring(5, image.length - 2);
						jQuery(this).css(
						{
							'backgroundImage': 'none',
							'filter': "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=crop, src='" + image + "')"
						});
					}
				}
			);
		}
	},
	
	refreshPage : function()
	{
		self.location.href=self.location.href;
	}
};
ACT.setUTF8();

jQuery.timer = function (interval, callback)
{
/**
 *
 * timer() provides a cleaner way to handle intervals  
 *
 *	@usage
 * $.timer(interval, callback);
 *
 *
 * @example
 * $.timer(1000, function (timer) {
 * 	alert("hello");
 * 	timer.stop();
 * });
 * @desc Show an alert box after 1 second and stop
 * 
 * @example
 * var second = false;
 *	$.timer(1000, function (timer) {
 *		if (!second) {
 *			alert('First time!');
 *			second = true;
 *			timer.reset(3000);
 *		}
 *		else {
 *			alert('Second time');
 *			timer.stop();
 *		}
 *	});
 * @desc Show an alert box after 1 second and show another after 3 seconds
 *
 * 
 */

	var interval = interval || 100;

	if (!callback)
		return false;
	
	_timer = function (interval, callback) {
		this.stop = function () {
			clearInterval(self.id);
		};
		
		this.internalCallback = function () {
			callback(self);
		};
		
		this.reset = function (val) {
			if (self.id)
				clearInterval(self.id);
			
			var val = val || 100;
			this.id = setInterval(this.internalCallback, val);
		};
		
		this.interval = interval;
		this.id = setInterval(this.internalCallback, this.interval);
		
		var self = this;
	};
	
	return new _timer(interval, callback);
};