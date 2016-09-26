(function () {

    var document = angular.module('document');

    document.controller('DocTypeController', ['$http', '$scope','$compile','$timeout', function ($http, $scope,$compile,$timeout) {

        $scope.docType ={};
        $scope.form = {};

        $scope.getTypes = function () {
            $http({url: '/document/types', method: 'get', params: {active: false}}).then(function (succ) {
                $scope.types = succ.data;
            });
        }

        $scope.remove = function(row){
            if(row.defaultValue==false){
                $http({url: '/doctype/'+row.id, method: 'delete'}).then(function (succ) {
                    $scope.types = succ.data;
                });
            }
        };

        $scope.addDocType = function () {
            console.log('docTypeForm'+$scope.docType.id);
            var form = $scope.form['docTypeForm'+$scope.docType.id];
            form.$submitted=true;
            if (form.$valid) {
                $("#add-doc-type-"+$scope.docType.id+"-modal").modal('hide');
                $http({url: '/domain/docType', method: 'post', data: $scope.docType}).then(function (succ) {
                    //$scope.types = succ.data;
                    $timeout(function () {
                        $("#setup").html("");
                        var setup = $compile("<ng-include src=\"'/partials/domain/setup-tab.html'\"></ng-include>")($scope);
                        $("#setup").html(setup);
                    },500);
                });
            }
        };

        $scope.toggleDocType = function (row) {
            $http({url: '/domain/docType/'+row.id, method: 'put',params:{toggle:row.active}}).then(function (succ) {
                $scope.types = succ.data;
            });
        };

    }]);

})();