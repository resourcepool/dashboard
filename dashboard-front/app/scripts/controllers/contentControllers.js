
'use strict'

var contentControllers = angular.module('contentControllers', []);


contentControllers.controller('ContentAddCtrl', ['$scope', '$http', '$routeParams', '$window', 'API', function ($scope, $http, $routeParams, $window, API) {

  $scope.content = {};
  var id = $routeParams.slideshowId;

  // requete d'upload de contenu web
  var uploadWebContentRequest = {
    method: 'POST',
    url: API.BASE_URL + "contents",
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    data: $scope.content
  };

  // On récupère le slideshow correspond
  $http({
    method: 'GET',
    url: API.BASE_URL + "slideshows/" + id + "?json=light"
  }).then(function(success) {
      $scope.content.slideShow = angular.fromJson(success.data.objectAsJson);
      $scope.title = "Add a content to " + $scope.content.slideShow.title;
  }, function(error) {
    console.log(error);
  });

  


  $scope.submit = function (isValid) {
    console.log(isValid);
    if (isValid) {

      // si upload de type Web Content
      if ($scope.content['@type'] == 'WebContent') {
        $http(uploadWebContentRequest).then(
          function success(response) {
            console.log(response);
            $window.location.href = '#/dashboard';
          },
          function error(response) {
            console.log(response);
          }
        )
      }

      // sinon upload d'une image ou video
      else {

        var file = $scope.file;
		    console.log($scope.content);
        console.log('file is ' );
        console.dir(file);
        //$scope.content.file = file;

        var fileForm = new FormData();
        fileForm.append('file', file);

        $http.post(API.BASE_URL + "contents", fileForm, {
          //transformRequest: angular.identity,
          headers: {
    			  'Content-Type': undefined,
    			  'Content': JSON.stringify($scope.content)
			     }
        })
          .success(function(success) {
            console.log(success);
            $window.location.href = '#/dashboard';
          })
          .error(function(error) {
            console.log(error);
          });
      }
    }
  }

}]);
contentControllers.controller('ContentEditCtrl', ['$scope', '$http', '$routeParams', '$window', 'API', function ($scope, $http, $routeParams, $window, API) {

  var idContent = $routeParams.contentId;
  var idSlideShow = $routeParams.slideshowId;

  $scope.content = {};
  $scope.title = "Edit Content";

  // On récupère le slideshow correspond
  $http({
    method: 'GET',
    url: API.BASE_URL + "slideshows/" + idSlideShow + "?json=light"
  }).then(function(success) {
    $scope.content.slideShow = angular.fromJson(success.data.objectAsJson);
    console.log($scope.slideShow);
    console.log(success);
    $scope.title = "Edit content from " + $scope.content.slideShow.title;
  }, function(error) {
    console.log(error);
  });
  
  // On récupère le content
  $http({
    method: 'GET',
    url: API.BASE_URL + "contents/" + idContent
  }).then(function(success) {
    console.log(success);
    $scope.content = angular.fromJson(success.data.objectAsJson);
    console.log($scope.content);
    //$scope.title = "Edit content from " + $scope
    
  }, function(error) {
    console.log(error);
    // traitement
    // $location.path : redirection
  });

  $scope.remove = function(id) {
    if (confirm("Do you want to delete selected content ?")) {
      $http.delete(API.BASE_URL + "contents/" + id)
        .success(function (success) {
          console.log(success);
          $window.location.href = '#/dashboard';
        })
        .error(function (error) {
          console.log(error);
        });
    }
  };

  $scope.submit = function (isValid) {
    console.log(isValid);
    if (isValid) {

      // si upload de type Web Content
      if ($scope.content['@type'] == 'WebContent') {
        $http(uploadWebContentRequest).then(
          function success(response) {
            console.log(response);
            $window.location.href = '#/dashboard';
          },
          function error(response) {
            console.log(response);
          }
        )
      }

      // sinon upload d'une image ou video
      else {

        var file = $scope.file;
        console.log($scope.content);
        console.log('file is ' );
        console.dir(file);
        //$scope.content.file = file;

        var fileForm = new FormData();
        fileForm.append('file', file);

        $http.post(API.BASE_URL + "contents", fileForm, {
          //transformRequest: angular.identity,
          headers: {
            'Content-Type': undefined,
            'Content': JSON.stringify($scope.content)
           }
        })
          .success(function(success) {
            console.log(success);
            $window.location.href = '#/dashboard';
          })
          .error(function(error) {
            console.log(error);
          });
      }
    }
  }



}]);

