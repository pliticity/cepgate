(function () {

    var product = angular.module('product');

    product.factory('Product', ['$resource',
        function ($resource) {
            return $resource('/product/:id', {}, {'query': {'url': '/product/query', 'isArray': true},'save':{'url':'/product',method:'post'}});
        }]);

    product.service('productService', ['Product', function (Product) {

        CgService(this,Product);

    }]);

})();