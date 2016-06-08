'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaEditController', ['$scope', '$http', '$routeParams', '$window', 'API', 'MSG', 'bundleService', 'mediaService',
    function ($scope, $http, $routeParams, $window, API, MSG, bundleService, mediaService ) {

      var bundleId = $routeParams.bundleId;
      var mediaId = $routeParams.mediaId;

      $scope.bundle = {};
      $scope.media = {};
      $scope.file = {};
      $scope.title = "Edit";
      $scope.loading = false;

      // On récupère le bundle correspondant
      bundleService.getById(bundleId).then(function(data) {
        console.log(data);
        $scope.bundle = data;
        $scope.nav = "Edit " + $scope.bundle.name;
      }, function(error) {
        $scope.error = MSG.ERR.GET_BUNDLE;
      });


      // On récupère le bundle correspondant
      mediaService.getById(bundleId).then(function(data) {
        console.log(data);
        $scope.media = data;
      }, function(error) {
        $scope.error = MSG.ERR.GET_MEDIA;
      });


      // on définit la fonction de submit
      $scope.submit = function (isValid) {
      if (isValid) {
        $scope.loading = true;

        if ($scope.content['@type'] == 'WebContent') {
          mediaService.addWebContent($scope.media).then(function(response) {
            console.log(response);
            $window.location.href = '#/bundle/' + bundleId;
          }, function(error){
            $scope.error = MSG.ERR.UPLOAD_MEDIA;
          });
        }
        else {
            mediaService.uploadMedia($scope.media, $scope.file).then(function(response) {
              console.log(response);
              $window.location.href = '#/bundle/' + bundleId;
            }, function(error){
              $scope.error = MSG.ERR.UPLOAD_MEDIA;
            });
          }
        }
      }
  }]);
