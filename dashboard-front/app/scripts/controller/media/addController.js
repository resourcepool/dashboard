'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaAddController',
    ['$scope', '$routeParams', '$location', 'DATE', 'MSG', 'bundleService', 'mediaService', 'firewallService', 'responseService',
      function ($scope, $routeParams, $location, DATE, MSG, bundleService, mediaService, firewallService, responseService) {

        var bundleTag = $routeParams.bundleTag;
        var mediaId = $routeParams.mediaId;

        firewallService.isAuthenticated();
        $scope.validity = {};
        $scope.bundle = {};
        $scope.media = {};
        $scope.media.validity = {};
        $scope.regexDate = DATE.REGEXP;

        $scope.media.bundleTag = bundleTag;
        $scope.file = {};
        $scope.nav = "Add a media";
        $scope.title = "Add";

        // On récupère le bundle correspondant
        bundleService.getByTag(bundleTag).then(function (response) {
          if (responseService.isResponseOK($scope, response)) {
            $scope.bundle = response.data;
          } else {
            $scope.error = MSG.ERR.SERVER;
          }
        });
        
        $scope.updateBox = function() {
          $scope.bundle.validity = $
        };

        // on définit la fonction de submit
        $scope.submit = function (isValid) {
          if (isValid) {
            $scope.loading = true;
            console.log($scope.media);

            mediaService.saveMedia($scope.media, $scope.file).then(function (response) {
              console.log(response);
              if (responseService.isResponseOK($scope, response)) {
                $location.path('/bundle/' + bundleTag + "/media");
              } else {
                $scope.loading = false;
                $scope.error = MSG.ERR.SERVER;
              }
            });
          }
        }
      }]);
