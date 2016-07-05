'use strict';
app.controller('UpdateLevelCtrl', ['$http', '$location', '$scope', '$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	var nomeInformadoAntesDaEdicao;
	var idLevel = $routeParams.idLevel;

	$scope.page = 'update-level';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.carregarLevelPorId();
	}
	
	// atualizar Equipamento
	self.updateLevel = function (){
		
		self.level.id = idLevel;
		console.log(self.level);
		
		var config = {
				headers:  {
					'Content-Type': 'application/x-www-form-urlencoded'
				}
	};
		
		$http.put($scope.applicationUrl + "api/level/"+ idLevel, $.param({name: self.level.name, time: self.level.time}), config).then(function (response) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Level <span style='color:#00ff18;'>Editado</span> com sucesso.");
			window.location.href = "#list-level";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com o TwoDev");
			window.location.href = "#home";
			
		});
		
	}
	
	// Carregar Equipamento Por ID
	self.carregarLevelPorId = function (){
		
		console.log(idLevel);

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/level/" + idLevel +"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.level = response.data;
			
			self.nomeInformadoAntesDaEdicao = self.level.name;
			self.timeInformadoAntesDaEdicao = self.level.time;
			
		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			window.location.href = "#home";
		});
		
	}
	
}]);