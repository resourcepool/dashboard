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
    this.formatDateTime = formatDateTime;


    function getById(id) {
      return $http.get(API.BASE_URL + "bundle/" + id, {
      }).then(function(success) {
        return success.data;
      });
    }

    function save(bundle) {
      return $http.post(API.BASE_URL + "bundle", bundle, {
      })
        .then(function(success) {
        return success.data;
      });
    }

    function removeById(id) {
      return $http.delete(API.BASE_URL + "bundle/" + id, {
      })
        .then(function(success) {
          return success;
        })
    }

    function getAll() {
      return $http.get(API.BASE_URL + "bundle", {
      })
      .then(function(success) {
          return formatDateTime(success.data);
      });
    }

    function formatDateTime(bundles) {
      for (var bundle of bundles) {
        bundle.validity.start = moment(bundle.validity.start, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
        bundle.validity.end = moment(bundle.validity.end, DATE.SERVER_FORMAT).format(DATE.DISPLAY_FORMAT);
      }
      return bundles;
    }
  }]);


