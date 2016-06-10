'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('dateService', ['DATE', function (DATE) {

    this.validateDates = validateDates;
    this.formatDates = formatDates;

    function validateDates(start, end, $scope) {
      if (start && end) {
        var bool = moment(start, DATE.SERVER_FORMAT).isBefore(end, DATE.SERVER_FORMAT);
        if (!bool) {
          $scope.error = "Start datetime must be before End datetime";
          return bool;
        }
        return true;
      }
    }

    function formatDates(entities) {
      for (var entity of entities) {
        entity.validity.start = moment(entity.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        entity.validity.end = moment(entity.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
      }
      return entities;
    }

  }]);


