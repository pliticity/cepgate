(function () {

    var project = angular.module('project');

    project.controller('ProjectController', ['$scope','principalService','projectService','classificationService','tabService', function ($scope,principalService,projectService,classificationService,tabService) {

        var ctrl = this;

        CgModelController(ctrl, projectService, tabService,'name','project-tabs','project',$scope);
        CgClassifiableController(ctrl, classificationService,'project');
        CgPrincipalController(ctrl, principalService);

    }]);

})();