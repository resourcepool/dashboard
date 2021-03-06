'use strict';

angular
  .module('dashboardFrontApp')
  .controller('MediaListController',
    [ '$scope', '$location', '$routeParams', 'DATE', 'MSG', 'mediaService', 'bundleService', 'firewallService', 'responseService', 'dateService',
      function ($scope, $location, $routeParams, DATE, MSG, mediaService, bundleService, firewallService, responseService, dateService) {

        var bundleTag = $routeParams.bundleTag;
        $scope.bundle = {};
        $scope.medias = {};
        this.getMedias = getMedias;

        firewallService.isAuthenticated();
        getMedias();

        // on récupère tous les medias pour le bundle precisé
        bundleService.getByTag(bundleTag).then(function(response) {
          if (responseService.isResponseOK($scope, response)) {
            $scope.bundle = response.data;
          }
        }, function(error) {
          $scope.error = MSG.ERR.SERVER;
        });

        function getMedias() {
          // on récupère tous les medias pour le bundle precisé
          mediaService.getAllByBundle(bundleTag).then(function(response) {
            console.log(response);
            if (responseService.isResponseOK($scope, response)) {
              $scope.medias = dateService.formatDates(response.data);
            }
          }, function(error) {
            $scope.error = MSG.ERR.SERVER;
          });
        }

        // on bind la fonction de suppression
        $scope.remove = function(id) {
          if (confirm(MSG.CONF.DELETE_MEDIA)) {
            mediaService.removeById(id, bundleTag).then(
              function (response) {
                console.log(response);
                if (responseService.isResponseOK($scope, response)) {
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
