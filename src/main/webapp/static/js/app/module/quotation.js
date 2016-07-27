(function () {
    'use strict';

    var quotation = angular.module('quotation', ['common','ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    quotation.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/quotation/index.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();