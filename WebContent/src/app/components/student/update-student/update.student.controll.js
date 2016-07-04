'use strict';
app.controller('UpdateStudentCtrl', ['$http', '$location', '$scope', '$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	var nomeInformadoAntesDaEdicao;
	var idStudent = $routeParams.idStudent;

	$scope.page = 'update-student';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.carregarStudentPorId();
	}
	
	// editar Local
	self.updateStudent = function (){
		
		self.student.id = idStudent;
		
		var config = {
				headers:  {
					'Content-Type': 'application/x-www-form-urlencoded'
				}
	};
		
		$http.put($scope.applicationUrl + "api/student/" + idStudent, $.param({name: self.student.name,
																   cpf: self.student.cpf,
																   email: self.student.email,
																   birthday: self.student.birthday,
																   password: self.student.password,
																   confirmPassword: self.student.confirmPassword}), config).then(function (response) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Aluno <span style='color:#00ff18;'>Atualizado</span> com sucesso.");
			window.location.href = "#list-student";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com os Administradores do Sistema");
			window.location.href = "#home";
			
		});
		
	}
	
	// Carregar Local Por ID
	self.carregarStudentPorId = function (){
		
		console.log(idStudent);

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/student/" + idStudent +"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.student = response.data;
			
			self.nomeInformadoAntesDaEdicao = self.student.name;
			
		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			window.location.href = "#home";
		});
		
	}
	
}]);