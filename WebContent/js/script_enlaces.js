
function cargarVistaEnlaces(){
	$.post('Data_Enlaces', {
		metodo : 'cargarVistaEnlaces',
	}, function (response){
		$('#vistaEnlaces').html(response);
	});
}





var clienteSeleccionado=0;
function enlaces_SelectClient(id_client,x) {
	//mostramos que estamos cargando la informacion
	$('.loader').removeClass("fadeIn");
	$('.blockAll').addClass('visible');
	$(x).children('.loader').addClass("fadeIn");
	
	
	var id_cliente_before = "";
	var mes = $('.datedropper .picker [data-k="m"] .pick-sl').val();
	mes = mes <10 ? "0"+mes : 0;
	var fecha = $('.datedropper .picker [data-k="y"] .pick-sl').val()+"-"+mes;//obtenemos el año y la fecha
	
	//obtenemos los usuarios seleccionados
	/*$('.filtros_Enlaces').find('div[data-filter="empleado"]').find('input:ckeched').each(function(){
		alert($this.closest('div[data-filter="empleado"]').attr('data-valor'));
	});*/
	var users= "";
	$('.filtros_Enlaces').find('div[data-filter="empleado"]').find('input:checked').each(function(){
		users += $(this).closest('div[data-filter="empleado"]').attr('data-valor')+","
	})
	users += ";";users = users.replace(",;","");
	//alert(users);
	
	//revisamos si el cliente seleccionado esta disponible
	$.post('Data_Enlaces', {
		metodo : "enlaces_SelectClient",
		id_client : id_client,
		fecha : fecha,
		users:users
	}, function(response) {
		
		if(response.blocked=="1"){
			
			$(x).children('.pop_up_info_blocked').children('.msg_name_blocked').text(response.userEditando);
			$(x).children('.pop_up_info_blocked').addClass("fadeIn");
			$(x).children('.loader').removeClass("fadeIn");
			
			$('.blockAll').removeClass('visible');
			setTimeout(function () {
				$(x).children('.pop_up_info_blocked').removeClass("fadeIn");
		    }, 5000);
			
			//cambiar vista a bloqueada
			$(x).children('.blockClient').removeClass('visible').addClass('visible');
			$(x).children('.itemChild').removeClass('blur').addClass('blur');
			$(x).children('.itemChild').children('.noti').removeClass('opacity_0').addClass('opacity_0');

		}else if(response.blocked=="2"){
			$(x).children('.loader').removeClass("fadeIn");
			$('.blockAll').removeClass('visible');

		}else if(response.blocked=="0"){
			$('#uniqueClient').html(response.html);
			$(x).children('.itemChild').children('.noti').replaceWith(response.noti);
			changeThemeC(id_client);
			clienteSeleccionado = id_client;
			
			$(x).children('.blockClient').removeClass('visible');
			$(x).children('.itemChild').removeClass('blur');
			$(x).children('.itemChild').children('.noti').removeClass('opacity_0');
		}
		
		//setTimeout(function () {$(x).children('.loader').removeClass("fadeIn");}, 600);
		
	});
}


