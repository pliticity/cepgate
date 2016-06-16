(function () {

    var dhd = angular.module('dhd');

    dhd.service('authorizationService', ['$http', '$window', function ($http, $window) {

        this.principal = {};

    }]);

})();