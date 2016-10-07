(function () {

    var document = angular.module('document');

    document.service('fileService', ['$http', '$window','Upload','$timeout', function ($http, $window,Upload,$timeout) {

        var service = this;

        service.downloadFiles = function (filesId,tableId) {
            var files = [];
            for (var i = 0; i < filesId.length; i++) {
                var fId = filesId[i];
                $("#files-"+tableId+"-" + fId + " option:selected").map(function (a, item) {
                    return item.value
                }).each(function (index, item) {
                    files.push(item);
                });
            }
            $http({url: '/files', method: 'post', data: files}).then(function (succ) {
                $window.open("/file/" + succ.data.symbol + "?temp=true", '_blank');
            });
        };

        service.mailFiles = function (filesId, zip,mail,tId,transmittal) {
            var files = [];
            for (var i = 0; i < filesId.length; i++) {
                var fId = filesId[i];
                $("#files-"+tId+"-" + fId + " option:selected").map(function (a, item) {
                    return item.value
                }).each(function (index, item) {
                    files.push(item);
                });
            }

            mail.files = files;

            $http({url: '/document/0/mail',params:{zip:zip,transmittal:transmittal}, method: 'post', data: mail}).then(function(succ){
            });

            $("#mail-modal-"+tId).modal('hide');
        };

        service.anySelected = function(fileId,tableId){
            return $("#files-"+tableId+"-" + fileId + " option:selected").length>0;
        };

        service.selectedFiles = function(rowId,tableId){
            var farray = [];
            $("#files-"+tableId+"-" + rowId + " option:selected").map(function (a, item) {
                return item.value
            }).each(function (index, item) {
                farray.push(item);
            });
            return farray;
        }

        service.fileNames = function (fNames,tableId, fileIds) {
            $http({url: '/files/names', method: 'post', data: fileIds}).then(function (succ) {
                fNames[tableId] = succ.data;
            });
        };

        service.uploadFiles = function (files, docId,callback) {
            if (files && files.length) {
                service.uploadFile(files, 0, docId,callback);
            }
        };

        service.uploadFile = function (files, index, docId,callback) {
            Upload.upload({
                url: '/document/' + docId + '/upload',
                data: {file: files[index]}
            }).then(function (resp) {
                if ((index + 1) == files.length) {
                    callback(resp);
                } else {
                    service.uploadFile(files, index + 1, docId);
                }
            });
        };

        service.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
        };

        service.changeFileName = function (file) {
            $http({url: '/files/' + file.id, data: file, method: 'post'});
        };

        service.deleteFile = function (documentId, fId,callback) {
            $timeout(function () {
                $http({
                    method: 'delete',
                    url: '/document/' + documentId + '/delete/' + fId
                }).then(callback);
            },500);
        };

    }]);

})();