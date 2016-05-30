(function () {

    var admin = angular.module('admin');

    admin.controller('AdminController', ['$http', '$scope', '$window','$resource', function ($http, $scope, $window,$resource) {

        $scope.itemsPerPage = 10;

        var Domain = $resource('/admin/domain/:id', {}, {'query': {'url': '/admin/domain', 'isArray': true}});

        $scope.domains = Domain.query();

    }]);

})();