'use strict'

var slideshowControllers = angular.module('slideshowControllers', []);

slideshowControllers.controller('HomeCtrl', [ '$scope', '$http', function ($scope, $http) {
	
	$scope.slideshows = {};
	
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/slideshows?json=tv",
	}).then(function(success) {
		$scope.slideshows = angular.fromJson(success.data.objectAsJson);
		
		for (var slideshow of $scope.slideshows) {
			console.log(slideshow);
			slideshow.startDateTime = moment(slideshow.startDateTime, "dd-MM-YYYY HH:mm:ss").format("DD/MM/YYYY HH:mm");
			slideshow.endDateTime = moment(slideshow.endDateTime, "dd-MM-YYYY HH:mm:ss").format("DD/MM/YYYY HH:mm");
		}
		console.log($scope.slideshows);
		console.log(success);
		
	}, function(error) {
		console.log(error);
		// traitement
		// $location.path : redirection
	});
}]);

slideshowControllers.controller('SlideshowAddCtrl', ['$scope', '$http', function ($scope, $http) {
	$scope.slideshow = {};
	$scope.title = "Add a slideshow :";
	
	$('.datetimepicker').each(function(){
		jQuery(this).datetimepicker({
			format: 'd-m-Y H:i',
			theme: 'dark'
		});
	});
	
	$scope.submit = function (isValid) {
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
}]);
slideshowControllers.controller('SlideshowEditCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
	
	var id = $routeParams.contentId;
	
	$scope.slideshow = {};
	$scope.title = "Edit a slideshow :";
	
	$('.datetimepicker').each(function(){
		jQuery(this).datetimepicker({
			format: 'd-m-Y H:i',
			theme: 'dark'
		});
	});
	
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/slideshows/" + 1 + "?json=full"
	}).then(function(success) {
		$scope.slideshow = angular.fromJson(success.data.objectAsJson);
		console.log($scope.slideshow);
		console.log(success);
		
		$scope.slideshow.startDateTime = moment($scope.slideshow.startDateTime, "dd-MM-YYYY HH:mm:ss").format("DD-MM-YYYY HH:mm");
		$scope.slideshow.endDateTime = moment($scope.slideshow.endDateTime, "dd-MM-YYYY HH:mm:ss").format("DD-MM-YYYY HH:mm");
		
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

