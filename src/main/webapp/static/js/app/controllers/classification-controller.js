(function () {

    var dhd = angular.module('dhd');

    dhd.controller('ClassificationController', ['$http', '$scope', function ($http, $scope) {

        $scope.classifications = [];
        $scope.classification = {};

        $http({url: '/classification', method: 'get',params:{active:false}}).then(function (succ) {
            $scope.classifications = succ.data;
        });

        $scope.addClassification = function () {
            if ($scope.form.classificationForm.$valid) {
                $scope.objectify($scope.classification.children);
                $scope.objectify($scope.classification.parents);
                $("#add-doc-type-modal").remove();
                
                $http({url: '/classification', method: 'post', data: $scope.classification}).then(function (succ) {
                    $scope.classifications = succ.data;
                });
            }
        };

        $scope.objectify = function(array){
            for(var i=0; i< array.length; i++){
                array[i] = JSON.parse(array[i]);
            }
        };

        $scope.toggleClassification = function (row) {
            $http({url: '/classification/'+row.id, method: 'put',params:{toggle:row.active}}).then(function (succ) {
                $scope.classifications = succ.data;
            });
        };

    }]);

})();