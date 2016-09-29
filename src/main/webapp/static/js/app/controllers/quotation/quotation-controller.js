(function () {

    var quotation = angular.module('quotation');

    quotation.controller('QuotationController', ['$scope','principalService','quotationService','classificationService','tabService', function ($scope,principalService,quotationService,classificationService,tabService) {

        var ctrl = this;

        CgModelController(ctrl, quotationService, tabService,'name','quotation-tabs','quotation',$scope);
        CgClassifiableController(ctrl, classificationService,'quotation');
        CgPrincipalController(ctrl, principalService);

    }]);

})();