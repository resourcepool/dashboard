'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('HomeController',
    [ '$scope', '$cookieStore','DATE', 'MSG', 'bundleService', 'firewallService', 'messageService',
      function ($scope, $cookieStore, DATE, MSG, bundleService, firewallService, messageService) {

        $scope.bundles = {};

        firewallService.isAuthenticated();

        // on récupère tous les bundles
        function getBundles() {
          bundleService.getAll().then(function(response) {
            console.log(response);
            messageService.checkResponse($scope, response);
            $scope.bundles = response.data;
          }, function(error) {
            $scope.error = MSG.ERR.GET_MEDIAS;
          });
        }
        getBundles();


        // on bind la fonction de suppression
        $scope.remove = function(id) {
          if (confirm(MSG.CONF.DELETE_BUNDLE)) {
            bundleService.removeById(id).then(
              function (response) {
                messageService.checkResponse($scope, response);
                console.log(response);
                getBundles()
              },
              function (response) {
                $scope.error = MSG.CONF.ERR.DELETE_BUNDLE;
              }
            )
          }
        };
      }]);
