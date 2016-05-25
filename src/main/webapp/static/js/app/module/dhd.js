(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['ngCookies', 'ngResource', 'ngRoute','ngAnimate','ngTagsInput','ngFileUpload','smart-table','ngLoadingSpinner']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/document/index.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();