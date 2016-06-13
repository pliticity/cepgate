(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/document/index.html'
            }).when('/domain/:id', {
                templateUrl: 'partials/domain/index.html',
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();