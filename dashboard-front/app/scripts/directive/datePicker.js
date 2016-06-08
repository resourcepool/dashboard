'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .directive('datePicker', ['$parse', 'DATE', function ($parse, DATE) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var propery = attrs['datePicker'];
        console.log(propery);
        console.log(scope);
        $(element).datetimepicker({
          format: 'Y-m-d H:i:s',
          theme: 'dark',
          onChangeDateTime: function(date) {
            console.log(attrs);
            console.log(moment(date).format(DATE.SERVER_FORMAT));
            var datetime = moment(date).format(DATE.SERVER_FORMAT);
            console.log(element);
            scope.bundle.validity[attrs['datePicker']] = datetime;
            /*scope.$apply();
            */
          }
        });
        element.bind('change', function(){
          console.log("ok");
        });
      }
    };
  }]);
