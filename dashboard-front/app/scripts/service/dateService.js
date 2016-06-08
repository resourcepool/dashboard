'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('dateService', ['DATE', function (DATE) {

    this.validateDates = validateDates;

    function validateDates(start, end, $scope) {
      var bool = moment(start, DATE.SERVER_FORMAT).isBefore(end, DATE.SERVER_FORMAT);
      if (!bool) {
        $scope.error = "Start datetime must be before End datetime";
        return bool;
      }
      return true;
    }
  }]);


