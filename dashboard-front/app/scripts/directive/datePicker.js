'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .directive('datePicker', ['$parse', 'DATE', function ($parse, DATE) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function(scope, element, attrs, ngModel) {
        var propery = attrs['datePicker'];
        console.log(propery);
        console.log(scope);
        console.log(ngModel);

        ngModel.$setViewValue("ok");

        $(element).datetimepicker({
          format: 'Y-m-d H:i:s',
          theme: 'dark',
          onChangeDateTime: function(date){
            var datetime = moment(date).format(DATE.SERVER_FORMAT);
            var ngModel = attrs['ngModel'];
            var entity = ngModel.substring(0, ngModel.indexOf('.'));
            scope[entity].validity[attrs['datePicker']] = datetime;
            scope.$apply;

          }
            /*
            function(date, ngModel) {
            console.log(attrs);
            console.log(moment(date).format(DATE.SERVER_FORMAT));
            var datetime = moment(date).format(DATE.SERVER_FORMAT);
            console.log(element);
            element.val(datetime);
            console.log(attrs['ngModel']);

            scope.$apply(function() {
              ngModel.$setViewValue(datetime);
            });
            //var ngModel = attrs['ngModel'];

            //scope[ngModel] = datetime;
            //scope.bundle.validity[attrs['datePicker']] = datetime;
            scope.$apply();
          }
          */
        });
        /*
        ngModel.$render = function() {
          element.val(ngModel.$modelValue);
          element.change();
        };
        */

        element.bind('change', function(){
          console.log("okefzefefez");
        });
      }
    };
  }]);
