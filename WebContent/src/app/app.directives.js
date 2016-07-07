'use strict';
app.directive('myInputDate', function() {

	return function(scope, element, attrs) {

		element.attr("placeholder", " / /");
		element.attr("onkeyup", "mascara(this, mdata)");
		element.attr("maxlength", "10");
		element.attr("autocomplete", "off");
		$(element).focusout(function() {
			var str = mdata($(this).val());
			;
			if (str.length < 10) {
				$(element).val('');
			}
		});

	}

});

app.directive('myInputCpf', function() {

	return function(scope, element, attrs) {

		element.attr("placeholder", " . . -");
		element.attr("onkeyup", "mascara(this, mcpf)");
		element.attr("maxlength", "14");
		element.attr("autocomplete", "off");
		$(element).focusout(function() {
			var str = mcpf($(this).val());
			;
			if (str.length < 14) {
				$(element).val('');
			}
		});
	}
});

app.directive('myInputDouble', function() {

	return function(scope, element, attrs) {
		element.attr("onkeyup", "mascara(this, mvalor)");
		element.attr("autocomplete", "off");
		$(element).focusout(function() {

			var str = mvalor($(this).val());
			if (str.length == 1 || str.length == 2) {
				$(element).val(str + '.00');
			} else {
				$(this).val(mvalor($(this).val()));
			}

		});
	}

});

app.directive('myInputInteger', function() {

	return function(scope, element, attrs) {

		element.attr("onkeyup", "mascara(this, mnum)");
		element.attr("autocomplete", "off");
		$(element).focusout(function() {
			$(this).val(mnum($(this).val()));
		});

	}

});
app.directive('myNewInputDate', function() {
	
	return {
	    require: 'ngModel',
	    link: function(scope, el, attr, ngModel) {
	      
	      $(el).attr("maxlength","10");
	      $(el).attr("autocomplete","off");
	      $(el).attr("placeholder"," / /");
	      $(el).attr("onkeyup","mascara(this, mdata)");
	      $(el).focusout(function () {
	    	  $(el).val( $(el).val() );
	    	  var str = mdata( $(this).val() );;
	    	  if(str.length<10){
	    		  $(el).val('');
	    	  }
	      });
	      if( $(el).attr("data-minimal-date-today") == "true" ){
	    	  
	    	  var today = new Date();
	    	  var dd = today.getDate();
	    	  var mm = today.getMonth(); //January is 0!
	    	  var yyyy = today.getFullYear();
	    	  
	    	  $(el).datepicker({
		    	  dateFormat: 'dd/mm/yy',
		    	  dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
		    	  dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
		    	  dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
		    	  monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		    	  monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		    	  nextText: 'Próximo',
		    	  prevText: 'Anterior',
		    	  minDate: new Date(yyyy, mm, dd),
		    	  onSelect: function(dateText) {
		    		  scope.$apply(function() {
		    			  ngModel.$setViewValue(dateText);
		    		  });
		    	  }
		      });
	      }else{
	    	  $(el).datepicker({
		    	  dateFormat: 'dd/mm/yy',
		    	  dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
		    	  dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
		    	  dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
		    	  monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		    	  monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		    	  nextText: 'Próximo',
		    	  prevText: 'Anterior',
		    	  onSelect: function(dateText) {
		    		  scope.$apply(function() {
		    			  ngModel.$setViewValue(dateText);
		    		  });
		    	  }
		      });
	      }
	    }
	  };

});