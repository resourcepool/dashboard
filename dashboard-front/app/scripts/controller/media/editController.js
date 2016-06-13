'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaEditController',
    ['$scope', '$routeParams', '$location', 'MSG', 'bundleService', 'mediaService', 'firewallService', 'responseService',
      function ($scope, $routeParams, $location, MSG, bundleService, mediaService, firewallService, responseService) {

        var bundleTag = $routeParams.bundleTag;
        var mediaId = $routeParams.mediaId;

        firewallService.isAuthenticated();

        $scope.bundle = {};
        $scope.media = {};
        $scope.media.validity = {};
        $scope.file = {};
        $scope.title = "Edit";

        // On récupère le bundle correspondant
        bundleService.getByTag(bundleTag).then(function (response) {
          console.log(response);
          if (responseService.isResponseOK($scope, response)) {
            $scope.bundle = response.data;
            $scope.nav = "Edit " + $scope.bundle.name;
          } else {
            $scope.error = MSG.ERR.SERVER;
          }
        });


        // On récupère le bundle correspondant
        mediaService.getById(mediaId, bundleTag).then(function (response) {
          console.log(response);
          if (responseService.isResponseOK($scope, response)) {
            $scope.media = response.data;
          } else {
            $scope.error = MSG.ERR.SERVER;
          }
        });

        // on définit la fonction de submit
        $scope.submit = function (isValid) {
          if (isValid) {
            $scope.loading = true;
            mediaService.saveMedia($scope.media, $scope.file, true).then(function (response) {
              console.log(response);
              if (responseService.isResponseOK($scope, response)) {
                $location.path("/bundle/" + bundleTag);
              } else {
                $scope.error = MSG.ERR.SERVER;
              }
            });
          }
        }
      }
    ])
;
