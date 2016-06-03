(function () {

    var dhd = angular.module('dhd');

    dhd.service('documentService', ['$resource', '$filter', '$http', function ($resource, $filter, $http) {

        var Document = $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});

        this.query = function (pars) {
            return Document.query(pars);
        };

        this.new = function () {
            return Document.get({id: 'new'});
        };

        this.delete = function (docId, callback) {
            Document.delete({id: docId}, callback);
        };

        this.copy = function (docId, callback) {
            $http({method: 'post', url: '/document/' + docId + '/copy'}).then(callback);
        };

        this.favourite = function (docId,val) {
            $http({method: 'post', url: '/document/' + docId,params:{favourite:val}});
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