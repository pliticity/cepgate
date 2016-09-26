(function () {

    var document = angular.module('document');

    document.controller('PrincipalListController', ['$http', '$scope', function ($http, $scope) {

        $scope.principals = {};

        $scope.principals = function () {
            $http({url: '/principal', method: 'get'}).then(function (succ) {
                $scope.principals = succ.data;
            });
        };

        $scope.principals();

        this.querySearch = function(recipient){
            console.log(recipient);
        };

        $scope.currentPrincipal = function () {
            $http({url: '/principal/current', method: 'get'}).then(function (succ) {
                $scope.mail.recipient = succ.data.email;
            });
        };

    }]);

})();