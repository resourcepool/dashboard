'use strict';
angular
  .module('dashboardFrontApp')
  .controller('FeedAddController',
    ['$scope', '$location', 'bundleService', 'feedService', 'firewallService', 'responseService', 'DATE', 'MSG',
      function ($scope, $location, bundleService, feedService, firewallService, responseService, DATE, MSG) {

        $scope.feed = {};
        $scope.bundles = {};
        $scope.nav = "Add a feed";
        $scope.title = "Add";

        firewallService.isAuthenticated();

        bundleService.getAll().then(function (response) {
          if (responseService.isResponseOK($scope, response)) {
            for (var i in response.data) {
              $scope.bundles[response.data[i].tag] = {name: response.data[i].name, selected: false};
            }
          }
        }, function (error) {
          $scope.error = MSG.ERR.GET_BUNDLES;
        });

        $scope.select = function (tag) {
          tag.selected = !tag.selected;
        };

        var getSelected = function () {
          var r = [];
          for (var i in $scope.bundles) {
            if ($scope.bundles[i].selected)
              r.push(i);
          }
          return r;
        };

        $scope.submit = function (isValid) {
          if (getSelected().length === 0) {
            $scope.error = MSG.ERR.FEED_MUST_HAVE_BUNDLES;
          } else if (isValid) {
            $scope.loading = true;
            $scope.feed.bundleTags = getSelected();
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
        };
      }]);
