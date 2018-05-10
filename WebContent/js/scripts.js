
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
		
		$(".allKeywords").css('display', 'block');
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");

		cargarCategorias();
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

/*function checkClients(id_client){
	$.post('Data', {
		metodo : ckc, 
		id_client : id_client,
	}, function(responseText) {
		$('#lstC').html(responseText);
	});
}*/

function selectClient(id_client) {
	
	var id_cliente_anterior = $("#lstC .item_select").attr("id");
	if(id_cliente_anterior == undefined){id_cliente_anterior = -1;}
	alert(id_cliente_anterior);
	
	//revisamos si el cliente seleccionado esta disponible
	$.post('Data', {
		metodo : "ckc",
		id_client : id_client,
		id_cliente_anterior : id_cliente_anterior
	}, function(responseText) {
		
		var r = responseText.length;
		if(r == 0){
			alert("Esta bloqueado");
		}else{
			//si el cliente selecionado esta disponible 
			$('#uniqueClient').html(responseText);
		}
		
		//Comprobamos otra vez la disponibiladad de todos los clientes
		$.post('Data', {
			metodo : "ckcs",
			id_client : id_client,
		}, function(responseText) {
			//actualizamos la lsita de clientes 
			$('#lstC').html(responseText);
			changeThemeC(id_client);
		});
		
	});
	
	/*
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
	*/
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
	
	$("#lkk #"+id).addClass("item_select");
	$("#lkk #"+id+" .line").addClass("line_select");
	$("#lkk #"+id+" .nameItem, "+"#"+id+" .dominioItem").addClass("nameItem_select");

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
		metodo : 'scs',
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
	
	$(".rotArrow").removeClass("rotArrow");
	
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $("#selCat_"+id).attr("class");
	if(clase.includes("visible")){
		
		$(".slCt").removeClass("visible");
		$(".slWeb").removeClass("visible");
		
		
	}else{
		//cerrar los anteriores
		$(".slCt").removeClass("visible");
		$(".slWeb").removeClass("visible");
		//-------------------------------
		$("#selCat_"+id).addClass("visible");
		
		//modificamos la flecha 
		$("#dvCat_"+id+" .arrow").addClass("rotArrow");
	}
	
	
	
	
}
function selectWeb(id_tr){
	
	$(".rotArrow").removeClass("rotArrow");
	
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $("#selWeb_"+id_tr).attr("class");
	if(clase.includes("visible")){
		
		$(".slCt").removeClass("visible");
		$(".slWeb").removeClass("visible");
		
		
	}else{
		
		//cerrar los anteriores
		$(".slWeb").removeClass("visible");
		$(".slCt").removeClass("visible");
		//-------------------------------
		
		var id_categoria = $("#selCat_"+id_tr+" .liActive").attr('id');;
		cargarWebs(id_categoria,id_tr);
		
		
	}
	
	
	
}
function cargarWebs(id_categoria,id_tr){
	var posArryC = $("#lstC .item_select").attr('id');
	
	$.post('Data', {
		metodo : 'cwbs',
		id_categoria: id_categoria,
		id_tr: id_tr,
		posArryC : posArryC
	}, function(responseText) {
		$('#tdWeb_'+id_tr+" ul").html(responseText);
		$("#selWeb_"+id_tr).addClass("visible");
		//modificamos la flecha 
		$("#dvWeb_"+id_tr+" .arrow").addClass("rotArrow");
		
	});
	
}





//Seleccionamos la categoria de cada enlace
function liSelectCat(id_categoria,id_tr){
	
	$("#selCat_"+id_tr+" li").removeClass("liActive");
	$("#selCat_"+id_tr+" > #"+id_categoria).addClass("liActive");
	$("#selCat_"+id_tr).removeClass("visible");
	
	//le damos el texto seleccionado a nuestra vista de categorias
	var op = $("#selCat_"+id_tr+" > #"+id_categoria).text();
	$("#spCat_"+id_tr).text(op);
	
	guardarCategoria(id_categoria, id_tr);
}
function guardarCategoria(id_categoria, id_tr){
	var posArryC = $("#lstC .item_select").attr('id');
	$.post('Data', {
		metodo : 'sc',
		id_categoria: id_categoria,
		posArryC: posArryC,
		id_tr: id_tr
	});
}

//-------------------------------------------------------------------------



function liSelectWeb(pArrayF,id_tr){
	$("#selWeb_"+id_tr+" li").removeClass("liActive");
	$("#selWeb_"+id_tr+" > #"+pArrayF).addClass("liActive");
	$("#selWeb_"+id_tr).removeClass("visible");
	
	//var op = $("#selWeb_"+id_tr+" > #"+pArrayF).text();
	//$("#spWeb_"+id_tr).text(op);
	
	guardarWeb(pArrayF, id_tr);
}

