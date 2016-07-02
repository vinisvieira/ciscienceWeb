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
});