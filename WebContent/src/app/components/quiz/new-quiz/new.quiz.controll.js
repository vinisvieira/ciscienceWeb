'use strict';
app.controller('NewQuizCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;
	self.etapa = 1;
	
	self.questions = [];
	
	self.quiz;
		

	$scope.page = 'new-quiz';
		
	$scope.onLoadHtmlFileInNgView = function () {
		self.listcontest();
		self.listQuestion();
	}

	
	// Inserir quiz
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
	// listar quest
	self.listQuestion = function () {

		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/question"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);

			var arrayForNgRepeat = response.data;

			//Ordenar Por Name -- INICIO
			arrayForNgRepeat.sort(function(a,b) {
			    if(a.name < b.name) return -1;
			    if(a.name > b.name) return 1;
			    return 0;
			});
			//Ordenar Por Name -- TÉRMINO

			$scope.dataOfGenericList = arrayForNgRepeat;
			$scope.initialDataOfGenericList = arrayForNgRepeat;

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			alert("Foi Erroo");
			window.location.href = "#home";
		});

	}
    self.addQuestion = function(question){
    	
    	var exists = false;
    	
    	if (self.questions.length == 0) {
    		self.questions.push(question);
    	} else {
    		for (var i = 0; i < self.questions.length; i++) {
    			if (self.questions[i].id == question.id) {
    				self.questions.splice(i, 1);
    				exists = true;
    				break;
    			}
    		}
    		
    		if (!exists) {
    			self.questions.push(question);
    		}
    		
    	}
    	
    	console.log(self.questions);
    	
    };
    	
}]);