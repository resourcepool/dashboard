'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaAddController',[
    '$scope', '$http', '$routeParams', '$window', 'API', 'MSG', 'bundleService', 'mediaService',
    function ($scope, $http, $routeParams, $window, API, MSG, bundleService, mediaService ) {

      var bundleId = $routeParams.bundleId;
      var mediaId = $routeParams.mediaId;

      $scope.bundle = {};
      $scope.media = {};
      $scope.file = {};
      $scope.media.validity = {};
      $scope.nav = "Add a media";
      $scope.title = "Add";
      //$scope.regexDate = DATE.REGEXP;
      $scope.loading = false;


      /*
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
      */

      // On récupère le bundle correspondant
      bundleService.getById(bundleId).then(function(data) {
        console.log(data);
        $scope.bundle = data;
      }, function(error) {
        $scope.error = MSG.ERR.GET_BUNDLE;
      });




      /*
      $http({
        method: 'GET',
        url: API.BASE_URL + "slideshows/" + id + "?json=light"
      }).then(function(success) {
        $scope.content.bundle = angular.fromJson(success.data.objectAsJson);
        $scope.title = "Add a content to " + $scope.content.bundle.title;
      }, function(error) {
        console.log(error);
      });
      */


      /*
      $scope.submit = function (isValid) {
        console.log(isValid);
        if (isValid) {
          $("#status").show();
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
      */

    }]);
