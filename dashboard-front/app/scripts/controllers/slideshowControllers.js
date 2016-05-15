'use strict'

var slideshowControllers = angular.module('slideshowControllers', []);

slideshowControllers.controller('HomeCtrl', [ '$scope', '$http', '$window', 'API', 'DATE', function ($scope, $http, $window, API, DATE) {

	$scope.slideShows = {};


	$http({
		method: 'GET',
		url: API.BASE_URL + "slideshows?json=tv",
	}).then(function(success) {
		$scope.slideShows = angular.fromJson(success.data.objectAsJson);

		// on formatte les dates avec momentJS
		for (var slideShow of $scope.slideShows) {
			console.log(slideShow);
			slideShow.startDateTime = moment(slideShow.startDateTime, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
			slideShow.endDateTime = moment(slideShow.endDateTime, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
		}
		 $scope.remove = function(id){
			 if (confirm("Do you want to delete selected slideshow ?")) {

				 $http.delete(API.BASE_URL + "slideshows/" + id)
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

slideshowControllers.controller('SlideshowAddCtrl', ['$scope', '$http', '$window', 'API', function ($scope, $http, $window, API) {
	$scope.slideShow = {};
	$scope.title = "Add a slideshow :";

	// requete d'envoi d'un slideshow
	var addRequest = {
		method: 'POST',
	    url: API.BASE_URL + "slideshows/",
	    headers: {
	      'Content-Type': 'application/json',
	    },
	    data: $scope.slideShow
	  };

	$scope.submit = function (isValid) {
		if (isValid) {
			$("#status").text("Sending...").show();
			console.log($scope.slideShow);
			
			$http(addRequest).then(
	          function success(response) {
	            console.log(success);
	            $window.location.href = '#/dashboard';
	          },
	          function error(response) {
	            console.log(response);
	          }
	        )
		}
	}
}]);
slideshowControllers.controller('SlideshowEditCtrl', ['$scope', '$http', '$routeParams', '$window', 'API', 'DATE', function ($scope, $http, $routeParams, $window, API, DATE) {

	var id = $routeParams.slideshowId;

	$scope.slideShow = {};

	// on définit les datetimepicker
	$('.datetimepicker').each(function(){
		jQuery(this).datetimepicker({
			format: 'd-m-Y H:i',
			theme: 'dark'
		});
	});

	// requete de mise à jour d'un slideshow
	  var updateRequest = {
	    method: 'PUT',
	    url: API.BASE_URL + "slideshows/",
	    headers: {
	      'Content-Type': 'application/json',
	    },
	    data: $scope.slideShow
	  };


	// on récupère le slideshow
	$http({
		method: 'GET',
		url: API.BASE_URL + "slideshows/" + id + "?json=full"
	}).then(function(success) {
		$scope.slideShow = angular.fromJson(success.data.objectAsJson);
		console.log(success);
		$scope.title = "Edit slideshow : " + $scope.slideShow.title;
	}, function(error) {
		console.log(error);
		// traitement
	});
	
	$scope.submit = function (isValid) {
		console.log(isValid);
		if (isValid) {
			$http(updateRequest).then(
	          function success(response) {
	            console.log(success);
	            $window.location.href = '#/dashboard';
	          },
	          function error(response) {
	            console.log(response);
	          }
	        )
		}
	}
}]);

