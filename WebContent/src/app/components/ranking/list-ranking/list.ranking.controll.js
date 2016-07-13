'use strict';
app.controller('ListRankingCtrl', ['$http', '$location', '$scope', function($http, $location, $scope) {

	var self = this;
	var appCtrl = $scope.appCtrl;

	$scope.page = 'list-ranking';
	
	$scope.onLoadHtmlFileInNgView = function () {
		self.listRanking();
	}

	// ----- Filter List
    $scope.filterList = function () {
    	var arrayFilter = [];
    	for (var int = 0; int < $scope.initialDataOfGenericList.length; int++) {
    		var sInput = $scope.filterListInput;
    			sInput = sInput.toLowerCase();
			var sComparative = "";
			sComparative = $scope.initialDataOfGenericList[int].name;
			sComparative = sComparative.toLowerCase();
			if( sComparative.indexOf(sInput) != -1 ){
	    		arrayFilter.push( $scope.initialDataOfGenericList[int] );
			}
		}
    	$scope.currentPageOfList = 0;
    	$scope.dataOfGenericList = arrayFilter;
    }
	// ----- ./Filter List

	self.listRanking = function () {

		appCtrl.loadSpiner(true);

		$http.get( $scope.applicationUrl + "api/student/ranking"+"?radom="+Math.random() ).then(function(response) {

			appCtrl.loadSpiner(false);

			var arrayForNgRepeat = response.data;
			
			$scope.dataOfGenericList = arrayForNgRepeat;
			$scope.initialDataOfGenericList = arrayForNgRepeat;

		}, function(response) {
			//ERRO
			appCtrl.loadSpiner(false);
			alert("Foi Erroo");
			window.location.href = "#home";
		});

	}	
}]);