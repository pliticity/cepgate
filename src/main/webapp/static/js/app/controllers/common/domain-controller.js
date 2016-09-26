(function () {

    var common = angular.module('common');

    common.controller('DomainController', ['$http', '$scope', '$resource', '$route','domainService', function ($http, $scope, $resource, $route,domainService) {

        var ctrl = this;

        $scope.form = {};
        $scope.domain = Domain.get({id: $route.current.params.id});


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

    }]);

})();