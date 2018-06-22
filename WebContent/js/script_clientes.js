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
	var id_empleado_anterior = $(x).closest('td').children('div.tdWeb').children('span').attr('data-id-empleado');
	var id_empleado_seleccionado = $(x).attr('data-id-empleado');
	var tipo_empleado_seleccionado = $(x).attr('data-tipo-empleado');
	
	$(x).closest('td').children('div.tdWeb').children('span').attr('data-id-empleado',id_empleado_seleccionado);
	$(x).closest('td').children('div.tdWeb').children('span').attr('data-tipo-empleado',tipo_empleado_seleccionado);
	$(x).closest('td').find('div span').text($(x).text());
	
	if(!$(x).closest('table').attr('id').includes('tNuevoCliente')){
		$.post('Data_Clientes', {
			metodo : "guardarEmpleado",
			id_cliente: id_cliente,
			id_empleado_anterior:id_empleado_anterior,
			id_empleado_seleccionado: id_empleado_seleccionado,
			tipo_empleado_seleccionado: tipo_empleado_seleccionado
		}, function(){
			$("#websGuardar").removeClass('cSave');
			
		});
	}
	
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
	$.post('Data_Clientes', {
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
