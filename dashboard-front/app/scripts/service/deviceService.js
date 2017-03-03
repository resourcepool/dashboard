'use strict'
angular
  .module("dashboardFrontApp")
  .service('deviceService', ['$http', 'API', function ($http, API) {

    this.get = get;
    this.save = save;
    this.remove = remove;
    this.getAll = getAll;

    function get(id) {
      return $http.get(API.BASE_URL + "/device/" + id).then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function save(device) {
      var p;
      if (device.id) {
        // If update, use PUT method
        p = $http.put(API.BASE_URL + "/device", device);
      } else {
        // If new, use POST method
        p = $http.post(API.BASE_URL + "/device", device);
      }
      return p.then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function remove(id) {
      return $http.delete(API.BASE_URL + "/device/" + id)
        .then(function(response) {
          return response;
        }, function (error){
            return error;
        });
    }

    function getAll() {
      return $http.get(API.BASE_URL + "/device")
      .then(function(response) {
          return response;
      }, function (error){
        return error;
      });
    }

  }]);


