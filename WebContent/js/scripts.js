
var c = 0;

function changePanel(id) {
	
	
	
	
	$(".allClients").css('display', "none");
	$(".allKeywords").css('display', "none");

	$(".circleIcono").removeClass("btnSelected");
	$(".circleIcono i").removeClass("btnSelected");

	if(id == "btnClientes"){
		$(".allClients").css('display', "block");
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
	}else{
		
		if(c==1){
			$(".allKeywords").css('display', 'block');
			$("#"+id).addClass("btnSelected");
			$("#"+id+" i").addClass("btnSelected");
		}else{
			$(".allClients").css('display', "block");
			$("#btnClientes").addClass("btnSelected");
			$("#"+"btnClientes"+" i").addClass("btnSelected");
			cambiarInstruccion();
		}
		
		
	} 
}

function abrirBox(id) {
	
	var idDiv= 'div#'+id+'_plus';
	$(idDiv).toggleClass('open');
	/*var w_position=0;
	var w_url=0;
	var padding_position=15;
	var padding_url = 35;
	w_itmTable = $( ".itemTable" ).width();
	w_date = $( ".dateKey" ).width();
	//$( ".urlKey" ).width();
	alert(w_date);
	$( ".box" ).width(w_itmTable - w_date);
	$( ".box" ).css('margin-left', "-15px");
	*/
	//alert("Hola");

}

/*$(window).on('resize', function(){
	var w_position=0;
	var w_url=0;
	var padding_position=15;
	var padding_url = 35;
	w_position = $( ".posiKey" ).width();
	w_date = $( ".dateKey" ).width();
	//$( ".urlKey" ).width();
	alert(w_date);
	$( ".box" ).width(calc(100% - w_date));
	$( ".box" ).css('margin-left', "-15px");
});*/

function selectClient(id, metodo) {
	changeThemeC(id);
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();
	$(".datedropper").remove();
	
	//pintamos las keywords del cliente seleccionado
	
	$.post('Data', {
		metodo : metodo, 
		posicion : id,
		mes: mes,
		year: year
	}, function(responseText) {
		$('#uniqueClient').html(responseText);
	});

	
	c = 1;
	
}

function changeThemeC(id){
	
	$("#lstC .item").removeClass("item_select");
	$("#lstC .item .line").removeClass("line_select");
	$("#lstC .item .nameItem,  #lstC .item .dominioItem").removeClass("nameItem_select");
	
	$("#"+id).addClass("item_select");
	$("#"+id+" .line").addClass("line_select");
	$("#"+id+" .nameItem, "+"#"+id+" .dominioItem").addClass("nameItem_select");
	
}

function changeThemeK(id){
	
	$("#lkk .item").removeClass("item_select");
	$("#lkk .item .line").removeClass("line_select");
	$("#lkk .item .nameItem,  #lkk .item .dominioItem").removeClass("nameItem_select");
	
	$("#"+id).addClass("item_select");
	$("#"+id+" .line").addClass("line_select");
	$("#"+id+" .nameItem, "+"#"+id+" .dominioItem").addClass("nameItem_select");

}

function selectKeyword(id, metodo) {
	
	changeThemeK(id);
	
	//pintamos las busquedas de la keyword selecionada
	$.post('Data', {metodo : metodo, id : id
	}, function(responseText) {
		$('#kywData').html(responseText);
	});
	
}

function searchK() {
	
	var k = $('#searchK').val();
	var p = $( ".item_select" ).attr('id');
	
	$.post('Data', {
		metodo : 'sk',
		posicion: p,
		keyword: k
	}, function(responseText) {
		$('#lkkI').html(responseText);
	});
	
}
function setK() {
	var elements = $( "#lkkI .item" ).toArray();
	$( "#lkk .info" ).text(elements.length+" keywords");
}

function searchKey(e) {
	
	var element = event.target.nodeName;
	
	
	if(element!="INPUT"){
		var ti = $( "#ipkey .addKi" ).text();
		if(ti=="search"){
			$("#searchK" ).addClass("vv");
			
			$( "#ipkey" ).addClass("ms");
			$( "#addK" ).css('display', "none");
			$("#ipkey .addKi").text("clear");
			$("#searchK" ).autofocus;
			
			$("#searchK" ).focus();
		}else{
			$( "#searchK" ).removeClass("vv");
			$( "#ipkey" ).removeClass("ms");
			$("#searchK").val("");
			$( "#addK" ).css('display', "inline-block");
			$("#ipkey .addKi").text("search");
			searchK();
		}
	}

}

function searchCliente(e) {
	
	var element = event.target.nodeName;
	
	if(element!="INPUT"){
		var ti = $( "#ipCLient .addKi" ).text();
		if(ti=="search"){
			$("#searchC" ).addClass("vv");
			
			$( "#ipCLient" ).addClass("ms");
			$( "#addC" ).css('display', "none");
			$("#ipCLient .addKi").text("clear");
			$("#searchC" ).autofocus;
			
			$("#searchC" ).focus();
		}else{
			$( "#searchC" ).removeClass("vv");
			$( "#ipCLient" ).removeClass("ms");
			$("#searchC").val("");
			$( "#addC" ).css('display', "inline-block");
			$("#ipCLient .addKi").text("search");
			searchC();
		}
	}
}

function searchC() {
	
	var k = $('#searchC').val();
	
	$.post('Data', {
		metodo : 'sc',
		keyword: k
	}, function(responseText) {
		$('#lstC').html(responseText);
	});
	
}

function setC() {
	var elements = $( "#lstC .item" ).toArray();
	$( "#lcc .info" ).text(elements.length+" clientes");
}



function cambiarInstruccion(){
	
	$(".msg").text("");
	$(".ityped-cursor").text("");
	
	window.ityped.init(document.querySelector('.msg'),{
		strings : ['He dicho que seleciones un cliente ¬¬'],
		disableBackTyping: true,
		loop : false
	})
	
}

function verify(){
	
	/*var u = $('#username').val();
	var p = $('#password').val();
	
	//alert(p);
	$.post('Login', {
		user : u,
		password: p
	}, function(responseText) {
		$('#lstC').html(responseText);
	});
	*/
	document.login.submit();
}


// funciones de la nueva aplicacion

function selectCategory(id){
	//cerrar los anteriores
	$(".slCt").removeClass("visible");
	//-------------------------------
	$("#selCat_"+id).addClass("visible");
}

//Seleccionamos la categoria de cada enlace
function liSelectCat(id,id_ul){
	
	$("#selCat_"+id_ul+" li").removeClass("liActive");
	$("#selCat_"+id_ul+" > #"+id).addClass("liActive");
	$("#selCat_"+id_ul).removeClass("visible");
	
	//le damos el texto seleccionado a nuestra vista de categorias
	var op = $("#selCat_"+id_ul+" > #"+id).text();
	$("#spCat_"+id_ul).text(op);
	
	
	
}

//Hide the menus if visible
window.addEventListener('click', function(e){  
	
	var clase = e.target.className;
	
	if (clase.includes("tdCat")){
		//si entra en este if significa que hemos hecho click en la categoria
	}else{
		 $(".slCt").removeClass("visible");
	}
});


//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();
	var id = $("#lstC .item_select").attr('id');
	
	//chv = change view
	$.post('Data', {
		metodo : 'chv',
		mes: mes,
		year: year,
		id: id
	}, function(responseText) {
		$('#results_Client table tbody').html(responseText);
	});
	
}

