function guardarEnlaceResultado(x){
	bloqueamosPantalla();
	
	var id_resultado = $(x).closest('tr').attr("id");
	//var posicion = $(x).closest('tr').attr("posicion");
	var link = $(x).val();
	var tipo = $("tr#"+id_resultado+" .cTipo").attr("tipo");
	
	var mes = $('.datedropper .picker [data-k="m"] .pick-sl').val();  mes = mes <10 ? "0"+mes : 0;
	var year = $('.datedropper .picker [data-k="y"] .pick-sl').val();

	var id_empleado = $(x).closest('tr').children('td.cCUser').find("div span[data-info='empCli']").attr('data-id-empleado');
	if(link.trim()!=""){
		$(x).closest('tr').find('td.cCUser div span[data-info="empDone"]').attr('data-id-empleado-done', id_empleado);
	}else{
		$(x).closest('tr').find('td.cCUser div span[data-info="empDone"]').attr('data-id-empleado-done', -1);
	}
	
	var follows_por_hacer=0,nofollows_done=0;
	
	$(x).closest('tbody').find('tr td.cLink input').each(function(){
		var value = $(this).val();
		if(value.trim() == "") follows_por_hacer ++;
	});
	
	
	var follows_done_empleado=0;
	
	//empleado que tiene que hacer este enlace
	$(x).closest('tbody').find('tr td.cCUser').find("div span[data-id-empleado-done='"+id_empleado+"']").each(function(){
		follows_done_empleado ++;
	});
	
	
	//bloqueamos toda la pantalla
	
	$.post('Data_Enlaces', {
		metodo : 'guardarEnlaceResultado',
		id_resultado: id_resultado,
		//posicion: posicion,
		link: link,
		tipo: tipo,
		//follows_done: follows_done,
		nofollows_done: nofollows_done,
		
		id_empleado: id_empleado,
		follows_done_empleado: follows_done_empleado,
		follows_por_hacer:follows_por_hacer,
		
		mes: mes,
		year: year
	}, function(responseText) {
		$("#cGuardar").removeClass('cSave');
		
		$('#lstC .item_select .itemChild .noti').replaceWith(responseText);
		
		var tipo = $(x).parent().parent().children().children('.divStatus');
		tipo.removeClass("sOK").removeClass("sPendiente");
		if(link.trim()!=""){
			tipo.addClass("sOK");
		}else{
			tipo.addClass("sPendiente");
		}
		desbloqueamosPantalla();
	});
	
	
	
	
}
function openCategoriaResultado(x){
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	//comprobamos si se ha hecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).children('ul').attr("class");
	$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
	$(".slWeb").removeClass("visible");
	$(x).find('ul li').removeClass("liActive");
	if(!clase.includes("visible")){
		$(x).children('ul').addClass("visible");
		$(x).children('div').children('i').addClass("rotArrow");
		var id_categoria = $(x).children('div.tdCat').children('span').attr('data-id-categoria');
		$(x).find('ul li#'+id_categoria).addClass('liActive');	
	}
}
function openDestinos(x){
	move2Left(x);
    //$(x).children('div.pop_up').addClass('visible');
    
    $(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).closest('td').find('div.pop_up').attr("class");
	$(".slCt").removeClass("visible");
	$(".pop_up").removeClass("visible");
	$(".slWeb").removeClass("visible");
	if(!clase.includes("visible")){
		$(x).children('div.pop_up').addClass('visible');
		$(x).children('div').children('i').addClass("rotArrow");
	}
    
    
    //alert(position_width_tr + " - "+position_width_pop_up+ " = "+  retroceder);
}
function enlaces_nuevoDestino(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var url = $(x).closest('div').children('input').val();
	$.post('Data_Enlaces', {
		metodo : 'enlaces_nuevoDestino',
		id_resultado: id_resultado,
		url: url
	}, function(rt) {
		if(rt.status=="0"){
			alert("El destino debe contener http:// o https://");
		}else{
			$(x).closest('div').children('input').val("");
			$(".slCt").removeClass("visible");
			$(".pop_up").removeClass("visible");
			$(".slWeb").removeClass("visible");
			$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
			$(x).closest('td').find('div.tdWeb span').text(url);
		}
	});
}
function enlaces_nuevoDestinoEnter(e,x){
    var keycode = (e.keyCode ? e.keyCode : e.which);
    if (keycode == '13') {
    	enlaces_nuevoDestino(x);
    }
}
function openWebResultado(x){
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).closest('td').find('div.pop_up').attr("class");
	$(".slCt").removeClass("visible");
	$(".pop_up").removeClass("visible");
	$(".slWeb").removeClass("visible");
	if(!clase.includes("visible")){
		var id_resultado = $(x).closest('tr').attr('id');
		var id_categoria = $(x).closest('tr').find('td.cCateg div span[data-id-categoria]').attr('data-id-categoria');
		//if (typeof id_categoria !== 'undefined'){
			$.post('Data_Enlaces', {
				metodo : 'mostrarWebResultado',
				id_categoria: id_categoria,
				id_resultado: id_resultado
			}, function(responseText) {
				$(x).find('div.pop_up ul').html(responseText);
				$(x).find('div.pop_up').addClass("visible");
				//modificamos la flecha 
				$(x).children('div').children('i.arrow').addClass("rotArrow");
				
			});
		//}
		
	}
}
function borrarCategoria(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var posicion = $(x).closest('tr').attr("posicion");
	$(x).closest('ul').removeClass("visible");
	//$(x).closest('td').children('div').children('span').text('Selecciona una categoría');
	
	var id_foro_anterior = $(x).closest('tr').children('td.cWeb').children('div').children('span').attr("data-id-foro");
	var categoria_foro_anterior = $(x).closest('tr').children('td.cWeb').children('div').children('span').attr("data-id-categoria");
	var medio_anterior = $(x).closest('tr').children('td.cWeb').children('div').children('span').text();
	var descripcion_foro_anterior = $(x).closest('tr').children('td.cWeb').children('textarea').text();
	
	$.post('Data_Enlaces', {
		metodo : 'borrarCategoriaResultado',
		id_resultado: id_resultado,
		posicion: posicion,
		id_foro_anterior:id_foro_anterior,
		categoria_foro_anterior:categoria_foro_anterior,
		medio_anterior:medio_anterior,
		descripcion_foro_anterior: descripcion_foro_anterior
	}, function (rt){
		var spanMedio = $(x).closest('tr').children('td.cWeb').find('div.tdWeb span');
		$(spanMedio).attr("data-id-foro","0");
		$(spanMedio).attr("data-posicion-foro","-1");
		$(spanMedio).attr("data-id-categoria","0");
		$(spanMedio).text("");
		$(x).closest('tr').children('td.cWeb').find('div.tdWeb i.description_enlace').remove();
		
		$(x).closest('td').children('div.tdCat').html(rt);
		$(x).closest('td').find('li').removeClass("liActive");
	});
	
}

