'use strict';
app.controller('ContestListCtrl', ['$http', '$location', '$scope','$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	
	$scope.page = 'list-contest';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listarContest();
	}

	// ----- Filter List.
    $scope.filterList = function () {
    	var arrayFilter = [];
    	for (var int = 0; int < $scope.initialDataOfGenericList.length; int++) {
    		var sInput = $scope.filterListInput;
    			sInput = sInput.toLowerCase();
			var sComparative = "";
			sComparative = $scope.initialDataOfGenericList[int].name;
			sComparative = sComparative.toLowerCase();
			if( sComparative.indexOf(sInput) != -1 ){
	    		arrayFilter.push( $scope.initialDataOfGenericList[int] );
			}
		}
    	$scope.currentPageOfList = 0;
    	$scope.dataOfGenericList = arrayFilter;
    }
    // ----- ./Filter List.   
    	
	//---- ./List Content.	
	self.listarContest = function () {
		
		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/contest/active"+"?radom="+Math.random() ).then(function(response) {

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
	//----- ./List Reserva Local.
	
	//-------/Cancelar Reserva
	self.inativaContestList = function (idContent) {
		
    	console.log(idContent);
    	
    	var req = {
    			method: 'DELETE',
    			url: $scope.applicationUrl + "api/contest/" + idContent,
    	}
		
		$http(req).then(function (response) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Concurso <span style='color:#00ff18;'>CANCELADO</span> com sucesso.");
			window.location.href = "#list-contest";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com TWODEV");
			window.location.href = "#home";
			
		})};
		
	//--------/Cancelar Reserva
}]);