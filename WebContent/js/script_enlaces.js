function enlaces_SelectClient(id_client) {
	
	var id_cliente_before = "";
	var mes = $('.datedropper .picker [data-k="m"] .pick-sl').val();
	mes = mes <10 ? "0"+mes : 0;
	var fecha = $('.datedropper .picker [data-k="y"] .pick-sl').val()+"-"+mes;//obtenemos el año y la fecha
	
	//revisamos si el cliente seleccionado esta disponible
	$.post('Data_Enlaces', {
		metodo : "enlaces_SelectClient",
		id_client : id_client,
		fecha : fecha
	}, function(responseText) {
		
		$('#uniqueClient').html(responseText);
		changeThemeC(id_client);
		/*
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
		$.post('Data_Enlaces', {
			metodo : "enlaces_CheckClients",
			id_client : id_client,
			id_cliente_before: id_cliente_before
		}, function(responseText) {
			//actualizamos la lsita de clientes 
			$('#lstC').html(responseText);
			changeThemeC(id_cliente_before);
		});
		
		*/
	});
}


function guardarEnlaceResultado(x){
	
	var id_resultado = $(x).closest('tr').attr("id");
	//var posicion = $(x).closest('tr').attr("posicion");
	var link = $(x).val();
	var tipo = $("tr#"+id_resultado+" .cTipo").attr("tipo");
	
	//var mes = $(".datedropper .picker .pick-m li.pick-sl").attr("value");
	//var year = $(".datedropper .picker .pick-y li.pick-sl").attr("value");
	
	var mes = $('.datedropper .picker [data-k="m"] .pick-sl').val();  mes = mes <10 ? "0"+mes : 0;
	var year = $('.datedropper .picker [data-k="y"] .pick-sl').val();

	var follows_done=0,nofollows_done=0;
	$(x).closest('tbody').children('tr').each(function(){
		var tipo = $(this).children(".cTipo").attr("tipo");
		var link_done = $(this).children(".cLink").children('input').val();
		if(tipo=="follow" && link_done.trim()!=""){
			follows_done++;
		}else if(tipo=="nofollow" && link_done.trim()!=""){
			nofollows_done++;
		}
	});
	
	$.post('Data_Enlaces', {
		metodo : 'guardarEnlaceResultado',
		id_resultado: id_resultado,
		//posicion: posicion,
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
function openCategoriaResultado(x){
	$(".rotArrow").removeClass("rotArrow");
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
    
    $(".rotArrow").removeClass("rotArrow");
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
			$(".rotArrow").removeClass("rotArrow");
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
	$(".rotArrow").removeClass("rotArrow");
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
				$(x).children('div').children('i').addClass("rotArrow");
				
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
	
	$.post('Data_Enlaces', {
		metodo : 'borrarCategoriaResultado',
		id_resultado: id_resultado,
		posicion: posicion,
		id_foro_anterior:id_foro_anterior,
		categoria_foro_anterior:categoria_foro_anterior,
		medio_anterior:medio_anterior
	}, function (rt){
		var spanMedio = $(x).closest('tr').children('td.cWeb').find('div.tdWeb span');
		$(spanMedio).attr("data-id-foro","0");
		$(spanMedio).attr("data-posicion-foro","-1");
		$(spanMedio).attr("data-id-categoria","0");
		$(spanMedio).text("");
		
		$(x).closest('td').children('div.tdCat').html(rt);
		$(x).closest('td').find('li').removeClass("liActive");
	});
	
}

function enlaces_guradarDestino(x){
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
		
	});
}

function guardarCategoriaResultado(x){// controlar que cuando selecciones otra categoria se elimine el foro 
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

	$.post('Data_Enlaces', {
		metodo : 'enlaces_ResultadosMes',
		mes: mes,
		year: year,
	}, function(responseText) {
		$('#results_Client table tbody').html(responseText);
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
		medio_anterior: medio_anterior
	}, function(responseText) {
		
		if(id_categoria_tdCategoria==0){
			$(x).closest('tr').children('td.cCateg').find('div.tdCat span').attr("data-id-categoria", categoria_foro);
			var textoCategoria = $(x).closest('tr').find('td.cCateg ul li#'+categoria_foro).text();
			if(textoCategoria!=""){
				$(x).closest('tr').children('td.cCateg').find('div.tdCat span').text(textoCategoria);
			}
			
		}
		$(x).closest('td').children('div.tdCat').html(responseText);
		
	});
	
}
function guardarAnchor(x){
	var id_resultado = $(x).closest('tr').attr("id");
	var anchor = $(x).val();
	
	$.post('Data_Enlaces', {
		metodo : 'enlaces_GuardarAnchor',
		id_resultado: id_resultado,
		anchor: anchor,
	}, function(responseText) {
		$('#cGuardar').removeClass('cSave');
	});
	
}



























