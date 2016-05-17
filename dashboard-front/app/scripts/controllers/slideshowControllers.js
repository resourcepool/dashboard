'use strict'

var slideshowControllers = angular.module('slideshowControllers', []);

slideshowControllers.controller('HomeCtrl', [ '$scope', '$http', '$window', 'API', 'DATE', 'slideShowService', function ($scope, $http, $window, API, DATE, slideShowService) {

	$scope.slideShows = {};

	// on récupère tous les slideshows
	slideShowService.getAll("tv",
		
		// récupération des slideshows OK
		function (success) {

			$scope.slideShows = angular.fromJson(success.data.objectAsJson);
			
			// on formatte les dates avec momentJS
			for (var slideShow of $scope.slideShows) {
				console.log(slideShow);
				slideShow.startDateTime = moment(slideShow.startDateTime, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
				slideShow.endDateTime = moment(slideShow.endDateTime, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
			}

			// on bind la fonction de suppression
			$scope.remove = function(id) {
				 if (confirm("Do you want to delete selected slideshow ?")) {
				 	slideShowService.removeById(id,
				 		function (success) {
				 			console.log(success);
			            	$window.location.href = '#/dashboard';
				 		},
				 		function (error) {
				 			console.log(error);
				 		}
				 	)
			      }
			};
		},

		// Error lors de la récupération des slideshows
		function (error) {
			console.log(error);
		}
		);
}]);

slideshowControllers.controller('SlideshowAddCtrl', ['$scope', '$http', '$window', 'API', function ($scope, $http, $window, API) {
	$scope.slideShow = {};
	$scope.title = "Add a slideshow :";

	// requete d'envoi d'un slideshow
	var addRequest = {
		method: 'POST',
	    url: API.BASE_URL + "slideshows/",
	    data: $scope.slideShow
	  };

	$scope.submit = function (isValid) {
		if (isValid) {
			$("#status").show();
			console.log($scope.slideShow);
			
			$http(addRequest).then(
	          function success(response) {
	            console.log(success);
	            $window.location.href = '#/dashboard';
	          },
	          function error(response) {
	          	$("#status").hide();
	            console.log(response);
	          }
	        )
		}
	}
}]);

slideshowControllers.controller('SlideshowEditCtrl', ['$scope', '$http', '$routeParams', '$window', 'API', 'DATE', 'slideShowService', function ($scope, $http, $routeParams, $window, API, DATE, slideShowService) {

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
	    data: $scope.slideShow
	  };


	slideShowService.getById(id,
		function(success){
			$scope.slideShow = angular.fromJson(success.data.objectAsJson);
			console.log(success);
			$scope.title = "Edit slideshow : " + $scope.slideShow.title;
		},
		function(error) {
			console.log(error);
		});

	$scope.submit = function (isValid) {
		console.log(isValid);
		if (isValid) {
			$("#status").show();
			$http(updateRequest).then(
	          function success(response) {
	            console.log(success);
	            $window.location.href = '#/dashboard';
	          },
	          function error(response) {
	          	$("#status").hide();
	            console.log(response);
	          }
	        )
		}
	}
}]);

