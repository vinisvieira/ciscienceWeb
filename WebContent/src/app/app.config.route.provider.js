'use strict';
app.config(function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : 'src/app/components/login/login.html',
		controller : 'LoginCtrl as loginCtrl'
	})
	$routeProvider.when('/home', {
		templateUrl : 'src/app/components/home/home.html',
		controller : 'HomeCtrl as homeCtrl'
	})
	$routeProvider.when('/new-student', {
		templateUrl : 'src/app/components/student/new-student/new-student.html',
		controller : 'InserirStudentCtrl as inserirStudentCtrl'
	})
	$routeProvider.when('/list-student', {
		templateUrl : 'src/app/components/student/list-student/list-student.html',
		controller : 'ListStudentCtrl as listStudentCtrl'
	})
	$routeProvider.when('/update-student/:idStudent', {
		templateUrl : 'src/app/components/student/update-student/update-student.html',
		controller : 'UpdateStudentCtrl as updateStudentCtrl'
	})
	$routeProvider.when('/new-contest', {
		templateUrl : 'src/app/components/contest/new-contest/new-contest.html',
		controller : 'NewContestCtrl as newContestCtrl'
	})
	$routeProvider.when('/list-contest', {
		templateUrl : 'src/app/components/contest/list-contest/list-contest.html',
		controller : 'ContestListCtrl as contestListCtrl'
	})
	$routeProvider.when('/update-contest/:idContest', {
		templateUrl : 'src/app/components/contest/update-contest/update-contest.html',
		controller : 'UpdateContestCtrl as updateContestCtrl'
	})
	$routeProvider.when('/new-level', {
		templateUrl : 'src/app/components/level/new-level/new-level.html',
		controller : 'NewLevelCtrl as newLevelCtrl'
	})
	$routeProvider.when('/list-level', {
		templateUrl : 'src/app/components/level/list-level/list-level.html',
		controller : 'ListLevelCtrl as listLevelCtrl'
	})
	$routeProvider.when('/update-level/:idLevel', {
		templateUrl : 'src/app/components/level/update-level/update-level.html',
		controller : 'UpdateLevelCtrl as updateLevelCtrl'
	})
	$routeProvider.when('/new-question', {
		templateUrl : 'src/app/components/question/new-question/new-question.html',
		controller : 'NewQuestionCtrl as newQuestionCtrl'
	}).otherwise({
		redirectTo : 'home'
	})
	$routeProvider.when('/new-quiz', {
		templateUrl : 'src/app/components/quiz/new-quiz/new-quiz.html',
		controller : 'NewQuizCtrl as newQuizCtrl'
	}).otherwise({
		redirectTo : 'home'
	})
	$routeProvider.when('/list-quiz', {
		templateUrl : 'src/app/components/quiz/list-quiz/list-quiz.html',
		controller : 'QuizListCtrl as quizListCtrl'
	}).otherwise({
		redirectTo : 'home'
	})
	
});