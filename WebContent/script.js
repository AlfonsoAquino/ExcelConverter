/**
 * 
 */
function send() {
	if (document.getElementById("starter").files.length != 0 && document.getElementById("model").files.length != 0) {
		document.getElementById("formSub").submit();
		return true;
	}
	return false;
}
function modelConfirm(){	
	
	return true;
}