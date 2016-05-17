(function () {
    'use strict';
    var documentApp = angular.module('document');
    documentApp.controller('MenuController', ['$cookies', '$http', '$scope', '$aside', function ($cookies, $http, $scope, $aside) {
        var menu = this;
        menu.isAuthenticated = false;
        $http.get('/signup/authenticated', {withCredentials: true}).success(function (data, status, headers, config) {
            console.log('app-menu-authenticated');
            setTimeout(function () {
                console.log($cookies.getAll());
                console.log(headers());
                console.log($cookies.session)
                console.log($cookies.get('JSESSIONID'));
            }, 500);
            console.log(headers());
            //$cookies.put('JSESSIONID', 'ID');
            menu.isAuthenticated = true;
        });

        $scope.openAside = function (position) {
            $aside.open({
                templateUrl: 'aside.html',
                placement: position,
                backdrop: true,
                controller: function ($scope, $uibModalInstance) {
                    $scope.ok = function (e) {
                        $uibModalInstance.close();
                        e.stopPropagation();
                    };
                    $scope.cancel = function (e) {
                        $uibModalInstance.dismiss();
                        e.stopPropagation();
                    };
                }
            })
        }
    }]);
})();	 