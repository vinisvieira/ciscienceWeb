'use strict';
app.controller('NewQuestionCtrl', ['$http', '$location', '$scope','$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'new-question';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listLevel();
		self.listContest();
		
	}
	
	console.log("yes");

	$scope.choices = [{id: 'choice1'}];

	$scope.addNewChoice = function() {
	    var newItemNo = $scope.choices.length+1;
	    $scope.choices.push({'id':'choice' +newItemNo});
	};

	$scope.showAddChoice = function(choice) {
	   return choice.id === $scope.choices[$scope.choices.length-1].id;
	   
	};
	
	// List Level
	self.listLevel = function (){

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/level"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.levels = response.data;

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			window.location.href = "#home";
		});
		
	}

	// ./List Level
	
	// List Contest
	self.listContest = function (){

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/contest/active"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.contests = response.data;

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			window.location.href = "#home";
		});
		
	}
	// ./List Contest
}]);