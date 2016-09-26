(function () {

    var common = angular.module('common');


    common.controller('HeaderController', ['$http', '$location', 'principalService', '$scope', function ($http, $location, principalService, $scope) {

        var ctrl = this;

        $scope.admin = false;
        $scope.globalAdmin = false;
        $scope.domain = '';

        ctrl.password;

        $scope.navigateToAdmin = function () {
            $location.path('/domain/' + $scope.domain);
        };

        $scope.resolveDomain = function () {
            $http({
                url: '/domain',
                method: 'get',
                params: {domain: ''}
            }).then(function (succ) {
                $scope.domain = succ.data.id;
            });
        };

        $scope.resolveIsAdmin = function () {
            $http({
                url: '/domain',
                method: 'get',
                params: {isAdmin: ''}
            }).then(function (succ) {
                $scope.admin = succ.data;
            });
        };

        $scope.resolveIsGlobalAdmin = function () {
            $http({
                url: '/domain',
                method: 'get',
                params: {isGlobalAdmin: ''}
            }).then(function (succ) {
                $scope.globalAdmin = succ.data;
            });
        };

        ctrl.changePassword = function(){
            $http({url:'/principal/password',method:'post',data:{password:ctrl.password}});
            ctrl.password = null;
        };

        $scope.resolveDomain();
        $scope.resolveIsAdmin();
        $scope.resolveIsGlobalAdmin();

    }]);

})();