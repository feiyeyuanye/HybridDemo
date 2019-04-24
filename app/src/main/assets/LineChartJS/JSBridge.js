var callbacks=new Array();

function jsCallAndroid(obj,method,params,callback){
	var port=callbacks.length;
	callbacks[port]=callback;
	var url='JSBridge://'+obj+":"+port+"/"+method+"?"+JSON.stringify(params);
//	组合出符合规则的url，并传递给java层
	prompt(url);
}
function onAndroidFinished(port,jsonObj){
//  从callbacks中取出对应的回调函数
	var callback=callbacks[port];
	callback(jsonObj);
//	从callbacks中删除callback
	delete callback[port];
}
