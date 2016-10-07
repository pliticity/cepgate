(function () {

    var document = angular.module('document');

    document.factory('Document', ['$resource','linkService',
        function ($resource,linkService) {
            var parseDates = function(response){
                var data = response.data, key, value;
                for (key in data) {
                    if (!data.hasOwnProperty(key) && // don't parse prototype or non-string props
                        toString.call(data[key]) !== '[object String]') continue;
                    value = Date.parse(data[key].creationDate); // try to parse to date
                    if (value !== NaN) response.resource[key].creationDate = value;
                }
                return response;
            };

            return $resource('/document/:id', {}, {'get':{'url':'/document/:id',interceptor: {response: linkService.fetchObjects}},'query': {'url': '/document/query', 'isArray': true,interceptor: {response: parseDates}},'save':{'url':'/document',method:'post'},'delete' : {'url' : '/document',method:'delete'}});
        }]);

    document.service('documentService', ['$http','Document', function ($http,Document) {

        var service = this;

        CgService(service,Document);

        service.archive = function (dic,callback) {
            $http({
                method: 'put',
                url: '/document/' + dic.id,
                data: dic
            }).then(function (response) {
                $http({
                    url: '/document/' + dic.id + '/state/ARCHIVED',
                    method: 'put'
                }).then(callback);
            });
        };

        service.createRevision = function (dic,callback) {
            $http({url: '/document/' + dic.id + '/revision', method: 'post'}).then(callback);
        };

        service.fetchRevision = function (dic,rev,callback) {
            $http({
                url: '/document/' + dic.id + '/revision/' + rev.effective,
                method: 'get'
            }).then(callback);
        };

        service.getDocTypes = function(active,callback){
            $http({url: '/doctype', method: 'get',params:{active:active}}).then(callback);
        };

        service.copy = function (docId, callback) {
            $http({method: 'post', url: '/document/' + docId + '/copy'}).then(callback);
        };

        service.favourite = function (docId, val) {
            $http({method: 'post', url: '/document/' + docId, params: {favourite: val}});
        };

        service.canDelete = function (row) {
            if (row.noOfFiles < 1) {
                var tab = $("#tab-" + row.id);
                if (tab.length < 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        };

    }]);

})();