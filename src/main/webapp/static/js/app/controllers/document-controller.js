(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['fileService', 'documentService', 'Upload', 'Document', '$http', '$scope', '$location','$window', function (fileService, documentService, Upload, Document, $http, $scope, $location,$window) {

        // DOCUMENT

        $scope.documentInfo = {};
        $scope.form = {};
        $scope.itemsPerPage = 10;
        $scope.qParams = {};
        $scope.users={};

        $scope.query = function () {
            $scope.documents = documentService.query($scope.qParams);
        };

        $scope.new = function () {
            $scope.documentInfo = documentService.new();
        };

        $scope.delete = function (docId) {
            documentService.delete(docId, function (res) {
                $scope.query();
            });
        };

        $scope.principals = function () {
            $http({url: '/principal', method: 'get'}).then(function (succ) {
                $scope.users=succ.data;
            });
        };

        $scope.favourite = function () {
            documentService.favourite($scope.documentInfo.id, true);
            $scope.documentInfo.favourite = true;
        };

        $scope.unFavourite = function () {
            documentService.favourite($scope.documentInfo.id, false);
            $scope.documentInfo.favourite = false;
        };

        $scope.copy = function (docId) {
            documentService.copy(docId, function (res) {
                $scope.query();
            });
        };

        $scope.get = function (documentId) {
            Document.get({id: documentId}, function (res) {
                $scope.documentInfo = res;
                //$scope.documentInfo.creationDate = new Date($scope.documentInfo.creationDate);
                //$scope.documentInfo.plannedIssueDate = new Date($scope.documentInfo.plannedIssueDate);
            });
        };

        $scope.create = function () {
            $scope.form.documentForm.$submitted = true;
            if ($scope.form.documentForm.$valid) {
                Document.save({id: $scope.documentInfo.id}, $scope.documentInfo, function (response) {
                    var docId = response.id;
                    $scope.new();
                    var files = $scope.files;
                    $scope.files = [];
                    $scope.uploadFiles(files, docId);
                    $scope.form.documentForm.$submitted = false;
                });
            }
        };

        $scope.save = function () {
            $scope.form.documentForm.$submitted = true;
            if ($scope.form.documentForm.$valid) {
                $http({method: 'put', url: '/document/' + $scope.documentInfo.id, data: $scope.documentInfo});
            }
        };

        // FILES

        $scope.openFile = function(symbol){
            $window.open("/file/"+symbol, '_blank');
        };

        $scope.download = function (filesId) {
            fileService.downloadFiles(filesId);
        };

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