(function () {

    var document = angular.module('document');

    document.factory('Document', ['$resource',
        function ($resource) {
            return $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true}});
        }]);

    document.controller('DocumentController', ['$timeout','settingsService','authorizationService','fileService', 'documentService', 'Upload', 'Document', '$http', '$scope', '$location', '$window','$route',"$compile", function ($timeout,settingsService,authorizationService,fileService, documentService, Upload, Document, $http, $scope, $location, $window,$route,$compile) {

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

            $("#documentTabs").append($compile(link)($scope));
            $("#documentsTabContent").append($compile(tab)($scope));

            $("#tab-" + id + " a:last").tab('show');
        };

        // EXPOSE SERVICE

        $http({url: '/doctype', method: 'get',params:{active:true}}).then(function (succ) {
            $scope.types = succ.data
        });

        $http({url: '/classification', method: 'get',params:{active:true,for:'0'}}).then(function (succ) {
            $scope.classifications = succ.data;
        });

        $scope.typeSelected = function(){
            var selected = {};
            for(var i =0; i<$scope.types.length; i++){
                var type = $scope.types[i];
                if(type.id == $scope.documentInfo.docType.id){
                    selected = type;
                    break;
                }
            }
            $scope.documentInfo.docType.typeId = selected.typeId;
            $scope.documentInfo.docType.name = selected.name;
        };

        $scope.classificationSelected = function(){
            var selected = {};
            for(var i =0; i<$scope.classifications.length; i++){
                var type = $scope.classifications[i];
                if(type.id == $scope.documentInfo.classification.id){
                    selected = type;
                    break;
                }
            }
            $scope.documentInfo.classification.classificationId = selected.classificationId;
            $scope.documentInfo.classification.name = selected.name;
        };

        $scope.auth = authorizationService;

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

        $scope.fetchRevision = function (rev) {
            $http({
                url: '/document/' + $scope.documentInfo.id + '/revision/' + rev.effective,
                method: 'get'
            }).then(function (succ) {
                $scope.revision = true;
                $scope.documentInfo = succ.data;
                $('#details-'+$scope.documentInfo.id+'-tabs a:first').tab('show');
            });
        };

        $scope.archive = function () {
            $http({
                method: 'put',
                url: '/document/' + $scope.documentInfo.id,
                data: $scope.documentInfo
            }).then(function (succ) {
                $http({
                    url: '/document/' + $scope.documentInfo.id + '/state/ARCHIVED',
                    method: 'put'
                }).then(function (succ) {
                    $scope.documentInfo.state = succ.data.state;
                    $scope.documentInfo.archivedDate = succ.data.archivedDate;
                });
            });
        };

        $scope.readonly = function(){
          return $scope.revision==true || $scope.documentInfo.state == 'ARCHIVED';
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
                $scope.documentInfo.revision.number++;
                $scope.documentInfo.state = 'IN_PROGRESS';
                $scope.documentInfo.archivedDate = null;
                $scope.documentInfo.revision.prefix = $scope.documentInfo.revision.number.toString();
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

        $scope.get = function (documentId) {
            $scope.documentInfo = Document.get({id: documentId}, function (res) {
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

                    var id = docId
                    var name = response.documentName;

                    var e = $("#tab-"+id);
                    if(e.length>0){
                        $("#tab-"+id+" a:last").tab('show');
                        return;
                    }

                    var link = "<li id='tab-"+id+"' role='presentation'><a close-tab="+id+" href='#" + id + "' aria-controls='" + id + "' role='tab'data-toggle='tab'><span id='tab-name-"+id+"'>" + name + "</span></li>";
                    var tab = "<div role='tabpanel' class='tab-pane' id='" + id + "' ng-controller='DocumentController'> <div ng-init=\"get('" + id + "')\"></div> <ng-include src=\"'/partials/document/details.html'\"></ng-include></div>";

                    $("#documentTabs").append($compile(link)($scope));
                    $("#documentsTabContent").append($compile(tab)($scope));

                    $("#tab-"+id+" a:last").tab('show');
                });
            }
        };

        $scope.save = function () {
            $scope.form.documentForm.$submitted = true;
            if ($scope.form.documentForm.$valid) {
                $http({method: 'put', url: '/document/' + $scope.documentInfo.id, data: $scope.documentInfo});
            }
            $("span[id='tab-name-" + $scope.documentInfo.id + "']").html($scope.documentInfo.documentNumber);
        };

        // FILES

        $scope.changeFileName = function (file) {
            $http({url: '/files/' + file.id, data: file, method: 'post'});
        };

        $scope.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
        };

        $scope.download = function (filesId) {
            fileService.downloadFiles(filesId,$scope.tableId);
        };

        $scope.selectNewFiles = function (files) {
            $scope.files = files;
        };

        $scope.deleteNewFile = function (index) {
            $scope.files.splice(index, 1);
        };

        $scope.deleteFile = function (documentId, fId) {
            $timeout(function () {
                $http({
                    method: 'delete',
                    url: '/document/' + documentId + '/delete/' + fId
                }).then(function (response) {
                    $scope.documentInfo.files = response.data;
                });
            },500);
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