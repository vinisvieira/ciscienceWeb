'use strict';
app.directive('myInputDate', function() {

	return function(scope, element, attrs) {

		element.attr("placeholder"," / /");
		element.attr("onkeyup","mascara(this, mdata)");
		$(element).datepicker({
			dateFormat: 'dd/mm/yy',
			dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
		    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
		    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
		    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		    nextText: 'Próximo',
		    prevText: 'Anterior'
		});
		element.attr("maxlength","10");
		element.attr("autocomplete","off");
		$(element).focusout(function () {
			var str = mdata( $(this).val() );;
			if(str.length<10){
				$(element).val('');
			}
		});

	}

});

app.directive('myInputDouble', function() {

	return function(scope, element, attrs) {
		element.attr("onkeyup","mascara(this, mvalor)");
		element.attr("autocomplete","off");
		$(element).focusout(function () {
			
			var str = mvalor( $(this).val() );
			if(str.length==1 || str.length==2){
				$(element).val( str + '.00');
			}else{
				$(this).val( mvalor( $(this).val() ) );
			}

		});
	}

});

app.directive('myInputInteger', function() {

	return function(scope, element, attrs) {

		element.attr("onkeyup","mascara(this, mnum)");
		element.attr("autocomplete","off");
		$(element).focusout(function () {
			$(this).val( mnum( $(this).val() ) );
		});

	}

});