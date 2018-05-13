
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
function selectWeb(id_resultado){
	
	$(".rotArrow").removeClass("rotArrow");
	
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $("#selWeb_"+id_resultado).attr("class");
	if(clase.includes("visible")){
		
		$(".slCt").removeClass("visible");
		$(".slWeb").removeClass("visible");
		
		
	}else{
		
		//cerrar los anteriores
		$(".slWeb").removeClass("visible");
		$(".slCt").removeClass("visible");
		//-------------------------------
		
		var id_categoria = $("#selCat_"+id_resultado+" .liActive").attr('id');;
		cargarWebs(id_categoria,id_resultado);
		
		
	}
	
	
	
}
function cargarWebs(id_categoria,id_resultado){
	
	$.post('Data', {
		metodo : 'cwbs',
		id_categoria: id_categoria,
		id_resultado: id_resultado
	}, function(responseText) {
		$('#tdWeb_'+id_resultado+" ul").html(responseText);
		$("#selWeb_"+id_resultado).addClass("visible");
		//modificamos la flecha 
		$("#dvWeb_"+id_resultado+" .arrow").addClass("rotArrow");
		
	});
	
}





//Seleccionamos la categoria de cada enlace
function liSelectCat(x){
	var id_resultado = $(x).parent().parent().parent().attr("id");
	var id_categoria = $(x).attr("id");
	
	$("#selCat_"+id_resultado+" li").removeClass("liActive");
	$("#selCat_"+id_resultado+" > #"+id_categoria).addClass("liActive");
	$("#selCat_"+id_resultado).removeClass("visible");
	
	//le damos el texto seleccionado a nuestra vista de categorias
	var op = $("#selCat_"+id_resultado+" > #"+id_categoria).text();
	$("#spCat_"+id_resultado).text(op);
	
	guardarCategoria(id_categoria, id_resultado);
}
function guardarCategoria(id_categoria, id_resultado){
	var id_cliente = $("#lstC .item_select").attr('id');
	$.post('Data', {
		metodo : 'sc',
		id_categoria: id_categoria,
		id_cliente: id_cliente,
		id_resultado: id_resultado
	});
}

//-------------------------------------------------------------------------



function liSelectWeb(id_foro,id_resultado){
	
	$("#selWeb_"+id_resultado+" li").removeClass("liActive");
	$("#selWeb_"+id_resultado+" > #"+id_foro).addClass("liActive");
	//$("#selWeb_"+id_resultado).removeClass("visible");
	
	//var op = $("#selWeb_"+id_tr+" > #"+pArrayF).text();
	//$("#spWeb_"+id_tr).text(op);
	
	guardarWeb(id_foro, id_resultado);
}

//guardamos en la bbdd y en el array la web selecionada
function guardarWeb(id_foro, id_resultado){
	//cogemos el elemento que ya esta seleccionado para proceder a quitarlo de la lista de los foros utilizados para este cliente
	var elemtAnterior = $("#spWeb_"+id_resultado).attr("mweb");
	$.post('Data', {
		metodo : 'sw',
		id_foro: id_foro,
		id_resultado: id_resultado,
		elemtAnterior: elemtAnterior
	}, function(responseText) {
		$('#dvWeb_'+id_resultado).html(responseText);
		
	});
	
}

//Hide the menus if visible
window.addEventListener('click', function(e){  
	var clase = e.target.className;
	
	if (clase.includes("tdCat")){
		//si entra en este if significa que hemos hecho click en la categoria
	}else if(clase.includes("rositaGuay")){
		//ignorar click require
	}else if(clase.includes("slT")){
		//ignorar click tematica
	}else {
		$(".rotArrow").removeClass("rotArrow");
		$(".slCt").removeClass("visible");
	}
});


//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	
	
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();
	
	//limpiar cajas categoria
	//$(".slCt").html("");
	//Limpiamos cajas de webs
	//$(".slWeb").html("");
	
	//chv = change view
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
	var id_resultado = $(x).parent().parent().attr("id");
	var link = $(x).val();
	var tipo = $("tr#"+id_resultado+" .cTipo").attr("tipo");

	$.post('Data', {
		metodo : 'chl',
		id_resultado: id_resultado,
		link: link,
		tipo: tipo
	}, function(responseText) {
		$('#lstC .item_select .itemChild').html(responseText);
	});
}


//*****************  DANI  ********************

//--CARGAR CATEGORIAS---------------

//para cargar la lista de categorias(la lista de la izq)
function cargarCategorias(){
	$.post('Data', {
		metodo : 'cats'
	}, function(responseText) {
		$('#lkk ').html(responseText);
	});
}

