(function () {

    var project = angular.module('project');

    project.factory('Project', ['$resource',
        function ($resource) {
            return $resource('/project/:id', {}, {'query': {'url': '/project/query', 'isArray': true}});
        }]);

    project.controller('ProjectListController', ['$http', '$scope','Project', function ($http, $scope,Project) {

        var ctrl = this;

        ctrl.projects = [];

        ctrl.getProjects = function(){
            ctrl.projects = Project.query();
        }

    }]);

})();