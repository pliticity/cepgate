(function () {

    var document = angular.module('document');

    document.directive('existsValidator', function ($http, $q) {
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

    document.directive('acronymValidator', function ($http, $q) {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$asyncValidators.acronym = function (modelValue, viewValue) {
                    var deferred = $q.defer();
                    $http({
                        method: 'get',
                        url: '/principal/acronym',
                        params: {id:attrs.acronymValidator, q: modelValue}
                    }).then(function successCallback(response) {
                        if (response.data == true) {
                            deferred.reject('Acronym already exists.');
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

    document.controller('PrincipalController', ['$http', '$scope', '$window', '$resource', '$route','$timeout', function ($http, $scope, $window, $resource, $route,$timeout) {

        $scope.principal = {};
        $scope.form = {};

        $scope.save = function () {
            if ($scope.form.userForm.$valid) {
                $http({url: '/principal', method: 'post', data: $scope.principal, params:{"domainId" : $scope.domain.id}}).then(function (succ) {
                    $scope.domain.principals.push(succ.data);
                });
            }
        };

        $scope.edit = function () {
            var form = $scope.form['userForm' + $scope.principal.id];
            form.$submitted=true;
            if (form.$valid) {
                $("#edit-user-modal-" + $scope.principal.id).modal('hide');
                $timeout(function () {
                    $http({
                        url: '/principal',
                        method: 'put',
                        data: $scope.principal
                    }).then(function (succ) {
                        $scope.domain.principals = succ.data;
                    });
                }, 500);
            }
        };

        $scope.setPrincipal = function(pr){
            $scope.principal =pr;
        }

    }]);

})();