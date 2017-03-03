'use strict';

angular
  .module('dashboardFrontApp')
  .controller('BundleEditController',
    ['$scope', '$routeParams', '$location', 'DATE', 'MSG', 'bundleService', 'dateService', 'firewallService', 'responseService',
    function ($scope, $routeParams, $location, DATE, MSG, bundleService, dateService, firewallService, responseService) {

    var tag = $routeParams.bundleTag;
    $scope.bundle = {};
    $scope.title = "Edit";

    firewallService.isAuthenticated();

    bundleService.getByTag(tag).then(function(response) {
      if (responseService.isResponseOK($scope, response)) {
        $scope.bundle = response.data;
        $scope.nav = $scope.bundle.name;
      }
    }, function(error) {
      $scope.error = MSG.ERR.SERVER;
    });

    $scope.submit = function (isValid) {
      if (isValid  && (!$scope.bundle.validity || dateService.validateDates($scope.bundle.validity.start, $scope.bundle.validity.end, $scope))) {
        $scope.loading = true;
        bundleService.save($scope.bundle).then(function (response) {
          if (responseService.isResponseOK($scope, response)) {
            $location.path("/");
          } else {
            $scope.loading = false;
            $scope.error = MSG.ERR.SERVER;
          }
        });
      }
    }
  }]);
