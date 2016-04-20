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
 'dashboardControllers'
 ]);

dashboardFrontApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeCtrl'
		}).
		when('/diaporama/create/', {
			templateUrl: 'partials/diaporama/create.html',
			controller: 'DiaporamaCreateCtrl'
		}).
		when('/diaporama/edit/:diaporamaId', {
			templateUrl: 'partials/diaporama/edit.html',
			controller: 'DiaporamaEditCtrl'
		}).
		otherwise({
			redirectTo: '/home'
		});
	}]);