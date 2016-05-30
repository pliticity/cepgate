(function () {

    var admin = angular.module('admin');

    admin.controller('DomainController', ['$http', '$scope', '$window','$resource','$route', function ($http, $scope, $window,$resource,$route) {

        var Domain = $resource('/admin/domain/:id', {}, {'query': {'url': '/admin/domain', 'isArray': true}});

        $scope.form = {};
        $scope.domain = Domain.get({id:$route.current.params.id});

    }]);

})();