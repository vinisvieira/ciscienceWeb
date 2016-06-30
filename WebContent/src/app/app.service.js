'use strict';
app.service('loginService', function($http, $location) {

	this.loggedUser = function() {
		return $.get( "api/login/get-user-loged" );
	};

	this.logout = function() {
		return $.post( "api/login/logout" );
	};

});