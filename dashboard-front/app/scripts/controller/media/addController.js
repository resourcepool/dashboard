'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaAddController',[
    '$scope', '$http', '$routeParams', '$window', 'API', 'DATE', 'MSG', 'bundleService', 'mediaService', 'firewallService',
    function ($scope, $http, $routeParams, $window, API, DATE, MSG, bundleService, mediaService, firewallService ) {

      var bundleId = $routeParams.bundleId;
      var mediaId = $routeParams.mediaId;

      firewallService.isAuthenticated();

      $scope.bundle = {};
      $scope.media = {};
      $scope.media.validity = {};
      $scope.regexDate = DATE.REGEXP;
      //$scope.revision = 0;

      $scope.media.uuidBundle = bundleId;
      $scope.file = {};
      $scope.nav = "Add a media";
      $scope.title = "Add";
      $scope.loading = false;

      // On récupère le bundle correspondant
      bundleService.getById(bundleId).then(function(data) {
        $scope.bundle = data;
      }, function(error) {
        $scope.error = MSG.ERR.GET_BUNDLE;
      });

      // on définit la fonction de submit
      $scope.submit = function (isValid) {
        if (isValid) {
          $scope.loading = true;
          console.log($scope.media);
          if ($scope.media.mediaType == 'image' || $scope.media.mediaType == 'video') {
            mediaService.saveMedia($scope.media, $scope.file).then(function(response) {
              console.log(response);
              $window.location.href = '#/bundle/' + bundleId;
            }, function(error){
              $scope.error = MSG.ERR.UPLOAD_MEDIA;
            });
          }
          else {
            mediaService.saveWebMedia($scope.media).then(function(response) {
              console.log(response);
              $window.location.href = '#/bundle/' + bundleId;
            }, function(error){
              $scope.error = MSG.ERR.UPLOAD_MEDIA;
            });
          }
        }
      }
    }]);
