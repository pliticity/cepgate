(function () {
    'use strict';

    var document = angular.module('document', ['common','ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    document.config(['$routeProvider',
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