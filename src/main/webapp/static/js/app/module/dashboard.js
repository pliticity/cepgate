(function () {
    'use strict';

    var dashboard = angular.module('dashboard', ['common','document','ngRoute']);

    dashboard.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/dashboard/index.html',
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();