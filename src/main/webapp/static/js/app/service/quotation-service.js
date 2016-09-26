(function () {

    var quotation = angular.module('quotation');

    quotation.factory('Quotation', ['$resource',
        function ($resource) {
            return $resource('/quotation/:id', {}, {'query': {'url': '/quotation/query', 'isArray': true},'save':{'url':'/quotation',method:'post'}});
        }]);

    quotation.service('quotationService', ['Quotation', function (Quotation) {

        CgService(this,Quotation);

    }]);

})();