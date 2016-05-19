(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['Document', '$http', '$scope', function (Document, $http, $scope) {

        $scope.documentInfo = {};

        $scope.query = function (pars) {
            return Document.query(pars);
        };

        $scope.new = function () {
            $http({method: 'get', url: '/document/new'}).then(function (success) {
                $scope.documentInfo = success.data;
            });
        };

        $scope.get = function(documentId){
            $scope.documentInfo = Document.get({id:documentId});
        }

    }]);

})();