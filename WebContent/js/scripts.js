var websInicio=false;
function changePanel(id) {
	
	$(".allClients").css('display', "none");
	$(".allKeywords").css('display', "none");
	$(".allClientes").css('display', "none");

	$(".circleIcono").removeClass("btnSelected");
	$(".circleIcono i").removeClass("btnSelected");

	if(id == "btnEnlaces"){
		$("#vistaEnlaces").css('display', "block");
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
		
		if (typeof $('#lcc').attr('id') === 'undefined'){
			cargarVistaEnlaces();
		}
		
		guardarPanelSesion("enlaces")
		
	}else if(id == "btnMedios"){
		$(".allKeywords").css('display', 'block');
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
		
		if (typeof $('.list_categoria_medios').attr('class') === 'undefined'){
			cargarCategorias();
		}
		
		guardarPanelSesion("medios")
		
	}else if(id = "btnClientes"){
		$(".allClientes").css('display', 'block');
		$("#"+id).addClass("btnSelected");
		$("#"+id+" i").addClass("btnSelected");
		
		if (typeof $('#divClientes .keywordsClient').attr('class') === 'undefined'){
			cargarListaClientes();
		}
		
		guardarPanelSesion("clientes")
		
	}
}

function guardarPanelSesion(panel){
	$.post('Data', {metodo : 'guardarPanelSesion',panel: panel});
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
		$(".slCt").removeClass("visible");
		$(".pop_up").removeClass("visible");
		$('.paid_inputs').removeClass('open_inputs');
		
		$('.pop_up').removeClass("visible");
	}
	}
});

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

function bloqueamosPantalla(){
	$('.blockAll').addClass('visible');
	$('.blockAll .loader').addClass('fadeIn');
}

function desbloqueamosPantalla(){
	$('.blockAll').removeClass('visible');
	$('.blockAll .loader').removeClass('fadeIn');
}





