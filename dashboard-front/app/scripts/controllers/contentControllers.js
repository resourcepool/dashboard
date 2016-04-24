'use strict'

var contentControllers = angular.module('contentControllers', []);


contentControllers.controller('ContentCreateCtrl', ['$scope', '$http', function ($scope, $http) {
	
	$scope.content = {};
	
	var req = {
			 method: 'POST',
			 url: 'http://example.com',
			 headers: {
			   'content': undefined /*{"@type":"ImageContent", "durationInDiaporama":100,"globalDuration":200,"title":"SuperTitre3",
				   "url":"none(uploader par le serveur)","id":0,
				   "diaporama":{"title":"title","startDateTime":"18-05-1000","endDateTime":"18-05-2000","id":1}}*/
			 },
			 data: { test: 'test' }
			}

	//$http(req).then(function(){...}, function(){...});
	
	// utiliser momentJS
	// start et end en datetime
	
	/*
	$scope.doUpload = function () {
	    upload({
	      url: '/upload',
	      method: 'POST',
	      data: {
	        anint: 123,
	        aBlob: Blob([1,2,3]), // Only works in newer browsers
	        aFile: $scope.myFile, // a jqLite type="file" element, upload() will extract all the files from the input and put them into the FormData object before sending.
	      }
	    }).then(
	      function (response) {
	        console.log(response.data); // will output whatever you choose to return from the server on a successful upload
	      },
	      function (response) {
	          console.error(response); //  Will return if status code is above 200 and lower than 300, same as $http
	      }
	    );
	  }
	*/
}]);
contentControllers.controller('ContentEditCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
	
	var id = $routeParams.get('diaporamaId');
	
	$scope.diaporama = {};
	$http({
		method: 'GET',
		url: 'url_get_diapo',
		data: id,
	}).then(function(success) {
		$scope.diaporama = success.data;
		
	}, function(error) {
		alert(error);
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

