(function () {

    var project = angular.module('project');

    project.controller('ProjectListController', ['$http', '$scope','projectService','settingsService', function ($http, $scope,projectService,settingsService) {

        var ctrl = this;

        ctrl.projects = [];
        ctrl.queryParams = {};

        ctrl.settings = settingsService.settings;

        ctrl.getProjects = function () {
            ctrl.projects = projectService.getAll(ctrl.queryParams);
        }

    }]);

})();