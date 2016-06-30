'use strict';
app.controller('LoginCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'login';

	$scope.onLoadHtmlFileInNgView = function () {
		$scope.inlogin = 'true';
		var emptyPerfil = new Object;
			emptyPerfil.perfil = ""
		self.obj = emptyPerfil;
	}

	self.login = function () {

		appCtrl.loadSpiner(true);

		var loginObj = new Object();
			loginObj = self.obj;

			var perfil = loginObj.perfil;
			
			console.log(loginObj);

			var loginUrl = $scope.applicationUrl + "api/login/" + perfil;

			$.post( loginUrl, loginObj ).done( function(returnOfRequest) {
			
			}).done(function(returnOfRequest) {

				appCtrl.loadSpiner(false);
				
				window.location.href = '#home';

			}).fail(function(returnOfRequest) {
				
				appCtrl.loadSpiner(false);

				console.log(returnOfRequest);

			}).always(function() {
			
			});

	}

}]);