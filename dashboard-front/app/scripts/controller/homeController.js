'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('HomeController',
    [ '$scope', '$cookieStore', '$window', 'DATE', 'MSG', 'bundleService', 'firewallService',
      function ($scope, $cookieStore, $window, DATE, MSG, bundleService, firewallService) {

        $scope.bundles = {};

        console.log($cookieStore.get('globals'));
        firewallService.isAuthenticated();

        // on récupère tous les bundles
        bundleService.getAll().then(function(data) {
          console.log(data);
          $scope.bundles = data;
        }, function(error) {
          $scope.error = MSG.ERR.GET_MEDIAS;
        });

        // on bind la fonction de suppression
        $scope.remove = function(id) {
          if (confirm(MSG.CONF.DELETE_BUNDLE)) {
            bundleService.removeById(id).then(
              function (success) {
                console.log(success);
                $window.location.href = '#/';
              },
              function (error) {
                $scope.error = MSG.CONF.ERR.DELETE_BUNDLE;
              }
            )
          }
        };
      }]);
