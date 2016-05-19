(function () {
    'use strict';

    var dhdModule = angular.module('dhd', ['ngCookies', 'ngResource', 'ngRoute','ngAnimate','ngTagsInput']);

    dhdModule.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/document/index.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();