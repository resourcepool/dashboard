'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular.module("dashboardFrontApp")
  .service('responseService', ['DATE', function (DATE) {

    this.isResponseOK = isResponseOK;

    function isResponseOK($scope, response) {
      if (response.status != 200) {
        console.error("Error in response");
        $scope.error = response ? response.data ? response.data.message : null : null;
        $scope.loading = false;
        return false;
      }
      return true;
    }

  }]);


