'use strict';
app.controller('NewQuestionCtrl', ['$http', '$location', '$scope','$routeParams', function($http, $location, $scope, $routeParams) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'new-question';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listLevel();
		self.listContest();
		
	}
	
	self.sendQuestion = function (){
		
		appCtrl.loadSpiner(true);
		
		var formData = new FormData();
		self.question.level = self.level;
		self.question.contest = self.contest;
		self.question.alternatives = $scope.alternativas;
		self.question.status = true;
		
		console.log(self.question);
		
		formData.append("question",JSON.stringify(self.question));
		formData.append("media", $("#inputFileImageQuestion")[0].files[0] );

		var request = new XMLHttpRequest();
		request.open("POST", "api/question");
		//request.setRequestHeader('Content-type', 'charset=UTF-8')
		request.onload = function () {
			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Questao <span style='color:#00ff18;'>Enviada</span> com sucesso.");
			window.location.href = "#list-question";
		};
		request.send(formData);
		
	}
	
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
	
	$scope.alternativas = [{"status":true, "answer":false}];

	self.addNewAlternativa = function() {
	    var newItemNo = $scope.alternativas.length+1;
	    $scope.alternativas.push({"status":true, "answer":false});
	};

	$scope.showAddAlternativa = function (alternativa) {
	   return alternativa.id === $scope.alternativas[$scope.alternativas.length-1].id;
	};
	
	$scope.updateSelection = function(position, alternativas) {
		  angular.forEach(alternativas, function(alternativa, index) {
		    if (position != index) 
		    	alternativa.answer = false;
		  });
		}
}]);