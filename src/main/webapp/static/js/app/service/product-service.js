(function () {

    var product = angular.module('product');

    product.factory('Product', ['$resource',
        function ($resource) {
            return $resource('/product/:id', {}, {'query': {'url': '/product/query', 'isArray': true},'save':{'url':'/product',method:'post'}});
        }]);

    product.service('productService', ['$http', 'Product', function ($http, Product) {

        this.getAll = function (params) {
            return Product.query(params);
        };

        this.getOne = function (id) {
            return Product.get({id:id});
        };

        this.saveProduct = function (form, product) {
            form.$submitted = true;
            if (form.$valid) {
                Product.save({},product);
                form.$submitted = false;
                return true;
            }else{
                return false;
            }
        };

        this.getNew = function () {
            return Product.get({new:true});
        };

    }]);

})();