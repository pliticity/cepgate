(function () {

    var product = angular.module('product');

    product.controller('ProductListController', ['$http', '$scope','productService', function ($http, $scope,productService) {

        var ctrl = this;

        ctrl.products = [];
        ctrl.queryParams = {};

        ctrl.getProducts = function(){
            ctrl.products = productService.getAll(ctrl.queryParams);
        }

    }]);

})();