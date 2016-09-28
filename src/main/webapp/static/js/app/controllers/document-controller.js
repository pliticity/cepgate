(function () {

    var document = angular.module('document');

    document.factory('Document', ['$resource','linkService',
        function ($resource,linkService) {
            return $resource('/document/:id', {}, {'get':{'url':'/document/:id',interceptor: {response: linkService.fetchObjects}},'query': {'url': '/document/query', 'isArray': true}});
        }]);

    document.controller('DocumentController', ['$timeout','settingsService','fileService', 'documentService', 'Upload', 'Document', '$http', '$scope', '$location', '$window','$route',"$compile", function ($timeout,settingsService,fileService, documentService, Upload, Document, $http, $scope, $location, $window,$route,$compile) {

        // DOCUMENT

        $scope.documentInfo = {};
        $scope.form = {};
        $scope.itemsPerPage = 10;
        $scope.qParams = {};
        $scope.users = {};
        $scope.tableId = 'search';
        $scope.revision = false;
        $scope.types = [];
        $scope.classifications = [];
        $scope.mail = {};
        $scope.fNames = {};

        $scope.settings = settingsService.settings;

        $scope.mailPopupOpened = function (tableId) {
            var files = [];
            $scope.doForSelectedRows(function (rows) {
                for (var i = 0; i < rows.length; i++) {
                    var fls = fileService.selectedFiles(rows[i],tableId);
                    console.log(fls);
                    for (var j = 0; j < fls.length; j++) {
                        files.push((fls[j]));
                    }
                }
            });
            fileService.fileNames($scope.fNames,$scope.tableId,files);
        };

        $scope.openDic = function () {
            if ($route.current.params.id) {
                Document.get({id: $route.current.params.id}, function (res) {
                    var id = res.id;
                    var name = res.documentNumber;
                    $scope.openDicTab(id, name);
                });
            }
        };

        $scope.openDicTab = function (id, name) {
            var link = "<li id='tab-" + id + "' role='presentation'><a close-tab=" + id + " href='#" + id + "' aria-controls='" + id + "' role='tab'data-toggle='tab'><span id='tab-name-" + id + "'>" + name + "</span></li>";
            var tab = "<div role='tabpanel' class='tab-pane' id='" + id + "' ng-controller='DocumentController'> <div ng-init=\"get('" + id + "')\"></div> <ng-include src=\"'/partials/document/details.html'\"></ng-include></div>";

            $("#document-tabs").append($compile(link)($scope));
            $("#document-tabs-content").append($compile(tab)($scope));

            $("#tab-" + id + " a:last").tab('show');
        };

        $scope.canDownload = function () {
            var flag = $("table#" + $scope.tableId + " tr.st-selected td[data='files'] select option:selected").length > 0;
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
                if(allAreEmpty){
                    var ids = [];
                    $("table#" + $scope.tableId + " tr.st-selected").each(function (i, e) {
                        ids.push($(e).attr("docId"));
                    });

                    var c = true;

                    for(var i=0; i < $scope.documents.length ;i++){
                        var e = $scope.documents[i];
                        if(ids.containsObject(e.id)){
                            if(e.classification.modelId!=null){
                                c = false;
                            }
                        }
                    }

                    return c;
                }else{
                    return false;
                }
            } else {
                return false;
            }
        };

        $scope.anySelected = function () {
            return $("table#" + $scope.tableId + " tr.st-selected").length > 0;
        };

        $scope.query = function () {
            $scope.documents = documentService.getAll($scope.qParams);
        };

        $scope.principals = function () {
            $http({url: '/principal', method: 'get'}).then(function (succ) {
                $scope.users = succ.data;
            });
        };

        $scope.doForSelectedRows = function (func) {
            var ids = [];
            $("table#" + $scope.tableId + " tr.st-selected").each(function (i, e) {
                ids.push($(e).attr("docId"));
            });
            func(ids);
        };

        $scope.copyMulti = function () {
            $scope.doForSelectedRows(function (e) {
                var docDto = [];
                e.forEach(function(i){
                    var fArray = fileService.selectedFiles(i,$scope.tableId);
                    docDto.push({"id":i,"files":fArray});
                });
                $http({url: '/document/copy', method: 'post', data: docDto}).then(function (s) {
                    s = s.data;
                    for (var i = 0; i < s.length; i++) {
                        $scope.documents.push(s[i]);
                        $scope.openDicTab(s[i].id,s[i].documentNumber);
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

        // FILES

        $scope.download = function (filesId) {
            fileService.downloadFiles(filesId,$scope.tableId);
        };

        $scope.attachFiles = false;

        $scope.sendMail = function (zip,transmittal) {
            $scope.doForSelectedRows(function (e) {
                fileService.mailFiles(e,!zip,$scope.mail,$scope.tableId,transmittal);
            });
        };

        $scope.canSendMail = function () {
            var flag = $("table#" + $scope.tableId + " tr.st-selected td[data='files'] select option:selected").length > 0;
            return flag;
        };
    }]);

})();