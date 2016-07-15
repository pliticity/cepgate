(function () {

    var admin = angular.module('dhd');

    admin.controller('DomainController', ['$http', '$scope', '$window', '$resource', '$route', function ($http, $scope, $window, $resource, $route) {

        var Domain = $resource('/domain/:id', {}, {'query': {'url': '/domain', 'isArray': true}});


        $scope.form = {};
        $scope.domain = Domain.get({id: $route.current.params.id});
        $scope.types =[];
        $scope.docType ={};

        $http({url: '/document/types', method: 'get',params:{active:false}}).then(function (succ) {
            $scope.types = succ.data
        });

        $scope.changeActive = function (row) {
            $http({url: '/principal/' + row.id, method: 'post', params: {'active': row.active}}).then(function (succ) {
                $scope.domain.principals = succ.data;
            });
        };

        $scope.changeRole = function (row) {
            $http({url: '/principal/' + row.id, method: 'put', params: {'role': row.role}}).then(function (succ) {
                $scope.domain.principals = succ.data;
            });
        };

        $scope.addDocType = function () {
            if ($scope.form.docTypeForm.$valid) {
                $http({url: '/domain/docType', method: 'post', data: $scope.docType}).then(function (succ) {
                    $scope.types = succ.data;
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