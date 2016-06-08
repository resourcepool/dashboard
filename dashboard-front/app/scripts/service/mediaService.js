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

    /*
    this.update = update;
    this.create = create;
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
          return formatDateTime(success.data);
        });
    }

    function removeById(id) {
      return $http.delete(API.BASE_URL + "medias/" + id)
        .then(function(success){
          return success;
        })
    }


    function formatDateTime(medias) {
      for (var media of medias) {
        media.validity.start = moment(media.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        media.validity.end = moment(media.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
      }
      return medias;
    }

  }]);


