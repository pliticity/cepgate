(function () {

    var dhd = angular.module('dhd');

    var principalService = dhd.service('principalService', ['$http', '$route', function ($http, $route) {

        this.resolveDomain = function () {
            return $http({
                url: '/domain',
                method: 'get',
                params: {domain: ''}
            }).then(function (succ) {
                return succ.data.id;
            });
        };

        this.resolveIsAdmin = function () {
            return $http({
                url: '/domain',
                method: 'get',
                params: {isAdmin: ''}
            }).then(function (succ) {
                return succ.data;
            });
        };

    }]);

})();