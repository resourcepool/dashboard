'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('MediaListController',
    [ '$scope', '$location', '$routeParams', 'DATE', 'MSG', 'mediaService', 'bundleService', 'firewallService', 'responseService', 'dateService',
      function ($scope, $location, $routeParams, DATE, MSG, mediaService, bundleService, firewallService, responseService, dateService) {
 
        var bundleId = $routeParams.bundleId;
        $scope.bundle = {}
        $scope.medias = {};
        this.getMedias = getMedias;

        firewallService.isAuthenticated();
        getMedias();

        // on récupère tous les medias pour le bundle precisé
        bundleService.getById(bundleId).then(function(response) {
          if (responseService.isResponseOk($scope, response)) {
            $scope.bundle = response.data;
          }
        }, function(error) {
          $scope.error = MSG.ERR.SERVER;
        });

        function getMedias() {
          // on récupère tous les medias pour le bundle precisé
          mediaService.getAllByBundle(bundleId).then(function(response) {
            console.log(response);
            if (responseService.isResponseOk($scope, response)) {
              $scope.medias = dateService.formatDates(response.data);
            }
          }, function(error) {
            $scope.error = MSG.ERR.SERVER;
          });
        }

        // on bind la fonction de suppression
        $scope.remove = function(id) {
          if (confirm(MSG.CONF.DELETE_MEDIA)) {
            mediaService.removeById(id, bundleId).then(
              function (response) {
                console.log(response);
                if (responseService.isResponseOk($scope, response)) {
                  getMedias();
                }
              },
              function (error) {
                $scope.error = MSG.ERR.SERVER;
              }
            )
          }
        };
      }]);
