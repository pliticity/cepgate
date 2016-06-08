(function () {

    var dhd = angular.module('dhd');


    dhd.controller('HeaderController', ['$http', '$location', 'principalService', '$scope', function ($http, $location, principalService, $scope) {

        $scope.admin = false;
        $scope.domain = '';

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
        }

        $scope.resolveDomain();
        $scope.resolveIsAdmin();

    }]);

})();