function enlaces_guradarDestino(x){
	bloqueamosPantalla();
	
	var id_resultado = $(x).closest('tr').attr("id");
	var destino = $(x).text();
	
	//combio en el span de mi td
	$(x).closest('td').find('.tdWeb span').text(destino);
	
	//si se seleciona la X borraremos ese resultado de nuestra bbdd y de nuestro array
	if(typeof $(x).attr('class') != 'undefined'){
		if($(x).attr('class').includes('crossReset')){
			$(x).closest('td').children('div').children('span').text('');
			destino="";
		}
	}

	$.post('Data_Enlaces', {
		metodo : 'enlaces_guradarDestino',
		id_resultado: id_resultado,
		destino: destino,
	}, function (){
		desbloqueamosPantalla();
	});
}

function guardarCategoriaResultado(x){// controlar que cuando selecciones otra categoria se elimine el foro 
	bloqueamosPantalla();
	var id_resultado = $(x).closest('tr').attr("id");
	var posicion = $(x).closest('tr').attr("posicion");
	var id_categoria = $(x).attr("id");
	
	var id_medio_seleccionado = $(x).closest('tr').children('td.cWeb').find("div.tdWeb span").attr('data-id-foro');
	
	
	
	$(x).closest('ul').children('li').removeClass("liActive");
	$(x).addClass("liActive");
	$(x).closest('ul').removeClass("visible");
	
	if(id_medio_seleccionado==0){//si el campo del medio esta vacio o lo que el lo mismo, su id_foro es == 0 podremos cambiar de categoria sino no
		var op = $(x).text();
		$(x).closest('td').find('div span[data-id-categoria]').text(op);
		
		$.post('Data_Enlaces', {
			metodo : 'guardarCategoriaResultado',
			id_categoria: id_categoria,
			id_resultado: id_resultado,
			posicion: posicion
		}, function (){
			$(x).closest('td').find('div span[data-id-categoria]').attr('data-id-categoria', id_categoria);
			desbloqueamosPantalla();
		});
	}
	
}

