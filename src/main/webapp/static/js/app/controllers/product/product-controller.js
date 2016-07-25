(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$http', '$scope','classificationService','productService','principalService', function ($http, $scope,classificationService,productService,principalService) {

        var ctrl = this;

        ctrl.product = {};
        ctrl.productForm = {};
        ctrl.classifications = [];
        ctrl.principals = [];

        ctrl.onClassificationSelected = function(){
            classificationService.onClassificationSelected(ctrl.classifications,ctrl.product.classification);
        };

        ctrl.saveProduct = function(){
            if(productService.saveProduct(ctrl.productForm,ctrl.product)==true){
                ctrl.product = productService.getNew();
            };
        };

        // INIT FUNCTIONS

        ctrl.setProduct = function(productId){
            ctrl.product = productService.getOne(productId);
        };

        ctrl.newProduct = function(){
            ctrl.product = productService.getNew();
        };

        ctrl.getClassifications = function(){
            ctrl.classifications = classificationService.getAll(true);
        };

        ctrl.getPrincipals = function(){
            ctrl.principals = principalService.getPrincipalsInDomain();
        };

        ctrl.getClassifications();
        ctrl.getPrincipals();

    }]);

})();