//guardamos en la bbdd y en el array la web selecionada
function guardarWeb(pArrayF, id_tr){
	var posArryC = $("#lstC .item_select").attr('id');
	
	//cogemos el elemento que ya esta seleccionado para proceder a quitarlo de la lista de los foros utilizados para este cliente
	var elemtAnterior = $("#spWeb_"+id_tr).attr("mweb");
	
	$.post('Data', {
		metodo : 'sw',
		pArrayF: pArrayF,
		posArryC: posArryC,
		id_tr: id_tr,
		elemtAnterior: elemtAnterior
	}, function(responseText) {
		$('#dvWeb_'+id_tr).html(responseText);
		
	});
	
}

//Hide the menus if visible
window.addEventListener('click', function(e){  
	
	var clase = e.target.className;
	
	if (clase.includes("tdCat")){
		//si entra en este if significa que hemos hecho click en la categoria
	}else{
		$(".rotArrow").removeClass("rotArrow");
		$(".slCt").removeClass("visible");
	}
});


//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	
	
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();
	var id = $("#lstC .item_select").attr('id');
	
	//limpiar cajas categoria
	//$(".slCt").html("");
	//Limpiamos cajas de webs
	//$(".slWeb").html("");
	
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

function sizeHeader(){
	
	var wLink = $("#tClientes tbody .cLink").css("width");
	var wCate = $("#tClientes tbody .cCateg").css("width");
	var wWeb = $("#tClientes tbody .cWeb").css("width");
	var wDest = $("#tClientes tbody .cDest").css("width");
	var wTipo = $("#tClientes tbody .cTipo").css("width");
	
	$("#tClientes thead .cLink").css("width", wLink);
	$("#tClientes thead .cCateg").css("width", wCate);
	$("#tClientes thead .cWeb").css("width", wWeb);
	$("#tClientes thead .cDest").css("width", wDest);
	$("#tClientes thead .cTipo").css("width", wTipo);
	
}
$(document).ready(function(){
    $(window).resize(function(){
    	$("span").stop();
    	sizeHeader()
    });
});


function viewAll(x){
	var w = $(x).css("width").replace("px","");
	var recorrido = w-246;
	if(w > 274){
		$(x).animate({
			overflow: "visible",
			marginLeft: "-"+recorrido+"px",
		  }, 3000, function() {
			  	$(x).removeAttr("style");
		  });
	}
}


function viewCampo(x){
	
	var superw = $(x).parents().width();
	var w = $(x).width();
	
	var recorrido = w-superw+40;
	
	if(recorrido>0){
		$(x).animate({
			marginLeft: "-"+recorrido+"px",
		  }, 3000, function() {
			  	$(x).removeAttr("style");
		  });
	}
	
}

function openUrl(x, event){
	
	event.stopPropagation();
	var url = $(x).text();
	
	if(!url.startsWith("http"))
		url = "http://"+url;
	
	var win = window.open(url, '_blank');
	win.focus();
	
}

function updateLink(x){
	alert("Modificando link..")
}


//Dani------------------
function cargarCategorias(){
	$.post('Data', {
		metodo : 'cats'
	}, function(responseText) {
		$('#lkk ').html(responseText);
	});
}

function selectCategoria(id) {
	changeThemeK(id);
	
	//pintamos las busquedas de la keyword selecionada
	
	$.post('Data', {
		metodo : 'selectCat', 
		id : id
	}, function(responseText) {
		$('#kywData').html(responseText);
	});
	
}

function selectTematica(id){

	//cerrar los anteriores
	$(".slCt").removeClass("visible");
	
	//-------------------------------
	$("#selTem_"+id).addClass("visible");
	
	$(".pr").removeClass("pr");
	$("#td_"+id).addClass("pr");
	
}

//para que funcione el checkbox de "seleccionar todas" en las tematicas
function selectAll(source,id_sp) {
	
	var thisInput = $(source).is(":checked")
	var checkboxes = $('#selTem_'+id_sp+' li input[type="checkbox"]');
	if(thisInput){
	    for (var i = 0; i < checkboxes.length; i++) {
	        if (checkboxes[i] != source){
	        	checkboxes[i].checked = source.checked;
	        	$("#spTem_"+id_sp).text("Todas");  
	        }
	            
	    }
	}else{
		//vaciar
	    for (var i = 0; i < checkboxes.length; i++) {
	        checkboxes[i].checked = source.checked;
	        $("#spTem_"+id_sp).text("Selecciona temática");  
	            
	    }
	}
	
    
      
}

//para seleccionar la tematica del foro
function liSelectTem(id,id_ul){
	//alert("DENTROv");
	var t = "";
	$("#selTem_"+id_ul+" li input:checked").each(function(){t += ($(this).attr("id"))+" "});
	
	if (t == ""){
		t = "Selecciona tematica";
	}
	$("#spTem_"+id_ul).text(t);
	//$("#selTem_21 li input:checked").each(function(){console.log($(this).attr("id"))})
	//alert("BIEN");
}

//---------------------------





















