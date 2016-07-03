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
	}).otherwise({
		redirectTo : 'home'
	})
	$routeProvider.when('/list-contest', {
		templateUrl : 'src/app/components/contest/list-contest/list-contest.html',
		controller : 'ContestListCtrl as contestListCtrl'
	}).otherwise({
		redirectTo : 'home'
	})
	$routeProvider.when('/new-contest', {
		templateUrl : 'src/app/components/contest/new-contest/new-contest.html',
		controller : 'NewContestCtrl as newContestCtrl'
	}).otherwise({
		redirectTo : 'home'
	})
});