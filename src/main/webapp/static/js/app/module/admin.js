(function () {
    'use strict';

    var admin = angular.module('admin', ['common','document','auth','ngResource', 'ngRoute', 'smart-table', 'ngLoadingSpinner']);

    admin.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/:id', {
                templateUrl: 'partials/admin/index.html'
            });
        }]);

})();