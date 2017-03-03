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
      }
      return true;
    }

    function formatDates(entities) {
      console.log(entities)
      for (var entity in entities) {

        if (!entity.validity) {
          continue;
        }
        if (entity.validity.start) {
          entity.validity.start = moment(entity.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        }
        if (entity.validity.end) {
          entity.validity.end = moment(entity.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        }
      }
      return entities;
    }
  }]);


