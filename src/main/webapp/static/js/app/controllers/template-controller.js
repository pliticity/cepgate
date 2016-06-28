(function () {

    var dhd = angular.module('dhd');

    dhd.controller('TemplateController', ['$http', '$scope','$window','Upload', function ($http, $scope,$window,Upload) {

        $scope.templates = [];
        $scope.selectedTemplate = {id:""};

        $http({url:'/template',method:'get'}).then(function (succ) {
            $scope.templates = succ.data;
        });

        $scope.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
        };

        $scope.remove = function (id) {
            $http({url:'/template/'+id,method:'delete'}).then(function (succ) {
                $scope.templates = succ.data;
            });
        };

        $scope.uploadFile = function (file) {
            Upload.upload({
                url: '/template/upload',
                data: {file: file}
            }).then(function (resp) {
                $scope.templates.push(resp.data);
            });
        }

        $scope.addFileFromTemplate = function (docId) {
            $http({url: '/document/' + docId + '/template/' + $scope.selectedTemplate.id, method: 'put'}).then(function (succ) {
                $scope.documentInfo.files.push(succ.data);
            });
        }

    }]);

})();