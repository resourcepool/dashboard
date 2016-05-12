'use strict'

var slideshowControllers = angular.module('slideshowControllers', []);

slideshowControllers.controller('HomeCtrl', [ '$scope', '$http', '$window', function ($scope, $http, $window) {
	
	$scope.slideShows = {};
	
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/slideshows?json=tv",
	}).then(function(success) {
		$scope.slideShows = angular.fromJson(success.data.objectAsJson);
		
		// on formatte les dates avec momentJS
		for (var slideShow of $scope.slideShows) {
			console.log(slideShow);
			slideShow.startDateTime = moment(slideShow.startDateTime, "dd-MM-YYYY HH:mm:ss").format("DD/MM/YYYY HH:mm");
			slideShow.endDateTime = moment(slideShow.endDateTime, "dd-MM-YYYY HH:mm:ss").format("DD/MM/YYYY HH:mm");
		}
		 $scope.remove = function(id){
			 if (confirm("Do you want to delete selected slideshow ?")) {
				 
				 $http.delete("http://vps229493.ovh.net:8080/dashboard/api/slideshows/" + id)
		            .success(function (success) {
		            	console.log(success);
		            	$window.location.href = '#/dashboard';
		            })
		            .error(function (error) {
		                console.log(error);
		            });
		        }
			 };
			  
		
	}, function(error) {
		console.log(error);
	});
}]);

slideshowControllers.controller('SlideshowAddCtrl', ['$scope', '$http', '$window', function ($scope, $http, $window) {
	$scope.slideShow = {};
	$scope.title = "Add a slideshow :";
	
	/*
	$('.datetimepicker').each(function() {
		jQuery(this).datetimepicker({
			format: 'd-m-Y H:i:s',
			theme: 'dark',
			onSelect: function(date) {
				alert(date);
			}
		});
	});
	*/
	
	$scope.submit = function (isValid) {
		if (isValid) {
			$("#status").text("Sending...").show();
			console.log($scope.slideShow);
			$http({
				method: 'POST',
				url: 'http://vps229493.ovh.net:8080/dashboard/api/slideshows ',
				data: $scope.slideShow
			}).then(function (success) {
				console.log(success);
				$window.location.href = '#/dashboard';
				
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

