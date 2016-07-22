(function () {

    var dhd = angular.module('dhd');

    dhd.directive('classificationExists', function ($http, $q) {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$asyncValidators.exists = function (modelValue, viewValue) {
                    var deferred = $q.defer();
                        $http({
                            method: 'get',
                            url: '/classification/exists',
                            params: {id:attrs.classificationExists, clId: modelValue}
                        }).then(function successCallback(response) {
                            if (response.data == true) {
                                deferred.reject('Classification exists');
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

    dhd.controller('ClassificationController', ['$http', '$scope','$compile','$timeout', function ($http, $scope,$compile,$timeout) {

        $scope.classifications = [];
        $scope.classification = {id:'0'};

        $scope.getClassifications = function () {
            $http({
                url: '/classification',
                method: 'get',
                params: {active: false, for: $scope.classification.id}
            }).then(function (succ) {
                $scope.classifications = succ.data;
            });
        };

        $scope.mapParents = function (row) {
            if (row.parents != null) {
                return row.parents.map(function (elem) {
                    return elem.classificationId;
                }).join(",");
            }
        };

        $scope.addClassification = function () {
            var form = $scope.form['classificationForm'+$scope.classification.id];
            form.$submitted=true;
            if (form.$valid) {
                //console.log($scope.classification.children);
                //$scope.objectify($scope.classification.children);
                //$scope.objectify($scope.classification.parents);
                $("#add-classification-modal-"+$scope.classification.id).modal('hide');
                $http({url: '/classification', method: 'post', data: $scope.classification}).then(function (succ) {
                    $timeout(function () {
                        $("#setup").html("");
                        var setup = $compile("<ng-include src=\"'/partials/domain/setup-tab.html'\"></ng-include>")($scope);
                        $("#setup").html(setup);
                    },500);
                    //$scope.classifications = succ.data;
                });
            }
        };

        $scope.objectify = function (array) {
            if (array != undefined) {
                for (var i = 0; i < array.length; i++) {
                    array[i] = JSON.parse(array[i]);
                }
            }
        };

        $scope.toggleClassification = function (row) {
            $http({url: '/classification/'+row.id, method: 'put',params:{toggle:row.active}}).then(function (succ) {
                $scope.classifications = succ.data;
            });
        };

        $scope.remove = function(row){
            if(row.defaultValue==false){
                $http({url: '/classification/'+row.id, method: 'delete'}).then(function (succ) {
                    $scope.classifications = succ.data;
                });
            }
        }

    }]);

})();