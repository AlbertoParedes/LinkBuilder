/*var inputElement = document.getElementById("excelFile");

inputElement.onchange = function(event) {
	var target = event.target || event.srcElement;
	if (target.value.length == 0) {
		var name = document.search.nombre;
		name.value = "";
		$('.upload').removeClass("updated");
		$('#fileUploaded').html('');
	}else {
		var element = document.getElementById("excelFile");
		var file = element.files[0];
		var name = document.search.nombre;
		name.value = file.name;
		$('.upload').addClass("updated");
		$('#fileUploaded').html(name.value);
		//alert($('#excelFile').val());
		//alert(name.value);
	}
}

var name = document.search.nombre;
if(name.value == undefined){
	$('.upload').removeClass("updated");
	$('#fileUploaded').html('');
}


function searchBt() {
	$('#loadindPage').addClass("visible");
	$('#mainInicio').addClass("mainInicio");
	document.search.submit();
}

var input = document.getElementById("keyword");
input.addEventListener("keyup", function(event) {
    event.preventDefault();
    if (event.keyCode === 13) {
    	$('#loadindPage').addClass("visible");
    	$('#mainInicio').addClass("mainInicio");
    	document.search.submit();
    }
});

var input = document.getElementById("urlClient");
input.addEventListener("keyup", function(event) {
    event.preventDefault();
    if (event.keyCode === 13) {
    	$('#loadindPage').addClass("visible");
    	$('#mainInicio').addClass("mainInicio");
    	document.search.submit();
    }
});
*/


