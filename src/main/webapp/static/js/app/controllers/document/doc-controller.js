(function () {

    var document = angular.module('document');

    document.controller('DocController', ['$scope', 'classificationService', 'documentService', 'principalService', 'tabService','fileService','$route','$http','$timeout', function ($scope, classificationService, documentService, principalService, tabService,fileService,$route,$http,$timeout) {

        var ctrl = this;

        ctrl.revision = false;
        ctrl.types = [];
        ctrl.desktopExists=false;

        CgModelController(ctrl, documentService, tabService, 'documentNumber', 'document-tabs', 'document', $scope);
        CgClassifiableController(ctrl, classificationService,'document');
        CgPrincipalController(ctrl, principalService);

        ctrl.getDocTypes = function () {
            documentService.getDocTypes(true, function (succ) {
                ctrl.types = succ.data
            });
        };

        ctrl.onTypeSelected = function () {
            var selected = {};
            for (var i = 0; i < ctrl.types.length; i++) {
                var type = ctrl.types[i];
                if (type.id == ctrl.model.docType.id) {
                    selected = type;
                    break;
                }
            }
            ctrl.model.docType.typeId = selected.typeId;
            ctrl.model.docType.name = selected.name;
        };

        ctrl.readonly = function () {
            return ctrl.revision == true || ctrl.archived();
        };

        ctrl.archived = function () {
            return ctrl.revision == false && ctrl.model.state == 'ARCHIVED';
        };

        ctrl.returnToCurrentRevision = function (documentId) {
            ctrl.setModel(ctrl.model.id);
            ctrl.revision = false;
        };

        ctrl.saveDocument = function () {
            var callback = null;
            if (ctrl.model.id == null) {
                callback = function (response) {
                    var docId = response.id;
                    var files = ctrl.files;
                    ctrl.files = [];
                    if (files != null && files.length > 0) {
                        fileService.uploadFiles(files, docId, function (res) {
                            ctrl.openTabCallback(response);
                        });
                    } else {
                        ctrl.openTabCallback(response);
                    }
                }
            }
            ctrl.saveModel(callback);
        };

        ctrl.isFavourite = function(){
            return ctrl.model.id && ctrl.model.favourite;
        };

        ctrl.favourite = function () {
            documentService.favourite(ctrl.model.id, true);
            ctrl.model.favourite = true;
        };

        ctrl.unFavourite = function () {
            documentService.favourite(ctrl.model.id, false);
            ctrl.model.favourite = false;
        };

        ctrl.archive = function () {
            documentService.archive(ctrl.model,function(response){
                ctrl.model.state = response.data.state;
                ctrl.model.archivedDate = response.data.archivedDate;
            });
        };

        ctrl.createRevision = function () {
            documentService.createRevision(ctrl.model,function(response){
                ctrl.model.revisions = response.data;
                ctrl.model.revision.number++;
                ctrl.model.state = 'IN_PROGRESS';
                ctrl.model.archivedDate = null;
                ctrl.model.revision.prefix = ctrl.model.revision.number.toString();
            });
        };

        ctrl.fetchRevision = function (rev) {
            documentService.fetchRevision(ctrl.model,rev,function(response){
                ctrl.revision = true;
                ctrl.model = response.data;
                $('#details-'+ctrl.model.id+'-tabs a:first').tab('show');
            });
        };

        ctrl.uploadFiles = function () {
            fileService.uploadFiles(ctrl.files,ctrl.model.id,function(response){
                ctrl.model.files = response.data;
            });
        };

        ctrl.selectNewFiles = function(files){
            ctrl.files = files;
        };

        ctrl.openFile = function(symbol){
            fileService.openFile(symbol);
        };

        ctrl.changeFileName = function (file) {
            fileService.changeFileName(file);
        };

        ctrl.deleteFile = function (documentId, fId) {
            fileService.deleteFile(documentId,fId,function(response){
                ctrl.model.files = response.data;
            });
        };

        ctrl.deleteNewFile = function (index) {
            ctrl.files.splice(index, 1);
        };

        ctrl.openDicFromRoute = function(){
            var dicId = $route.current.params.id;
            var dic = documentService.getOne(dicId,function(){
                var args = {open : true, id: dic.id, name: dic.documentNumber, tabsId:'document-tabs', factory:'document'};
                tabService.addTab(args,$scope);
            });

        };

        ctrl.openOnDesktop = function (file) {
            $http({url: '/files/' + file.id + '/desktop', method: 'get'}).then(function (response) {
                file.locked = true;
            });
        };

        ctrl.refreshStatus = function(dicId){
            $http({url: '/files/' + dicId + '/isLocked', method: 'get'}).then(function (response) {
                ctrl.model.files = response.data;
            });
        };

        ctrl.getDesktopExists = function(){
            $http({url: '/auth/desktopExists', method: 'get'}).then(function (response) {
                ctrl.desktopExists = response.data;
            });
        };

        ctrl.unlockFile = function(file){
            $http({url: '/files/' + file.id + '/unlock', method: 'get'}).then(function (response) {
                file.locked = false;
            });
        };

        ctrl.getDocTypes();
        ctrl.getDesktopExists();
    }]);

})();