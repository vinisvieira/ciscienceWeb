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