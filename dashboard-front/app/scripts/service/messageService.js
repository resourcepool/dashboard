'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('messageService', ['DATE', function (DATE) {

    this.checkResponse = checkResponse;

    function checkResponse($scope, response) {
      if (response.status != 200) {
        $scope.error = response.data.message;
      }
    }

  }]);


