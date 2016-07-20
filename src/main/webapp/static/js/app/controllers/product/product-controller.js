(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$http', '$scope','Product','classificationService', function ($http, $scope,Product,classificationService) {

        var ctrl = this;

        ctrl.product = {};
        ctrl.productForm = {};
        ctrl.classifications = [];

        ctrl.getClassifications = function(){
            ctrl.classifications = classificationService.getAll(true);
        };

        ctrl.onClassificationSelected = function(){
            classificationService.onClassificationSelected(ctrl.classifications,ctrl.product.classification);
        };

        // INIT FUNCTIONS

        ctrl.getClassifications();

    }]);

})();