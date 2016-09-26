(function () {

    var document = angular.module('document');

    document.controller('DocumentListController', ['docService','settingsService', function (docService,settingsService) {

        var ctrl = this;

        CgListController(ctrl,settingsService,docService);

    }]);

})();