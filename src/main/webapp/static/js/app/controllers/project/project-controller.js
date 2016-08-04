(function () {

    var project = angular.module('project');

    project.controller('ProjectController', ['$http', '$scope','principalService','projectService','classificationService','$window', function ($http, $scope,principalService,projectService,classificationService,$window) {

        var ctrl = this;

        ctrl.project = {};
        ctrl.projectForm = {};
        ctrl.principals = [];
        ctrl.classifications = [];

        ctrl.onClassificationSelected = function(){
            classificationService.onClassificationSelected(ctrl.classifications,ctrl.project.classification);
        };
        
        ctrl.saveProject = function(){
            if(projectService.saveProject(ctrl.projectForm,ctrl.project)==true && ctrl.project.id == null){
                ctrl.project = projectService.getNew();
            };
        };

        // INIT FUNCTIONS

        ctrl.setProject = function(projectId){
            ctrl.project = projectService.getOne(projectId);
        };

        ctrl.newProject = function(){
            ctrl.project = projectService.getNew();
        };

        ctrl.getPrincipals = function(){
            ctrl.principals = principalService.getPrincipalsInDomain();
        };

        ctrl.getClassifications = function(){
            ctrl.classifications = classificationService.getAll(true);
        };

        ctrl.getPrincipals();
        ctrl.getClassifications();

    }]);

})();