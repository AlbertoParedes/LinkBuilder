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
	var posicion = $(x).closest('tr').attr("posicion");
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
		}else if(tipo=="follow" && link_done.trim()!=""){
			nofollows_done++;
		}
	});
	$.post('Data_Enlaces', {
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
function openCategoriaResultado(x){
	$(".rotArrow").removeClass("rotArrow");
	//comprobamos si se ha hecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).children('ul').attr("class");
	$(".slCt").removeClass("visible");
	$(".slWeb").removeClass("visible");
	if(!clase.includes("visible")){
		$(x).children('ul').addClass("visible");
		$(x).children('div').children('i').addClass("rotArrow");
	}
}
function openDestinos(x){
	move2Left(x);
    $(x).children('div.pop_up').addClass('visible');
    
    //alert(position_width_tr + " - "+position_width_pop_up+ " = "+  retroceder);
}
function openWebResultado(x){
	$(".rotArrow").removeClass("rotArrow");
	//comprobamos si se ha hhecho otro click sobre esta misma categoria para cerrar el ul
	var clase = $(x).closest('td').children('ul').attr("class");
	$(".slCt").removeClass("visible");
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
				$(x).children('ul').html(responseText);
				$(x).children('ul').addClass("visible");
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
	$(x).closest('td').children('div').children('span').text('Selecciona una categoría');
	
	$.post('Data_Enlaces', {
		metodo : 'borrarCategoriaResultado',
		id_resultado: id_resultado,
		posicion: posicion
	});
	
}

function updateAnchor(x){
	var id_resultado = $(x).parent().parent().attr("id");
	var anchor = $(x).val();
	
	$.post('Data_Enlaces', {
		metodo : 'cha',
		id_resultado: id_resultado,
		anchor: anchor,
	}, function (){
		//quitamos la clase para que parezca que guardamos los datos
		$("#cGuardar").removeClass('cSave');
	});
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

function saveClient(x){
	var clase_btn_guardar = $("#cGuardar").attr('class');
	if(!clase_btn_guardar.includes("cSave"))
		$("#cGuardar").addClass('cSave');
}

//caundo cambiamos el mes se cambiará la tabla
function changeMonth(){
	var mes = $(".picker .pick-m .pick-sl").val();
	var year = $(".picker .pick-y .pick-sl").val();

	$.post('Data_Enlaces', {
		metodo : 'enlaces_ResultadosMes',
		mes: mes,
		year: year,
	}, function(responseText) {
		$('#results_Client table tbody').html(responseText);
	});
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
	
	$.post('Data_Enlaces', {
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