function saveClient(x){
	var clase_btn_guardar = $("#cGuardar").attr('class');
	if(!clase_btn_guardar.includes("cSave"))
		$("#cGuardar").addClass('cSave');
}

//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	var mes = $(".picker .pick-m .pick-sl").val();
	mes = mes <10 ? "0"+mes : mes;
	var year = $(".picker .pick-y .pick-sl").val();

	var item = $('#lstC').find('.loader.fadeIn').attr('class');
	if(typeof item === 'undefined'){
		$('#lstC div.item_select').children('.loader').addClass('fadeIn');
		$('.blockAll').addClass('visible');
	}
	
	
	var filtros = [];
	var obj = {};
	$('div.filtros_Enlaces').find('div.txt_opciones_filter input:checked').each(function(){
		var tipo = $(this).closest('div.txt_opciones_filter').attr('data-filter');
		var valor = $(this).closest('div.txt_opciones_filter').attr('data-valor');
		var name = $(this).closest('div.txt_opciones_filter').attr('data-name');
		obj = {	'tipo': tipo, 'valor': valor, 'name' : name}
		filtros.push(obj);
	});
	var jsonEnlaces = JSON.stringify(filtros);
	
	$.post('Data_Enlaces', {
		metodo : 'enlaces_ResultadosMes',
		mes: mes,
		year: year,
		jsonEnlaces : jsonEnlaces
	}, function(responseText) {
		$('#results_Enlaces table tbody').html(responseText);
		resize_head_table_enlaces();
		setTimeout(function () {
			$('.loader').removeClass("fadeIn");
	    }, 600);
		$('.blockAll').removeClass('visible');
		
	});
}

function buscarMedio(x){
	var texto = $(x).val();
	var id_categoria = $(x).closest('tr').find('td.cCateg div span[data-id-categoria]').attr('data-id-categoria');
	
	$.post('Data_Enlaces', {
		metodo : 'enlaces_buscarMedio',
		texto: texto,
		id_categoria: id_categoria
	}, function(responseText) {
		$(x).closest('td').find('div.pop_up ul').html(responseText);
	});
	
}

function guardarWebResultado(x){
	bloqueamosPantalla();
	
	$(x).closest('ul').children('li').removeClass("liActive");
	$(x).addClass("liActive");
	var id_categoria_tdCategoria = $(x).closest('tr').children('td.cCateg').find('div.tdCat span').attr("data-id-categoria");
	
	
	var id_resultado=$(x).closest('tr').attr('id');
	
	var id_foro=$(x).attr('data-id-foro');//id del foro selecionado
	var posicion_foro = $(x).attr('data-posicion-foro');// posicion en el array del foro seleccionado
	var categoria_foro = $(x).attr('data-id-categoria');
	
	var id_foro_anterior = $(x).closest('td').children('div').children('span').attr("data-id-foro");
	var posicion_foro_anterior = $(x).closest('td').children('div').children('span').attr("data-posicion-foro");
	var categoria_foro_anterior = $(x).closest('td').children('div').children('span').attr("data-id-categoria");
	var medio_anterior = $(x).closest('td').children('div').children('span').text();
	var descripcion_foro_anterior = $(x).closest('td').children('textarea').text();
	
	//var posicionResultado = $(x).closest('tr').attr('posicion');
	
	//si se seleciona la X borraremos ese resultado de nuestra bbdd y de nuestro array
	if($(x).attr('class').includes('crossReset')){
		$(x).closest('td').children('div').children('span').text('');
		id_foro = 0;
		posicion_foro = -1;
		categoria_foro = 0;
	}
	
	$.post('Data_Enlaces', {
		metodo : 'guardarWebResultado',
		id_categoria_tdCategoria:id_categoria_tdCategoria,
		
		id_resultado: id_resultado,
		id_foro: id_foro,	
		posicion_foro: posicion_foro,
		categoria_foro: categoria_foro,
		
		id_foro_anterior:id_foro_anterior,
		posicion_foro_anterior:posicion_foro_anterior,
		categoria_foro_anterior: categoria_foro_anterior,
		medio_anterior: medio_anterior,
		descripcion_foro_anterior:descripcion_foro_anterior
	}, function(rt) {
		
		if(id_categoria_tdCategoria==0){
			$(x).closest('tr').children('td.cCateg').find('div.tdCat span').attr("data-id-categoria", categoria_foro);
			var textoCategoria = $(x).closest('tr').find('td.cCateg ul li#'+categoria_foro).text();
			if(textoCategoria!=""){
				$(x).closest('tr').children('td.cCateg').find('div.tdCat span').text(textoCategoria);
			}
			
		}
		$(x).closest('td').children('div.tdCat').html(rt.html);
		$(x).closest('td').children('textarea').text(rt.descripcion);
		
		desbloqueamosPantalla();
	});
	
}
function guardarAnchor(x){
	bloqueamosPantalla();
	var id_resultado = $(x).closest('tr').attr("id");
	var anchor = $(x).val();
	
	$.post('Data_Enlaces', {
		metodo : 'enlaces_GuardarAnchor',
		id_resultado: id_resultado,
		anchor: anchor,
	}, function(responseText) {
		$('#cGuardar').removeClass('cSave');
		desbloqueamosPantalla();
	});
	
}

