
var websInicio=false;
function changePanel(id) {
	
	$(".allClients").css('display', "none");
	$(".allKeywords").css('display', "none");
	$(".allClientes").css('display', "none");

	$(".circleIcono").removeClass("btnSelected");
	$(".circleIcono i").removeClass("btnSelected");

	if(id == "btnClientes"){
		$(".allClients").css('display', "block");
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
	}else if(id == "btnKeywords"){
		$(".allKeywords").css('display', 'block');
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
		if(websInicio==false){cargarCategorias();}
		
	}else if(btnListaClientes = "btnListaClientes"){
		$(".allClientes").css('display', 'block');
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
		cargarListaClientes();
	}
}

function abrirBox(id) {
	var idDiv= 'div#'+id+'_plus';
	$(idDiv).toggleClass('open');
}

function selectClient(id_client) {
	
	var id_cliente_before = "";
	
	//revisamos si el cliente seleccionado esta disponible
	$.post('Data', {
		metodo : "ckc",
		id_client : id_client
	}, function(responseText) {
		
		var r = responseText.length;
		if(r == 0){
			alert("Esta bloqueado");
			id_cliente_before = $('#lstC div.item_select').attr('id');
		}else{
			//si el cliente selecionado esta disponible 
			$('#uniqueClient').html(responseText);
			id_cliente_before = id_client;
			changeThemeC(id_client);
		}
		
		//Comprobamos otra vez la disponibiladad de todos los clientes
		$.post('Data', {
			metodo : "ckcs",
			id_client : id_client,
			id_cliente_before: id_cliente_before
		}, function(responseText) {
			//actualizamos la lsita de clientes 
			$('#lstC').html(responseText);
			changeThemeC(id_cliente_before);
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
	
	$.post('Data', {
		metodo : 'cleanBlocksUser',
	}, function(responseText) {
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
	});
	
	
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
	
	//si se seleciona la X borraremos ese resultado de nuestra bbdd y de nuestro array
	if($(x).attr('class').includes('crossReset')){
		$(x).closest('td').children('div').children('span').text('');
		id_foro = 0;
		posicionForo = 0;
	}
	
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
		id_resultado: id_resultado,
		posicion: posicion
	});
}
function borrarCategoria(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var posicion = $(x).closest('tr').attr("posicion");
	$(x).closest('ul').removeClass("visible");
	$(x).closest('td').children('div').children('span').text('Selecciona una categoría');
	
	$.post('Data', {
		metodo : 'borrarCategoriaResultado',
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
	
	var mes = $(".datedropper .picker .pick-m li.pick-sl").attr("value");
	var year = $(".datedropper .picker .pick-y li.pick-sl").attr("value");

	var follows_done=0,nofollows_done=0;
	$(x).closest('tbody').children('tr').each(function(){
		var tipo = $(this).children(".cTipo").attr("tipo");
		var link_done = $(this).children(".cLink").children('input').val();
		if(tipo=="follow" && link_done.trim()!=""){
			follows_done++;
		}else if(tipo=="follow" && link_done.trim()!=""){
			nofollows_done++;
		}
	});
	
	$.post('Data', {
		metodo : 'guardarEnlaceResultado',
		id_resultado: id_resultado,
		posicion: posicion,
		link: link,
		tipo: tipo,
		follows_done: follows_done,
		nofollows_done: nofollows_done,
		mes: mes,
		year: year
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


function saveClient(x){
	var clase_btn_guardar = $("#cGuardar").attr('class');
	if(!clase_btn_guardar.includes("cSave"))
		$("#cGuardar").addClass('cSave');
}
var valorKey="";
function getKey(e){
	valorKey="";
	var keycode = (e.keyCode ? e.keyCode : e.which);
	valorKey = keycode;
	if(keycode == '13'){
		$('.open_inputs').removeClass('open_inputs');
	}
}
function openCoste(x){
	$('.open_inputs').removeClass('open_inputs');
	$(x).children('input').addClass('open_inputs');
	
	var value = $(x).children('input').val();
	$(x).children('input').focus();
	$(x).children('input').val("").val(value);
}
function updatePrecio(x){

	if(valorKey!=""){
		$(x).removeClass('open_inputs');
	}
	var num = $(x).val();
	if(!$(x).val())num = 0;
	$(x).closest('td').children('span').text(num+" €");
	
	//modificamos el campo de beneficio
	var td = $(x).closest('tr');
	var compra = parseInt($(td).children('td[data-paid="compra"]').children('span').text().replace(" €",""));
	var venta = parseInt($(td).children('td[data-paid="venta"]').children('span').text().replace(" €",""));
	$(td).children('td[data-paid="beneficio"]').text((venta-compra)+" €");
	$(td).children('td[data-paid="incremento"]').text(parseInt(((venta-compra)/compra)*100)+"%");
	valorKey="";
}
function stopPropagation(){event.stopPropagation();}
function deleteEuro(x){
	//si hacemos click en un input donde vayamos a variar dinero tendremos que eliminar el caracter del euro para que no haya ningus problema
	if($(x).attr('class').includes('paid_inputs')){
		var value = $(x).val().replace(" €","");
		$(x).val(value);
	}
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

function openNewWeb(x){
	var padre = $(x).parent("div").parent('div');
	var panelNuevo = $(padre).children('.newSomething');
	$(panelNuevo).addClass("openNew");
	$('.divBlock').addClass("visible");
	$('.keywordsClient').addClass("blur");
}
function openNewCliente(x){
	var padre = $(x).parent("div").parent('div');
	var panelNuevo = $(padre).children('.newSomething');
	$(panelNuevo).addClass("openNew");
	$('.divBlockClientes').addClass("visible");
	$('.listaClientes').addClass("blur");
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
	
	
	var text = "";
	var aceptado = true;
	if(web==""){text="Introuce una web";aceptado = false;}
	else if(!web.startsWith('http://') && !web.startsWith('https://') ){text="Introuce una web correcta";aceptado = false;}
	else if(web.includes('.')==false){text="Introuce una web correcta";aceptado = false;}

	
	if(aceptado===false){
		$(x).closest('div.newSomething').children('.infoNew').text(text);
	}else{
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

//##################################-VENTANA CLIENTES-###############################################
function cargarListaClientes(){
	$.post('Data', {
		metodo : 'ventanaClientes'
	}, function(responseText) {
		$('#divClientes').html(responseText);
	});
}
//--CAMPO NOMBRE CLIENTE-----------------------------------------
function guardarNombreCliente(x){
	var campo ="nombre";
	var valor = $(x).val();
	var id_cliente = $(x).closest('tr').attr('id');
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
function guardarWebCliente(x){
	var campo ="web";
	var valor = $(x).val();
	var id_cliente = $(x).closest('tr').attr('id');
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
function openServicio(x){
	$(".rotArrow").removeClass("rotArrow");
	if(!$(x).find("ul").attr('class').includes("visible")){
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
		$(x).children('ul').addClass("visible");
		$(x).find('div i.arrow').addClass('rotArrow');
	}else{
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
	}
	
	if($(x).children('ul').attr('class').includes("nuevaWeb")){$(x).children('ul').css("width",$(x).css("width"))}
}
function opentUser(x){
	
	
	$(".rotArrow").removeClass("rotArrow");
	if(!$(x).find("ul").attr('class').includes("visible")){
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
		$(x).children('ul').addClass("visible");
		$(x).find('div i.arrow').addClass('rotArrow');
	}else{
		$(".slCt").removeClass("visible");
		$(".slT").removeClass("visible");
	}
	
	if($(x).children('ul').attr('class').includes("nuevaWeb")){$(x).children('ul').css("width",$(x).css("width"))}
}
function guardarServicio(x){
	$(x).closest('ul').find('li').removeClass("liActive");
	$(x).addClass('liActive');
	var texto = $(x).text();
	$(x).closest('td').find('div span').text(texto);
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).attr('id');
	var campo ="servicio";
	
	if(valor!='medida'){
		$(x).closest('tr').find('.cFollow input').prop('readonly', true);
		$(x).closest('tr').find('.cNoFollow input').prop('readonly', true);
		$(x).closest('tr').find('.cFollow input').val($(x).attr('data-follows'));
		$(x).closest('tr').find('.cNoFollow input').val($(x).attr('data-nofollows'));
	}else{
		$(x).closest('tr').find('.cFollow input').prop('readonly', false);
		$(x).closest('tr').find('.cNoFollow input').prop('readonly', false);
	}
	
	guardarFollows($(x).closest('tr').find('td.cFollow input'));
	guardarNoFollows($(x).closest('tr').find('td.cNoFollow input'));
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
function guardarFollows(x){
	var campo ="follows";
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).val();
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
function guardarNoFollows(x){
	var campo ="nofollows";
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).val();
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
//--CAMPO ANCHOR-----------------------------------------
function guardarAnchorCliente(x){
	var campo ="anchor";
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $ (x).val();
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);

}
function guardarBlog(x){
	var campo ="blog";
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).is(':checked') === true ? 1 : 0;
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);

}
function guardarIdioma(x){
	var campo ="idioma";
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).val();
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);

}

function guardarUser(x){
	var campo ="linkbuilder";
	var id_cliente = $(x).closest('tr').attr('id');
	//empleado anterior
	var data_user = $(x).closest('ul').find('li.liActive').attr('id')
	$(x).closest('ul').find('li').removeClass("liActive");//removemos liActive de todos las opciones
	//empleado nuevo
	var id_user= $(x).attr('id');
	//cadena a remplazar
	var data_list_user = $(x).closest('td').find('div span').attr('data-list-user');
	//cadena remplazada
	var valor = data_list_user.replace(data_user,id_user);
	
	//$(x).closest('td').find('div span').attr('data-user', id_user);
	$(x).closest('td').find('div span').attr('data-list-user', valor);
	$(x).closest('td').find('div span').text($(x).text());
	$(x).addClass('liActive');
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}

function guardarNewCliente(x){
	var ob = $(x).closest('div.newSomething').find('table tbody');
	var web = $(ob).find('td.cCWebCliente input').val();
	var nombre = $(ob).find('td.cCNombre input').val();
	var servicio = $(ob).find('td.tipoNew ul li.liActive').attr('id');
	var follow = $(ob).find('td.cFollow input').val();
	var nofollow = $(ob).find('td.cNoFollow input').val();
	var anchor = $(ob).find('td.anchorC input').val();
	var blog = $(ob).find('td.cCBlog div label input').is(':checked') === true ? 1 : 0;
	var idioma = $(ob).find('td.cCIdioma input').val();
	var user = $(ob).find('td.cCUser ul li.liActive').attr("id");

	var text = "";
	var aceptado = true;
	if(web==""){text="Introuce una web";aceptado = false;}
	else if(!web.startsWith('http://') && !web.startsWith('https://') ){text="Introuce una web correcta";aceptado = false;}
	else if(web.includes('.')==false){text="Introuce una web correcta";aceptado = false;}
	else if(typeof servicio === 'undefined'){text="Introuce un servicio";aceptado = false;}
	else if(follow==""){text="Introuce follows";aceptado = false;}
	else if(nofollow==""){text="Introuce no follows";aceptado = false;}
	else if(typeof user === 'undefined'){text="Selecciona un usuario";aceptado = false;}
	
	if(aceptado===false){
		$(x).closest('div.newSomething').children('.infoNew').text(text);
	}else{
		$.post('Data', {
			metodo : 'guardarNuevoCliente',
			web: web,
			nombre: nombre,
			servicio:servicio,
			follow:follow,
			nofollow:nofollow,
			anchor:anchor,
			blog:blog,
			idioma:idioma,
			user:user
		}, function(rt){
			var status = rt.status
			if(status==1){//cliente repetido
				$(x).closest('div.newSomething').children('.infoNew').text(rt.text);
			}else if(status==2){//coincidencia en el dominio
				$(x).closest('div.newSomething').children('.infoNew').html(rt.text+"<span style='font-weight:700;'>"+rt.c+"   </span>");
			}else if(status==0){//ok
				cancelNewCliente(ob);
				$('#btnListaClientes').click();
			}
		});
	}
}
function resetValoresNuevoCliente(x){
	var web = $(x).find('td.cCWebCliente input').val("");
	var nombre = $(x).find('td.cCNombre input').val(""); 
	
	var servicio = $(x).find('td.tipoNew ul li').removeClass('liActive');
	servicio = $(x).find('td.tipoNew div span').text('-');
	
	var follow = $(x).find('td.cFollow input').val("0");
	var nofollow = $(x).find('td.cNoFollow input').val("0");
	var anchor = $(x).find('td.anchorC input').val("");
	var blog = $(x).find('td.cCBlog div label input').prop('checked', false);
	var idioma = $(x).find('td.cCIdioma input').val("ESP");
	
	var user = $(x).find('td.cCUser ul li').removeClass('liActive');
	user = $(x).find('td.cCUser div span').text('-');
	
	$(x).closest('div.newSomething').children('.infoNew').html("");
};
function cancelNewCliente(x){
	x = $(x).closest('div.newSomething').find('table tbody');
	$(".openNew").removeClass('openNew');
	$('.divBlockClientes').removeClass("visible");
	$('.listaClientes').removeClass("blur");
	$("#websGuardar").removeClass('cSave');
	resetValoresNuevoCliente(x);
}


function guardarValoresCliente(id_cliente,campo,valor){
	$.post('Data', {
		metodo : "guardarValoresCliente",
		id_cliente: id_cliente,
		campo:campo,
		valor: valor
	}, function(){
		$("#websGuardar").removeClass('cSave');
	});
}
function deleteClient(x){
	var status = $(x).children('i').text();
	if(status == 'delete_outline'){
		
		$('.select_client').addClass('visible_td');
		$(x).children('i').text('delete_forever');
		$(x).children('i').removeClass('gris').addClass('lnf');
		
	}else if(status == 'delete_forever'){
		
		$('.select_client').removeClass('visible_td');
		$(x).children('i').text('delete_outline');
		$(x).children('i').removeClass('lnf').addClass('gris');
		
		eliminarClientesSeleccionados(x);
	}
}

function eliminarClientesSeleccionados(x){
	var clientes = [];
	var obj = {};
	$('#tClients tbody tr td.select_client div input:checked').each(function(){
		var id_cliente = $(this).closest('tr').attr('id');
		var web_cliente = $(this).closest('tr').find('td.cCWebCliente input').val();
		obj = {	'id_cliente': id_cliente, 'web_cliente': web_cliente}
		clientes.push(obj);
	});
	
	//preguntar si esta seguro eliminar todos esos clientes
	
	/*for (var i = 0; i < clientes.length; i++){
	    var obj = clientes[i];
	    $('#tClients tbody tr#'+obj.id_cliente).remove();
	    
	    console.log(obj.id_cliente+"  "+obj.web_cliente);
	}*/
	if(clientes.length>0){
		var json = JSON.stringify(clientes);
		$.post('Data', {
			metodo : "eliminarCliente",
			json: json
		}, function(){
			$('#btnListaClientes').click();
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
	}else if(clase.includes("td_input_precio")){
		//ignorar click dentro descripcion
	}else {
		$(".rotArrow").removeClass("rotArrow");
		$(".slCt").removeClass("visible");
		$('.paid_inputs').removeClass('open_inputs');
	}
	}
});













