(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$http', '$scope', 'classificationService', 'productService', 'principalService', 'tabService', function ($http, $scope, classificationService, productService, principalService, tabService) {

        var ctrl = this;

        CgModelController(ctrl, productService, tabService,'name','product-tabs','product',$scope);
        CgClassifiableController(ctrl, classificationService, ctrl.model);
        CgPrincipalController(ctrl, principalService);

    }]);

})();