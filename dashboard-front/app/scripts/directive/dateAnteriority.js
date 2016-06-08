/**
 * Created by excilys on 07/06/16.
 */

/*
angular.module('dashboardFrontApp')
  .directive('afterDateTime', function() {
    return {
      restrict: 'A',//E: element (after-date-time) / A:attribut / C: class / M: comment
      require: 'ngModel',
      scope: {
        bundle: '' //slide-show="bundle"
      },
      link: function(scope, element, attrs, ngModel) {

        console.log(attrs);
        console.log(scope.bundle.startDateTime);

        ngModel.$parsers.unshift(validate);

        scope.$watch(attrs.bundle.startDateTime, function() {
          ngModel.$setViewValue(ngModel.$viewValue);
        });

      function validate(value) {
        console.log("validation");
      }


      }
    };
  });

/*
 require: 'ngModel',
 link: function (scope, elem, attrs, ngModel) {
 ngModel.$parsers.unshift(validate);
 // Force-trigger the parsing pipeline.
 /*
 scope.$watch(attrs.afterDateTime, function() {
 ngModel.$setViewValue(ngModel.$viewValue);
 });
 */
/*
 function validate(value) {
 console.log(value);
 /* var isValid = scope.$eval(attrs.sameAs) == value;

 ngModel.$setValidity('same-as', isValid);

 return isValid ? value : undefined;
 }
 }
 };*/
