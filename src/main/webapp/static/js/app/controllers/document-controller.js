(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/all', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['Document', '$http', '$scope', function (Document, $http, $scope) {
        $scope.documents = Document.query();
    }]);

})();