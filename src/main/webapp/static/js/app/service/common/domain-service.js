(function () {

    var common = angular.module('common');

    common.factory('Domain', ['$resource',
        function ($resource) {
            return $resource('/domain/:id', {}, {'query': {'url': '/domain/query', 'isArray': true},'save':{'url':'/domain',method:'post'}});
        }]);

    common.service('domainService', ['$http','Domain', function ($http,Domain) {

        CgService(this,Domain);

        this.toggleActive = function (domain,callback) {
            $http({url: '/domain/' + domain.id, method: 'put', params: {active: domain.active}}).then(callback);
        };

        this.changeSU = function(domain,callback){
            $http({url: '/domain/' + domain.id, method: 'post', params: {newOwner: domain.owner.id}}).then(callback);
        }

    }]);

})();