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
 ]);

dashboardFrontApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeController'
		}).
		when('/bundle/add', {
			templateUrl: 'partials/bundle/form.html',
			controller: 'BundleAddController'
		}).
    when('/bundle/medias/:bundleId', {
      templateUrl: 'partials/media/list.html',
      controller: 'MediaListController'
    }).
		when('/bundle/edit/:bundleId', {
			templateUrl: 'partials/bundle/form.html',
			controller: 'BundleEditController'
		}).
		when('/bundle/:bundleId/media/add/', {
			templateUrl: 'partials/media/form.html',
			controller: 'MediaAddController'
		}).when('/bundle/:bundleId/media/edit/:mediaId', {
			templateUrl: 'partials/media/form.html',
			controller: 'MediaEditController'
		}).
		otherwise({
			redirectTo: '/home'
		});
	}]);
