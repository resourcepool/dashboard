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
        	/*
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
            */
        }
    };
}]);

/*
dashboardFrontApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });
    }
}]);
*/