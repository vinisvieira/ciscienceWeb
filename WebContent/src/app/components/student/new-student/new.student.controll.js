'use strict';
app.controller('InserirStudentCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'new-student';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.listcontest();
	}
	
	// Verificar Existencia
	self.verificarExistencia = function() {
		
		appCtrl.loadSpiner(true);
		
		var studentObj = self.student;
		
		var req = {
			method: 'GET',
			url: $scope.applicationUrl + "api/student/by-email"+"?radom="+Math.random(),
			headers: {
				'email': studentObj.email
			}
		}
		$http(req).then(function(response){
			
			if( response.data.length == 0 ){
				//Chamar função para inserir
				self.insertStudent();
			}else{
				appCtrl.loadSpiner(false);
				appCtrl.loadSnackbar("Já existe um Aluno com o email informado");
			}

		}, function(response){
			//ERRO
			console.log("ERRO");
		});

	}
	// ./Verificar Existencia
	
	// Inserir Local
	self.insertStudent = function (){
		
		self.student.contest = self.contests;
		console.log(self.student);
		console.log(self.student.contest);
		appCtrl.loadSpiner(true);
	
		$.post( $scope.applicationUrl + "api/student", self.student).done( function(returnOfRequest) {
		//On Finish
		}).done(function(returnOfRequest) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Aluno <span style='color:#00ff18;'>INSERIDO</span> com sucesso.");
			window.location.href = "#list-student";

		}).fail(function() {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com TwoDev");
			window.location.href = "#home";
		
		}).always(function() {
		
		});
	}
	// ./Inserir Local
}]);