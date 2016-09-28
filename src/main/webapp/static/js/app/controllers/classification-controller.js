(function () {

    var document = angular.module('document');

    document.directive('classificationExists', function ($http, $q) {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$asyncValidators.exists = function (modelValue, viewValue) {
                    var deferred = $q.defer();
                        $http({
                            method: 'get',
                            url: '/classification/exists',
                            params: {id:attrs.classificationExists, clId: modelValue,domainId:attrs.domainid}
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

    document.controller('ClassificationController', ['$http', '$scope','$compile','$timeout', function ($http, $scope,$compile,$timeout) {

        var ctrl = this;

        $scope.classifications = [];
        $scope.classification = {id:'0'};

        $scope.getClassifications = function () {
            $http({
                url: '/classification',
                method: 'get',
                params: {active: false, for: $scope.classification.id,domainId:ctrl.domainId}
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
                $http({url: '/classification', method: 'post', data: $scope.classification,params:{domainId:ctrl.domainId}}).then(function (succ) {
                    $timeout(function () {
                        $("#classifications").html("");
                        var setup = $compile("<ng-include src=\"'/partials/admin/setup/classification.html'\"></ng-include>")($scope);
                        $("#classifications").html(setup);
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
                $timeout(function () {
                    $("#classifications").html("");
                    var setup = $compile("<ng-include src=\"'/partials/admin/setup/classification.html'\"></ng-include>")($scope);
                    $("#classifications").html(setup);
                },0);
            });
        };

        $scope.remove = function(row){
            if(row.defaultValue==false){
                $http({url: '/classification/'+row.id, method: 'delete'}).then(function (succ) {
                    $timeout(function () {
                        $("#classifications").html("");
                        var setup = $compile("<ng-include src=\"'/partials/admin/setup/classification.html'\"></ng-include>")($scope);
                        $("#classifications").html(setup);
                    },0);
                });
            }
        }

    }]);

})();