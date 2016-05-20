(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['ngCookies', 'ngResource', 'ngRoute','ngAnimate','ngTagsInput','ngFileUpload']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/document/index.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();