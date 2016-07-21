(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$http', '$scope','classificationService','productService', function ($http, $scope,classificationService,productService) {

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

        ctrl.saveProduct = function(){
            productService.saveProduct(ctrl.productForm,ctrl.product);
        };

        // INIT FUNCTIONS

        ctrl.getClassifications();

    }]);

})();