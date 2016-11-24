(function () {
    'use strict';

    var product = angular.module('product', ['common','ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    product.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/product/index.html'
            }).when('/pic/:id', {
                templateUrl: 'partials/product/pic.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();