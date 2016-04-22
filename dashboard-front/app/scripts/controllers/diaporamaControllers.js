'use strict'

var diaporamaControllers = angular.module('diaporamaControllers', []);

diaporamaControllers.controller('HomeCtrl', [ function ($scope) {
	/*
	$scope.phones = [
     {'name': 'Nexus S',
      'snippet': 'Fast just got faster with Nexus S.'},
     {'name': 'Motorola XOOM™ with Wi-Fi',
      'snippet': 'The Next, Next Generation tablet.'},
     {'name': 'MOTOROLA XOOM™',
      'snippet': 'The Next, Next Generation tablet.'}
	 ];
	 */
}]);

diaporamaControllers.controller('DiaporamaCreateCtrl', ['$scope', '$http', function ($scope, $http) {
	$scope.diaporama = {};
	$scope.title = "Create a diaporama";
	
	$scope.submit = function (isValid) {
		console.log(isValid);
		console.log($scope.diaporama.name);
		console.log($("#status"));
		
		if (isValid) {
			$("#status").text("Sending...").show();
			$http({
				method: 'POST',
				url: 'http://localhost:8000/create',
				data: $scope.diaporama
			}).then(function (success){
				
			}, function (error) {
				$("#status").text("Erreur d'envoi");
				
				console.log(error);
			});
		}
	}
	
	// utiliser momentJS
	// start et end en datetime
	
}]);
diaporamaControllers.controller('DiaporamaEditCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
	
	console.log($routeParams);
	var id = $routeParams.diaporamaId;
	console.log(id);
	$scope.diaporama = {};
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/diaporamas/" + id,
	}).then(function(success) {
		$scope.diaporama = success.data;
		console.log(success);
		
	}, function(error) {
		console.log(error);
		// traitement
		// $location.path : redirection
	});
	
	$scope.create = function () {
		$http({
			method: 'POST',
			url: 'url_update',
			data: $scope.diaporama
		});
	}
}]);