function enlace_openDescription(x){
	
	
	$(".rotArrow").removeClass("rotArrow");$("i.description_enlace").removeClass("lf");
	//comprobamos si se ha hecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).children('ul').attr("class");
	$(".slCt").removeClass("visible");$(".pop_up").removeClass("visible");
	$(".slWeb").removeClass("visible");
	
	stopPropagation();
	$(x).addClass("lf");
	
	var textarea = $(x).closest('td').children('.div_enlaces_descripcion_pop_up');
	
	$(textarea).addClass('visible');
	resizeta(textarea,1);
}

function searchCliente(x,e) {
	
	
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
			aplicarFiltroEnlaces(x);
		}
	}
	

	
}

//function searchClient(x) {
//	
//	var k = $('#searchC').val();
//	
//	$.post('Data_Enlaces', {
//		metodo : 'enlaces_BuscarCliente',
//		keyword: k,
//		clienteSeleccionado:clienteSeleccionado
//	}, function(responseText) {
//		$('#lstC').html(responseText);
//		if(k == ""){
//			$('#lstC').animate({ 
//				scrollTop: $('#lstC').scrollTop() + $("#lstC .item_select").offset().top - $('#lstC').offset().top 
//				}, { duration: 1000, easing: 'linear'});
//		}
//	});
//	
//}

function aplicarFiltroEnlaces(x){
	var filtros = [];
	var obj = {};
	$('#lcc').find('div.filtros_Enlaces div.txt_opciones_filter input:checked').each(function(){
		var tipo = $(this).closest('div.txt_opciones_filter').attr('data-filter');
		var valor = $(this).closest('div.txt_opciones_filter').attr('data-valor');
		var name = $(this).closest('div.txt_opciones_filter').attr('data-name');
		obj = {	'tipo': tipo, 'valor': valor, 'name' : name}
		filtros.push(obj);
	});
	
	if($(x).attr("class").includes('btn_filter')){
		viewFiltersEnlaces(filtros,x);
	}
	
	var jsonEnlaces = JSON.stringify(filtros);
	var busquedaCliente = $('#lcc').find('input#searchC').val();
	$.post('Data_Enlaces', {
		metodo : "aplicarFiltrosEnlaces",
		jsonEnlaces: jsonEnlaces,
		busquedaCliente: busquedaCliente,
		clienteSeleccionado:clienteSeleccionado
	}, function(response){
		$('#lcc div.infoCategory i.liActive').removeClass('liActive');
		$('#lcc #lstC').html(response.listaClientes);
		$('#lcc .infoCategory .info').html(response.n_clientes +" clientes");
		if(busquedaCliente == "" && clienteSeleccionado>0){
			$('#lstC').animate({ 
				scrollTop: $('#lstC').scrollTop() + $("#lstC .item_select").offset().top - $('#lstC').offset().top - 40
			}, { duration: 1000, easing: 'linear'});
			
			$("#lstC .item_select").click();
		}
	});
}

