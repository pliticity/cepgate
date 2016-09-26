(function () {
    'use strict';

    var gadmin = angular.module('gadmin', ['common','document','auth','ngResource', 'ngRoute', 'smart-table', 'ngLoadingSpinner']);

    gadmin.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/gadmin/domains.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();