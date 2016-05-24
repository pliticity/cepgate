(function () {

    var dhd = angular.module('dhd');

    dhd.service('documentService', ['$resource', '$filter', '$http', function ($resource, $filter, $http) {

        var Document = $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});

        this.query = function (pars) {
            return Document.query(pars);
        };

        this.new = function () {
            return Document.get({id:'new'});
        };

    }]);

})();