//para cargar los foros al pinchar sobre una categoria
function selectCategoria(id) {
	changeThemeK(id);
	
	//pintamos las busquedas de la categoria selecionada	
	$.post('Data', {
		metodo : 'selectCat', 
		id : id
	}, function(responseText) {
		$('#kywData').html(responseText);
	});	
}

//--CAMPO WEB--------------------------------------------

//para detectar los cambios en la url dela columna WEB y enviarlos a la funcion que lo guarda
/*function guardarWeb(id){
	var cosa = $ ("#inputWeb_"+id).val();
	//alert(cosa);
	$.post('Data', {
		metodo : 'guardarWeb',
		id: id,
		cosa: cosa
	});
}*/

//--CAMPO TIPO-------------------------------------------

//para desplegar el ul don los dos tipos de foro y que pinte en azul el que esta actualmente seleccionado en la BBDD
function selectTipo(id){
	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");
	
	//esto pinta de azul el Reu que esta actualmente almacenado en la BBDD
	$("#selTipo_"+id+" li").removeClass("liActive");
	
	var span = $("#spTipo_"+id).text()

	if (span == $("#Tipo1").text()){
		$("#selTipo_"+id+" > #Tipo1").addClass("liActive");
	}else if (span == $("#Tipo2").text()){
		$("#selTipo_"+id+" > #Tipo2").addClass("liActive");
	}	 
	
	$("#selTipo_"+id).addClass("visible");

	$(".pr").removeClass("pr");
	$("#td_Tipo"+id).addClass("pr");
	
}
//Para seleccionar el Tipo, y que se cambie en el span
function liSelectTipo(id_tipo, id_tr){
	//alert("dentro");
	$("#selTipo_"+id_tr+" li").removeClass("liActive");
	$("#selTipo_"+id_tr+" > #"+id_tipo).addClass("liActive");
	$("#selTipo_"+id_tr).removeClass("visible");
	var texto = $("#"+id_tipo).text();
	$("#spTipo_"+id_tr).text(texto);
	
	cosa = texto;
	//alert (id_tr+"|"+cosa)
	
	guardarTipo(id_tr, cosa);
}

function guardarTipo(id_tr, cosa){
	$.post('Data', {
		metodo : 'guardarTipo',
		id_tr: id_tr,
		cosa: cosa
	});
}

//--CAMPO DR----------------------------------------------
function guardarDR(id){
	var cosa = $ ("#inputDR_"+id).val();
	//alert(cosa);
	$.post('Data', {
		metodo : 'guardarDR',
		id: id,
		cosa: cosa
	});
}
//--CAMPO DA----------------------------------------------
function guardarDA(id){
	var cosa = $ ("#inputDA_"+id).val();
	//alert(cosa);
	$.post('Data', {
		metodo : 'guardarDA',
		id: id,
		cosa: cosa
	});
}
//--CAMPO TEMATICA----------------------------------------

//para abrir le desplegable con la lista de tematicas al pulsar en el campo tematica
function selectTematica(id){
	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");
	
	$("#selTem_"+id).addClass("visible");
	
	//$('#spTem'+id+':contains("Todas")')
	
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
	        }            
	    }
	    //escribimos "todas" en el span
	    $("#spTem_"+id_sp).text("Todas");    
	    
	    //cogemos todas la tematicas y borramos "input_'id'" que es el id del checkbox "seleccionar todas"
	    //para que no se lo pase a la BBDD como si fuese una tematica mas, y llamamos a la funcion que lo guarda
	    var cosa = "";
	    $("#selTem_"+id_sp+" li input:checked").each(function(){cosa += ($(this).attr("id"))+" "});
	    //borramos "input_'id'"
	    cosa = cosa.replace("input_"+id_sp,"").trim();
	    guardarTematica(id_sp, cosa);
	    
	}else{
		//vaciar todos los checkboxes
	    for (var i = 0; i < checkboxes.length; i++) {
	        checkboxes[i].checked = source.checked;  	            
	    }
	    $("#spTem_"+id_sp).text("Selecciona tematica");
	    cosa = "";
	    guardarTematica(id_sp, cosa);
	}      
}

//para seleccionar los checkbox de la tematica del foro(menos el de select all)
function liSelectTem(id_ul){
	
	var t = "";
	$("#selTem_"+id_ul+" li input:checked").each(function(){t += ($(this).attr("id"))+" "});
	
	if (t == ""){
		t = "Selecciona tematica";
	}
	//esto escribe todo lo seleccionado en el span(no añade uno por uno)
	t = t.replace("input_"+id_ul,"").trim();
	$("#spTem_"+id_ul).text(t);
	//$("#selTem_21 li input:checked").each(function(){console.log($(this).attr("id"))})
	
	guardarTematica(id_ul, t);
}

