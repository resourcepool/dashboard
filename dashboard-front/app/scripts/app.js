'use strict';

/**
 * @ngdoc overview
 * @name dashboardFrontApp
 * @description
 * # dashboardFrontApp
 *
 * Main module of the application.
 */

var dashboardFrontApp = angular.module('dashboardFrontApp', [
 'ngRoute',
 'slideshowControllers',
 'contentControllers',
 ]);

dashboardFrontApp.constant("API", {
  "BASE_URL": "http://vps229493.ovh.net:8080/dashboard/api/"
})
.constant("DATE", {
    "SERVER_FORMAT": "dd-MM-YYYY HH:mm:ss",
    "DISPLAY_FORMAT": "DD-MM-YYYY HH:mm"
});


dashboardFrontApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeCtrl'
		}).
		when('/slideshow/add', {
			templateUrl: 'partials/slideshow/form.html',
			controller: 'SlideshowAddCtrl'
		}).
		when('/slideshow/edit/:slideshowId', {
			templateUrl: 'partials/slideshow/form.html',
			controller: 'SlideshowEditCtrl'
		}).
		when('/slideshow/:slideshowId/content/add/', {
			templateUrl: 'partials/content/form.html',
			controller: 'ContentAddCtrl'
		}).when('/slideshow/:slideshowId/content/edit/:contentId', {
			templateUrl: 'partials/content/form.html',
			controller: 'ContentEditCtrl'
		}).
		otherwise({
			redirectTo: '/home'
		});
	}]);


dashboardFrontApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

dashboardFrontApp.directive('datePicker', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
        	var propery = attrs['datePicker'];
        	console.log(propery);
        	console.log(scope);
        	$(element).datetimepicker({
    			format: 'd-m-Y H:i:s',
    			theme: 'dark',
    			onChangeDateTime: function(date) {
    				console.log(attrs);
    				console.log(moment(date).format("DD-MM-YYYY HH:mm:ss"));
    				var datetime = moment(date).format("DD-MM-YYYY HH:mm:ss");
    				scope.slideShow[attrs['datePicker']] = datetime;
                    scope.$apply();
    			}
    		});
        	element.bind('change', function(){
        		console.log("ok");
        	});
        }
    };
}]);


dashboardFrontApp.service('slideShowService', ['$http', 'API', function ($http, API) {
    this.getById = function (id, successCallBack, errorCallBack) {
        $http({
            method: 'GET',
            url: API.BASE_URL + "slideshows/" + id + "?json=full"
        }).then(function(success) {
            successCallBack(success);
        }, function(error) {
            errorCallBack(error);
        });
    }

    this.removeById = function(id, successCallBack, errorCallBack) {
         $http.delete(API.BASE_URL + "slideshows/" + id)
            .success(function (success) {
                successCallBack(success);
            })
            .error(function (error) {
                errorCallBack(error);
            });
    }

    this.getAll = function (param, successCallBack, errorCallBack) {
        
        $http({
            method: 'GET',
            url: API.BASE_URL + "slideshows?json=" + param
        }).then(function(success) {
            successCallBack(success);
        }, function(error) {
            errorCallBack(error);
        });
    }
}]);

