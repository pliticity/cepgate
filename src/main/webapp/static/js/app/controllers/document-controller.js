(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['Upload', 'Document', '$http', '$scope', '$location', function (Upload, Document, $http, $scope, $location) {

        // DOCUMENT

        $scope.documentInfo = {};
        $scope.form = {};

        $scope.query = function (pars) {
            return Document.query(pars);
        };

        $scope.new = function () {
            $http({method: 'get', url: '/document/new'}).then(function (success) {
                $scope.documentInfo = success.data;
                $scope.documentInfo.creationDate = new Date($scope.documentInfo.creationDate);
            });
        };

        $scope.get = function (documentId) {
            Document.get({id: documentId}, function (res) {
                $scope.documentInfo = res;
                $scope.documentInfo.creationDate = new Date($scope.documentInfo.creationDate);
                $scope.documentInfo.plannedIssueDate = new Date($scope.documentInfo.plannedIssueDate);
            });
        };

        $scope.create = function () {
            if ($scope.form.documentForm.$valid) {
                Document.save({id: $scope.documentInfo.id}, $scope.documentInfo, function (response) {
                    var docId = response.id;
                    $scope.uploadFiles($scope.files, docId);
                });
            }
        };

        $scope.save = function () {
            if ($scope.form.documentForm.$valid) {
                $http({method: 'put', url: '/document/' + $scope.documentInfo.id, data: $scope.documentInfo});
            }
        };

        // FILES

        $scope.selectNewFiles = function (files) {
            $scope.files = files;
        };

        $scope.deleteNewFile = function (index) {
            $scope.files.splice(index, 1);
        };

        $scope.deleteFile = function (documentId, fId) {
            $http({
                method: 'delete',
                url: '/document/' + documentId + '/delete/' + fId
            }).then(function (response) {
                $scope.documentInfo.files = response.data;
            });
        };

        $scope.uploadFiles = function (files, docId) {
            if (files && files.length) {
                $scope.uploadFile(files, 0, docId);
            }
        };

        $scope.uploadFile = function (files, index, docId) {
            Upload.upload({
                url: '/document/' + docId + '/upload',
                data: {file: files[index]}
            }).then(function (resp) {
                if ((index + 1) == files.length) {
                    $scope.documentInfo.files = resp.data;
                } else {
                    $scope.uploadFile(files, index + 1, docId);
                }
            });
        }
    }]);

})();