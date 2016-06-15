(function () {

    var admin = angular.module('admin');

    admin.controller('AdminDomainController', ['$http', '$scope', '$window','$resource','$route', function ($http, $scope, $window,$resource,$route) {

        var Domain = $resource('/admin/domain/:id', {}, {'query': {'url': '/admin/domain', 'isArray': true}});

        $scope.form = {};
        $scope.domain = Domain.get({id:$route.current.params.id});

        $scope.changeActive = function (row) {
            $http({url: '/principal/' + row.id, method: 'post', params: {'active': row.active}}).then(function (succ) {
                $scope.domain.principals = succ.data;
            });
        };

    }]);

})();