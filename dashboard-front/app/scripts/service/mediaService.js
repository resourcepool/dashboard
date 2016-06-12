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
    this.saveMedia = saveMedia;
    this.saveWebMedia = saveWebMedia;

    /*
    this.update = update;
    this.formatDateTime = formatDateTime;
    */

    function getAllByBundle(bundleId) {
      return $http.get(API.BASE_URL + "media/" + bundleId)
        .then(function(response) {
          return response;
          //return formatDateTime(success.data);
        }, function (error){
          return error;
        });
    }

    function getById(mediaId, bundleId) {
      return $http.get(API.BASE_URL + "media/"+ bundleId + "/" + mediaId)
        .then(function(response) {
          return response;
        }, function (error) {
          return error;
        });
    }

    function removeById(mediaId, bundleId) {
      return $http.delete(API.BASE_URL + "media/"+ bundleId + "/" + mediaId)
        .then(function(response){
          return response;
        }, function (error) {
          return error;
        })
    }

    function saveWebMedia(media) {
      return $http.post(API.BASE_URL + "media", media, {
          headers : {
           'Content-Type': 'application/json',
           'Accept': 'application/json',
          }
        }).then(function(response) {
          return response;
        }, function (error){
          return error;
        });
    }

    function saveMedia(media, file) {
      var fileForm = new FormData();
      console.log(media);
      fileForm.append('file', file);
      fileForm.append('media', JSON.stringify(media));
      //fileForm.append('media', media);

      return $http.post(API.BASE_URL + "media", fileForm, {
          headers: {
                'Content-Type': undefined,
          }
        }).then(function(response) {
          return response;
        }, function (error) {
          return error;
        });
    }
    /*
    function formatDateTime(medias) {
      for (var media of medias) {
        media.validity.start = moment(media.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        media.validity.end = moment(media.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
      }
      return medias;
    }
    */

  }]);


