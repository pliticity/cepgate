(function () {

    var quotation = angular.module('quotation');

    quotation.controller('QuotationController', ['$http', '$scope','principalService','quotationService','classificationService', function ($http, $scope,principalService,quotationService,classificationService) {

        var ctrl = this;

        ctrl.quotation = {};
        ctrl.quotationForm = {};
        ctrl.principals = [];
        ctrl.classifications = [];

        ctrl.onClassificationSelected = function(){
            classificationService.onClassificationSelected(ctrl.classifications,ctrl.quotation.classification);
        };
        
        ctrl.saveQuotation = function(){
            if(quotationService.saveQuotation(ctrl.quotationForm,ctrl.quotation)==true && ctrl.quotation.id == null){
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

        ctrl.getClassifications = function(){
            ctrl.classifications = classificationService.getAll(true);
        };

        ctrl.getPrincipals();
        ctrl.getClassifications();

    }]);

})();