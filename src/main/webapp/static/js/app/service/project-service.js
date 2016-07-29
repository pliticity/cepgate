(function () {

    var project = angular.module('project');

    project.factory('Project', ['$resource',
        function ($resource) {
            return $resource('/project/:id', {}, {'query': {'url': '/project/query', 'isArray': true},'save':{'url':'/project',method:'post'}});
        }]);

    project.service('projectService', ['$http', 'Project', function ($http, Project) {

        this.getAll = function (params) {
            return Project.query(params);
        };

        this.getOne = function (id) {
            return Project.get({id:id});
        };

        this.saveProject = function (form, project) {
            form.$submitted = true;
            if (form.$valid) {
                Project.save({},project);
                form.$submitted = false;
                return true;
            }else{
                return false;
            }
        };

        this.getNew = function () {
            return Project.get({new:true});
        };

    }]);

})();