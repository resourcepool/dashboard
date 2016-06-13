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
  'ngCookies'
 ]);

dashboardFrontApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
    when('/login', {
      templateUrl: 'partials/login.html',
      controller: 'LoginController'
    }).
		when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeController'
		}).
		when('/bundle/add', {
			templateUrl: 'partials/bundle/form.html',
			controller: 'BundleAddController'
		}).
    when('/bundle/:bundleTag/media', {
      templateUrl: 'partials/media/list.html',
      controller: 'MediaListController'
    }).
		when('/bundle/edit/:bundleTag', {
			templateUrl: 'partials/bundle/form.html',
			controller: 'BundleEditController'
		}).
		when('/bundle/:bundleTag/media/add', {
			templateUrl: 'partials/media/form.html',
			controller: 'MediaAddController'
		}).when('/bundle/:bundleTag/media/edit/:mediaId', {
			templateUrl: 'partials/media/form.html',
			controller: 'MediaEditController'
		}).otherwise({
			redirectTo: '/home'
		});
	}]);
