'use strict';

angular.module('dashboardFrontApp')
  .controller('LoginController',
    ['$scope', '$rootScope', '$cookieStore', '$location', 'authenticationService',
      function ($scope, $rootScope, $cookieStore, $location, authenticationService) {

        if ($cookieStore.get('globals')) {
          $scope.username = $cookieStore.get('globals').currentUser.username;
        }
        // reset login status
        authenticationService.clearCredentials();

        $scope.login = function (isValid) {

          if (isValid) {
            authenticationService.setCredentials($scope.username, $scope.password);
            $location.path('/');
          }
          /*
          console.log(isValid);

          console.log("ok");
          $scope.loading = true;

          authenticationService.login($scope.username, $scope.password, function(response) {
            if(response.success) {
              authenticationService.setCredentials($scope.username, $scope.password);
              //$location.path('/');
            } else {
              $scope.error = response.message;
              $scope.loading = false;
            }
          });
          */

        };
      }]);
