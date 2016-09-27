(function () {

    var project = angular.module('project');

    project.factory('Project', ['$resource','linkService',
        function ($resource,linkService) {
            return $resource('/project/:id', {}, {'get':{'url':'/project/:id',interceptor: {response: linkService.fetchObjects}},'query': {'url': '/project/query', 'isArray': true},'save':{'url':'/project',method:'post'}});
        }]);

    project.service('projectService', ['Project', function (Project) {

        CgService(this,Project);

    }]);

})();