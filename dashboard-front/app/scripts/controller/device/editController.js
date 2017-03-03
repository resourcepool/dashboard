'use strict';
angular
  .module('dashboardFrontApp')
  .controller('DeviceEditController',
    ['$scope', '$routeParams', '$location', 'DATE', 'MSG', 'deviceService', 'feedService', 'firewallService', 'responseService',
      function ($scope, $routeParams, $location, DATE, MSG, deviceService, feedService, firewallService, responseService) {

        var deviceId = $routeParams.deviceId;
        $scope.device = {};
        $scope.feeds = {};
        $scope.title = "Edit";

        firewallService.isAuthenticated();

        function getDevices() {
          deviceService.get(deviceId).then(function (response) {
            if (responseService.isResponseOK($scope, response)) {
              console.log("Ohh:");
              console.log(response.data);
              $scope.device = response.data;
              $scope.nav = $scope.device.name;
            }
          }, function (error) {
            $scope.error = MSG.ERR.SERVER;
          });
        }

        feedService.getAll().then(function (response) {
          if (responseService.isResponseOK($scope, response)) {
            $scope.feeds = response.data;
            getDevices();
          }
        }, function (error) {
          $scope.error = MSG.ERR.GET_BUNDLES;
        });

        $scope.submit = function (isValid) {
          if (isValid) {
            $scope.loading = true;
            deviceService.save($scope.device).then(
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
