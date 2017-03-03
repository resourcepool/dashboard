'use strict';

angular
  .module('dashboardFrontApp')
  .controller('BundleAddController',
    ['$scope', '$location', 'bundleService', 'dateService', 'firewallService', 'responseService', 'DATE', 'MSG',
      function ($scope, $location, bundleService, dateService, firewallService, responseService, DATE, MSG) {

        $scope.bundle = {};
        $scope.bundle.validity = {};
        $scope.nav = "Add a bundle";
        $scope.title = "Add";
        $scope.regexDate = DATE.REGEXP;

        firewallService.isAuthenticated();

        $scope.submit = function (isValid) {
          if (isValid && dateService.validateDates($scope.bundle.validity.start, $scope.bundle.validity.end, $scope)) {
            $scope.loading = true;
            bundleService.save($scope.bundle).then(
              function (response) {
                if (responseService.isResponseOK($scope, response)) {
                  $scope.loading = false;
                  $location.path("/");
                } else {
                  $scope.loading = false;
                  $scope.error = MSG.ERR.SERVER;
                }
              });
          }
        }
      }]);
