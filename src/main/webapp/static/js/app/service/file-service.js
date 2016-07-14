(function () {

    var dhd = angular.module('dhd');

    dhd.service('fileService', ['$http', '$window', function ($http, $window) {

        this.downloadFiles = function (filesId) {
            var files = [];
            for (var i = 0; i < filesId.length; i++) {
                var fId = filesId[i];
                $("#files-" + fId + " option:selected").map(function (a, item) {
                    return item.value
                }).each(function (index, item) {
                    files.push(item);
                });
            }
            $http({url: '/files', method: 'post', data: files}).then(function (succ) {
                $window.open("/file/" + succ.data.symbol + "?temp=true", '_blank');
            });
        };

        this.mailFiles = function (filesId, zip,mail,tId) {
            var files = [];
            for (var i = 0; i < filesId.length; i++) {
                var fId = filesId[i];
                $("#files-" + fId + " option:selected").map(function (a, item) {
                    return item.value
                }).each(function (index, item) {
                    files.push(item);
                });
            }

            mail.files = files;

            $http({url: '/document/0/mail',params:{zip:zip}, method: 'post', data: mail}).then(function(succ){
                console.log('succ');
            });

            $("#mail-modal-"+tId).modal('hide');
        };

        this.anySelected = function(fileId){
            return $("#files-" + fileId + " option:selected").length>0;
        };

        this.selectedFiles = function(rowId){
            var farray = [];
            $("#files-" + rowId + " option:selected").map(function (a, item) {
                return item.value
            }).each(function (index, item) {
                farray.push(item);
            });
            return farray;
        }

    }]);

})();