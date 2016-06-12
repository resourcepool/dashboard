'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaEditController', 
    ['$scope', '$routeParams', '$location', 'MSG', 'bundleService', 'mediaService', 'firewallService', 'responseService',
    function ($scope, $routeParams, $location, MSG, bundleService, mediaService, firewallService, responseService) {

      var bundleId = $routeParams.bundleId;
      var mediaId = $routeParams.mediaId;

      firewallService.isAuthenticated();

      $scope.bundle = {};
      $scope.media = {};
      $scope.media.validity = {};
      $scope.file = {};
      $scope.title = "Edit";

      // On récupère le bundle correspondant
      bundleService.getById(bundleId).then(function(response) {
        console.log(response);
        if (responseService.isResponseOk($scope, response)) {
          $$scope.bundle = response.data;
          $scope.nav = "Edit " + $scope.bundle.name;
        }
      }, function(error) {
        $scope.error = MSG.ERR.SERVER;
      });


      // On récupère le bundle correspondant
      mediaService.getById(mediaId, bundleId).then(function(response) {
        console.log(response);
        if (responseService.isResponseOk($scope, response)) {
          $scope.media = response.data;
        }
      }, function(error) {
        $scope.error = MSG.ERR.SERVER;
      });

      // on définit la fonction de submit
      $scope.submit = function (isValid) {
      if (isValid) {
        $scope.loading = true;

        if ($scope.media.mediaType == 'web') {
          mediaService.saveWebMedia($scope.media).then(function(response) {
            console.log(response);
            if (responseService.isResponseOk($scope, response)) {
              $location.path("/bundle/" + bundleId);
            }
          }, function(error){
            $scope.error = MSG.ERR.SERVER;
          });
        }
        else {
            mediaService.saveMedia($scope.media, $scope.file).then(function(response) {
              console.log(response);
              if (responseService.isResponseOk($scope, response)) {
                $location.path("/bundle/" + bundleId);
              }
            }, function(error) {
              $scope.error = MSG.ERR.SERVER;
            });
          }
        }
      }
  }]);
