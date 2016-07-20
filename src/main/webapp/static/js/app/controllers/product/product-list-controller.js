(function () {

    var product = angular.module('product');

    product.factory('Product', ['$resource',
        function ($resource) {
            return $resource('/product/:id', {}, {'query': {'url': '/product/query', 'isArray': true}});
        }]);

    product.controller('ProductListController', ['$http', '$scope','Product', function ($http, $scope,Product) {

        var ctrl = this;

        ctrl.products = [];
        ctrl.queryParams = {};

        ctrl.getProducts = function(){
            ctrl.products = Product.query(ctrl.queryParams);
        }

    }]);

})();