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
				self.updateStudent();
			}else{
				
				appCtrl.loadSpiner(false);
				appCtrl.loadSnackbar("Já existe um Aluno com o Email informado");
			}

		}, function(response){
			//ERRO
			console.log("ERRO");
		});

	}
	// ./Verificar Existencia
	
	// editar Local
	self.updateStudent = function (){
		
		self.student.id = idStudent;
		
		var config = {
				headers:  {
					'Content-Type': 'application/x-www-form-urlencoded'
				}
	};
		
		$http.put($scope.applicationUrl + "api/student", $.param({ id: self.student.id, name: self.student.name}), config).then(function (response) {
			
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