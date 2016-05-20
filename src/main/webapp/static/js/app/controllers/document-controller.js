(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['Upload', 'Document', '$http', '$scope', '$location', function (Upload, Document, $http, $scope, $location) {

        $scope.documentInfo = {};

        $scope.query = function (pars) {
            return Document.query(pars);
        };

        $scope.new = function () {
            $http({method: 'get', url: '/document/new'}).then(function (success) {
                $scope.documentInfo = success.data;
            });
        };

        $scope.get = function (documentId) {
            $scope.documentInfo = Document.get({id: documentId});
        };

        $scope.uploadFiles = function (files) {
            if (files && files.length) {
                console.log(files[0].name);
                for (var i = 0; i < files.length; i++) {
                    Upload.upload({url: '/document/upload', data: {file: files[i]}});
                }
            }
        };

    }]);

})();