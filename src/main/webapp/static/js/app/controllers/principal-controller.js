(function () {

    var admin = angular.module('dhd');

    admin.directive('existsValidator', function ($http, $q) {
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
                        if (response.data == true) {
                            deferred.reject('Email is already registered');
                        } else {
                            return deferred.resolve();
                        }

                    }, function errorCallback(response) {
                    });
                    return deferred.promise;
                };
            }
        };
    });

    admin.controller('PrincipalController', ['$http', '$scope', '$window', '$resource', '$route', function ($http, $scope, $window, $resource, $route) {

        $scope.principal = {};
        $scope.form = {};

        $scope.save = function () {
            if ($scope.form.userForm.$valid) {
                $http({url: '/principal', method: 'post', data: $scope.principal, params:{"domainId" : $scope.domain.id}}).then(function (succ) {
                    $scope.domain.principals.push(succ.data);
                });
            }
        };

    }]);

})();