'use strict';
app.controller('NewQuizCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	self.etapa =1;
	
	console.log(self.etapa);
		

	$scope.page = 'new-quiz';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.listcontest();
		
	}
	
//	// Verificar Existencia
//	self.verificarExistencia = function() {
//		
//		appCtrl.loadSpiner(true);
//		
//		var studentObj = self.student;
//		
//		var req = {
//			method: 'GET',
//			url: $scope.applicationUrl + "api/student/by-email"+"?radom="+Math.random(),
//			headers: {
//				'email': studentObj.email
//			}
//		}
//		$http(req).then(function(response){
//			
//			if( response.data.length == 0 ){
//				//Chamar função para inserir
//				self.inserirStudent();
//			}else{
//				appCtrl.loadSpiner(false);
//				appCtrl.loadSnackbar("Já existe um Aluno com o email informado");
//			}
//
//		}, function(response){
//			//ERRO
//			console.log("ERRO");
//		});
//
//	}
//	// ./Verificar Existencia
	
	// Inserir Local
	self.createQuiz = function (){

		appCtrl.loadSpiner(true);

		$.post( $scope.applicationUrl + "api/quiz", self.quiz ).done( function(returnOfRequest) {
		//On Finish
		}).done(function(returnOfRequest) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Quiz <span style='color:#00ff18;'>INSERIDO</span> com sucesso.");
			window.location.href = "#list-quiz";

		}).fail(function() {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com o TWODEV");
			window.location.href = "#home";
		
		}).always(function() {
		
		});
	}
	// ./Inserir 
	// Listar contest
	self.listcontest = function (){

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/contest/active"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.contest = response.data;
			console.log(response.data);
		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			//window.location.href = "#home";
		});
		
	}
	self.etapaOnclickAvanca = function(){
		
		self.etapa= self.etapa+1;
		
		
	}
	self.etapaOnclickVoltar = function(){
		
		self.etapa= self.etapa-1;
		
		
	}
}]);