(function () {

    var common = angular.module('common');

    common.factory('Principal', ['$resource',
        function ($resource) {
            return $resource('/product/:id', {}, {'query': {url: '/principal', method: 'get', 'isArray': true}});
        }]);

    common.service('principalService', ['$http', '$route','Principal', function ($http, $route,Principal) {

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

        this.getPrincipalsInDomain = function(){
            return Principal.query();
        }

    }]);

})();