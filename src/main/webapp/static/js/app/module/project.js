(function () {
    'use strict';

    var project = angular.module('project', ['common','ngCookies', 'ngResource', 'ngRoute', 'ngAnimate', 'ngTagsInput', 'ngFileUpload', 'smart-table', 'ngLoadingSpinner', 'ngMessages','ngMaterial']);

    project.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/project/index.html'
            }).otherwise({
                redirectTo: '/'
            });
        }]);

})();