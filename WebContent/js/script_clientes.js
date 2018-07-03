//##################################-VENTANA CLIENTES-###############################################
function cargarListaClientes(){
	$.post('Data_Clientes', {
		metodo : 'cargarListaClientes'
	}, function(responseText) {
		$('#divClientes').html(responseText);
	});
}
function guardarWebCliente(x){
	var campo ="web";
	var valor = $(x).val();
	var id_cliente = $(x).closest('tr').attr('id');
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}
function guardarNombreCliente(x){
	var campo ="nombre";
	var valor = $(x).val();
	var id_cliente = $(x).closest('tr').attr('id');
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
}

function openServicio(x){
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	if(!$(x).find("ul").attr('class').includes("visible")){
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
		$(x).children('ul').addClass("visible");
		$(x).find('div i.arrow').addClass('rotArrow');
	}else{
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}
	
	if($(x).children('ul').attr('class').includes("nuevaWeb")){$(x).children('ul').css("width",$(x).css("width"))}
}
function opentUser(x){
	
	
	$(".rotArrow").removeClass("rotArrow");
	$("i.description_enlace").removeClass("lf");
	
	$(".slCt").removeClass("visible");
	$(".pop_up").removeClass("visible");
	$(".slT").removeClass("visible");
	
	if(!$(x).find("ul").attr('class').includes("visible")){
		
		$(x).children('ul').addClass("visible");
		$(x).children('ul').find('li').removeClass("liActive");
		$(x).find('div i.arrow').addClass('rotArrow');
		
		var empleado_seleccionado = $(x).find('div.tdWeb span').attr('data-id-empleado');
		$(x).children('ul').children('li[data-id-empleado="'+empleado_seleccionado+'"]').addClass("liActive");
		
	}/*else{
		$(".slCt").removeClass("visible");
		$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}*/
	
	if($(x).children('ul').attr('class').includes("nuevaWeb")){$(x).children('ul').css("width",$(x).css("width"))}
}
function guardarServicio(x){
	$(x).closest('ul').find('li').removeClass("liActive");
	$(x).addClass('liActive');
	var texto = $(x).text();
	$(x).closest('td').find('div span').text(texto);
	$(x).closest('td').find('div span').attr("id",$(x).attr('id'));
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
function guardarEmpleado(x){
	
	var id_cliente = $(x).closest('tr').attr('id');
	var posicion = $(x).closest('tr').attr('posicion');
	var estado = $(x).find('input[type=checkbox]').prop('checked');
	var id_empleado = $(x).attr('data-id-empleado');
	var tipo_empleado = $(x).attr('data-tipo-empleado');
	
	if(estado == false){
		$(x).siblings('.div_elem_empl').find('div[data-id-empleado="'+id_empleado+'"][data-tipo-empleado="'+tipo_empleado+'"]').remove();
	}
	
	var empleados = [];
	var obj = {};
	
	//obtenemos en numero de enlaces que quedan libres para poder asignarselos a el nuevo cliente seleccionado
	var n_follows = $(x).closest('tr').find('td.cFollow input').val();
	var n_follows_selec = 0;
	$(x).siblings('.div_elem_empl').find('input[type="number"]').each(function(){
		n_follows_selec = parseInt(n_follows_selec) + parseInt($(this).val());
		obj = {	'id_empleado': $(this).attr('data-id-empleado'), 'tipo_empleado':  $(this).attr('data-tipo-empleado'), 'enlaces':  $(this).val()}
		empleados.push(obj);
	});
	var enlacesDisponibles = n_follows - n_follows_selec;
	var nameEmpleado = $(x).children('span').text();
	//-----------------------------------------------------------------------------------------------------------
	if(estado == true){
		obj = {	'id_empleado': id_empleado+"", 'tipo_empleado':  tipo_empleado+"", 'enlaces':  enlacesDisponibles+""}
		empleados.push(obj);
	}
	
	
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente')){
		var json = JSON.stringify(empleados);
		$.post('Data_Clientes', {
			metodo : "guardarEmpleado",
			id_cliente: id_cliente,
			posicion:posicion,
			estado:estado,
			id_empleado: id_empleado,
			tipo_empleado: tipo_empleado,
			enlacesDisponibles: enlacesDisponibles,
			nameEmpleado: nameEmpleado,
			json: json
		}, function(rt){
			$("#websGuardar").removeClass('cSave');
			
			if(estado == false){
				$(x).siblings('.div_elem_empl').find('div[data-id-empleado="'+id_empleado+'"][data-tipo-empleado="'+tipo_empleado+'"]').remove();
			}else {
				$(x).siblings('.div_elem_empl').append(rt);
			}
			
			
		});
	}
	
	/*
	
	
	var id_empleado_anterior = $(x).closest('td').children('div.tdWeb').children('span').attr('data-id-empleado');
	var id_empleado_seleccionado = $(x).attr('data-id-empleado');
	var tipo_empleado_seleccionado = $(x).attr('data-tipo-empleado');
	var nombre_empleado_seleccionado = $(x).text();
	
	$(x).closest('td').children('div.tdWeb').children('span').attr('data-id-empleado',id_empleado_seleccionado);
	$(x).closest('td').children('div.tdWeb').children('span').attr('data-tipo-empleado',tipo_empleado_seleccionado);
	$(x).closest('td').find('div span').text($(x).text());
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente')){
		$.post('Data_Clientes', {
			metodo : "guardarEmpleado",
			id_cliente: id_cliente,
			posicion:posicion,
			id_empleado_anterior:id_empleado_anterior,
			id_empleado_seleccionado: id_empleado_seleccionado,
			tipo_empleado_seleccionado: tipo_empleado_seleccionado,
			nombre_empleado_seleccionado:nombre_empleado_seleccionado
		}, function(){
			$("#websGuardar").removeClass('cSave');
			
		});
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
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
		guardarValoresClienteEmpleado(id_cliente,campo,valor, data_user);
	*/
	
}
function openNewCliente(x){
	var padre = $(x).parent("div").parent('div');
	var panelNuevo = $(padre).children('.newSomething');
	$(panelNuevo).addClass("openNew");
	$('.divBlockClientes').addClass("visible");
	$('.listaClientes').addClass("blur");
}
function guardarNewCliente(x){
	var ob = $(x).closest('div.newSomething').find('table tbody');
	var web = $(ob).find('td.cCWebCliente input').val();
	var nombre = $(ob).find('td.cCNombre input').val();
	var servicio = $(ob).find('td.tipoNew .tdWeb span').attr('id');
	var follow = $(ob).find('td.cFollow input').val();
	var nofollow = $(ob).find('td.cNoFollow input').val();
	var anchor = $(ob).find('td.anchorC input').val();
	var blog = $(ob).find('td.cCBlog div label input').is(':checked') === true ? 1 : 0;
	var idioma = $(ob).find('td.cCIdioma input').val();
	var user = $(ob).find('td.cCUser .tdWeb span').attr('data-id-empleado');
	var user_tipo = $(ob).find('td.cCUser .tdWeb span').attr('data-tipo-empleado');

	
	var text = "";
	var aceptado = true;
	/*if(web==""){text="Introuce una web";aceptado = false;}
	else if(!web.startsWith('http://') && !web.startsWith('https://') ){text="Introuce una web correcta";aceptado = false;}
	else if(web.includes('.')==false){text="Introuce una web correcta";aceptado = false;}
	else if(typeof servicio === 'undefined'){text="Introuce un servicio";aceptado = false;}
	else if(follow==""){text="Introuce follows";aceptado = false;}
	else if(nofollow==""){text="Introuce no follows";aceptado = false;}
	else if(typeof user === 'undefined'){text="Selecciona un usuario";aceptado = false;}
	*/
	
	
	if(aceptado===false){
		$(x).closest('div.newSomething').children('.infoNew').text(text);
	}else{
		$.post('Data_Clientes', {
			metodo : 'guardarNuevoCliente',
			web: web,
			nombre: nombre,
			servicio:servicio,
			follow:follow,
			nofollow:nofollow,
			anchor:anchor,
			blog:blog,
			idioma:idioma,
			user:user,
			user_tipo:user_tipo
		}, function(rt){
			var status = rt.status;
			if(status==0){
				$(x).closest('div.newSomething').children('.infoNew').text(rt.text);
			}else if(status==1){
				cancelNewCliente(ob);
				$('#btnListaClientes').click();
			}else if(status==2){
				$(x).closest('div.newSomething').children('.infoNew').text(rt.text);
			}else if(status==3){
				$(x).closest('div.newSomething').children('.infoNew').html(rt.text+"<span style='font-weight:700;'>"+rt.c+"   </span>");
			}
			
			/*
			if(status==1){//cliente repetido
				$(x).closest('div.newSomething').children('.infoNew').text(rt.text);
			}else if(status==2){//coincidencia en el dominio
				
			}else if(status==0){//ok
				cancelNewCliente(ob);
				$('#btnListaClientes').click();
			}*/
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
	var posicion = $('#tClients tr#'+id_cliente).attr('posicion');
	$.post('Data_Clientes', {
		metodo : "guardarValoresCliente",
		id_cliente: id_cliente,
		campo:campo,
		valor: valor,
		posicion:posicion
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
	alert(clientes);
	if(clientes.length>0){
		var json = JSON.stringify(clientes);
		$.post('Data_Clientes', {
			metodo : "eliminarCliente",
			json: json
		}, function(){
			$('#btnListaClientes').click();
		});
	}
}


//subir factura a bbdd
function uploadExcelFactura(x){
	var target = event.target || event.srcElement;
	
	if (target.value.length == 0) {
		$(x).closest('form').find('input[name="nombre"]').val('');
	}else {
		var file = $("#excelFactura")[0].files[0];
		$('#uploadFactura input[name="nombre"]').val(file.name);
		var form = $('#uploadFactura')[0]; 
	    var data = new FormData(form);
		
		$.ajax({
			type:"post",
			url:"Data",
			enctype : 'multipart/form-data',
			data : data,
            processData : false,
            contentType : false,
            cache : false,
            
            success : function(responseText) {
               $('#panelConfirmacion').addClass('displayTable');
               $('#panelConfirmacion .tableRow .tableCell').html(responseText);
            } 
		
		});
		
		
		
	}
}
function openOpcionesNuevaFactura(x){
	$(x).children('ul').addClass('visible');
}
function selectCoincidencia(x){
	$(x).closest('ul').find('li').removeClass('liActive');
	$(x).addClass("liActive");
	$(x).closest('td').find('div span.tdCat').text($(x).text());
}

function replaceInputMedio(x){
	var checked = $(x).is(':checked') === true ? true : false;
	$(x).closest('ul').find('input:checked').prop('checked', false);
	$(x).prop('checked', checked);

	
}

function resetEnlaceFactura(x){
	var span = $(x).closest('td').find('div span.tdCat');
	$(span).text($(span).attr('data-origen'));
	
	$(x).closest('ul').children('li').removeClass('liActive');
}


function showPopUp(x) {
    $(x).children('div.popup').addClass("show");
}
function hidePopUp(x){
	$(x).children('div.popup').removeClass("show");
}

var timerCoincidencia;
function showPopUpCoincidencias(x) {
    
	timerCoincidencia = setTimeout(function () {
		//seleccionamos la url principal
		var url = $(x).closest('td').find('div span.tdCat').attr('data-origen');
		var urlARemplazar = $(x).closest('li').children('span').text();
		var mensaje = "Remplazar <strong class='lnf'>" + urlARemplazar +"</strong> por " + "<strong class='color_green'>" + url+"</strong>";
		$(x).closest('ul').find('.mensaje_coincidencia').html(mensaje);
		$(x).closest('ul').children('div.popup').addClass("show");
	}, 1000);
	
    
    
}
function hidePopUpCoincidencias(x){
	clearTimeout(timerCoincidencia);
	$(x).stop();
	$(x).closest('ul').children('div.popup').removeClass("show");
	
}





function deleteDestino(x){
	
	var url = $(x).closest('li').children('span').text();
	var id_cliente = $(x).closest('tr').attr('id');
	$.post('Data_Clientes', {
		metodo : "removeDestino",
		id_cliente: id_cliente,
		url: url
	}, function(rt){
		$(x).closest('div').find('div input').val("");
		$(x).closest('li').remove();
	});
}
function addDestino(x){
	var url = $(x).closest('div').children('input').val();
	var id_cliente = $(x).closest('tr').attr('id');
	$.post('Data_Clientes', {
		metodo : "addDestino",
		id_cliente: id_cliente,
		url: url
	}, function(rt){
		
		$(x).closest('td').children('div').children('ul').append(rt.html);
		$('div.pop_up').removeClass('visible');
	});
}

function openEstadoCliente(x){
	
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	if(!$(x).find("div.pop_up").attr('class').includes("visible")){
		$(".slCt").removeClass("visible");
		$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
		$(x).find('div.pop_up').addClass("visible");
	}else{
		$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
		$(".slT").removeClass("visible");
	}
	
	//if($(x).children('ul').attr('class').includes("nuevaWeb")){$(x).children('ul').css("width",$(x).css("width"))}
	
}
function guardarEstadoCliente(x){
	
	$(x).closest('ul').find('li').removeClass("liActive");
	$(x).addClass('liActive');
	var clase = $(x).children('div').attr('data-class');
	$(x).closest('td').children('div.divStatus').removeClass('sOK').removeClass('sPendiente').removeClass('sYS').addClass(clase);
	
	var id_cliente = $(x).closest('tr').attr('id');
	var valor = $(x).attr('data-status-cliente');
	var campo ="status";
	
	//if(!$(x).closest('table').attr('id').includes('tNuevoCliente'))
		guardarValoresCliente(id_cliente,campo,valor);
	
}

function move2Left(x){
	var p_tr = $(x).closest('tr').position().left + parseInt($(x).closest('tr').css('padding-left'), 10);
    var position_width_tr = $(x).closest('tr').width()+p_tr;
    
    var p_pop_up = $(x).position().left + parseInt($(x).css('padding-left'), 10);
    var position_width_pop_up = $(x).children('div.pop_up').width()+p_pop_up;
    
    var retroceder = position_width_tr - (position_width_pop_up + 2) ;//si el border es 1 por cada lado sumaremos el borde izquierdo y derecho
    
    if(retroceder<0){
    	$(x).children('div.pop_up').css('margin-left', retroceder+"px");
    }else{
    	
    }
}


function aplicarFiltro(x){
	
	
	var filtros = [];
	var obj = {};
	$(x).closest('div#divClientes').find('.ctoolbar div.txt_opciones_filter input:checked').each(function(){
		var tipo = $(this).closest('div.txt_opciones_filter').attr('data-filter');
		var valor = $(this).closest('div.txt_opciones_filter').attr('data-valor');
		obj = {	'tipo': tipo, 'valor': valor}
		filtros.push(obj);
	});
	var json = JSON.stringify(filtros);
	
	var clase = $(x).attr('class');
	if(clase.includes('cabeceraTable')){
		var tipo = $(x).children('i.arrowOrdenar').text().trim();
		if(tipo == "arrow_downward" ) tipo = "des";
		else tipo = "asc";
		var campo = $(x).attr('data-tipo');
	}else if(clase.includes('btn_filter')){
		var tipo = "";
		var campo = "";
	}
	
	
	$.post('Data_Clientes', {
		metodo : "ordenarListaClientes",
		campo: campo,
		tipo: tipo,
		json: json
	}, function(responseText){
		$('#tClients tbody').html(responseText);
		if(clase.includes('cabeceraTable')){
			if(tipo=="des"){$(x).children('i.arrowOrdenar').text('arrow_upward');}
			else {$(x).children('i.arrowOrdenar').text('arrow_downward');}
			
			$('#tClients thead i.arrowOrdenar').removeClass('visible');
			$(x).children('i.arrowOrdenar').addClass('visible');
			$('#tClients thead i.arrowOrdenar:not(.visible)').text('arrow_upward');
		}else if(clase.includes('btn_filter')){
			viewFilters(filtros);
		}
		
		//hacer scrrol hacia arriba de la tabla
		
		$('#divClientes').find('div.contentTable').animate({ 
			scrollTop: $('#divClientes').scrollTop()
			}, { duration: 600, easing: 'linear'});
		
	});
}
function openFilter(x){
	$(x).children('div.pop_up').addClass('visible');
}

function viewFilters(filtros){
	var html="", texto="";
	var contenedor = $('#divClientes div.ctoolbar div.div_group_filter');
	for (var i = 0; i < filtros.length; i++){
		var obj = filtros[i];
		if(obj.valor=="pro")texto="seo pro";
		else if(obj.valor=="lite")texto="seo lite";
		else if(obj.valor=="pro")texto="seo pro";
		else if(obj.valor=="premium")texto="seo premium";
		else if(obj.valor=="medida")texto="seo a medida";
		else if(obj.valor=="new")texto="clientes nuevos";
		else if(obj.valor=="old")texto="clientes normales";
		else if(obj.valor=="our")texto="clientes yoseo";
		else texto=obj.valor;
		html+="<div class='item_filter_group' data-filter='"+obj.tipo+"' data-valor='"+obj.valor+"'>"+texto+"<svg onclick='deteleItemFilter(this)' class='delete_item_filter' height='24' viewBox='0 0 24 24' width='17'><path class='btn_detele_item_filter' d='M12 2c-5.53 0-10 4.47-10 10s4.47 10 10 10 10-4.47 10-10-4.47-10-10-10zm5 13.59l-1.41 1.41-3.59-3.59-3.59 3.59-1.41-1.41 3.59-3.59-3.59-3.59 1.41-1.41 3.59 3.59 3.59-3.59 1.41 1.41-3.59 3.59 3.59 3.59z'></path></svg></div>";
		console.log(obj.tipo+" - "+obj.valor);
	}
	$(contenedor).html(html);
	
	$('.pop_up').removeClass('visible');
	
}

function deteleItemFilter(x){
	var data_filter = $(x).closest('div.item_filter_group').attr('data-filter');
	var data_valor = $(x).closest('div.item_filter_group').attr('data-valor');
	
	//desmarcar checkbox del filtro
	
	$(x).closest('.ctoolbar').find('.filter_client .div_filtro div.txt_opciones_filter[data-filter="'+data_filter+'"][data-valor="'+data_valor+'"] input').prop('checked', false);
	$(x).closest('.ctoolbar').find('.filter_client .div_filtro .btn_filter').click();
	$('.pop_up').removeClass('visible');
	//alert(texto);
	
}

function modifyEnlacesEmpleado(x){
	
	var maxValue = parseFloat($(x).closest('tr').find('td.cFollow input').val());
	
	var action = $(x).attr('data-action');
	var oldValue = $(x).siblings('input[type="number"]').val();
	
	
	//obtenemos todos los valores de todos los inputs
	var sumaValores = 0;
	$(x).closest('.div_elem_empl').find('input[type="number"]').each(function(i) {sumaValores += parseFloat($(this).val());});
	var newVal = oldValue;
	if(action == "increase"){
		if(sumaValores < maxValue){
			newVal = parseFloat(oldValue) + 1;
		}
	}else if(action == "decrease"){
		if(oldValue>0){
			newVal = parseFloat(oldValue) - 1;
		}
		
	}
	
	$(x).siblings('input[type="number"]').val(newVal);
	
	//alert(oldValue);
}

function openEmpleadoEnlaces(x){
	stopPropagation();
	$(x).closest('div').siblings('div.div_elem_empl').addClass('visible');
}

function saveEnlacesEmpleado(x){
	
	var empleados = [];
	var obj = {};
	var id_cliente = $(x).closest('tr').attr('id');
	$(x).closest('.div_elem_empl').find('input[type="number"]').each(function(i) {
		var valor = $(this).val();
		var id_empleado = $(this).attr('data-id-empleado');
		var tipo_empleado = $(this).attr('data-tipo-empleado');
		obj = {	'id_cliente': id_cliente, 'id_empleado': id_empleado, 'tipo_empleado': tipo_empleado, 'valor':valor}
		empleados.push(obj);
	});
	
	if(empleados.length>0){
		var json = JSON.stringify(empleados);
		$.post('Data_Clientes', {
			metodo : "modificarEnlacesEmpleado",
			json: json
		}, function(){
			//$('#btnListaClientes').click();
			closeAllPopUps();
		});
	}
}


function resize_head_table_clientes(){
	var tr = $('#tClients tbody tr:first-child');
	
	//$('#tClients thead').width($(tr).outerWidth());
	
	$('#tClients tbody tr:first-child td').each(function(i) {
		var position = i+1;
		var w = $(this).width();
		//alert(position+" -  "+w);
		$('#tClients thead tr th:nth-child('+position+')').width(w);
	});
	
	
	
	$('.resize_head_table_clientes').remove();
	
}
























