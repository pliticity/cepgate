(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$scope', 'classificationService', 'productService', 'principalService', 'tabService','$route', function ($scope, classificationService, productService, principalService, tabService,$route) {

        var ctrl = this;

        CgModelController(ctrl, productService, tabService,'name','product-tabs','product',$scope);
        CgClassifiableController(ctrl, classificationService,'product');
        CgPrincipalController(ctrl, principalService);

        ctrl.openPicFromRoute = function(){
            var picId = $route.current.params.id;
            var pic = productService.getOne(picId,function(){
                var args = {open : true, id: pic.id, name: pic.name, tabsId:'product-tabs', factory:'product'};
                tabService.addTab(args,$scope);
            });

        };

    }]);

})();