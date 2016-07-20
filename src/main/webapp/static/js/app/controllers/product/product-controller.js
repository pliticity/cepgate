(function () {

    var product = angular.module('product');

    product.controller('ProductController', ['$http', '$scope','Product', function ($http, $scope,Product) {

        var ctrl = this;

        ctrl.product = {};

    }]);

})();