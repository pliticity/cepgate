(function () {

    var quotation = angular.module('quotation');

    quotation.controller('QuotationController', ['$http', '$scope','principalService','quotationService', function ($http, $scope,principalService,quotationService) {

        var ctrl = this;

        ctrl.quotation = {};
        ctrl.quotationForm = {};
        ctrl.principals = [];

        ctrl.saveQuotation = function(){
            if(quotationService.saveQuotation(ctrl.quotationForm,ctrl.quotation)==true){
                ctrl.quotation = quotationService.getNew();
            };
        };

        // INIT FUNCTIONS

        ctrl.setQuotation = function(quotationId){
            ctrl.quotation = quotationService.getOne(quotationId);
        };

        ctrl.newQuotation = function(){
            ctrl.quotation = quotationService.getNew();
        };

        ctrl.getPrincipals = function(){
            ctrl.principals = principalService.getPrincipalsInDomain();
        };

        ctrl.getPrincipals();

    }]);

})();