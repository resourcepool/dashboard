'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('responseService', ['DATE', function (DATE) {

    this.isResponseOK = isResponseOK;

    function isResponseOK($scope, response) {
      if (response.status != 200) {
        $scope.error = response.data.message;
        $scope.loading = false;
        return false;
      }
      return true;
    }

  }]);


