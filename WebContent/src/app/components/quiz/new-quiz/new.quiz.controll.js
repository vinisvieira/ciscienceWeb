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
		
	}
	self.dateFormatJson = function(){
		
		var dateJson = self.quiz.date;
		var from = dateJson.split("/");
		dateJson = from[2]+"-"+from[1]+"-"+from[0]+"T03:00:00Z";
		self.quiz.date=dateJson;
		
		
	}

	
	// Inserir quiz
	self.createQuiz = function (){
		
		self.quiz.questions = self.questions;
		console.log(self.quiz.date);
		console.log(JSON.stringify(self.quiz));
		
		
		var ObjectQuiz = new Object();
		ObjectQuiz.quiz = JSON.stringify(self.quiz);
		
		appCtrl.loadSpiner(true);
		

		$.post( $scope.applicationUrl + "api/quiz", ObjectQuiz).done( function(returnOfRequest) {
		//On Finish
		}).done(function(returnOfRequest) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar("Quiz <span style='color:#00ff18;'>INSERIDO</span> com sucesso.");
			window.location.href = "#list-quiz";

		}).fail(function(response) {

			appCtrl.loadSpiner(false);
			appCtrl.loadSnackbar(response.responseText);
		
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
	
	// Listar level
	self.listLevel = function (){

		appCtrl.loadSpiner(true);
		
		$http.get( $scope.applicationUrl + "api/level"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);
			self.levels = response.data;
			console.log(response.data);
		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			//window.location.href = "#home";
		});
		
	}
	self.etapaOnclickAvanca = function(){
		
		if(self.quiz.contest == null || self.quiz.name == null || self.quiz.date == null ){
			appCtrl.loadSnackbar("<span style='color:#FFFD7C;'> Preencha Todos os Campos obrigatórios.</span> ");
		}else{
			self.listQuestion(); 
			self.listLevel();
			self.dateFormatJson();
			self.etapa= self.etapa+1;
		}
		
		
		
		
	}
	self.etapaOnclickVoltar = function(){
		
		self.etapa= self.etapa-1;
		
		
	}
	// listar quest
	self.listQuestion = function () {
		var idContest = self.quiz.contest.id
		
		console.log(idContest);
		
		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/question/filter/"+idContest+"/0"+"?radom="+Math.random() ).then(function(response) {

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
			var index = $scope.dataOfGenericList.indexOf(question);
			$scope.dataOfGenericList[index].checked = true;
			var obj = new Object();
			obj.id = question.id;
    		self.questions.push(obj);
    	} else {
    		for (var i = 0; i < self.questions.length; i++) {
    			if (self.questions == question.id) {
    				self.questions.splice(i, 1);
    				var index = $scope.dataOfGenericList.indexOf(question);
    				$scope.dataOfGenericList[index].checked = false;
    				exists = true;
    				break;
    			}
    		}
    		
    		if (!exists) {
				var index = $scope.dataOfGenericList.indexOf(question);
				$scope.dataOfGenericList[index].checked = true;
				var obj = new Object();
				obj.id = question.id;
	    		self.questions.push(obj);
    		}
    		
    	}
    	
    	
    		
    	
    	
    	console.log(self.questions);
    	
    };
    self.selectionLevel = function(idLevel){
    	console.log(idLevel);
    	var arrayFilter = [];
    	for (var int = 0; int < $scope.initialDataOfGenericList.length; int++) {
    		
    			if($scope.initialDataOfGenericList[int].level.id == idLevel){
    				
    				arrayFilter.push( $scope.initialDataOfGenericList[int] );
    				
    			}else if(idLevel == null){
    				arrayFilter.push( $scope.initialDataOfGenericList[int] );
    			}
    			
    		}
    	$scope.currentPageOfList = 0;
    	$scope.dataOfGenericList = arrayFilter;
		    	
    }
    
   
    
    
 // ----- Filter List
    $scope.filterList = function () {
    	var arrayFilter = [];
    	for (var int = 0; int < $scope.initialDataOfGenericList.length; int++) {
    		var sInput = $scope.filterListInput;
    			sInput = sInput.toLowerCase();
			var sComparative = "";
			sComparative = $scope.initialDataOfGenericList[int].text;
			sComparative = sComparative.toLowerCase();
			if( sComparative.indexOf(sInput) != -1 ){
	    		arrayFilter.push( $scope.initialDataOfGenericList[int] );
			}
		}
    	$scope.currentPageOfList = 0;
    	$scope.dataOfGenericList = arrayFilter;
    }
	// ----- ./Filter List
    	
}]);