(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['ngCookies', 'ngResource', 'ngRoute','ngAnimate']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/documents', {
                templateUrl: 'partials/document/list.html',
                controller: 'DocumentController'
            }).when('/document/:docId', {
                templateUrl: 'partials/document/details.html',
                controller: 'DocumentController'
            }).otherwise({
                redirectTo: '/documents'
            });
        }]);

})();