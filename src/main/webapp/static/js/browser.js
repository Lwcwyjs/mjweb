/**
 * 
 */

	function IE(){
		if ((navigator.userAgent.indexOf('MSIE') >= 0)&& (navigator.userAgent.indexOf('Opera') < 0)){
			var b_version=navigator.appVersion;
			//alert(b_version);
			var version=b_version.split(";"); 
			var trim_Version=version[1].replace(/[ ]/g,""); 
			if(trim_Version=="MSIE6.0"){
				//alert('你是使用ie6')
				return 6;
			}else if(trim_Version=="MSIE7.0"){
				//alert('你是使用ie7')
				return 7;
			}else if(trim_Version=="MSIE8.0"){
				//alert('你是使用ie8')
				return 8;
			}else if(trim_Version=="MSIE9.0"){
				//alert('你是使用ie9')
				return 9;
			}else if(trim_Version=="MSIE10.0"){
				//alert('你是使用ie10')
				return 10;
			}else if(trim_Version=="MSIE11.0"){
				//alert('你是使用ie11')
				return 11;
			}
		}else if (navigator.userAgent.indexOf('Firefox') >= 0){
			    //alert('你是使用Firefox');
			    return 20;
		}else if (navigator.userAgent.indexOf('Opera') >= 0){
			    //alert('你是使用Opera');
			    return 30;
		}else if (navigator.userAgent.indexOf('Chrome') >= 0){
		    	//alert('你是使用Chrome');
		    	return 40;
		}else{
			    //alert(navigator.userAgent);
			    return 50;
		}
	}
	
	function isie(){
		var b=false;
        if (!!window.ActiveXObject || "ActiveXObject" in window)
        	b = true;
        else
        	b = false;
        
		return b || (IE()<20);
	}