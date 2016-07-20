(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['common','ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/document/index.html'
            }).when('/domain/:id', {
                templateUrl: 'partials/domain/index.html'
            }).when('/dic/:id', {
                templateUrl: 'partials/document/dic.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();