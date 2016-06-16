(function () {

    var dhdModule = angular.module('dhd');

    dhdModule.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    dhdModule.controller('DocumentController', ['fileService', 'documentService', 'Upload', 'Document', '$http', '$scope', '$location', '$window', function (fileService, documentService, Upload, Document, $http, $scope, $location, $window) {

        // DOCUMENT

        $scope.documentInfo = {};
        $scope.form = {};
        $scope.itemsPerPage = 10;
        $scope.qParams = {};
        $scope.users = {};
        $scope.tableId = 'search';
        $scope.revision = false;


        $scope.canDownload = function () {
            var flag = $("table#" + $scope.tableId + " tr.st-selected td[data='files'] select option:selected").length > 0;
            0
            return flag;
        };
        $scope.canCopy = function () {
            return $scope.anySelected();
        };
        $scope.canDelete = function () {
            if ($scope.anySelected()) {
                var allAreEmpty = true;
                $("table#" + $scope.tableId + " tr.st-selected td[data='noOfFiles']").each(function (i, e) {
                    if (Number($(e).html()) > 0) {
                        allAreEmpty = false;
                    }
                });
                return allAreEmpty;
            } else {
                return false;
            }
        };

        $scope.fetchRevision = function (rev) {
            $http({
                url: '/document/' + $scope.documentInfo.id + '/revision/' + rev,
                method: 'get'
            }).then(function (succ) {
                $scope.revision = true;
                $scope.documentInfo = succ.data;
                $('#details-'+$scope.documentInfo.id+'-tabs a:first').tab('show');
            });
        };

        $scope.anySelected = function () {
            return $("table#" + $scope.tableId + " tr.st-selected").length > 0;
        };

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

        $scope.createRevision = function () {
            $http({url: '/document/' + $scope.documentInfo.id + '/revision', method: 'post'}).then(function (succ) {
                $scope.documentInfo.revisions = succ.data;
                $scope.documentInfo.revision++;
            });
        };

        $scope.principals = function () {
            $http({url: '/principal', method: 'get'}).then(function (succ) {
                $scope.users = succ.data;
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

        $scope.doForSelectedRows = function (func) {
            var ids = [];
            $("table#" + $scope.tableId + " tr.st-selected").each(function (i, e) {
                ids.push($(e).attr("docId"));
            });
            console.log(ids);
            func(ids);
        };

        $scope.copyMulti = function () {
            $scope.doForSelectedRows(function (e) {
                var docDto = [];
                e.forEach(function(i){
                    var fArray = fileService.selectedFiles(i);
                    docDto.push({"id":i,"files":fArray});
                });
                $http({url: '/document/copy', method: 'post', data: docDto}).then(function (s) {
                    s = s.data;
                    for (var i = 0; i < s.length; i++) {
                        $scope.documents.push(s[i]);
                    }
                });
            });
        };

        $scope.deleteMulti = function () {
            $scope.doForSelectedRows(function (e) {
                $scope.delete(e);
            });
        };

        $scope.downloadMulti = function () {
            $scope.doForSelectedRows(function (e) {
                $scope.download(e);
            });
        };

        $scope.copy = function (docId) {
            documentService.copy(docId, function (res) {
                $scope.documents.push(res);
            });
        };

        $scope.get = function (documentId) {
            Document.get({id: documentId}, function (res) {
                $scope.documentInfo = res;
                $scope.revision=false;
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
            $("span[id='tab-name-" + $scope.documentInfo.id + "']").html($scope.documentInfo.documentName);
        };

        // FILES

        $scope.changeFileName = function (file) {
            $http({url: '/files/' + file.id, data: file, method: 'post'});
        };

        $scope.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
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