(function () {

    var project = angular.module('project');

    project.factory('Project', ['$resource',
        function ($resource) {
            return $resource('/project/:id', {}, {'query': {'url': '/project/query', 'isArray': true},'save':{'url':'/project',method:'post'}});
        }]);

    project.service('projectService', ['Project', function (Project) {

        CgService(this,Project);

    }]);

})();