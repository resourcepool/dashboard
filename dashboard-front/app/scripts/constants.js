/**
 * Created by excilys on 06/06/16.
 */

angular
  .module('dashboardFrontApp')
  .constant("API", {
    "BASE_URL": "http://localhost:8080",
  })
  .constant("DATE", {
    "SERVER_FORMAT": "YYYY-MM-DD[T]HH:mm:ss",
    "DISPLAY_FORMAT": "MMMM Do YYYY, h:mm a",
    "REGEXP": "^2[0-9]{3}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$"
  })
  .constant("MSG", {
    "ERR" : {
      "GET_BUNDLE" : "Cannot get bundle",
      "GET_BUNDLES" : "Cannot get bundle list",
      "GET_MEDIA": "Cannot get media",
      "GET_MEDIAS": "Cannot get metdas",
      "DELETE_BUNDLE": "Cannot delete bundle",
      "DELETE_MEDIA": "Cannot delete media",
      "DELETE_FEED": "Cannot delete feed",
      "UPLOAD_MEDIA": "Error when trying to upload media",
      "ADD_BUNDLE": "Cannot create bundle",
      "ADD_MEDIA" : "Cannot create media",
      "SERVER": "Internal server error occured. Please try again later.",
      "FEED_MUST_HAVE_BUNDLES": "Your Feed must have at least one bundle."
    },
    "SUCC" : {
      "ADD_BUNDLE" : "Bundle created with success",
      "ADD_MEDIA" : "Media created with success",
      "UPDATE_BUNDLE" : "Bunde has been updated",
      "UPDATE_MEDIA": "Media created with success",
    },
    "CONF": {
      "DELETE_BUNDLE" : "Are you sure to delete selected bundle ?",
      "DELETE_MEDIA" : "Are you sure to delete selected media ?",
      "DELETE_FEED" : "Are you sure to delete selected feed ?"
    }
  });
