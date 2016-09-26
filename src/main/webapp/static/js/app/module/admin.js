(function () {
    'use strict';

    var admin = angular.module('admin', ['common','document','auth','ngResource', 'ngRoute', 'smart-table', 'ngLoadingSpinner']);

    admin.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/admin/domains.html',
                controller: 'AdminController'
            }).when('/domain/:id', {
                templateUrl: 'partials/admin/domain.html',
                controller: 'AdminDomainController'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();