'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('BundleEditController', ['$scope', '$http', '$routeParams', '$window', 'DATE', 'bundleService', 'dateService', function ($scope, $http, $routeParams, $window, DATE, bundleService, dateService) {

    var id = $routeParams.bundleId;

    $scope.bundle = {};
    $scope.title = "Edit";

    bundleService.getById(id).then(function(data){
      $scope.bundle = data;
      $scope.nav = $scope.bundle.name;
    }, function(error) {
      $scope.error = "Cannot get bundle";
    });

    $scope.submit = function (isValid) {
      if (isValid  && dateService.validateDates($scope.bundle.validity.start, $scope.bundle.validity.end, $scope)) {
        $scope.loading = true;
        bundleService.update($scope.bundle).then(function (success) {
          $window.location.href = '#/dashboard';
        }, function (error) {
          $scope.loading = false;
          $scope.error = "Cannot get bundle";
        });
      }
    }
  }]);
