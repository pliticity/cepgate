(function () {

    var document = angular.module('document');

    document.controller('TemplateController', ['$http', '$scope','$window','Upload', function ($http, $scope,$window,Upload) {

        var ctrl = this;

        ctrl.templates = [];
        ctrl.selectedTemplate = {};
        ctrl.dic = {};

        ctrl.getTemplates = function(){
            $http({url:'/template',method:'get',params:{domainId:ctrl.domainId}}).then(function (succ) {
                ctrl.templates = succ.data;
            });
        };


        ctrl.openFile = function (symbol) {
            $window.open("/file/" + symbol, '_blank');
        };

        ctrl.remove = function (id) {
            $http({url:'/template/'+id,method:'delete',params:{domainId:ctrl.domainId}}).then(function (succ) {
                ctrl.templates = succ.data;
            });
        };

        ctrl.uploadFile = function (file) {
            if (file != null) {
                Upload.upload({
                    url: '/template/upload',
                    data: {file: file},
                    params:{domainId:ctrl.domainId}
                }).then(function (resp) {
                    ctrl.templates.push(resp.data);
                }, function (err) {
                    alert("Given template is invalid");
                });
            }
        }

        ctrl.addFileFromTemplate = function (docId) {
            $http({url: '/document/' + docId + '/template/' + ctrl.selectedTemplate.id, method: 'put'}).then(function (succ) {
                ctrl.dic.files.push(succ.data);
            },function(reject){
                alert("Could not create file from the template.");
            });
        }

    }]);

})();