(function () {

    var document = angular.module('document');

    document.service('fileService', ['$http', '$window', function ($http, $window) {

        this.downloadFiles = function (filesId,tableId) {
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

        this.mailFiles = function (filesId, zip,mail,tId,transmittal) {
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

        this.anySelected = function(fileId,tableId){
            return $("#files-"+tableId+"-" + fileId + " option:selected").length>0;
        };

        this.selectedFiles = function(rowId,tableId){
            var farray = [];
            $("#files-"+tableId+"-" + rowId + " option:selected").map(function (a, item) {
                return item.value
            }).each(function (index, item) {
                farray.push(item);
            });
            return farray;
        }

        this.fileNames = function (fNames,tableId, fileIds) {
            $http({url: '/files/names', method: 'post', data: fileIds}).then(function (succ) {
                fNames[tableId] = succ.data;
            });
        };

    }]);

})();