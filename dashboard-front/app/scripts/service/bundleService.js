'use strict';

angular
  .module("dashboardFrontApp")
  .service('bundleService', ['$http', 'API', 'DATE', function ($http, API, DATE) {

    this.getByTag = getByTag;
    this.save = save;
    this.removeByTag = removeByTag;
    this.getAll = getAll;

    function getByTag(tag) {
      return $http.get(API.BASE_URL + "/bundle/tag/" + tag).then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function save(bundle) {
      var p;
      if (bundle.uuid) {
        // If update, use PUT method
        p = $http.put(API.BASE_URL + "/bundle", bundle);
      } else {
        // If new, use POST method
        p = $http.post(API.BASE_URL + "/bundle", bundle);
      }
      return p.then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function removeByTag(tag) {
      return $http.delete(API.BASE_URL + "/bundle/tag/" + tag)
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


