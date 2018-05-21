
var websInicio=false;
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
		
		if(websInicio==false){
			cargarCategorias();
		}
		
	} 
}

function abrirBox(id) {
	var idDiv= 'div#'+id+'_plus';
	$(idDiv).toggleClass('open');
}

function selectClient(id_client) {
	
	
	//revisamos si el cliente seleccionado esta disponible
	$.post('Data', {
		metodo : "ckc",
		id_client : id_client
	}, function(responseText) {
		
		var r = responseText.length;
		if(r == 0){
			alert("Esta bloqueado");
		}else{
			//si el cliente selecionado esta disponible 
			$('#uniqueClient').html(responseText);
			changeThemeC(id_client);
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

function verify(){
	document.login.submit();
}
function handleEnter(e){
    var keycode = (e.keyCode ? e.keyCode : e.which);
    if (keycode == '13') {
    	verify();
    }
}


// funciones de la nueva aplicacio
// Resultados webs ----------------------------------------------------------
function openWebResultado(x){
	$(".rotArrow").removeClass("rotArrow");
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).closest('td').children('ul').attr("class");
	$(".slCt").removeClass("visible");
	$(".slWeb").removeClass("visible");
	if(!clase.includes("visible")){
		var id_resultado = $(x).closest('tr').attr('id');
		var id_categoria = $(x).closest('tr').children('td.cCateg').children('ul').children('li.liActive').attr('id');
		if (typeof id_categoria !== 'undefined'){
			$.post('Data', {
				metodo : 'mostrarWebResultado',
				id_categoria: id_categoria,
				id_resultado: id_resultado
			}, function(responseText) {
				$(x).children('ul').html(responseText);
				$(x).children('ul').addClass("visible");
				//modificamos la flecha 
				$(x).children('div').children('i').addClass("rotArrow");
			});
		}
		
	}
}
function guardarWebResultado(x){
	$(x).closest('ul').children('li').removeClass("liActive");
	$(x).addClass("liActive");
	
	var id_foro=$(x).attr('id');
	var id_resultado=$(x).closest('tr').attr('id');
	var elemtAnterior = $(x).closest('td').children('div').children('span').attr("mweb");
	var posicionResultado = $(x).closest('tr').attr('posicion');
	var posicionForo = $(x).attr('posicion');
	
	$.post('Data', {
		metodo : 'guardarWebResultado',
		id_foro: id_foro,
		id_resultado: id_resultado,
		elemtAnterior: elemtAnterior,
		posicionResultado:posicionResultado,
		posicionForo:posicionForo
	}, function(responseText) {
		$(x).closest('td').children('div').html(responseText);
		
	});
}


function openCategoriaResultado(x){
	$(".rotArrow").removeClass("rotArrow");
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).children('ul').attr("class");
	$(".slCt").removeClass("visible");
	$(".slWeb").removeClass("visible");
	if(!clase.includes("visible")){
		$(x).children('ul').addClass("visible");
		$(x).children('div').children('i').addClass("rotArrow");
	}
}
function guardarCategoriaResultado(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var posicion = $(x).closest('tr').attr("posicion");
	var id_categoria = $(x).attr("id");
	
	$(x).closest('ul').children('li').removeClass("liActive");
	$(x).addClass("liActive");
	$(x).closest('ul').removeClass("visible");
	
	//le damos el texto seleccionado a nuestra vista de categorias
	var op = $(x).text();
	$(x).closest('td').children('div').children('span').text(op);
	
	$.post('Data', {
		metodo : 'guardarCategoriaResultado',
		id_categoria: id_categoria,
		id_cliente: id_cliente,
		id_resultado: id_resultado,
		posicion: posicion
	});
}
//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();

	$.post('Data', {
		metodo : 'chv',
		mes: mes,
		year: year,
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

var timer;
function viewCampo(x){
	timer = setTimeout(function () {
		var superw = $(x).parents().width();
		var w = $(x).width();
		var recorrido = w-superw+40;
		var time = 1000;
		if(recorrido>100 && recorrido<200){time = 2000;}
		else if(recorrido>=200 && recorrido<300){time = 3000;}
		else if(recorrido>=300 && recorrido<400){time = 10000;}
		else if(recorrido>=400 && recorrido<800){time = 15000;}
		else if(recorrido>=800){time = 30000;}

		if(recorrido>0){
			$(x).animate({
				marginLeft: "-"+recorrido+"px",
			  }, time,"linear", function() {
				  	$(x).removeAttr("style");
			  });
		}
    }, 500);
}

function restartCampo(x){
	clearTimeout(timer);
	$(x).stop();
	$(x).removeAttr("style");
}

function openUrl(x, event){
	event.stopPropagation();
	var url = $(x).text();
	if(!url.startsWith("http")) url = "http://"+url;
	var win = window.open(url, '_blank');
	win.focus();
}

function guardarEnlaceResultado(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var posicion = $(x).closest('tr').attr("posicion");
	var link = $(x).val();
	var tipo = $("tr#"+id_resultado+" .cTipo").attr("tipo");

	$.post('Data', {
		metodo : 'guardarEnlaceResultado',
		id_resultado: id_resultado,
		posicion: posicion,
		link: link,
		tipo: tipo
	}, function(responseText) {
		$('#lstC .item_select .itemChild').html(responseText);
		$("#cGuardar").removeClass('cSave');
		var tipo = $(x).parent().parent().children().children('.divStatus');
		tipo.removeClass("sOK").removeClass("sPendiente");
		if(link.trim()!=""){
			tipo.addClass("sOK");
		}else{
			tipo.addClass("sPendiente");
		}
	});
}

function updateDestino(x){
	var id_resultado = $(x).parent().parent().attr("id");
	var destino = $(x).val();
	
	$.post('Data', {
		metodo : 'chd',
		id_resultado: id_resultado,
		destino: destino,
	}, function (){
		//quitamos la clase para que parezca que guardamos los datos
		$("#cGuardar").removeClass('cSave');
	});
}

function updateAnchor(x){
	var id_resultado = $(x).parent().parent().attr("id");
	var anchor = $(x).val();
	
	$.post('Data', {
		metodo : 'cha',
		id_resultado: id_resultado,
		anchor: anchor,
	}, function (){
		//quitamos la clase para que parezca que guardamos los datos
		$("#cGuardar").removeClass('cSave');
	});
}


function saveClient(){
	var clase_btn_guardar = $("#cGuardar").attr('class');
	
	if(!clase_btn_guardar.includes("cSave"))
		$("#cGuardar").addClass('cSave');
}

//*****************  DANI  ********************
//--CARGAR CATEGORIAS---------------
function cargarCategorias(){
	$.post('Data', {
		metodo : 'cats'
	}, function(responseText) {
		$('#lkk ').html(responseText);
		websInicio = true;
	});
}
function selectCategoria(x) {
	var id_categoria = $(x).attr('id');
	var posicion = $(x).attr('posicion');
	changeThemeK(id_categoria);
	//pintamos las busquedas de la categoria selecionada	
	$.post('Data', {
		metodo : 'selectCat', 
		id_categoria : id_categoria,
		posicion: posicion
	}, function(responseText) {
		$('#kywData').html(responseText);
	});	
}
//--CAMPO WEB--------------------------------------------
function guardarWebForo(x){
	var campo = "web_foro";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = $ (x).val();
	guardarForo(campo,id_foro,valor);
}

//--CAMPO TIPO-------------------------------------------
function openTipo(x){
	$(".rotArrow").removeClass("rotArrow");
	$(".slCt").removeClass("visible");
	var ul = $(x).children('ul');
	$(ul).addClass('visible');	
	
	if($(ul).attr('class').includes("nuevaWeb")){$(ul).css("width",$(x).css("width"))}
}
function guardarTipo(x){
	var campo = "tipo";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = $ (x).attr('tipo');
	
	var clase = "";
	if(valor=="follow") clase = "lf";
	else if(valor=="nofollow")clase ="lnf";
	
	var i = $(x).closest("td").children("i");
	$(i).attr("tipo" ,valor);
	$(i).removeClass("lf").removeClass("lnf").addClass(clase);
	
	if(!$(x).closest('ul').attr('class').includes('nuevaWeb'))
		guardarForo(campo,id_foro,valor);
	
}
//--CAMPO DR----------------------------------------------
function guardarDR(x){
	var campo = "DR";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = $ (x).val();
	guardarForo(campo,id_foro,valor);
}
//--CAMPO DA----------------------------------------------
function guardarDA(x){
	var campo = "DA";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = $ (x).val();
	guardarForo(campo,id_foro,valor);
}
//--CAMPO TEMATICA----------------------------------------
function openTematica(x){
	$(".rotArrow").removeClass("rotArrow");
	var ul = $(x).children('ul');
	// si el ul ya es visible lo cerramos
	if($(ul).attr('class').includes("visible")){
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$(".slCt").removeClass("visible");
		$(x).children('div').children('i.arrow').addClass('rotArrow');
		
		$(ul).addClass("visible");
		//
		var elementos = $(ul).children('div.nameTem').children('input:checked').length;
		var longitud = $(ul).attr('size');
		
		if(longitud==elementos){
			$(ul).children('div').children('input#all').prop('checked', true);
		}else if(elementos<longitud){
			$(ul).children('div').children('input#all').prop('checked', false);
		}
	}
	if($(ul).attr('class').includes("nuevaWeb")){$(ul).css("width",$(x).css("width"))}
}
function guardarTematica(x){
	
	var ul = $(x).closest('ul');
	$(ul).removeClass("visible");
	if($(x).attr('id')=="all"){
		//seleccionamos o deseleccionamos todas
		$(ul).children('div.nameTem').children('input').each(function(){
			$(this).prop('checked', $(x).is(':checked'));
		});
	}
	
	var campo = "tematica";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = "";

	//recorremos todos los inputs checked
	var elementos = 0;
	var longitud = $(ul).attr('size');
	$(ul).children('div.nameTem').children('input:checked').each(function(){
		elementos+=1;
		valor = valor + $(this).attr('tematica')+", ";
	});
	valor = valor.substring(0, valor.lastIndexOf(", "));
	if(elementos==longitud){$(x).closest('td').children('div').children('span').text('Todas');}
	else $(x).closest('td').children('div').children('span').text(valor);
	
	if(!$(x).closest('ul').attr('class').includes('nuevaWeb'))
		guardarForo(campo,id_foro,valor);
}


//--CAMPO REUTILIZABLE----------------------------------
function openReutilizable(x){
	
	$(".rotArrow").removeClass("rotArrow");
	var ul = $(x).children('ul');
	
	if($(ul).attr('class').includes("visible")){
		//cerrar las ventanitas que esten abiertas
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$(".slCt").removeClass("visible");
		$(x).children('div').children('i.arrow').addClass('rotArrow');
		$(ul).addClass('visible');
		$(ul).children('li').removeClass('liActive');
		var spanText =  $(x).children('div').children('span').text();
		var opcion="null";
		$(ul).children('li').each(function(){
			if($(this).text()==spanText)opcion = $(this);
		});
		$(opcion).addClass('liActive');
	}
	if($(ul).attr('class').includes("nuevaWeb")){$(ul).css("width",$(x).css("width"))}
}
function guardarReutilizable(x){
	var campo = "reutilizable";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor =  $(x).attr('contenido');
	$(x).closest('td').children('div').children('span').text($(x).text());
	$(x).closest('td').children('div').children('span').attr('contenido', valor);
	if(!$(x).closest('ul').attr('class').includes('nuevaWeb'))
		guardarForo(campo,id_foro,valor);
}

//--CAMPO DESCRIPCION:---------------------------------
var editando = 0;
function openDescripcion(x){
	if(editando==0)$(".rotArrow").removeClass("rotArrow");
	var textarea = $(x).children('.taWeb');

	if($(textarea).attr('class').includes("visible")){
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$("#websGuardar").removeClass('cSave');
		//cerrar las ventanitas que esten abiertas
		$(".slCt").removeClass("visible");
		if (editando == 0) $(x).children('div').children('i.arrow').addClass('rotArrow');
		$(textarea).addClass("visible");
		resizeta(textarea,1);
	}
	
	if($(textarea).attr('class').includes("nuevaWeb")){$(textarea).css("width",$(x).css("width"))}
}
function editandoDescripcion(x){//este metodo nos permite hacer click dentro de la descripcion para poder modificarla
	editando = 1;
	$(x).removeClass('visible');
}
function guardarDescripcion(x){
	editando = 0;
	var campo = "descripcion";
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor =  $(x).val();
	$(x).closest('td').children('div').children('span').text(valor);
	if(!($(x).attr('class')).includes('nuevaWeb'))
		guardarForo(campo,id_foro,valor);
}
function resizeta(x,e){
	var u = $(x).prop("scrollHeight");
	$(x).css("height", u+"px");
	if(e==0){
		showGuardar();
	}
}
//--CAMPO REQUIERE---------------------------
function openRequiere(x){
	var ul = $(x).children('ul');
	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");
	$(".rotArrow").removeClass("rotArrow");
	$(ul).addClass("visible");
}
function guardarRequiere(x){
	var campo = $(x).attr('id');
	var id_foro = $(x).closest( "tr" ).attr('id');
	var valor = $(x).is(':checked');
	if(!$(x).closest('ul').attr('class').includes('nuevaWeb'))
		guardarForo(campo,id_foro,valor);
}

//actualizar los campos editables de los foros-------------------
function guardarForo(campo,id_foro,valor){
	$.post('Data', {
		metodo : 'guardarForo',
		campo: campo,
		id_foro: id_foro,
		valor: valor
	}, function(){
		$("#websGuardar").removeClass('cSave');
	});
}

function openNew(x){
	var padre = $(x).parent("div").parent('div');
	var panelNuevo = $(padre).children('.newSomething');
	$(panelNuevo).addClass("openNew");
	$('.divBlock').addClass("visible");
	$('.keywordsClient').addClass("blur");
}
function cancelNew(x){
	//eliminamos la ventana para añadir 
	$(".openNew").removeClass('openNew');
	$('.divBlock').removeClass("visible");
	$('.keywordsClient').removeClass("blur");
	$("#websGuardar").removeClass('cSave');
	resetValoresNuevo(x);
}
function resetValoresNuevo(x){
	var tabla = $(x).closest('div.newSomething').children('table#tNuevaWeb').children('tbody').children('tr');
	var web = $(tabla).children('td.cCWeb').children('div').children('input').val("");
	var dr = $(tabla).children('td.cCDR').children('input').val('0');
	var da = $(tabla).children('td.cCDA').children('input').val('0');
	var descripcion = $(tabla).children('td.cCDesc').children('textarea').val("");
					  $(tabla).children('td.cCDesc').children('div').children('span').text("Introduce una descripción");
	var reutilizable = $(tabla).children('td.cCReut').children('div').children('span').attr('contenido',"");
					   $(tabla).children('td.cCReut').children('div').children('span').text("");
	var aprobacion = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(1)').children('div').children('label').children('input').prop('checked', false);
	var registro = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(2)').children('div').children('label').children('input').prop('checked', false);
	var fecha = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(3)').children('div').children('label').children('input').prop('checked', false);
	var tipo = $(tabla).children('td.cTipo').children('i').attr('tipo','follow');
			   $(tabla).children('td.cTipo').children('i').removeClass('lf').removeClass('lnf').addClass('lf');
	
	var ul = $(tabla).children('td.cCTem').children('ul');
	$(ul).children('div').children('input').each(function(){
		$(this).prop('checked', false);
	});
	$(tabla).children('td.cCTem').children('div').children('span').text('Seleciona temática');
	
};
function guardarNew(x){
	var categoria = $('#lkkI .item_select').attr('id');
	var tabla = $(x).closest('div.newSomething').children('table#tNuevaWeb').children('tbody').children('tr');
	
	//recorremos todos los inputs checked
	var ul = $(tabla).children('td.cCTem').children('ul');
	var elementos = 0, longitud = $(ul).attr('size'),tematica="";
	$(ul).children('div.nameTem').children('input:checked').each(function(){
		elementos+=1;
		tematica = tematica + $(this).attr('tematica')+", ";
	});
	tematica = tematica.substring(0, tematica.lastIndexOf(", "));
	
	var web = $(tabla).children('td.cCWeb').children('div').children('input').val();
	var dr = $(tabla).children('td.cCDR').children('input').val();
	var da = $(tabla).children('td.cCDA').children('input').val();
	var descripcion = $(tabla).children('td.cCDesc').children('textarea').val();
	var reutilizable = $(tabla).children('td.cCReut').children('div').children('span').attr('contenido');
	var tipo = $(tabla).children('td.cTipo').children('i').attr('tipo');
	
	var aprobacion = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(1)').children('div').children('label').children('input').is( ":checked" );
	var registro = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(2)').children('div').children('label').children('input').is( ":checked" );
	var fecha = $(tabla).children('td.cCReq').children('ul').children('li:nth-child(3)').children('div').children('label').children('input').is( ":checked" );
	
	aprobacion = aprobacion === true ? 1 : 0;
	registro = registro === true ? 1 : 0;
	fecha = fecha === true ? 1 : 0;
	if(web.trim()!=""){
		$.post('Data', {
			metodo : 'guardarForoCompleto',
			web: web,
			dr: dr,
			da: da,
			descripcion: descripcion,
			reutilizable: reutilizable,
			tipo: tipo,
			aprobacion: aprobacion,
			registro: registro,
			fecha: fecha,
			tematica: tematica,
			categoria: categoria
		}, function(){
			cancelNew(x);
			$('#lkkI .item_select').click();
		});
	}
}

//---------------------------
function showGuardar(){$("#websGuardar").addClass('cSave');}
//Hide the menus if visible
window.addEventListener('click', function(e){  
	var clase = $(e.target).attr("class");
	if (typeof clase === 'undefined'){}else{

	if (clase.includes("tdCat") || clase.includes("arrow")){
		//si entra en este if significa que hemos hecho click en la categoria
	}else if(clase.includes("req")){
		//ignorar click dentro de requerimientos
	}else if(clase.includes("taWeb")){
		//ignorar click dentro descripcion
	}else if(clase.includes("slT")){
		//ignorar click dentro descripcion
	}else {
		$(".rotArrow").removeClass("rotArrow");
		$(".slCt").removeClass("visible");	
	}
	}
});













