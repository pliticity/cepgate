(function () {

    var product = angular.module('product');

    product.controller('ProductListController', ['$http', '$scope','productService','settingsService', function ($http, $scope,productService,settingsService) {

        var ctrl = this;

        ctrl.products = [];
        ctrl.queryParams = {};

        ctrl.settings = settingsService.settings;

        ctrl.getProducts = function(){
            ctrl.products = productService.getAll(ctrl.queryParams);
        }

    }]);

})();