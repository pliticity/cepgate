(function () {

    var admin = angular.module('dhd');

    admin.controller('DomainController', ['$http', '$scope', '$window','$resource','$route', function ($http, $scope, $window,$resource,$route) {

        var Domain = $resource('/domain/:id', {}, {'query': {'url': '/domain', 'isArray': true}});

        $scope.form = {};
        $scope.domain = Domain.get({id:$route.current.params.id});

    }]);

})();