'use strict';
app.controller('NewLevelCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'new-level';
		
	$scope.onLoadHtmlFileInNgView = function () {
		
	}
	
	// Verificar Existencia
	self.verificarExistencia = function() {
		
		appCtrl.loadSpiner(true);
		
		var levelObj = self.level;
		
		var req = {
			method: 'GET',
			url: $scope.applicationUrl + "api/level/by-name"+"?radom="+Math.random(),
			headers: {
				'name': levelObj.name
			}
		}
		$http(req).then(function(response){
			
			if( response.data.length == 0 ){
				//Chamar função para inserir
				if (self.level.time < 0) {
					appCtrl.loadSpiner(false);
					appCtrl.loadSnackbar("Tempo não pode ser menor que 0");
				} else {
					self.inserirLevel();
				}
			}else{
				appCtrl.loadSpiner(false);
				appCtrl.loadSnackbar("Já existe um Level com o nome informado");
			}

		}, function(response){
			//ERRO
			console.log("ERRO");
		});

	}
	// ./Verificar Existencia
	
	// Inserir Level
	self.inserirLevel = function (){

		appCtrl.loadSpiner(true);

		$.post( $scope.applicationUrl + "api/level", self.level ).done( function(returnOfRequest) {
		//On Finish
		}).done(function(returnOfRequest) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Level <span style='color:#00ff18;'>INSERIDO</span> com sucesso.");
			window.location.href = "#list-level";

		}).fail(function() {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com TwoDev");
			window.location.href = "#home";
		
		}).always(function() {
		
		});
	}
	// ./Inserir Level

}]);