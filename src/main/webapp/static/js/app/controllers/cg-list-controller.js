function CgListController(ctrl,settingsService,modelService){

    ctrl.models = [];
    ctrl.queryParams = {};

    ctrl.settings = settingsService.settings;

    ctrl.getModels = function () {
        ctrl.models = modelService.getAll(ctrl.queryParams);
    }

};