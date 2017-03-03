'use strict';

angular
  .module("dashboardFrontApp")
  .service('mediaService', ['$http', 'API', 'DATE', function ($http, API, DATE) {

    this.getAllByBundle = getAllByBundle;
    this.getById = getById;
    this.removeById = removeById;
    this.saveMedia = saveMedia;


    function getAllByBundle(bundleTag) {
      return $http.get(API.BASE_URL + "/media?bundle=" + bundleTag)
        .then(function (response) {
          return response;
        }, function (error) {
          return error;
        });
    }

    function getById(mediaId) {
      return $http.get(API.BASE_URL + "/media/" + mediaId)
        .then(function (response) {
          return response;
        }, function (error) {
          return error;
        });
    }

    function removeById(mediaId) {
      return $http.delete(API.BASE_URL + "/media/" + mediaId)
        .then(function (response) {
          return response;
        }, function (error) {
          return error;
        })
    }

    function saveMedia(media, file, update) {
      var fileForm = new FormData();
      console.log(media);
      fileForm.append('file', file);
      fileForm.append('media', JSON.stringify(media));
      //fileForm.append('media', media);

      var p;
      if (update) {
        // Put if update
        p = $http.put(API.BASE_URL + "/media", fileForm, {
          headers: {
            'Content-Type': undefined
          }
        });
      } else {
        // Post if new
        p = $http.post(API.BASE_URL + "/media", fileForm, {
          headers: {
            'Content-Type': undefined
          }
        });
      }

      return p.then(function (response) {
        return response;
      }, function (error) {
        return error;
      });
    }

  }]);