function openFilterEnlaces(x){
	
	var height_filtro_selected = $(x).closest('#lcc').find('.div_filtros_enlaces .filtros_Enlaces_seleccionados').outerHeight();
	var height_filtro = $(x).closest('#lcc').find('.div_filtros_enlaces .filtros_Enlaces').outerHeight();
	var suma_height = height_filtro + height_filtro_selected;
	if(!$(x).attr("class").includes('liActive')){
		$(x).addClass('liActive');
		$(x).closest('#lcc').children('#lstC').stop().animate({ marginTop: suma_height+"px"}, 200);
	}else{
		cerrarFiltrosEnlaces(x, height_filtro_selected);
	}
	

}

function viewFiltersEnlaces(filtros,x){
	var html="", texto="";
	var contenedor = $('#lcc .filtros_Enlaces_seleccionados');
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
		else if(obj.tipo=="empleado")texto=obj.name;
		else texto=obj.valor;
		html+="<div class='item_filter_group mgn_top' data-filter='"+obj.tipo+"' data-valor='"+obj.valor+"'>"+texto+"<svg onclick='deteleItemFilterEnlaces(this)' class='delete_item_filter' height='24' viewBox='0 0 24 24' width='17'><path class='btn_detele_item_filter' d='M12 2c-5.53 0-10 4.47-10 10s4.47 10 10 10 10-4.47 10-10-4.47-10-10-10zm5 13.59l-1.41 1.41-3.59-3.59-3.59 3.59-1.41-1.41 3.59-3.59-3.59-3.59 1.41-1.41 3.59 3.59 3.59-3.59 1.41 1.41-3.59 3.59 3.59 3.59z'></path></svg></div>";
		console.log(obj.tipo+" - "+obj.valor);
	}
	$(contenedor).html(html);
	var altura = $(contenedor).outerHeight()
	cerrarFiltrosEnlaces(x,altura);
	
}

function cerrarFiltrosEnlaces(x, altura){
	$(x).removeClass('liActive');
	$(x).closest('#lcc').children('#lstC').stop().animate({ marginTop: altura+"px"}, 200);
}

function deteleItemFilterEnlaces(x){
	var data_filter = $(x).closest('div.item_filter_group').attr('data-filter');
	var data_valor = $(x).closest('div.item_filter_group').attr('data-valor');
	
	//desmarcar checkbox del filtro
	
	$(x).closest('.filtros_Enlaces_seleccionados').siblings('.filtros_Enlaces').find('div.txt_opciones_filter[data-filter="'+data_filter+'"][data-valor="'+data_valor+'"] input').prop('checked', false);
	$(x).closest('.filtros_Enlaces_seleccionados').siblings('.filtros_Enlaces').find('.btn_filter').click();
	$('.pop_up').removeClass('visible');
	//alert(texto);
	
}

function viewEmpleadoDone(x){
	
	var clase = $(x).closest('tr').find('th.cCUser i').attr('class');
	
	if (!clase.includes('sOK_color')){
		$(x).closest('tr').find('th.cCUser i').addClass('sOK_color');
		
		$(x).closest('table').find('tbody tr td.cCUser div span.empleadoDefault').removeClass('visible');
		$(x).closest('table').find('tbody tr td.cCUser div span.empleadoDone').addClass('visible');
		
	}else{
		
		$(x).closest('tr').find('th.cCUser i').removeClass('sOK_color');
		$(x).closest('table').find('tbody tr td.cCUser div span.empleadoDefault').addClass('visible');
		$(x).closest('table').find('tbody tr td.cCUser div span.empleadoDone').removeClass('visible');
	}
	
}

function resize_head_table_enlaces(){
	/*var tr = $('#tClientes tbody tr:first-child');
	
	$('#tClientes tbody tr:first-child td').each(function(i) {
		var position = i+1;
		var w = $(this).width();
		//alert(position+" -  "+w);
		$('#tClientes thead tr th:nth-child('+position+')').width(w);
	});
	*/
}


























