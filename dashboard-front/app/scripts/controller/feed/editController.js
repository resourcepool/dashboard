'use strict';
angular
  .module('dashboardFrontApp')
  .controller('FeedEditController',
    ['$scope', '$routeParams', '$location', 'DATE', 'MSG', 'bundleService', 'feedService', 'firewallService', 'responseService',
      function ($scope, $routeParams, $location, DATE, MSG, bundleService, feedService, firewallService, responseService) {

        var uuid = $routeParams.feedUuid;
        $scope.feed = {};
        $scope.bundles = {};
        $scope.title = "Edit";

        firewallService.isAuthenticated();

        function getFeeds() {
          feedService.get(uuid).then(function (response) {
            if (responseService.isResponseOK($scope, response)) {
              $scope.feed = response.data;
              for (var i in response.data.bundleTags) {
                $scope.bundles[response.data.bundleTags[i]].selected = true;
              }
              $scope.nav = $scope.feed.name;
            }
          }, function (error) {
            $scope.error = MSG.ERR.SERVER;
          });
        }

        bundleService.getAll().then(function (response) {
          if (responseService.isResponseOK($scope, response)) {
            for (var i in response.data) {
              $scope.bundles[response.data[i].tag] = {name: response.data[i].name, selected: false};
            }
            getFeeds();
          }
        }, function (error) {
          $scope.error = MSG.ERR.GET_BUNDLES;
        });

        var getSelected = function () {
          var r = [];
          for (var i in $scope.bundles) {
            if ($scope.bundles[i].selected)
              r.push(i);
          }
          return r;
        };

        $scope.select = function (tag) {
          tag.selected = !tag.selected;
        };

        $scope.submit = function (isValid) {
          if (getSelected().length === 0) {
            $scope.error = MSG.ERR.FEED_MUST_HAVE_BUNDLES;
          } else if (isValid) {
            $scope.loading = true;
            $scope.feed.bundleTags = getSelected();
            console.log(getSelected());
            feedService.save($scope.feed).then(
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
