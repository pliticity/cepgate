(function () {

    var project = angular.module('project');

    project.controller('ProjectListController', ['projectService','settingsService', function (projectService,settingsService) {

        var ctrl = this;

        CgListController(ctrl,settingsService,projectService);

    }]);

})();