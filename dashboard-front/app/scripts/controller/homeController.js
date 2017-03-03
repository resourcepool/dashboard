'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('HomeController',
    [ '$scope', '$cookieStore', '$location', 'DATE', 'MSG', 'bundleService', 'feedService', 'deviceService', 'firewallService', 'responseService', 'dateService',
      function ($scope, $cookieStore, $location, DATE, MSG, bundleService, feedService, deviceService, firewallService, responseService, dateService) {
        // Check authentication
        firewallService.isAuthenticated();

        // Devices
        $scope.devices = {};
        // Feeds
        $scope.feeds = {};
        // Bundles
        $scope.bundles = {};

        function getDevices() {
          deviceService.getAll().then(function(response) {
            console.log('Devices retrieved: ');
            console.log(response);
            if (responseService.isResponseOK($scope, response)) {
              $scope.devices = response.data;
            }
          }, function(error) {
            console.error('Error occured while retrieving devices: ');
            console.error(error);
            $scope.error = MSG.ERR.GET_DEVICES;
          });
        }

        // on récupère tous les devices
        getDevices();

        // on bind la fonction de suppression de device
        $scope.removeDevice = function(id) {
          if (confirm(MSG.CONF.DELETE_DEVICE)) {
            deviceService.remove(id).then(
              function (response) {
                if (responseService.isResponseOK($scope, response)) {
                  getDevices();
                }
              },
              function (response) {
                $scope.error = MSG.CONF.ERR.DELETE_DEVICE;
              }
            )
          }
        };


        function getFeeds() {
          feedService.getAll().then(function(response) {
            console.log('Feeds retrieved: ');
            console.log(response);
            if (responseService.isResponseOK($scope, response)) {
              $scope.feeds = response.data;
            }
          }, function(error) {
            console.error('Error occured while retrieving feeds: ');
            console.error(error);
            $scope.error = MSG.ERR.GET_FEEDS;
          });
        }

        // on récupère tous les feeds
        getFeeds();

        // on bind la fonction de suppression de feed
        $scope.removeFeed = function(uuid) {
          if (confirm(MSG.CONF.DELETE_FEED)) {
            feedService.remove(uuid).then(
              function (response) {
                if (responseService.isResponseOK($scope, response)) {
                  getFeeds();
                }
              },
              function (response) {
                $scope.error = MSG.CONF.ERR.DELETE_FEED;
              }
            )
          }
        };

        function getBundles() {
          bundleService.getAll().then(function(response) {
            console.log('Bundles retrieved: ');
            console.log(response);
            if (responseService.isResponseOK($scope, response)) {
            	$scope.bundles = dateService.formatDates(response.data);
            }
          }, function(error) {
            console.error('Error occured while retrieving bundles: ');
            console.error(error);
            $scope.error = MSG.ERR.GET_BUNDLES;
          });
        }

        // on récupère tous les bundles
        getBundles();

        // on bind la fonction de suppression de bundle
        $scope.removeBundle = function(tag) {
          if (confirm(MSG.CONF.DELETE_BUNDLE)) {
            bundleService.removeByTag(tag).then(
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
