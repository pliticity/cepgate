(function () {

    var quotation = angular.module('quotation');

    quotation.controller('QuotationListController', ['$http', '$scope','quotationService','settingsService', function ($http, $scope,quotationService,settingsService) {

        var ctrl = this;

        ctrl.quotations = [];
        ctrl.queryParams = {};

        ctrl.settings = settingsService.settings;

        ctrl.getQuotations = function () {
            ctrl.quotations = quotationService.getAll(ctrl.queryParams);
        }

    }]);

})();