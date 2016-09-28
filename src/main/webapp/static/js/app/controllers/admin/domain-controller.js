(function () {

    var admin = angular.module('admin');

    admin.controller('DomainController', ['$http', '$scope', '$resource', '$route', 'domainService','principalService', function ($http, $scope, $resource, $route, domainService,principalService) {

        var ctrl = this;

        $scope.form = {};
        $scope.domain = domainService.getOne($route.current.params.id);
        ctrl.domainId = $route.current.params.id;
        ctrl.countries = [];

        CgPrincipalController(ctrl,principalService,$route.current.params.id);

        $http({
            method: 'get',
            url: 'https://restcountries.eu/rest/v1/all'
        }).then(function successCallback(response) {
            ctrl.countries = response.data;
        }, function errorCallback(response) {
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

        ctrl.changeSU = function () {
            domainService.changeSU($scope.domain, function(resp){
                $scope.domain.owner = resp.data;
            });
        };

        ctrl.save = function () {
            var form = {};
            form.$valid = true;
            domainService.save(form, $scope.domain, function () {
            });
        };

    }]);

})();