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
 'diaporamaControllers',
 'contentControllers',
 'lr.upload'
 ]);

dashboardFrontApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeCtrl'
		}).
		when('/diaporama/create/', {
			templateUrl: 'partials/diaporama/form.html',
			controller: 'DiaporamaCreateCtrl'
		}).
		when('/diaporama/edit/:diaporamaId', {
			templateUrl: 'partials/diaporama/form.html',
			controller: 'DiaporamaEditCtrl'
		}).
		when('/diaporama/content/create/:diaporamaId', {
			templateUrl: 'partials/content/create.html',
			controller: 'ContentCreateCtrl'
		}).
		otherwise({
			redirectTo: '/home'
		});
	}]);