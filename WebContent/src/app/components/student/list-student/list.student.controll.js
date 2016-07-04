'use strict';
app.controller('ListStudentCtrl', ['$http', '$location', '$scope','$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	var idStudent = $routeParams.idStudent;
	var status = $routeParams.statusStudent;
	var studentObj = self.student;

	$scope.page = 'list-student';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listarStudent();
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
    	
	//---- ./List student.	
	self.listarStudent = function () {

		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/student/active"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);

			var arrayForNgRepeat = response.data;
			
			console.log(arrayForNgRepeat);

			//Ordenar Por Name -- INICIO
			arrayForNgRepeat.sort(function(a,b) {
			    if(a.name < b.name) return -1;
			    if(a.name > b.name) return 1;
			    return 0;
			});
			//Ordenar Por Name -- TÉRMINO

			$scope.dataOfGenericList = arrayForNgRepeat;
			$scope.initialDataOfGenericList = arrayForNgRepeat;
			
			console.log($scope.dataOfGenericList[0].status);

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			alert("Foi Erroo");
			window.location.href = "#home";
		});
	}
	//----- ./List student.
	
	//-------/Inativar student
	self.inativaStudent = function (idStudent) {
		
    	console.log(idStudent);
    	
    	var req = {
    			method: 'DELETE',
    			url: $scope.applicationUrl + "api/student/" + idStudent,
    	}
		
		$http(req).then(function (response) {
			
			/*appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Local <span style='color:#00ff18;'>ALTERADO</span> com sucesso.");*/
			window.location.href = "#list-student";
			
		}, function (error) {
			
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("<span style='color:#ff0000;'>FALHA.</span> Entre em contato com o N.I.T. FAFICA");
			window.location.href = "#home";
			
		})};
		
	//--------/Inativar student
}]);