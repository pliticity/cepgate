(function () {

    var quotation = angular.module('quotation');

    quotation.controller('QuotationListController', ['quotationService','settingsService', function (quotationService,settingsService) {

        var ctrl = this;

        CgListController(ctrl,settingsService,quotationService);

    }]);

})();