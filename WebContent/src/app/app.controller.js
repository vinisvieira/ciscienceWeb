'use strict';
app.controller('AppCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {


	$scope.applicationUrl = "http://localhost:8080/ciscience/";

	var self = this;
	
	self.loginOut = function () {
		console.log("cheguei aqui loginout");
		
		var loginUrl = $scope.applicationUrl + "api/login/logout";

		$.post( loginUrl ).done( function(returnOfRequest) {
		
		}).done(function(returnOfRequest) {

			self.loadSpiner(false);
			
			window.location.href = '#login';

		}).fail(function(returnOfRequest) {
			
			self.loadSpiner(false);

			console.log(returnOfRequest);

		}).always(function() {
		
		});
		
	}
	
	self.getNameUser = function(){
		

		$http.get( $scope.applicationUrl + "api/login/get-user-loged"+"?radom="+Math.random() ).then(function(response){

			$scope.user = response.data;

		}, function(response) {
			//ERRO
			self.loadSpiner(false);
			alert("Foi Erroo");
			window.location.href = "#home";
		});
	}
	
	/*
	 * SPINER
	 * 
	 * */
	var myTime = null;
	self.loadSpiner = function (param) {
		if( param ){
			$("#loadContentSpninner").addClass('active');
		}else{
			$("#loadContentSpninner").removeClass('active');
		}
	}
	// ../SPINER --------------------------------------------------------------------------------------------------

	/*
	 * SNACKBAR
	 * 
	 * */
	var myTime = null;
	self.loadSnackbar = function (msg) {
		$("#snackbar").html(msg);
		$("#snackbar").addClass('active');
		myTime = setTimeout(hideSnackbar, 5000);
	}
	function hideSnackbar() {
		$("#snackbar").removeClass('active');
	}
	// ../SNACKBAR --------------------------------------------------------------------------------------------------

	/*
	 * GENERIC LISTS NAVIGATION
	 * 
	 * */
 	$scope.initialDataOfGenericList = null;
	$scope.currentPageOfGenericList = 0;
    $scope.dataOfGenericList = [];
    $scope.exibirRegistrosInativos = "false";
    $scope.pageSizeOfGenericList = "5";
    $scope.numberOfPagesOfGenericList=function(){
        return Math.ceil($scope.dataOfGenericList.length/$scope.pageSizeOfGenericList);                
    }
	// ../GENERIC LISTS NAVIGATION ---------------------------------------------------------------------------------

}]);