(function () {

    var quotation = angular.module('quotation');

    quotation.factory('Quotation', ['$resource',
        function ($resource) {
            return $resource('/quotation/:id', {}, {'query': {'url': '/quotation/query', 'isArray': true},'save':{'url':'/quotation',method:'post'}});
        }]);

    quotation.service('quotationService', ['$http', 'Quotation', function ($http, Quotation) {

        this.getAll = function (params) {
            return Quotation.query(params);
        };

        this.getOne = function (id) {
            return Quotation.get({id:id});
        };

        this.saveQuotation = function (form, quotation) {
            form.$submitted = true;
            if (form.$valid) {
                Quotation.save({},quotation);
                form.$submitted = false;
                return true;
            }else{
                return false;
            }
        };

        this.getNew = function () {
            return Quotation.get({new:true});
        };

    }]);

})();