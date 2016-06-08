'use strict'
/**
 * Created by excilys on 06/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('mediaService', ['$http', 'API', 'DATE', function ($http, API, DATE) {

    this.getAllByBundle = getAllByBundle;
    this.getById = getById;
    this.removeById = removeById;
    this.uploadMedia = uploadMedia;
    this.addWebContent = addWebContent;

    /*
    this.update = update;
    this.formatDateTime = formatDateTime;
    */

    function getAllByBundle(bundleId) {
      return $http.get(API.BASE_URL + "/media/list.js")
        .then(function(success) {
          return formatDateTime(success.data);
        });
    }

    function getById(mediaId) {
      return $http.get(API.BASE_URL + "/media/media.js")
        .then(function(success) {
          return success.data;
        });
    }

    function removeById(id) {
      return $http.delete(API.BASE_URL + "medias/" + id)
        .then(function(success){
          return success;
        })
    }

    function addWebContent(media) {
      return $http.post(API.BASE_URL + "medias", media, {
          headers : {
           'Content-Type': 'application/json',
           'Accept': 'application/json',
          }
        }).then(function(response) {
          return response;
        });
    }

    function uploadMedia(media, file) {
      var fileForm = new FormData();
      fileForm.append('file', file);

      return $http.post(API.BASE_URL + "medias", fileForm, {
          headers: {
                'Content-Type': undefined,
                'Content': JSON.stringify($scope.content)
          }
        }).then(function(response) {
          return response;
        });
    }

    function formatDateTime(medias) {
      for (var media of medias) {
        media.validity.start = moment(media.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        media.validity.end = moment(media.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
      }
      return medias;
    }

  }]);


