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
			controller: 'ContentCreateCtrl'
		}).when('/slideshow/content/edit/:contentId', {
			templateUrl: 'partials/content/form.html',
			controller: 'ContentEditCtrl'
		}).
		otherwise({
			redirectTo: '/home'
		});
	}]);