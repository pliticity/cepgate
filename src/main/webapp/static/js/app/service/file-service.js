(function () {

    var dhd = angular.module('dhd');

    dhd.service('fileService', ['$http','$window', function ($http,$window) {

        this.downloadFiles = function (filesId) {
            var files = [];
            $("#files-" + filesId + " option:selected").map(function (a, item) {
                return item.value
            }).each(function (index, item) {
                files.push(item);
            });
            $http({url: '/files', method: 'post', data: files}).then(function (succ) {
                $window.open("/file/"+succ.data.symbol+"?temp=true", '_blank');
            });
        };

    }]);

})();