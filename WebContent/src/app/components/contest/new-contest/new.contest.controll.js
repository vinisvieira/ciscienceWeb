'use strict';
app.controller('NewContestCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'new-contest';
		
	$scope.onLoadHtmlFileInNgView = function () {
		
	}
	
	
	// Inserir Reserva Equipamento
	self.createContest = function (){
		

		
		
		console.log(self.contest);
		
		appCtrl.loadSpiner(true);

		$.post( $scope.applicationUrl + "api/contest", self.contest  ).done( function(returnOfRequest) {
		//On Finish
		}).done(function(returnOfRequest) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Novo Concurso  <span style='color:#00ff18;'>CRIADO</span> com sucesso.");
			window.location.href = "#list-contest";

		}).fail(function() {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com TWODEV");
			window.location.href = "#home";
		
		}).always(function() {
		
		});
	}

	
}]);