'use strict';

/**
 * @ngdoc overview
 * @name dashboardFrontApp
 * @description
 * # dashboardFrontApp
 *
 * Main module of the application.
 */

var dashboardFrontApp = angular.module('dashboardFrontApp', [
  'ngRoute',
  'ngCookies',
  'pascalprecht.translate'
]);

dashboardFrontApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
    when('/login', {
      templateUrl: 'partials/login.html',
      controller: 'LoginController'
    }).
    when('/home', {
      templateUrl: 'partials/home.html',
      controller: 'HomeController'
    }).
    when('/bundle/add', {
      templateUrl: 'partials/bundle/form.html',
      controller: 'BundleAddController'
    }).
    when('/bundle/:bundleTag/media', {
      templateUrl: 'partials/media/list.html',
      controller: 'MediaListController'
    }).
    when('/bundle/edit/:bundleTag', {
      templateUrl: 'partials/bundle/form.html',
      controller: 'BundleEditController'
    }).
    when('/bundle/:bundleTag/media/add', {
      templateUrl: 'partials/media/form.html',
      controller: 'MediaAddController'
    }).
    when('/bundle/:bundleTag/media/edit/:mediaId', {
      templateUrl: 'partials/media/form.html',
      controller: 'MediaEditController'
    }).
    when('/feed/add', {
      templateUrl: 'partials/feed/form_add.html',
      controller: 'FeedAddController'
    }).
    when('/feed/edit/:feedUuid', {
      templateUrl: 'partials/feed/form.html',
      controller: 'FeedEditController'
    }).
    when('/device/edit/:deviceId', {
      templateUrl: 'partials/device/form.html',
      controller: 'DeviceEditController'
    }).
    otherwise({
      redirectTo: '/home'
    });
  }]);

dashboardFrontApp.config(['$translateProvider',
  function ($translateProvider) {
    $translateProvider.translations('en_US', {
      "APP" : "Dashboard",
      "HOME" : "Home",
      "BUNDLE" : {
        "ADD" : "New Bundle",
        "NAME": "Name",
        "INSIDE": "Contains {{count}} bundles"
      },
      "DEVICE":{
        "LAST_KNOWN_IP": "Last Known IP",
        "LAST_HEALTHCHECK": "Last Healthcheck",
        "HAS_FEED": "Feed Bound"
      },
      "FEED": {
        "NAME": "Name",
        "DETAILS": "Details",
        "ADD": "New Feed"
      },
      "MEDIA": {
        "THIS": "Media",
        "NAME": "Name",
        "URL": "URL",
        "TYPE": "Type",
        "CONTENT": "Content",
        "DURATION": "Duration"
      },
      "TYPE" : {
        "THIS": "Content type",
        "IMAGE": "Image",
        "VIDEO": "Vidéo",
        "WEB": "Web",
        "NEWS": "News"
      },
      "USER" : {
        "USERNAME" : "Username",
        "PASSWORD" : "Password"
      },
      "VALIDITY": {
        "START": "Start date time",
        "END": "End date time"
      },
      "ACTIONS" : {
        "ADD" : "Add",
        "EDIT": "Edit",
        "DELETE": "Delete",
        "LOGIN": "Login",
        "SENDING" : "Sending ...",
        "SEND": "Send",
        "CANCEL": "Cancel",
      },
      "ERRORS": {
        "FIELDS": {
          "EMPTY": "The field must not be empty",
          "MINLENGTH": "The value must have at least {{ min }} characters",
          "MAXLENGTH": "The value must have no more than {{ max }} characters",
        }
      },
      "PLACEHOLDERS": {
        "BUNDLE": {
          "NAME": "Enter a name",
        },
        "MEDIA": {
          "NAME": "Enter a name",
          "CONTENT": "Enter a content",
          "URL": "Page web url",
          "TYPE": "Select a type",
          "NEWS": "News text content",
          "DURATION": "Duration (seconds)",
        },
        "VALIDITY": {
          "START": "Enter a start datetime",
          "END": "Enter a end datetime",
        }
      }
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('en_US');

  }]);