//para guardar la tematica en la BBDD
function guardarTematica(id, cosa){
	if(cosa=="Selecciona tematica"){
		cosa = "";
	}
	$.post('Data', {
		metodo : 'guardarTematica',
		id: id,
		cosa: cosa
	});
}


//--CAMPO REUTILIZABLE----------------------------------

//para abrir el desplegable al pulsar en el campo reutilizable
function selectReutilizable(id){
	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");
	
	//esto pinta de azul el Reu que esta actualmente almacenado en la BBDD
	$("#selReu_"+id+" li").removeClass("liActive");
	
	var span = $("#spReu_"+id).text()

	if (span == $("#Reu1").text()){
		$("#selReu_"+id+" > #Reu1").addClass("liActive");
	}else if (span == $("#Reu2").text()){
		$("#selReu_"+id+" > #Reu2").addClass("liActive");
	}else if (span == $("#Reu3").text()){
		$("#selReu_"+id+" > #Reu3").addClass("liActive");
	}else if (span == $("#Reu4").text()){
		$("#selReu_"+id+" > #Reu4").addClass("liActive");
	}	 
	//-------------------------------
	
	$("#selReu_"+id).addClass("visible");

	$(".pr").removeClass("pr");
	$("#td_"+id).addClass("pr");
	
}
//Seleccionamos la usabilidad del foro en el desplegable del campo reutilizable
function liSelectReu(id_reu,id_tr){
	
	$("#selReu_"+id_tr+" li").removeClass("liActive");
	$("#selReu_"+id_tr+" > #"+id_reu).addClass("liActive");
	$("#selReu_"+id_tr).removeClass("visible");

	var texto = $("#"+id_reu).text();
	$("#spReu_"+id_tr).text(texto);
	
	cosa = texto;
	guardarReutilizable(id_tr, cosa);
}
//para actualizar el campo reutilizable en la BBDD
function guardarReutilizable(id_tr, cosa){
	$.post('Data', {
		metodo : 'guardarReutilizable',
		id_tr: id_tr,
		cosa: cosa
	});
}

//--CAMPO DESCRIPCION:---------------------------------

//para abrir la ventanita y poder ver y editar la descripcion 
function editDescripcion(id){

	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");	
	$(".icon").removeClass("visible");
	//-------------------------------
	$("#desc_"+id).addClass("visible");
	$("#icon_"+id).addClass("visible");
	
	$(".pr").removeClass("pr");
	$("#td_Desc"+id).addClass("pr");
	
}
//para actualizar el <span> con la nueva descripcion y llamar a la funcion que la guarda en BBDD
function cambiarDescripcion(id, span){
	var cosa = $ ("#desc_"+id).val(); 
	$("#spDesc_"+id).text(cosa);
	
	id_tr = id;
	
	guardarDescripcion(id_tr, cosa);
}
//para actualizar la descripcion en la BBDD
function guardarDescripcion(id_tr, cosa){
	
	$.post('Data', {
		metodo : 'guardarDescripcion',
		id_tr: id_tr,
		cosa: cosa
	});
}

//--CAMPO REQUIERE---------------------------

//para abrir el desplegable con las tres opciones de requerimiento
function selectRequiere(id){
	//alert("DENTROo");
	//cerrar las ventanitas que esten abiertas
	$(".slCt").removeClass("visible");
	$("#selReq_"+id).addClass("visible");
	
	//esto saca el id de los checkbox que YA ESTAN MARCADOS
	var t = "";
	$("#selReq_"+id+" li input:checked").each(function(){t += ($(this).attr("id"))+" "});
	//alert("Esta pulsado -> "+t);
	//-----------
	
	$(".pr").removeClass("pr");
	$("#tdR_"+id).addClass("pr");
	
}
	
function cambiarRequiere(id, id_tr){
		
	//var t = "";
	//$("#selReq_"+id_tr+" li input:checked").each(function(){t += ($(this).attr("id"))+" "});	
	//alert("Pulsando -> "+t);
	
	var x = $("#"+id).is(':checked');
	//alert(x+" "+id);
	
	guardarRequiere(id_tr, id, x);
}

//para guardar los reuqerimientos en la BBDD
function guardarRequiere(id_tr, id, state){
	//alert("Funcion Guarda -> "+id_tr+" "+id+" "+state);
	$.post('Data', {
		metodo : 'guardarRequiere',
		id_tr: id_tr,
		id: id,
		state: state
	});
}


//---------------------------















