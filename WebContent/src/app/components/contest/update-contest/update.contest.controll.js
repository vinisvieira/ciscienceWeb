'use strict';
app.controller('UpdateContestCtrl', ['$http', '$location', '$scope', '$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	var nomeInformadoAntesDaEdicao;
	var idContest = $routeParams.idContest;

	$scope.page = 'update-contest';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.getByIdContest();
	}
	
	// Verificar Existencia
	self.checkExistence = function() {
		
		appCtrl.loadSpiner(true);
		
		var grupoObj = self.contest;
		
		var req = {
			method: 'GET',
			url: $scope.applicationUrl + "api/contest/by-name"+"?radom="+Math.random(),
			headers: {
				'name': grupoObj.name
			}
		}
		$http(req).then(function(response){
			
			if( response.data.length == 0 ){
				//Chamar função para inserir
				self.updateContest();
			}else{
				
				appCtrl.loadSpiner(false);
				appCtrl.loadSnackbar("Já existe um Concurso com o nome informado");
			}

		}, function(response){
			//ERRO
			console.log("ERRO");
		});

	}
	// Verificar Existencia
	
	// atualizar contest
	self.updateContest = function (){
		
		self.contest.id = idContest;
		console.log(self.contest);
		
		var config = {
				headers:  {
					'Content-Type': 'application/x-www-form-urlencoded'
				}
	};
		
		$http.put($scope.applicationUrl + "api/contest", $.param({ id: self.contest.id, name: self.contest.name}), config).then(function (response) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Concurso <span style='color:#00ff18;'>Editar</span> com sucesso.");
			window.location.href = "#list-contest";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com o TWODEV");
			window.location.href = "#home";
			
		});
		
	}
	
	// Carregar Equipamento Por ID
	self.getByIdContest = function (){
		
		console.log(idContest);

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/contest/" + idContest +"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.contest = response.data;
			
			self.nomeInformadoAntesDaEdicao = self.contest.name;
			
			
		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			window.location.href = "#home";
		});
		
	}
	
}]);