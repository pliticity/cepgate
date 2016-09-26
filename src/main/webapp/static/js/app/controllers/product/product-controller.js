(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$scope', 'classificationService', 'productService', 'principalService', 'tabService', function ($scope, classificationService, productService, principalService, tabService) {

        var ctrl = this;

        CgModelController(ctrl, productService, tabService,'name','product-tabs','product',$scope);
        CgClassifiableController(ctrl, classificationService);
        CgPrincipalController(ctrl, principalService);

    }]);

})();