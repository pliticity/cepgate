(function () {

    var quotation = angular.module('quotation');

    quotation.factory('Quotation', ['$resource','linkService',
        function ($resource,linkService) {
            return $resource('/quotation/:id', {}, {'get':{'url':'/quotation/:id',interceptor: {response: linkService.fetchObjects}},'query': {'url': '/quotation/query', 'isArray': true},'save':{'url':'/quotation',method:'post'}});
        }]);

    quotation.service('quotationService', ['Quotation', function (Quotation) {

        CgService(this,Quotation);

    }]);

})();