'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('HomeController',
    [ '$scope', '$cookieStore','DATE', 'MSG', 'bundleService', 'firewallService', 'responseService', 'dateService',
      function ($scope, $cookieStore, DATE, MSG, bundleService, firewallService, responseService, dateService) {

        $scope.bundles = {};

        firewallService.isAuthenticated();

        function getBundles() {
          bundleService.getAll().then(function(response) {
            console.log(response);
            if (responseService.isResponseOK($scope, response)) {
            	$scope.bundles = dateService.formatDates(response.data);
            }
          }, function(error) {
            $scope.error = MSG.ERR.GET_MEDIAS;
          });
        }
        // on récupère tous les bundles
        getBundles();


        // on bind la fonction de suppression
        $scope.remove = function(id) {
          if (confirm(MSG.CONF.DELETE_BUNDLE)) {
            bundleService.removeById(id).then(
              function (response) {
                if (responseService.isResponseOK($scope, response)) {
                	getBundles();
                }
              },
              function (response) {
                $scope.error = MSG.CONF.ERR.DELETE_BUNDLE;
              }
            )
          }
        };
      }]);
