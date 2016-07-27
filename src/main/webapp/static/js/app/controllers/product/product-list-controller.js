(function () {

    var product = angular.module('product');

    product.controller('ProductListController', ['productService','settingsService', function (productService,settingsService) {

        var ctrl = this;

        CgListController(ctrl,settingsService,productService);

    }]);

})();