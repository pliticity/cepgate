(function () {

    var product = angular.module('product');

    product.factory('Product', ['$resource','linkService',
        function ($resource,linkService) {
            return $resource('/product/:id', {}, {'get':{'url':'/product/:id',interceptor: {response: linkService.fetchObjects}},'query': {'url': '/product/query', 'isArray': true},'save':{'url':'/product',method:'post'}});
        }]);

    product.service('productService', ['Product', function (Product) {

        CgService(this,Product);

    }]);

})();