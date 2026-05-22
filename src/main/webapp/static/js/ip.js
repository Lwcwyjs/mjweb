/**
 * 函数名: chkIP 功 能: 校验IP地址是否合法,并检查IP地址类型. 参 数: ###.###.###.### 格式的IP字符串 返回值:
 * 整数,意义见注释 日 期: 2006-7-19 作 者: 梅雪香(meixx)
 */
function chkIP(ipStr){
	// 参数格式校验 成功继续,失败返回-1
	ipStr = ipStr.replace(/\s/g, "");
	var   reg = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
	if(reg.test(ipStr) == false){
		return   -1;
	}
	// ip地址合法性校验 成功继续 ,失败返回-2
	var   arr = ipStr.split( ".");
	for(var i=0;i <4;i++){
		arr[i] = parseInt(arr[i],10);
		if(parseInt(arr[i],10)>255)
		return   -2;
	}
	var   ip = arr.join( ".");
	// 返回IP地址的类型 包括:
	// 异常:0 A类:1 B类:2 C类:3 D类:4 E类:5 A类私有:6 B类私有:7 C类私有:8 本机IP:9 广播地址:10
	// A类子网掩码:11 B类子网掩码:12 C类子网掩码:13
	var   retVal = 0;
	var   n = arr[0];
	
	if(ip == "255.255.255.255") retVal = 10;
	else if(ip == "255.255.255.0") retVal = 13;
	else if(ip == "255.255.0.0") retVal = 12;
	else if(ip == "255.0.0.0") retVal = 11;
	else if(ip == "0.0.0.0" || ip == "127.0.0.1") retVal = 9;
	else if(n <= 126) retVal = (n == 10  ?  6 : 1);
	else if(n <= 191) retVal = (n == 172 ?  7 : 2);
	else if(n <= 223) retVal = (n == 192 ?  8 : 3);
	else if(n <= 239) retVal = 4;
	else if(n <= 255) retVal = 5;
	
	return retVal;
}