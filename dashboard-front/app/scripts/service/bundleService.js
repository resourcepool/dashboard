'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('bundleService', ['$http', 'API', 'DATE', function ($http, API, DATE) {

    this.getById = getById;
    this.save = save;
    this.removeById = removeById;
    this.getAll = getAll;

    function getById(id) {
      return $http.get(API.BASE_URL + "/bundle/" + id).then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function save(bundle) {
      return $http.post(API.BASE_URL + "/bundle", bundle)
        .then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function removeById(id) {
      return $http.delete(API.BASE_URL + "/bundle/" + id)
        .then(function(response) {
          return response;
        }, function (error){
            return error;
        });
    }

    function getAll() {
      return $http.get(API.BASE_URL + "/bundle")
      .then(function(response) {
          return response;
      }, function (error){
        return error;
      });
    }

  }]);


