'use strict'
angular
  .module("dashboardFrontApp")
  .service('feedService', ['$http', 'API', 'DATE', function ($http, API, DATE) {

    this.get = get;
    this.save = save;
    this.remove = remove;
    this.getAll = getAll;

    function get(uuid) {
      return $http.get(API.BASE_URL + "/feed/" + uuid).then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function save(feed) {
      var p;
      if (feed.uuid) {
        // If update, use PUT method
        p = $http.put(API.BASE_URL + "/feed", feed);
      } else {
        // If new, use POST method
        p = $http.post(API.BASE_URL + "/feed", feed);
      }
      return p.then(function(response) {
        return response;
      }, function(error) {
        return error;
      });
    }

    function remove(uuid) {
      return $http.delete(API.BASE_URL + "/feed/" + uuid)
        .then(function(response) {
          return response;
        }, function (error){
            return error;
        });
    }

    function getAll() {
      return $http.get(API.BASE_URL + "/feed")
      .then(function(response) {
          return response;
      }, function (error){
        return error;
      });
    }

  }]);


