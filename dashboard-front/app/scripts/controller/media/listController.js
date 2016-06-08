'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaListController', [ '$scope', '$http', '$window', '$routeParams', 'DATE', 'MSG', 'mediaService', 'bundleService', function ($scope, $http, $window, $routeParams, DATE, MSG, mediaService, bundleService) {

    var bundleId = $routeParams.bundleId;
    $scope.bundle = {}
    $scope.medias = {};

    // on récupère tous les medias pour le bundle precisé
    bundleService.getById(bundleId).then(function(data) {
      console.log(data);
      $scope.bundle = data;
    }, function(error) {
      $scope.error = MSG.ERR.GET_BUNDLE;
    });


    // on récupère tous les medias pour le bundle precisé
    mediaService.getAllByBundle(bundleId).then(function(data) {
      console.log(data);
      $scope.medias = data;
    }, function(error) {
      $scope.error = MSG.ERR.GET_MEDIAS;
    });

    // on bind la fonction de suppression
    $scope.remove = function(id) {
      if (confirm(MSG.CONF.DELETE_MEDIA)) {
        mediaService.removeById(id,
          function (success) {
            console.log(success);
            $window.location.href = '#/bundle/medias/' + bundleId;
          },
          function (error) {
            $scope.error = MSG.ERR.DELETE_MEDIA;
          }
        )
      }
    };
  }]);
