(function () {

    var auth = angular.module('auth');

    auth.directive('existsValidator', function ($http, $q) {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$asyncValidators.exists = function (modelValue, viewValue) {
                    var deferred = $q.defer();
                    $http({
                        method: 'get',
                        url: '/auth/exists',
                        params: {email: modelValue}
                    }).then(function successCallback(response) {
                        if (response.data==true) {
                            deferred.reject('Email is already registered');
                        }else{
                            return deferred.resolve();
                        }

                    }, function errorCallback(response) {
                    });
                    return deferred.promise;
                };
            }
        };
    });

    auth.controller('SignUpController', ['$http', '$scope', '$window', function ($http, $scope, $window) {

        $scope.countries = {};
        $scope.principal = {};

        // fetch countries list from the external serivce

        $http({
            method: 'get',
            url: 'https://restcountries.eu/rest/v1/all'
        }).then(function successCallback(response) {
            $scope.countries = response.data;
        }, function errorCallback(response) {
        });

        // attempt to create a new account

        $scope.signUp = function (redirect) {
            if ($scope.signupForm.$valid) {
                $http({
                    method: 'post',
                    url: '/auth/signup',
                    data: angular.toJson($scope.principal, false),
                    params: {"signin":true}
                }).then(function successCallback(response) {
                    $window.location.href = '/document';
                }, function errorCallback(response) {
                });
            }
            ;
        };

        $scope.addDomain = function (redirect) {
            if ($scope.signupForm.$valid) {
                $http({
                    method: 'post',
                    url: '/auth/signup',
                    data: angular.toJson($scope.principal, false),
                    params: {"signin":false}
                }).then(function successCallback(succ) {
                    $scope.domains.push(succ.data.domain);
                }, function errorCallback(response) {
                });
            }
            ;
        };

    }]);

})();