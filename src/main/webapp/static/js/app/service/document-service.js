(function () {

    var dhd = angular.module('dhd');

    dhd.service('documentService', ['$resource', '$filter', '$http', function ($resource, $filter, $http) {

        var thisService = this;

        thisService.parseResponseDates = function(response) {
            var data = response.data, key, value;
            for (key in data) {
                if (!data.hasOwnProperty(key) && // don't parse prototype or non-string props
                    toString.call(data[key]) !== '[object String]') continue;
                value = Date.parse(data[key].creationDate); // try to parse to date
                if (value !== NaN) response.resource[key].creationDate = value;
            }
            return response;
        }

        var Document = $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true,interceptor: {response: thisService.parseResponseDates}}});

        this.query = function (pars) {
            return Document.query(pars);
        };

        this.new = function () {
            return Document.get({id: 'new'});
        };

        this.delete = function (docId, callback) {
            $http({
                url: '/document', method: 'delete', data: docId, headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(callback);
            //Document.delete({url:'/document',method:'delete',data:}, callback);
        };

        this.copy = function (docId, callback) {
            $http({method: 'post', url: '/document/' + docId + '/copy'}).then(callback);
        };

        this.favourite = function (docId, val) {
            $http({method: 'post', url: '/document/' + docId, params: {favourite: val}});
        };

        this.canDelete = function (row) {
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