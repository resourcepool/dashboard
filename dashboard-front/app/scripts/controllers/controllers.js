var dashboardControllers = angular.module('dashboardControllers', []);

dashboardControllers.controller('HomeCtrl', [ function ($scope) {
	/*
	$scope.phones = [
     {'name': 'Nexus S',
      'snippet': 'Fast just got faster with Nexus S.'},
     {'name': 'Motorola XOOM™ with Wi-Fi',
      'snippet': 'The Next, Next Generation tablet.'},
     {'name': 'MOTOROLA XOOM™',
      'snippet': 'The Next, Next Generation tablet.'}
	 ];
	 */
}]);

dashboardControllers.controller('DiaporamaCreateCtrl', ['$scope', '$http', function ($http, $scope) {
}]);
dashboardControllers.controller('DiaporamaEditCtrl', ['$scope', '$http', '$routeParams', function ($scope, $routeParams) {
}]);