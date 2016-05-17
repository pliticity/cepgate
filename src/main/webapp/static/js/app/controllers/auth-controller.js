(function () {

    var auth = angular.module('auth');

    auth.controller('AuthController', ['$http', '$scope', '$window', function ($http, $scope, $window) {

        $scope.principal = {};

        // attempt to authenticate if the form is valid

        $scope.auth = function () {
            if ($scope.authForm.$valid) {
                $http({
                    method: 'post',
                    url: '/auth/',
                    data: angular.toJson($scope.principal, false)
                }).then(function successCallback(response) {
                    $window.location.href = '/document';
                }, function errorCallback(response) {
                    $scope.message = response.data.message;
                });
                ;
            }
            ;
        };

    }]);

})();