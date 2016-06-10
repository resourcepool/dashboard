'use strict'

/**
 * Created by excilys on 09/06/16.
 */

angular
  .module("dashboardFrontApp")
  .service('firewallService', ['$cookieStore', '$http', '$location', function ($cookieStore, $http, $location ) {

    this.isAuthenticated = isAuthenticated;

    function isAuthenticated() {
      if ($cookieStore.get('globals')) {
        var authdata = $cookieStore.get('globals').currentUser.authdata;
        $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
      }
      else {
        $location.path('/login');
      }
    }
  }]);


