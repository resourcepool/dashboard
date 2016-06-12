'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('BundleEditController',
    ['$scope', '$routeParams', '$location', 'DATE', 'MSG', 'bundleService', 'dateService', 'firewallService', 'responseService',
    function ($scope, $routeParams, $location, DATE, MSG, bundleService, dateService, firewallService, responseService) {

    var id = $routeParams.bundleId;
    $scope.bundle = {};
    $scope.title = "Edit";

    firewallService.isAuthenticated();

    bundleService.getById(id).then(function(response) {
      if (responseService.isResponseOk($scope, response)) {
        $scope.bunde = response.data;
        $scope.nav = $scope.bundle.name
      }
    }, function(error) {
      $scope.error = MSG.ERR.SERVER;
    });

    $scope.submit = function (isValid) {
      if (isValid  && dateService.validateDates($scope.bundle.validity.start, $scope.bundle.validity.end, $scope)) {
        $scope.loading = true;
        bundleService.save($scope.bundle).then(function (success) {
          if (responseService.isResponseOk($scope, response)) {
            $location.path("/");
          }
        }, function (error) {
          $scope.loading = false;
          $scope.error = MSG.ERR.SERVER;
        });
      }
    }
  }]);
