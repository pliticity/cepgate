(function () {
    'use strict';

    var auth = angular.module('auth', ['ngResource', 'ngRoute','ngMessages']);

    auth.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/auth/sign-in.html',
                controller: 'AuthController'
            }).when('/sign-up', {
                templateUrl: 'partials/auth/sign-up.html',
                controller: 'SignUpController'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();