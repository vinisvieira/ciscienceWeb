'use strict';
app.config(function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl: 'src/app/components/login/login.html',
		controller: 'LoginCtrl as loginCtrl'
	})
	$routeProvider.when('/home', {
		templateUrl: 'src/app/components/home/home.html',
		controller: 'HomeCtrl as homeCtrl'
	}).otherwise({
		redirectTo: 'home'
	})
});