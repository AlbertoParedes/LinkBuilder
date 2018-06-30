
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
	
	hoverGoLinkIn(x);
	
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
	
	hoverGoLinkOut(x);
	
	clearTimeout(timer);
	$(x).stop();
	$(x).removeAttr("style");
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
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
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
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	var ul = $(x).children('ul');
	// si el ul ya es visible lo cerramos
	if($(ul).attr('class').includes("visible")){
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
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
	
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	var ul = $(x).children('ul');
	
	if($(ul).attr('class').includes("visible")){
		//cerrar las ventanitas que esten abiertas
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
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
	if(editando==0)$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	var textarea = $(x).children('.taWeb');

	if($(textarea).attr('class').includes("visible")){
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}else{
		$("#websGuardar").removeClass('cSave');
		//cerrar las ventanitas que esten abiertas
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
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
	$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
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
		}, function(rt){
			
			var status = rt.status
			if(status==1){//cliente repetido
				$(x).closest('div.newSomething').children('.infoNew').text(rt.text);
			}else if(status==2){//coincidencia en el dominio
				$(x).closest('div.newSomething').children('.infoNew').html(rt.text+"<span style='font-weight:700;'>"+rt.c+"   </span>");
			}else if(status==0){//ok
				cancelNew(x);
				$('#lkkI .item_select').click();
			}
			
			
		});
	}
}


function bloquearTodo(){
	console.log("cargando datos");
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
	}else if(clase.includes("inner_pop_up")){
		//ignorar click dentro del popup
	}else {
		$(".rotArrow").removeClass("rotArrow");
		$("i.description_enlace").removeClass("lf");
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$('.paid_inputs').removeClass('open_inputs');
		
		$('.pop_up').removeClass("visible");
	}
	}
});


function resize_head_table_medios(){
	var tr = $('#tCategorias thead tr');
	var withTable = $('#tCategorias').width();
	$('#table_head_medio').width(withTable);
	
	$(tr).find('th').each(function(i) {
		var position = i+1;
		var w = $(this).width();
		$('#table_head_medio thead tr th:nth-child('+position+')').width(w);
	});
	
	
}

window.onresize = function() {
	resize_head_table_clientes();
	resize_head_table_medios();
}


var cntrlIsPressed = false;
$(document).keydown(function(event){
    if(event.which=="17")cntrlIsPressed = true;
});
$(document).keyup(function(){
    cntrlIsPressed = false;
});



function openUrl(x, event){
	if(cntrlIsPressed){
		
		
		event.stopPropagation();
		var url = $(x).text();
		if(!url.startsWith("http")) url = "http://"+url;
		var win = window.open(url, '_blank');
		win.focus();
		
		cntrlIsPressed = false;
	}
	
	
	
	
}


function goLink(x){
	if(cntrlIsPressed){
		var element = $(x).prop("tagName");
		
		if(element=="INPUT"){
			var url = $(x).val();
			var win = window.open(url, '_blank');
			win.focus();
		}
      	cntrlIsPressed = false;
	}
	
}


function hoverGoLinkIn(x){
	if(cntrlIsPressed){
		var element = $(x).prop("tagName");
		
		if(element=="INPUT"){
			$(x).addClass('activeGoLink');
		}else if(element=="SPAN"){
			$(x).addClass('activeGoLink');
		}
	}
}
function hoverGoLinkOut(x){
	var element = $(x).prop("tagName");
	
	if(element=="INPUT"){
		$(x).removeClass('activeGoLink');
	}else if(element=="SPAN"){
		$(x).removeClass('activeGoLink');
	}
	
}

function closeAllPopUps(){
	$(".slCt").removeClass("visible");
	$(".pop_up").removeClass("visible");
	$(".slWeb").removeClass("visible");
}







