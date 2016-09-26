(function () {

    var gadmin = angular.module('gadmin');

    gadmin.controller('DomainListController', ['domainService','settingsService', function (domainService,settingsService) {

        var ctrl = this;

        CgListController(ctrl,settingsService,domainService);

        ctrl.toggleActive = function (row) {
            var callback = function(response){
                ctrl.models = response.data;
            };
            domainService.toggleActive(row,callback);
        };

    }]);

})();