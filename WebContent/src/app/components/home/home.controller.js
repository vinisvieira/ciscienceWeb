'use strict';
app.controller('HomeCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	$scope.page = 'home';
	var appCtrl = $scope.appCtrl;

	var self = this;

	$scope.onLoadHtmlFileInNgView = function () {

		appCtrl.getNameUser();
	}

}]);