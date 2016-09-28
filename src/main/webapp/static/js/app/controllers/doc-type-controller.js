(function () {

    var document = angular.module('document');

    document.controller('DocTypeController', ['$http', '$scope','$compile','$timeout', function ($http, $scope,$compile,$timeout) {

        var ctrl = this;

        $scope.docType ={};
        $scope.form = {};

        $scope.getTypes = function () {
            $http({url: '/doctype', method: 'get', params: {active: false,domainId:ctrl.domainId}}).then(function (succ) {
                $scope.types = succ.data;
            });
        };

        $scope.remove = function(row){
            if(row.defaultValue==false){
                $http({url: '/doctype/'+row.id, method: 'delete'}).then(function (succ) {
                    $scope.types = succ.data;
                });
            }
        };

        $scope.addDocType = function () {
            var form = $scope.form['docTypeForm'+$scope.docType.id];
            form.$submitted=true;
            if (form.$valid) {
                $("#add-doc-type-"+$scope.docType.id+"-modal").modal('hide');
                $http({url: '/doctype', method: 'post', data: $scope.docType,params:{domainId:ctrl.domainId}}).then(function (succ) {
                    $timeout(function () {
                        $("#setup").html("");
                        var setup = $compile("<ng-include src=\"'/partials/admin/setup-tab.html'\"></ng-include>")($scope);
                        $("#setup").html(setup);
                    },500);
                });
            }
        };

        $scope.toggleDocType = function (row) {
            $http({url: '/doctype/'+row.id, method: 'put',params:{toggle:row.active}}).then(function (succ) {
                $scope.types = succ.data;
            });
        };

    }]);

})();