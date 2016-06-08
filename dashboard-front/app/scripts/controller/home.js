'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .controller('HomeController', [ '$scope', '$http', '$window', 'DATE', 'bundleService', function ($scope, $http, $window, DATE, bundleService) {
    $scope.bundles = {};

    // on récupère tous les bundles
    bundleService.getAll().then(function(data) {
      console.log(data);
      $scope.bundles = data;
    }, function(error) {
      $scope.error = "Erreur lors de l'affichage des slideshows";
    });


    // on bind la fonction de suppression
    $scope.remove = function(id) {
      if (confirm("Do you want to delete selected slideshow ?")) {
        bundleService.removeById(id,
          function (success) {
            console.log(success);
            $window.location.href = '#/dashboard';
          },
          function (error) {
            $scope.error = "Erreur lors de la suppression d'un slideshow";
          }
        )
      }
    };
  }]);
