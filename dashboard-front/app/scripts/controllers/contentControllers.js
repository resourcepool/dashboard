
'use strict'

var contentControllers = angular.module('contentControllers', []);


contentControllers.controller('ContentAddCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
	
	$scope.content = {};
	$scope.slideShow = {};
	$scope.title = "Add content";
	var id = $routeParams.slideshowId;
	
	
	// On récupère le slideshow correspond
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/slideshows/" + id + "?json=full"
	}).then(function(success) {
		$scope.slideShow = angular.fromJson(success.data.objectAsJson);
		console.log($scope.slideShow);
		console.log(success);
	}, function(error) {
		console.log(error);
	});
	
	/*
	// On récupère le slideshow correspond
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/contents/" + id,
	}).then(function(success) {
		$scope.slideshow = angular.fromJson(success.data.objectAsJson);
		console.log($scope.slideshow);
		console.log(success);
	}, function(error) {
		console.log(error);
	});
	*/

	$scope.submit = function (isValid) {
		if (isValid) {
			var file = $scope.file;
			$scope.content.globalDuration = 3600;
			$scope.content.title = "test";
			$scope.content.positionInSlideShow = 1;
			$scope.content.id = 0;
			$scope.content.slideShow = $scope.slideShow;
			$scope.content.url = "test";
			console.log($scope.content);
			console.log('file is ' );
		    console.dir(file);
		    var uploadUrl = "http://vps229493.ovh.net:8080/dashboard/api/contents";
		    $scope.content.file = file;
		    /*var formData = new FormData();
		    formData.append('file', file);*/
		    //console.log(formData);
		    /*
		    // marche pour l'upload de contenu web
		    $http.post(uploadUrl, $scope.content, {
	            headers: {
	            	'Content-Type': 'application/json',
	            	'Accept': 'application/json'
	            }
	        })
	        */
		    $http.post(uploadUrl, $scope.content, {
	            //transformRequest: angular.identity,
	            headers: {
	            	'Content-Type': 'multipart/form-data',
	            	'content': $scope.content
	            }
	        })
	        .success(function(){
	        	console.log("success");
	        })
	        .error(function(){
	        	console.log("echec");
	        });
		}
	    
	}
	/*
	var req = {
			 method: 'POST',
			 url: 'http://example.com',
			 headers: {
			   'content': undefined /*{"@type":"ImageContent", "durationInDiaporama":100,"globalDuration":200,"title":"SuperTitre3",
				   "url":"none(uploader par le serveur)","id":0,
				   "diaporama":{"title":"title","startDateTime":"18-05-1000","endDateTime":"18-05-2000","id":1}}*/
			/* },
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
	
	var id = $routeParams.contentId;
	
	$scope.content = {};
	$scope.title = "Edit Content";
	
	
	$http({
		method: 'GET',
		url: "http://vps229493.ovh.net:8080/dashboard/api/contents/" + id + "?json=full"
	}).then(function(success) {
		$scope.content = angular.fromJson(success.data.objectAsJson);
		console.log($scope.content);
		console.log(success);
		/*
		$scope.slideshow.startDateTime = moment($scope.slideshow.startDateTime, "dd-MM-YYYY HH:mm:ss").format("DD-MM-YYYY HH:mm");
		$scope.slideshow.endDateTime = moment($scope.slideshow.endDateTime, "dd-MM-YYYY HH:mm:ss").format("DD-MM-YYYY HH:mm");
		*/
	}, function(error) {
		console.log(error);
		// traitement
		// $location.path : redirection
	});
	
	
	//http://vps229493.ovh.net:8080/dashboard/api/contents/{id} 
		  
	/*
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
	*/
}]);

