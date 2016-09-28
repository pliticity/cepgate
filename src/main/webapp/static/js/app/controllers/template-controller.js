(function () {

    var document = angular.module('document');

    document.controller('TemplateController', ['$http', '$scope','$window','Upload', function ($http, $scope,$window,Upload) {

        var ctrl = this;

        $scope.templates = [];
        $scope.selectedTemplate = {id:""};

        ctrl.getTemplates = function(){
            $http({url:'/template',method:'get',params:{domainId:ctrl.domainId}}).then(function (succ) {
                $scope.templates = succ.data;
            });
        };


        $scope.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
        };

        $scope.remove = function (id) {
            $http({url:'/template/'+id,method:'delete',params:{domainId:ctrl.domainId}}).then(function (succ) {
                $scope.templates = succ.data;
            });
        };

        $scope.uploadFile = function (file) {
            if (file != null) {
                Upload.upload({
                    url: '/template/upload',
                    data: {file: file},
                    params:{domainId:ctrl.domainId}
                }).then(function (resp) {
                    $scope.templates.push(resp.data);
                }, function (err) {
                    alert("Given template is invalid");
                });
            }
        }

        $scope.addFileFromTemplate = function (docId) {
            $http({url: '/document/' + docId + '/template/' + $scope.selectedTemplate.id, method: 'put'}).then(function (succ) {
                $scope.documentInfo.files.push(succ.data);
            },function(reject){
                alert("Could not create file from the template.");
            });
        }

    }]);

})();