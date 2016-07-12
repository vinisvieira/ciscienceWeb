'use strict';
app.controller('ListQuestionCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'list-question';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listQuestions();
	}

	// ----- Filter List
    $scope.filterList = function () {
    	var arrayFilter = [];
    	for (var int = 0; int < $scope.initialDataOfGenericList.length; int++) {
    		var sInput = $scope.filterListInput;
    			sInput = sInput.toLowerCase();
			var sComparative = "";
			sComparative = $scope.initialDataOfGenericList[int].text;
			sComparative = sComparative.toLowerCase();
			if( sComparative.indexOf(sInput) != -1 ){
	    		arrayFilter.push( $scope.initialDataOfGenericList[int] );
			}
		}
    	$scope.currentPageOfList = 0;
    	$scope.dataOfGenericList = arrayFilter;
    }
	// ----- ./Filter List

	self.listQuestions = function () {

		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/question"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);

			var arrayForNgRepeat = response.data;

			//Ordenar Por Name -- INICIO
			arrayForNgRepeat.sort(function(a,b) {
			    if(a.name < b.name) return -1;
			    if(a.name > b.name) return 1;
			    return 0;
			});
			//Ordenar Por Name -- TÉRMINO

			$scope.dataOfGenericList = arrayForNgRepeat;
			$scope.initialDataOfGenericList = arrayForNgRepeat;

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			alert("Foi Erroo");
			window.location.href = "#home";
		});

	}
	
	//----- Inactivate Equipamento.
    self.inativaQuestion = function (idQuestion) {
		
    	console.log(idLevel);
    	
    	var req = {
    			method: 'DELETE',
    			url: $scope.applicationUrl + "api/question/" + idQuestion,
    	}
		
		$http(req).then(function (response) {
			
			/*appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Grupo <span style='color:#00ff18;'>ALTERADO</span> com sucesso.");*/
			window.location.href = "#list-question";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com o TwoDev");
			window.location.href = "#home";
			
		})};
		
}]);